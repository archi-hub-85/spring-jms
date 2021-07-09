package ru.akh.spring_jms.converter;

import org.springframework.core.convert.converter.Converter;

import ru.akh.spring_jms.schema.Author;

public class AuthorReadConverter implements Converter<Author, ru.akh.spring_jms.dto.Author> {

    public static final AuthorReadConverter INSTANCE = new AuthorReadConverter();

    private AuthorReadConverter() {
    }

    @Override
    public ru.akh.spring_jms.dto.Author convert(Author source) {
        ru.akh.spring_jms.dto.Author author = new ru.akh.spring_jms.dto.Author();
        author.setId(source.getId());
        author.setName(source.getName());

        return author;
    }

}
