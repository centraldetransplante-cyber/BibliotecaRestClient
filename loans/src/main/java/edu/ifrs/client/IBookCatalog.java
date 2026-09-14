package edu.ifrs.client;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import edu.ifrs.model.Book;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(baseUri = "http://localhost:9080/books")
public interface IBookCatalog {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<Book> listBooks();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Book getBook(@PathParam("id") Long id);

    @PUT
    @Path("/{id}/loan")
    @Produces(MediaType.APPLICATION_JSON)
    Book markAsLoaned(@PathParam("id") Long id);
}