package edu.ifrs;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import edu.ifrs.client.IBookCatalog;
import edu.ifrs.model.Book;
import edu.ifrs.model.Loan;
import edu.ifrs.model.LoanRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/loans")
public class LoanResource {

    @Inject
    @RestClient
    IBookCatalog bookCatalog;

    private Map<Long, Loan> loans = new ConcurrentHashMap<>();
    private AtomicLong idGenerator = new AtomicLong(1);

    @GET
    @Path("/books")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> listBooks() {
        return bookCatalog.listBooks();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response newLoan(LoanRequest request) {
        Book book;
        try {
            book = bookCatalog.getBook(request.bookId());
        } catch (WebApplicationException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (book.loaned()) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        bookCatalog.markAsLoaned(request.bookId());

        Long id = idGenerator.getAndIncrement();
        Loan loan = new Loan(id, request.bookId(), request.borrower());
        loans.put(id, loan);

        return Response.status(Response.Status.CREATED).entity(loan).build();
    }
}
