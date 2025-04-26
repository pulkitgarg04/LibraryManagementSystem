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

    public String toCsvFormat() {
        return String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                name, author, genre, isIssued,
                studentName != null ? studentName : "",
                studentId != null ? studentId : "",
                issueDate != null ? issueDate : "",
                returnDate != null ? returnDate : "");
    }
}

public class LibraryManagementSystem {
    private static ArrayList<Book> books = new ArrayList<>();
    private static final String CSV_FILE_NAME = "books.csv";
    private static final String CSV_HEADER = "Name,Author,Genre,IsIssued,StudentName,StudentID,IssueDate,ReturnDate";
    private static HashMap<String, Book> bookMap = new HashMap<>();

    public void addBook(String name, String author, String genre) {
        Book book = new Book(name, author, genre);
        books.add(book);
        bookMap.put(name.toLowerCase(), book);
        System.out.println("Book added successfully: " + name);
        saveToCsv();
    }

    public void issueBook(String bookName, String studentName, String studentId) {
        Book book = binarySearchBook(bookName);
        if (book != null && !book.isIssued) {
            book.issueBook(studentName, studentId);
            saveToCsv();
        } else {
            System.out.println("Book is either not available or already issued.");
        }
    }

    public void returnBook(String bookName) {
        Book book = binarySearchBook(bookName);
        if (book != null && book.isIssued) {
            book.returnBook();
            saveToCsv();
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

    private void sortBooks() {
        books = mergeSort(books);
    }

    private ArrayList<Book> mergeSort(ArrayList<Book> list) {
        if (list.size() <= 1) return list;

        int mid = list.size() / 2;
        ArrayList<Book> left = mergeSort(new ArrayList<>(list.subList(0, mid)));
        ArrayList<Book> right = mergeSort(new ArrayList<>(list.subList(mid, list.size())));

        return merge(left, right);
    }

    private ArrayList<Book> merge(ArrayList<Book> left, ArrayList<Book> right) {
        ArrayList<Book> result = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i).name.compareToIgnoreCase(right.get(j).name) <= 0) {
                result.add(left.get(i++));
            } else {
                result.add(right.get(j++));
            }
        }
        while (i < left.size()) result.add(left.get(i++));
        while (j < right.size()) result.add(right.get(j++));
        return result;
    }

    private Book binarySearchBook(String name) {
        sortBooks();
        int left = 0;
        int right = books.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            Book midBook = books.get(mid);
            int cmp = midBook.name.compareToIgnoreCase(name);

            if (cmp == 0) return midBook;
            else if (cmp < 0) left = mid + 1;
            else right = mid - 1;
        }
        return null;
    }

    private void saveToCsv() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_NAME))) {
            writer.write(CSV_HEADER);
            writer.newLine();
            for (Book book : books) {
                writer.write(book.toCsvFormat());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }

    private void loadFromCsv() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_NAME))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                Book book = new Book(parts[0].replace("\"", ""), parts[1].replace("\"", ""), parts[2].replace("\"", ""));
                book.isIssued = Boolean.parseBoolean(parts[3].replace("\"", ""));
                book.studentName = parts[4].replace("\"", "").isEmpty() ? null : parts[4].replace("\"", "");
                book.studentId = parts[5].replace("\"", "").isEmpty() ? null : parts[5].replace("\"", "");
                book.issueDate = parts[6].replace("\"", "").isEmpty() ? null : parts[6].replace("\"", "");
                book.returnDate = parts[7].replace("\"", "").isEmpty() ? null : parts[7].replace("\"", "");
                books.add(book);
                bookMap.put(book.name.toLowerCase(), book);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No previous data found. Starting fresh.");
        } catch (IOException e) {
            System.out.println("Error loading data from file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        LibraryManagementSystem library = new LibraryManagementSystem();
        library.loadFromCsv();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1) Add Book");
            System.out.println("2) Issue Book");
            System.out.println("3) Return Book");
            System.out.println("4) Display Books");
            System.out.println("5) Exit");
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
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
            }
        }
    }
}