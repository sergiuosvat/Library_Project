package model;

public class EBook extends Book{
    private String format;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public String toString(){
        return String.format("Book author: %s | title: %s | Published Date: %s | Format: %s.", super.getAuthor(), super.getTitle(), super.getPublishedDate(), format);
    }
}
