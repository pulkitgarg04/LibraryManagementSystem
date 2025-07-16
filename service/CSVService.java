package service;

import model.Book;
import java.io.*;
import java.util.*;

public class CSVService {
    private final String filename;

    public CSVService(String filename) {
        this.filename = filename;
    }

    public void exportBooksToCSV(List<Book> books) {
        try (PrintWriter writer = new PrintWriter(new File(filename))) {
            writer.println("ID,Book Name,Author,Genre,Quantity,Issued,Student Name,Student ID,Issue Date,Return Date");
            for (Book book : books) {
                writer.printf("%s,%s,%s,%s,%d,%s,%s,%s,%s,%s\n",
                        book.id,
                        book.name,
                        book.author,
                        book.genre,
                        book.quantity,
                        book.isIssued,
                        book.studentName != null ? book.studentName : "",
                        book.studentId != null ? book.studentId : "",
                        book.issueDate != null ? book.issueDate : "",
                        book.returnDate != null ? book.returnDate : "");
            }
        } catch (IOException e) {
            System.out.println("Error exporting books: " + e.getMessage());
        }
    }

    public void loadBooksFromCSV(List<Book> books) {
        books.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                Book book = new Book(parts[1], parts[2], parts[3], Integer.parseInt(parts[4]));
                book.id = parts[0];
                book.isIssued = Boolean.parseBoolean(parts[5]);
                book.studentName = parts[6].isEmpty() ? null : parts[6];
                book.studentId = parts[7].isEmpty() ? null : parts[7];
                book.issueDate = parts[8].isEmpty() ? null : parts[8];
                book.returnDate = parts[9].isEmpty() ? null : parts[9];
                books.add(book);
            }
        } catch (IOException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
    }
} 