package edu.ifrs;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import edu.ifrs.model.Book;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/books")
public class CatalogResource {

    private Map<Long, Book> books = new ConcurrentHashMap<>();
    private AtomicLong idGenerator = new AtomicLong(1);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBook(Book book) {
        Long id = idGenerator.getAndIncrement();
        Book newBook = new Book(id, book.title(), book.author(), false);
        books.put(id, newBook);
        return Response.status(Response.Status.CREATED).entity(newBook).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> listBooks() {
        return new java.util.ArrayList<>(books.values());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBook(@PathParam("id") Long id) {
        Book book = books.get(id);
        if (book == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(book).build();
    }

    @PUT
    @Path("/{id}/loan")
    @Produces(MediaType.APPLICATION_JSON)
    public Response loanBook(@PathParam("id") Long id) {
        Book book = books.get(id);
        if (book == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (book.loaned()) {
            return Response.status(Response.Status.CONFLICT).build();
        }
        Book updated = new Book(book.id(), book.title(), book.author(), true);
        books.put(id, updated);
        return Response.ok(updated).build();
    }

    @PUT
    @Path("/{id}/return")
    @Produces(MediaType.APPLICATION_JSON)
    public Response returnBook(@PathParam("id") Long id) {
        Book book = books.get(id);
        if (book == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Book updated = new Book(book.id(), book.title(), book.author(), false);
        books.put(id, updated);
        return Response.ok(updated).build();
    }
}
