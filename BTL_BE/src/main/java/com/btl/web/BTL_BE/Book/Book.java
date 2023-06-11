package com.btl.web.BTL_BE.Book;

import java.sql.Date;

public class Book {

    private int id;
    private String title;
    private String author;
    private int categoryId;
    private String category;
    private Date releaseDate;
    private int pageNumber;
    private int soldNumber;
    private String cover;
    private String description;

    public Book(int id, String title, String author, int categoryID, String category, Date releaseDate, int pageNumber, int soldNumber, String cover, String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.categoryId = categoryID;
        this.category = category;
        this.releaseDate = releaseDate;
        this.pageNumber = pageNumber;
        this.soldNumber = soldNumber;
        this.cover = cover;
        this.description = description;
    }

    public Book() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getSoldNumber() {
        return soldNumber;
    }

    public void setSoldNumber(int soldNumber) {
        this.soldNumber = soldNumber;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
