package com.example.book;

import com.example.author.Author;
import com.example.author.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
public class BookService {

    private BookRepository repository;
    private AuthorService authorService;

    @Autowired
    public BookService(BookRepository repository, AuthorService authorService) {
        this.repository = repository;
        this.authorService = authorService;
    }

    public List<Book> findAllBooks() {
        return repository.findAll();
    }

    public Optional<Book> findBookByBookId(Long bookId) {
        return repository.findById(bookId);
    }

    public Optional<Book> findBookByName(String name) {
        return repository.findByName(name);
    }

    public Stream<Book> findBooksByAuthorId(Long authorId) {
        return repository.findBooksForAuthorId(authorId);
    }

    public Book save(Book book) {
        findBookByName(book.getName())
                .ifPresent(ignore -> {
                    throw new BookAlreadyExistsException();
                });

        List<Author> authors = book.getAuthors().stream()
                .map(author ->
                        authorService.findAuthorByName(author.getName())
                                .orElseGet(() -> {
                                    authorService.save(author);
                                    return author;
                                })
                ).collect(toList());

        book.setAuthors(authors);
        return repository.save(book);
    }
}
