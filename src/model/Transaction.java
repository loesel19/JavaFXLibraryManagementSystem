package model;

import java.time.LocalDate;
import java.util.*;

public class Transaction {

    private int bookID;
    private int userID;
    private LocalDate issueDate;
    private boolean status; // true : active false : complete

    public Transaction(int book, int user, LocalDate date, boolean stat){
        this.bookID = book;
        this.userID = user;
        this.issueDate = date;
        this.status = stat;
    }


    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public LocalDate getIssueDate() { return issueDate; }

    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Transaction(int bookID, int userID) {
        this.bookID = bookID;
        this.userID = userID;
        this.issueDate = LocalDate.now();
        this.status=true;
    }
}
