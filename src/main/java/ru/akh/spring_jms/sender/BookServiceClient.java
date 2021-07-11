package ru.akh.spring_jms.sender;

import java.util.List;
import java.util.stream.Collectors;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsUtils;

import ru.akh.spring_jms.constants.ApplicationConstants;
import ru.akh.spring_jms.converter.BookContentReadConverter;
import ru.akh.spring_jms.converter.BookContentWriteConverter;
import ru.akh.spring_jms.converter.BookReadConverter;
import ru.akh.spring_jms.converter.BookWriteConverter;
import ru.akh.spring_jms.dto.Book;
import ru.akh.spring_jms.dto.BookContent;
import ru.akh.spring_jms.schema.BookField;
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
import ru.akh.spring_jms.schema.PutContentResponse;
import ru.akh.spring_jms.schema.PutRequest;
import ru.akh.spring_jms.schema.PutResponse;

public class BookServiceClient {

    private final JmsTemplate template;

    private final ObjectFactory factory = new ObjectFactory();

    public BookServiceClient(JmsTemplate template) {
        this.template = template;
    }

    @SuppressWarnings("unchecked")
    private <T> T sendAndReceive(Object request, Class<T> responseClass) {
        Message responseMessage = template.sendAndReceive(ApplicationConstants.REQUEST_QUEUE, session -> {
            Message message = template.getMessageConverter().toMessage(request, session);
            message.setStringProperty(ApplicationConstants.REQUEST_TYPE_PROPERTY, request.getClass().getSimpleName());
            return message;
        });

        T response;
        try {
            response = (T) template.getMessageConverter().fromMessage(responseMessage);
        } catch (JMSException e) {
            throw JmsUtils.convertJmsAccessException(e);
        }

        return response;
    }

    public Book get(long id) {
        GetRequest request = factory.createGetRequest();
        request.setId(id);

        GetResponse response = sendAndReceive(request, GetResponse.class);
        return BookReadConverter.INSTANCE.convert(response.getResult());
    }

    public long put(Book book) {
        PutRequest request = factory.createPutRequest();
        request.setBook(BookWriteConverter.INSTANCE.convert(book));

        PutResponse response = sendAndReceive(request, PutResponse.class);
        return response.getResult();
    }

    public List<Book> getTopBooks(Book.Field field, int limit) {
        GetTopBooksRequest request = factory.createGetTopBooksRequest();
        request.setField(BookField.valueOf(field.name()));
        request.setLimit(limit);

        GetTopBooksResponse response = sendAndReceive(request, GetTopBooksResponse.class);
        return response.getResult().stream().map(BookReadConverter.INSTANCE::convert).collect(Collectors.toList());
    }

    public List<Book> getBooksByAuthor(String author) {
        GetBooksByAuthorRequest request = factory.createGetBooksByAuthorRequest();
        request.setAuthor(author);

        GetBooksByAuthorResponse response = sendAndReceive(request, GetBooksByAuthorResponse.class);
        return response.getResult().stream().map(BookReadConverter.INSTANCE::convert).collect(Collectors.toList());
    }

    public BookContent getContent(long id) {
        GetContentRequest request = factory.createGetContentRequest();
        request.setId(id);

        GetContentResponse response = sendAndReceive(request, GetContentResponse.class);
        return BookContentReadConverter.INSTANCE.convert(response.getResult());
    }

    public void putContent(BookContent content) {
        PutContentRequest request = factory.createPutContentRequest();
        request.setContent(BookContentWriteConverter.INSTANCE.convert(content));

        sendAndReceive(request, PutContentResponse.class);
    }

}
