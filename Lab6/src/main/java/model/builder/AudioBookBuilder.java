package model.builder;

import model.AudioBook;

public class AudioBookBuilder extends BookBuilder {
    private int runTime;

    public AudioBookBuilder setRunTime(int runTime)
    {
        this.runTime = runTime;
        return this;
    }

    public AudioBook build()
    {
        AudioBook audioBook = new AudioBook();
        audioBook.setId(super.build().getId());
        audioBook.setAuthor(super.build().getAuthor());
        audioBook.setTitle(super.build().getTitle());
        audioBook.setPublishedDate(super.build().getPublishedDate());
        audioBook.setRunTime(runTime);
        return audioBook;
    }

}
