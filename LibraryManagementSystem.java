import java.io.*;
import java.util.*;

class Book {
    String name;
    String author;
    String genre;
    String studentName;
    String studentId;
    boolean isIssued;
    String issueDate;
    String returnDate;

    public Book(String name, String author, String genre) {
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.isIssued = false;
    }

    public void issueBook(String studentName, String studentId) {
        if (!isIssued) {
            this.isIssued = true;
            this.studentName = studentName;
            this.studentId = studentId;
            this.issueDate = new Date().toString();
            System.out.println("Book issued to " + studentName + " (" + studentId + ") successfully!");
        } else {
            System.out.println("Book is already issued to " + this.studentName);
        }
    }

    public void returnBook() {
        if (isIssued) {
            this.isIssued = false;
            this.returnDate = new Date().toString();
            System.out.println("Book returned successfully.");
        } else {
            System.out.println("Book is not issued!");
        }
    }

    public void displayBook() {
        System.out.println("\nBook Name: " + name);
        System.out.println("Author: " + author);
        System.out.println("Genre: " + genre);
        System.out.println("Issued: " + (isIssued ? "Yes" : "No"));
        if (isIssued) {
            System.out.println("Issued to: " + studentName + " (ID: " + studentId + ")");
            System.out.println("Issue Date: " + issueDate);
        }
        if (returnDate != null) {
            System.out.println("Last Return Date: " + returnDate);
        }
    }
}

public class LibraryManagementSystem {
    private static final String FILENAME = "books.csv";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "password123";

    private static ArrayList<Book> books = new ArrayList<>();

    public LibraryManagementSystem() {
        loadBooksFromCSV();
    }

    public void addBook(String name, String author, String genre) {
        Book book = new Book(name, author, genre);
        books.add(book);
        System.out.println("Book added successfully: " + name);
        exportBooksToCSV();
    }

    public void issueBook(String bookName, String studentName, String studentId) {
        Book book = findBook(bookName);
        if (book != null && !book.isIssued) {
            book.issueBook(studentName, studentId);
            exportBooksToCSV();
        } else {
            System.out.println("Book is either not available or already issued.");
        }
    }

    public void returnBook(String bookName) {
        Book book = findBook(bookName);
        if (book != null && book.isIssued) {
            book.returnBook();
            exportBooksToCSV();
        } else {
            System.out.println("No issued book found with this name.");
        }
    }

    public void displayBooks() {
        if (books.isEmpty()) {
            System.out.println("No books are available.");
            return;
        }
        for (Book book : books) {
            book.displayBook();
        }
    }

    private Book findBook(String name) {
        for (Book book : books) {
            if (book.name.equalsIgnoreCase(name)) {
                return book;
            }
        }
        return null;
    }

    public void exportBooksToCSV() {
        try (PrintWriter writer = new PrintWriter(new File(FILENAME))) {
            StringBuilder sb = new StringBuilder();
            sb.append("Book Name,Author,Genre,Issued,Student Name,Student ID,Issue Date,Return Date\n");
            for (Book book : books) {
                sb.append(book.name).append(",");
                sb.append(book.author).append(",");
                sb.append(book.genre).append(",");
                sb.append(book.isIssued).append(",");
                sb.append(book.studentName != null ? book.studentName : "").append(",");
                sb.append(book.studentId != null ? book.studentId : "").append(",");
                sb.append(book.issueDate != null ? book.issueDate : "").append(",");
                sb.append(book.returnDate != null ? book.returnDate : "").append("\n");
            }
            writer.write(sb.toString());
            System.out.println("Books exported to CSV file: " + FILENAME);
        } catch (IOException e) {
            System.out.println("An error occurred while exporting books to CSV: " + e.getMessage());
        }
    }

    public void loadBooksFromCSV() {
        books.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                Book book = new Book(parts[0], parts[1], parts[2]);
                book.isIssued = Boolean.parseBoolean(parts[3]);
                book.studentName = parts[4].isEmpty() ? null : parts[4];
                book.studentId = parts[5].isEmpty() ? null : parts[5];
                book.issueDate = parts[6].isEmpty() ? null : parts[6];
                book.returnDate = parts[7].isEmpty() ? null : parts[7];
                books.add(book);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No previous data found. Starting fresh.");
        } catch (IOException e) {
            System.out.println("An error occurred while loading books: " + e.getMessage());
        }
    }

    private boolean login() {
        Scanner sc = new Scanner(System.in);
        System.out.println("===== Admin Login =====");
        System.out.print("Enter Username: ");
        String username = sc.nextLine();
        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            System.out.println("Login Successful!");
            return true;
        } else {
            System.out.println("Invalid Credentials! Access Denied.");
            return false;
        }
    }

    private void showAvailableBooks() {
        System.out.println("\nAvailable Books:");
        System.out.println("Name | Author | Genre");
        System.out.println("---------------------");

        boolean hasAvailableBooks = false;

        for (Book book : books) {
            if (!book.isIssued) {
                System.out.println(book.name + " | " + book.author + " | " + book.genre);
                hasAvailableBooks = true;
            }
        }

        if (!hasAvailableBooks) {
            System.out.println("No books are currently available.");
        }
    }

    public static void main(String[] args) {
        LibraryManagementSystem library = new LibraryManagementSystem();
        Scanner sc = new Scanner(System.in);

        if (!library.login()) {
            System.out.println("Exiting system...");
            return;
        }

        while (true) {
            System.out.println("\nLibrary Management System");
            System.out.println("1. Add Book");
            System.out.println("2. Issue Book");
            System.out.println("3. Return Book");
            System.out.println("4. Show All Books");
            System.out.println("5. Show Available Books");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Book Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Author: ");
                    String author = sc.nextLine();
                    System.out.print("Enter Genre: ");
                    String genre = sc.nextLine();
                    library.addBook(name, author, genre);
                    break;
                case 2:
                    System.out.print("Enter Book Name to Issue: ");
                    String bookNameIssue = sc.nextLine();
                    System.out.print("Enter Student Name: ");
                    String studentName = sc.nextLine();
                    System.out.print("Enter Student ID: ");
                    String studentId = sc.nextLine();
                    library.issueBook(bookNameIssue, studentName, studentId);
                    break;
                case 3:
                    System.out.print("Enter Book Name to Return: ");
                    String bookNameReturn = sc.nextLine();
                    library.returnBook(bookNameReturn);
                    break;
                case 4:
                    library.displayBooks();
                    break;
                case 5:
                    library.showAvailableBooks();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
            }
        }
    }
}