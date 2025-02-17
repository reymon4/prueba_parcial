package com.programacion.distribuida.books.repo;

import com.programacion.distribuida.books.db.Book;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Optional;

@ApplicationScoped
@Transactional
public class BookRepository implements PanacheRepositoryBase<Book, Integer> {

    public Optional<Book> updateBook(Integer id, Book book) {
        var obj = this.findByIdOptional(id);

        if(obj.isEmpty()){
            return Optional.empty();
        }

        var bookObj = obj.get();
        bookObj.setIsbn(book.getIsbn());
        bookObj.setTittle(book.getTittle());
        bookObj.setPrice(book.getPrice());
        bookObj.setAuthorId(book.getAuthorId());
        return Optional.of(bookObj);
    }

}
