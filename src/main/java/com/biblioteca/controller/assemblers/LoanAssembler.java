package com.biblioteca.controller.assemblers;

import com.biblioteca.controller.endpoints.LoanEndpoint;
import com.biblioteca.controller.models.request.LoanRequestModel;
import com.biblioteca.controller.models.response.LoanResponseModel;
import com.biblioteca.model.entities.Book;
import com.biblioteca.model.entities.Client;
import com.biblioteca.model.entities.Loan;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LoanAssembler extends RepresentationModelAssemblerSupport<Loan, LoanResponseModel> {

    public LoanAssembler() {
        super(LoanEndpoint.class, LoanResponseModel.class);
    }

    public Link buildLoanSelfLink(String loanId) {
        return linkTo(methodOn(LoanEndpoint.class).getById(loanId)).withSelfRel();
    }

    @Override
    public LoanResponseModel toModel(Loan loan) {
        final LoanResponseModel model = new LoanResponseModel(loan);
        model.add(buildLoanSelfLink(loan.getId()));
        return model;
    }

    public Loan toEntity(LoanRequestModel loanRequestModel, Book book, Client client) {
        Loan loan = new Loan();
        loan.setBook(book);
        loan.setClient(client);
        loan.setDevolutionDate(loanRequestModel.getDevolutionDate());
        return loan;
    }
}