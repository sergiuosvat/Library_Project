package model.builder;

import model.Book;
import model.EBook;

public class EBookBuilder extends BookBuilder{
    private String format;

    public EBookBuilder setFormat(String format)
    {
        this.format = format;
        return this;
    }

    @Override
    public EBook build()
    {
        EBook ebook = new EBook();
        ebook.setId(super.build().getId());
        ebook.setAuthor(super.build().getAuthor());
        ebook.setTitle(super.build().getTitle());
        ebook.setPublishedDate(super.build().getPublishedDate());
        ebook.setFormat(format);
        return ebook;
    }
}
