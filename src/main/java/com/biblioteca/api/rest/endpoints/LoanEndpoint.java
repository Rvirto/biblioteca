package com.biblioteca.api.rest.endpoints;

import com.biblioteca.api.rest.assemblers.LoanAssembler;
import com.biblioteca.api.rest.models.request.LoanRequestModel;
import com.biblioteca.api.rest.models.response.LoanResponseModel;
import com.biblioteca.api.rest.validators.annotations.LoanMatrixParamValidation;
import com.biblioteca.domain.builders.LoanBuilder;
import com.biblioteca.domain.entities.Book;
import com.biblioteca.domain.entities.Client;
import com.biblioteca.domain.entities.Loan;
import com.biblioteca.domain.enumeration.LoanStatusEnum;
import com.biblioteca.domain.exceptions.BadRequestException;
import com.biblioteca.domain.exceptions.ConflictException;
import com.biblioteca.domain.exceptions.NotFoundException;
import com.biblioteca.domain.services.BookService;
import com.biblioteca.domain.services.ClientService;
import com.biblioteca.domain.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.biblioteca.domain.enumeration.ExceptionMessagesEnum.BOOK_NOT_FOUND;
import static com.biblioteca.domain.enumeration.ExceptionMessagesEnum.CLIENT_NOT_FOUND;
import static com.biblioteca.domain.enumeration.ExceptionMessagesEnum.LOAN_ALREADY_EXISTS;
import static com.biblioteca.domain.enumeration.ExceptionMessagesEnum.LOAN_NOT_FOUND;

@RestController
@Validated
public class LoanEndpoint {

    public static final String LOAN_RESOURCE_PATH = "/api/loans";
    public static final String LOAN_SELF_PATH = LOAN_RESOURCE_PATH + "/{loanId}";
    public static final String LOAN_DEVOLUTION_RESOURCE_PATH = LOAN_SELF_PATH + "/devolution";

    private final ClientService clientService;
    private final LoanService loanService;
    private final BookService bookService;
    private final LoanAssembler loanAssembler;
    private final PagedResourcesAssembler<Loan> pagedResponseAssembler;

    @Autowired
    public LoanEndpoint(ClientService clientService, LoanService loanService, BookService bookService, LoanAssembler loanAssembler, PagedResourcesAssembler<Loan> pagedResponseAssembler) {
        this.clientService = clientService;
        this.loanService = loanService;
        this.bookService = bookService;
        this.loanAssembler = loanAssembler;
        this.pagedResponseAssembler = pagedResponseAssembler;
    }

    @GetMapping(LOAN_SELF_PATH)
    public ResponseEntity<LoanResponseModel> getById(@PathVariable("loanId") final String loanId) {
        final Loan loan = loanService.findById(loanId).orElseThrow(() -> new NotFoundException(LOAN_NOT_FOUND));
        return ResponseEntity.ok().body(loanAssembler.toModel(loan));
    }

    @PostMapping(LOAN_RESOURCE_PATH)
    public ResponseEntity<?> loan(@RequestBody @Valid LoanRequestModel loanRequestModel) {
        final Book book = bookService.findById(loanRequestModel.getBookId()).orElseThrow(() -> new BadRequestException(BOOK_NOT_FOUND));
        final Client client = clientService.findById(loanRequestModel.getClientId()).orElseThrow(() -> new BadRequestException(CLIENT_NOT_FOUND));
        Loan loan = loanAssembler.toEntity(loanRequestModel, book, client);

        loanService.findLoan(loan).ifPresent(searchedLoan -> {
            throw new ConflictException(
                    LOAN_ALREADY_EXISTS, loanAssembler
                    .buildLoanSelfLink(searchedLoan.getId()).toUri());
        });

        loan = loanService.loan(loan);
        return ResponseEntity.created(loanAssembler.buildLoanSelfLink(loan.getId()).toUri()).build();
    }

    @PostMapping(LOAN_DEVOLUTION_RESOURCE_PATH)
    public ResponseEntity<?> devolution(@PathVariable("loanId") final String loanId) {
        Loan loan = loanService.findById(loanId).orElseThrow(() -> new BadRequestException(LOAN_NOT_FOUND));
        loanService.devolution(loan);
        return ResponseEntity.accepted().build();
    }

    @GetMapping(LOAN_RESOURCE_PATH + "{matrixParam}")
    @LoanMatrixParamValidation
    public ResponseEntity<?> getByValues(Pageable pageable,
                                         @MatrixVariable(value = "clientId", required = false) final String clientId,
                                         @MatrixVariable(value = "bookId", required = false) final String bookId,
                                         @MatrixVariable(value = "status", required = false) final LoanStatusEnum status) {
        Loan loan = LoanBuilder.builder().withClientId(clientId).withBookId(bookId).build();
        Page<Loan> loanPage = this.loanService.getByLoan(loan, pageable, status);

        return ResponseEntity.ok()
                .body(pagedResponseAssembler.toModel(loanPage, loanAssembler));
    }
}
