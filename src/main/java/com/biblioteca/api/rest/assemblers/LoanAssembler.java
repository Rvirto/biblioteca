package com.biblioteca.api.rest.assemblers;

import com.biblioteca.api.rest.endpoints.LoanEndpoint;
import com.biblioteca.api.rest.models.request.LoanRequestModel;
import com.biblioteca.api.rest.models.response.LoanResponseModel;
import com.biblioteca.domain.entities.Book;
import com.biblioteca.domain.entities.Client;
import com.biblioteca.domain.entities.Loan;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Loan Assembler class used to work with input and output of endpoint information
 *
 * @author Renato Virto (renatovirtomoreira@outlook.com)
 * @since 1.0.0
 */
@Component
public class LoanAssembler extends RepresentationModelAssemblerSupport<Loan, LoanResponseModel> {

    public LoanAssembler() {
        super(LoanEndpoint.class, LoanResponseModel.class);
    }

    /**
     * Method used for a link with the loan ID
     * @param loanId
     * @return
     */
    public Link buildLoanSelfLink(String loanId) {
        return linkTo(methodOn(LoanEndpoint.class).getById(loanId)).withSelfRel();
    }

    /**
     * Method used to create a loan return
     * @param loan
     * @return
     */
    @Override
    public LoanResponseModel toModel(Loan loan) {
        final LoanResponseModel model = new LoanResponseModel(loan);
        model.add(buildLoanSelfLink(loan.getId()));
        return model;
    }

    /**
     * Method used for a loan entity based on a requestModel
     * @param loanRequestModel
     * @param book
     * @param client
     * @return
     */
    public Loan toEntity(LoanRequestModel loanRequestModel, Book book, Client client) {
        Loan loan = new Loan();
        loan.setBook(book);
        loan.setClient(client);
        loan.setDevolutionDate(loanRequestModel.getDevolutionDate());
        return loan;
    }
}