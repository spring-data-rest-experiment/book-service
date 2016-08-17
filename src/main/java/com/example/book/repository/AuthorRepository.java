package com.example.book.repository;

import com.example.book.domain.Author;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
@RefreshScope
public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findById(@Param("id") Long id);

    Optional<Author> findByName(@Param("name") String name);

    @Query("SELECT author FROM Author author INNER JOIN author.books book WHERE book.id = :bookId")
    Stream<Author> findByBookId(@Param("bookId") Long bookId);
}
