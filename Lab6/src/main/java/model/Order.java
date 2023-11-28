package model;

import java.time.LocalDate;

public class Order {
    private Long id;
    private String author;
    private String title;
    private LocalDate publishedDate;
    private Long user_id;
    private Long employee_id = 0L;

    private int quantity;
    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Long employee_id) {
        this.employee_id = employee_id;
    }

    public boolean hasEmployeeId()
    {
        return getEmployee_id()!=0;
    }
}
