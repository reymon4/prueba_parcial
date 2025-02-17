package com.programacion.distribuida.books.rest;

import com.programacion.distribuida.books.clients.AuthorRestClient;
import com.programacion.distribuida.books.db.Book;
import com.programacion.distribuida.books.dto.AuthorDTO;
import com.programacion.distribuida.books.repo.BookRepository;
import com.programacion.distribuida.books.dto.BookDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class BookRest {

    @Inject
    BookRepository repository;

    @Inject
    @RestClient
    AuthorRestClient authorRestClient;
   /* @ConfigProperty(name="authors.server")
    String authorsServer;*/

    @POST
    public Response create(Book book) {
       repository.persist(book);
       return Response.ok(book).build();
    }

    @GET
    public List<BookDTO> findAll() {
        System.out.println("entro a buscar todos los libros");
        //Version 1
        //return repository.findAll().list();

        //Version 2 with JAX-RS Client
        /*var client = ClientBuilder.newClient();
       return  repository.streamAll().map(book -> {
           //http://localhost:9090/authors/id
            System.out.println("Author con id: "+book.getAuthorId());
            var author = client.target(authorsServer).path("/authors/{id}")
                    .resolveTemplate("id", book.getAuthorId())
                    .request(MediaType.APPLICATION_JSON)
                    .get(AuthorDTO.class);

            var dto = new BookDTO();
            dto.setId(book.getId());
            dto.setIsbn(book.getIsbn());
            dto.setTittle(book.getTittle());
            dto.setPrice(book.getPrice());
            dto.setAuthor(author.getName() + " " + author.getApellido());
           System.out.println("Libro: "+dto);
            return dto;
        }).toList();*/

       //Version 3 with MP Client Manual
        /*AuthorRestClient client =RestClientBuilder.newBuilder().baseUri(authorsServer).build(AuthorRestClient.class);
        return  repository.streamAll().map(book -> {
            var author = client.findById(book.getAuthorId());
            var dto = new BookDTO();
            dto.setId(book.getId());
            dto.setIsbn(book.getIsbn());
            dto.setTittle(book.getTittle());
            dto.setPrice(book.getPrice());
            dto.setAuthor(author.getName() + " " + author.getApellido());
            return dto;
        }).toList();*/
        //Version 4 with MP Client Auto
        return  repository.streamAll().map(book -> {
            System.out.println("Author con id: "+book.getAuthorId());

            var author = authorRestClient.findById(book.getAuthorId());
            System.out.println(author.toString());
            var dto = new BookDTO();
            dto.setId(book.getId());
            dto.setIsbn(book.getIsbn());
            dto.setTittle(book.getTittle());
            dto.setPrice(book.getPrice());
            dto.setAuthor(author.getName() + " " + author.getLastName());
            System.out.println("Libro: "+dto);
            return dto;

        }).toList();

    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Integer id) {
        var obj = repository.findByIdOptional(id);

        if(obj.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            var book = obj.get();
            var author = authorRestClient.findById(book.getAuthorId());
            var dto = new BookDTO();
            dto.setId(book.getId());
            dto.setIsbn(book.getIsbn());
            dto.setTittle(book.getTittle());
            dto.setPrice(book.getPrice());
            dto.setAuthor(author.getName() + " " + author.getLastName());
            return Response.ok(dto).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Integer id, Book book) {
        var obj = repository.updateBook(id, book);

        if(obj.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(obj.get()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Integer id) {
        repository.deleteById(id);
    }
}
