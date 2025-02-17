package com.programacion.distribuida.authors.repo;

import com.programacion.distribuida.authors.db.Author;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.Optional;


@ApplicationScoped
@Transactional
public class AuthorRepository implements PanacheRepositoryBase<Author, Integer> {

    public Optional<Author> updateBook(Integer id, Author author) {
        var obj = this.findByIdOptional(id);

        if(obj.isEmpty()){
            return Optional.empty();
        }

        var authorObj = obj.get();
        authorObj.setName(author.getName());
        authorObj.setLastName(author.getLastName());
        return Optional.of(authorObj);
    }
}
