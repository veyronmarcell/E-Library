package com.library.management.service;

import com.library.management.dao.BookDao;
import com.library.management.dao.BorrowingRecordDao;
import com.library.management.dao.ReservationDao;
import com.library.management.model.Book;
import com.library.management.model.BorrowingRecord;
import com.library.management.model.Reservation;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class BookService {
    private BookDao bookDao = new BookDao();
    private BorrowingRecordDao borrowingRecordDao = new BorrowingRecordDao();
    private ReservationDao reservationDao = new ReservationDao();
    private static final int BORROW_LIMIT = 3;
    private static final int BORROW_DURATION_DAYS = 14;
    private static final BigDecimal LATE_FEE_PER_DAY = new BigDecimal("0.50");

    public boolean borrowBook(int bookId, int userId) {
        Optional<Book> bookOptional = bookDao.findBookById(bookId);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            if (book.getAvailabilityStatus() == Book.AvailabilityStatus.AVAILABLE) {
                List<BorrowingRecord> activeBorrows = borrowingRecordDao.findActiveBorrowingRecordsByUserId(userId);
                if (activeBorrows.size() < BORROW_LIMIT) {
                    // Update book status
                    book.setAvailabilityStatus(Book.AvailabilityStatus.BORROWED);
                    bookDao.updateBook(book);

                    // Create borrowing record
                    BorrowingRecord record = new BorrowingRecord();
                    record.setBookId(bookId);
                    record.setUserId(userId);
                    record.setBorrowDate(Date.valueOf(LocalDate.now()));
                    record.setDueDate(Date.valueOf(LocalDate.now().plusDays(BORROW_DURATION_DAYS)));
                    borrowingRecordDao.addBorrowingRecord(record);
                    
                    return true;
                } else {
                    System.out.println("User has reached the borrowing limit of " + BORROW_LIMIT + " books.");
                }
            } else {
                System.out.println("Book is currently not available.");
            }
        } else {
            System.out.println("Book not found.");
        }
        return false;
    }

    public boolean returnBook(int bookId, int userId) {
        List<BorrowingRecord> activeBorrows = borrowingRecordDao.findActiveBorrowingRecordsByUserId(userId);
        Optional<BorrowingRecord> recordOptional = activeBorrows.stream()
                .filter(r -> r.getBookId() == bookId)
                .findFirst();

        if (recordOptional.isPresent()) {
            BorrowingRecord record = recordOptional.get();
            
            // Update borrowing record
            record.setReturnDate(Date.valueOf(LocalDate.now()));
            calculateFine(record);
            borrowingRecordDao.updateBorrowingRecord(record);

            // Update book status
            Optional<Book> bookOptional = bookDao.findBookById(bookId);
            if(bookOptional.isPresent()){
                Book book = bookOptional.get();
                book.setAvailabilityStatus(Book.AvailabilityStatus.AVAILABLE);
                bookDao.updateBook(book);

                // Check for reservations
                notifyNextReservedUser(bookId);
                return true;
            }
        } else {
            System.out.println("User has not borrowed this book or it has already been returned.");
        }
        return false;
    }

    private void calculateFine(BorrowingRecord record) {
        long daysLate = ChronoUnit.DAYS.between(record.getDueDate().toLocalDate(), record.getReturnDate().toLocalDate());
        if (daysLate > 0) {
            BigDecimal fine = LATE_FEE_PER_DAY.multiply(new BigDecimal(daysLate));
            record.setFineAmount(fine);
            System.out.println("A fine of $" + fine + " has been applied for the late return.");
        }
    }

    public void reserveBook(int bookId, int userId) {
        Optional<Book> bookOptional = bookDao.findBookById(bookId);
        if (bookOptional.isPresent() && bookOptional.get().getAvailabilityStatus() == Book.AvailabilityStatus.BORROWED) {
            Reservation reservation = new Reservation();
            reservation.setBookId(bookId);
            reservation.setUserId(userId);
            reservation.setStatus(Reservation.Status.ACTIVE);
            reservationDao.addReservation(reservation);
            System.out.println("Book reserved successfully.");
        } else {
            System.out.println("Book is available for borrowing or does not exist. Reservation not possible.");
        }
    }

    private void notifyNextReservedUser(int bookId) {
        Optional<Reservation> reservationOptional = reservationDao.findActiveReservationByBookId(bookId);
        if (reservationOptional.isPresent()) {
            Reservation reservation = reservationOptional.get();
            System.out.println("Notification (simulation): Book '" + bookDao.findBookById(bookId).get().getTitle() + 
                               "' is now available for user ID " + reservation.getUserId());
            reservationDao.updateReservationStatus(reservation.getReservationId(), Reservation.Status.FULFILLED);
        }
    }

    public Book addBook(String title, String author, String genre, String publisher, String isbn) {
        Book newBook = new Book(title, author, genre, publisher, isbn);
        return bookDao.addBook(newBook);
    }

    // Other methods for searching, etc. can be added here
    public List<Book> getAllBooks() {
        return bookDao.findAllBooks();
    }
} 