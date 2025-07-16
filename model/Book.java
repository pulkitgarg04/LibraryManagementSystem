package model;

import java.util.*;

public class Book implements Comparable<Book> {
    private static int counter = 1;
    public String id;
    public String name;
    public String author;
    public String genre;
    public int quantity;
    public String studentName;
    public String studentId;
    public boolean isIssued;
    public String issueDate;
    public String returnDate;

    public Book(String name, String author, String genre, int quantity) {
        this.id = "B" + (counter++);
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.quantity = quantity;
        this.isIssued = false;
    }

    public void issueBook(String studentName, String studentId) {
        if (quantity <= 0) {
            System.out.println("No copies available for issuance.");
            return;
        }
        this.isIssued = true;
        this.studentName = studentName;
        this.studentId = studentId;
        this.issueDate = new Date().toString();
        this.quantity--;
        System.out.println("Book issued to " + studentName + " (" + studentId + ") successfully!");
    }

    public void returnBook() {
        if (isIssued) {
            this.isIssued = false;
            this.returnDate = new Date().toString();
            this.studentName = null;
            this.studentId = null;
            this.issueDate = null;
            this.quantity++;
            System.out.println("Book returned successfully.");
        } else {
            System.out.println("This book was not issued.");
        }
    }

    public void displayBook() {
        System.out.println("\nBook ID: " + id);
        System.out.println("Book Name: " + name);
        System.out.println("Author: " + author);
        System.out.println("Genre: " + genre);
        System.out.println("Quantity Available: " + quantity);
        System.out.println("Issued: " + (isIssued ? "Yes" : "No"));
        if (isIssued) {
            System.out.println("Issued to: " + studentName + " (ID: " + studentId + ")");
            System.out.println("Issue Date: " + issueDate);
        }
        if (returnDate != null) {
            System.out.println("Last Return Date: " + returnDate);
        }
    }

    @Override
    public int compareTo(Book other) {
        return this.name.compareToIgnoreCase(other.name);
    }
} 