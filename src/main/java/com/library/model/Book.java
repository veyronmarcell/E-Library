package com.library.model;

public class Book {
    private int bookId;
    private String title;
    private String author;
    private String genre;
    private String isbn;
    private int publicationYear;
    private boolean available;

    public Book() {}

    public Book(String title, String author, String genre, String isbn, int publicationYear) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.available = true;
    }

    // Getters and Setters
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    public int getPublicationYear() { return publicationYear; }
    public void setPublicationYear(int publicationYear) { this.publicationYear = publicationYear; }
    
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return title + " by " + author;
    }
} 