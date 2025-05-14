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
            this.studentName = null;
            this.studentId = null;
            this.issueDate = null;
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
        if (countBooksIssuedToStudent(studentId) >= 5) {
            System.out.println("Student has already borrowed 5 books. Cannot issue more.");
            return;
        }

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
        System.out.println("\nAll Books in Library:");

        if (books.isEmpty()) {
            System.out.println("No books are available.");
            return;
        }

        System.out.printf("%-35s %-20s %-25s %-10s %-20s %-10s %-30s %-30s\n",
                "Book Name", "Author", "Genre", "Issued", "Student Name", "ID", "Issue Date", "Return Date");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------");

        for (Book book : books) {
            System.out.printf("%-35s %-20s %-25s %-10s %-20s %-10s %-30s %-30s\n",
                    book.name,
                    book.author,
                    book.genre,
                    book.isIssued ? "Yes" : "No",
                    book.studentName != null ? book.studentName : "-",
                    book.studentId != null ? book.studentId : "-",
                    book.issueDate != null ? book.issueDate : "-",
                    book.returnDate != null ? book.returnDate : "-");
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
        System.out.println("\n===== Admin Login =====");
        System.out.print("Enter Username: ");
        String username = sc.nextLine();
        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            System.out.println("\nLogin Successful!");
            return true;
        } else {
            System.out.println("\nInvalid Credentials! Access Denied.");
            return false;
        }
    }

    private int countBooksIssuedToStudent(String studentId) {
        int count = 0;
        for (Book book : books) {
            if (book.isIssued && studentId.equals(book.studentId)) {
                count++;
            }
        }
        return count;
    }

    private void showAvailableBooks() {
        System.out.println("\nAvailable Books:");
        System.out.printf("%-35s %-20s %-25s\n", "Book Name", "Author", "Genre");
        System.out.println("---------------------------------------------------------------------");

        boolean hasAvailableBooks = false;

        for (Book book : books) {
            if (!book.isIssued) {
                System.out.printf("%-35s %-20s %-25s\n", book.name, book.author, book.genre);
                hasAvailableBooks = true;
            }
        }

        if (!hasAvailableBooks) {
            System.out.println("No books are currently available.");
        }
    }

    public void adminMenu(Scanner sc) {
        while (true) {
            System.out.println("\nLibrary Management System - Admin Panel");
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
                    System.out.print("\nEnter Book Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Author: ");
                    String author = sc.nextLine();
                    System.out.print("Enter Genre: ");
                    String genre = sc.nextLine();
                    addBook(name, author, genre);
                    break;
                case 2:
                    System.out.print("\nEnter Book Name to Issue: ");
                    String bookNameIssue = sc.nextLine();
                    System.out.print("Enter Student Name: ");
                    String studentName = sc.nextLine();
                    System.out.print("Enter Student ID: ");
                    String studentId = sc.nextLine();
                    issueBook(bookNameIssue, studentName, studentId);
                    break;
                case 3:
                    System.out.print("\nEnter Book Name to Return: ");
                    String bookNameReturn = sc.nextLine();
                    returnBook(bookNameReturn);
                    break;
                case 4:
                    displayBooks();
                    break;
                case 5:
                    showAvailableBooks();
                    break;
                case 6:
                    System.out.println("\nExiting Admin Panel...");
                    return;
                default:
                    System.out.println("\nInvalid choice. Please choose a valid option.");
            }
        }
    }

    public void studentPortal(Scanner sc) {
        while (true) {
            System.out.println("\nLibrary Management System - Student Portal");
            System.out.println("1. View Available Books");
            System.out.println("2. Search Book by Name");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    showAvailableBooks();
                    break;
                case 2:
                    System.out.print("\nEnter Book Name to Search: ");
                    String bookName = sc.nextLine();
                    Book book = findBook(bookName);
                    if (book != null) {
                        book.displayBook();
                    } else {
                        System.out.println("Book not found.");
                    }
                    break;
                case 3:
                    System.out.println("\nExiting Student Portal...");
                    return;
                default:
                    System.out.println("\nInvalid choice. Please choose a valid option.");
            }
        }
    }

    public static void main(String[] args) {
        LibraryManagementSystem library = new LibraryManagementSystem();
        Scanner sc = new Scanner(System.in);

        System.out.println("\nWelcome to Library Management System");
        System.out.println("1. Admin Login");
        System.out.println("2. Student Portal");
        System.out.print("Choose your role: ");

        int roleChoice = sc.nextInt();
        sc.nextLine();

        switch (roleChoice) {
            case 1:
                if (!library.login()) {
                    System.out.println("Exiting system...");
                    return;
                }
                library.adminMenu(sc);
                break;
            case 2:
                library.studentPortal(sc);
                break;
            default:
                System.out.println("Invalid role. Exiting...");
        }

        sc.close();
    }
}