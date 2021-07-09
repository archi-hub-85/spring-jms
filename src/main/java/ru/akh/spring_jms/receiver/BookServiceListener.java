package ru.akh.spring_jms.receiver;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import ru.akh.spring_jms.constants.ApplicationConstants;
import ru.akh.spring_jms.converter.BookContentReadConverter;
import ru.akh.spring_jms.converter.BookContentWriteConverter;
import ru.akh.spring_jms.converter.BookReadConverter;
import ru.akh.spring_jms.converter.BookWriteConverter;
import ru.akh.spring_jms.dao.BookRepository;
import ru.akh.spring_jms.dto.Book;
import ru.akh.spring_jms.dto.BookContent;
import ru.akh.spring_jms.schema.GetBooksByAuthorRequest;
import ru.akh.spring_jms.schema.GetBooksByAuthorResponse;
import ru.akh.spring_jms.schema.GetContentRequest;
import ru.akh.spring_jms.schema.GetContentResponse;
import ru.akh.spring_jms.schema.GetRequest;
import ru.akh.spring_jms.schema.GetResponse;
import ru.akh.spring_jms.schema.GetTopBooksRequest;
import ru.akh.spring_jms.schema.GetTopBooksResponse;
import ru.akh.spring_jms.schema.ObjectFactory;
import ru.akh.spring_jms.schema.PutContentRequest;
import ru.akh.spring_jms.schema.PutRequest;
import ru.akh.spring_jms.schema.PutResponse;

@Component
@SendTo(ApplicationConstants.RESPONSE_QUEUE)
public class BookServiceListener {

    private final BookRepository repository;

    private final ObjectFactory factory = new ObjectFactory();

    public BookServiceListener(BookRepository repository) {
        this.repository = repository;
    }

    @JmsListener(destination = ApplicationConstants.REQUEST_QUEUE, selector = ApplicationConstants.REQUEST_TYPE_PROPERTY
            + " = 'GetRequest'")
    public GetResponse get(GetRequest request) {
        Book book = repository.get(request.getId());

        GetResponse response = factory.createGetResponse();
        response.setResult(BookWriteConverter.INSTANCE.convert(book));

        return response;
    }

    @JmsListener(destination = ApplicationConstants.REQUEST_QUEUE, selector = ApplicationConstants.REQUEST_TYPE_PROPERTY
            + " = 'PutRequest'")
    public PutResponse put(PutRequest request) {
        Book book = BookReadConverter.INSTANCE.convert(request.getBook());
        long id = repository.put(book);

        PutResponse response = factory.createPutResponse();
        response.setResult(id);
        return response;
    }

    @JmsListener(destination = ApplicationConstants.REQUEST_QUEUE, selector = ApplicationConstants.REQUEST_TYPE_PROPERTY
            + " = 'GetTopBooksRequest'")
    public GetTopBooksResponse getTopBooks(GetTopBooksRequest request) {
        Book.Field field = Book.Field.valueOf(request.getField().name());
        List<Book> books = repository.getTopBooks(field, request.getLimit());

        GetTopBooksResponse response = factory.createGetTopBooksResponse();
        if (!books.isEmpty()) {
            response.getResult()
                    .addAll(books.stream().map(BookWriteConverter.INSTANCE::convert).collect(Collectors.toList()));
        }
        return response;
    }

    @JmsListener(destination = ApplicationConstants.REQUEST_QUEUE, selector = ApplicationConstants.REQUEST_TYPE_PROPERTY
            + " = 'GetBooksByAuthorRequest'")
    public GetBooksByAuthorResponse getBooksByAuthor(GetBooksByAuthorRequest request) {
        List<Book> books = repository.getBooksByAuthor(request.getAuthor());

        GetBooksByAuthorResponse response = factory.createGetBooksByAuthorResponse();
        if (!books.isEmpty()) {
            response.getResult()
                    .addAll(books.stream().map(BookWriteConverter.INSTANCE::convert).collect(Collectors.toList()));
        }
        return response;
    }

    @JmsListener(destination = ApplicationConstants.REQUEST_QUEUE, selector = ApplicationConstants.REQUEST_TYPE_PROPERTY
            + " = 'GetContentRequest'")
    public GetContentResponse getContent(GetContentRequest request) {
        BookContent bookContent = repository.getContent(request.getId());

        GetContentResponse response = factory.createGetContentResponse();
        response.setResult(BookContentWriteConverter.INSTANCE.convert(bookContent));
        return response;
    }

    @JmsListener(destination = ApplicationConstants.REQUEST_QUEUE, selector = ApplicationConstants.REQUEST_TYPE_PROPERTY
            + " = 'PutContentRequest'")
    public void putContent(PutContentRequest request) {
        BookContent bookContent = BookContentReadConverter.INSTANCE.convert(request.getContent());
        repository.putContent(bookContent);
    }

}
