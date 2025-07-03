package com.library.management.model;

public class Book {
    private int bookId;
    private String title;
    private String author;
    private String genre;
    private String publisher;
    private String isbn;
    private AvailabilityStatus availabilityStatus;
    private double averageRating;

    public enum AvailabilityStatus {
        AVAILABLE, BORROWED
    }

    // Constructors
    public Book() {
    }

    public Book(String title, String author, String genre, String publisher, String isbn) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publisher = publisher;
        this.isbn = isbn;
        this.availabilityStatus = AvailabilityStatus.AVAILABLE;
        this.averageRating = 0.0;
    }

    // Getters and Setters
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", availabilityStatus=" + availabilityStatus +
                '}';
    }
} 