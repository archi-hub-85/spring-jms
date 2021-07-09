package ru.akh.spring_jms.converter;

import org.springframework.core.convert.converter.Converter;

import ru.akh.spring_jms.schema.BookContent;
import ru.akh.spring_jms.schema.ObjectFactory;

public class BookContentWriteConverter implements Converter<ru.akh.spring_jms.dto.BookContent, BookContent> {

    public static final BookContentWriteConverter INSTANCE = new BookContentWriteConverter();

    private final ObjectFactory factory = new ObjectFactory();

    private BookContentWriteConverter() {
    }

    @Override
    public BookContent convert(ru.akh.spring_jms.dto.BookContent source) {
        BookContent bookContent = factory.createBookContent();
        bookContent.setId(source.getId());
        bookContent.setFileName(source.getFileName());
        bookContent.setMimeType(source.getMimeType());
        bookContent.setContent(source.getContent());
        bookContent.setSize(source.getSize());

        return bookContent;
    }

}
