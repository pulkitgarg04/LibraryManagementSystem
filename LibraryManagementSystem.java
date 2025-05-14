import java.io.*;
import java.util.*;

class Book implements Comparable<Book> {
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
        this.isIssued = true;
        this.studentName = studentName;
        this.studentId = studentId;
        this.issueDate = new Date().toString();
        System.out.println("Book issued to " + studentName + " (" + studentId + ") successfully!");
    }

    public void returnBook() {
        this.isIssued = false;
        this.returnDate = new Date().toString();
        this.studentName = null;
        this.studentId = null;
        this.issueDate = null;
        System.out.println("Book returned successfully.");
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

    @Override
    public int compareTo(Book other) {
        return this.name.compareToIgnoreCase(other.name);
    }
}

public class LibraryManagementSystem {
    private static final String FILENAME = "books.csv";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "password123";

    private static List<Book> books = new ArrayList<>();

    public LibraryManagementSystem() {
        loadBooksFromCSV();
    }

    private void mergeSort(List<Book> list, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(list, left, mid);
            mergeSort(list, mid + 1, right);
            merge(list, left, mid, right);
        }
    }
    
    private void merge(List<Book> list, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
    
        List<Book> leftList = new ArrayList<>();
        List<Book> rightList = new ArrayList<>();
    
        for (int i = 0; i < n1; i++) {
            leftList.add(list.get(left + i));
        }
        for (int j = 0; j < n2; j++) {
            rightList.add(list.get(mid + 1 + j));
        }
    
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (leftList.get(i).compareTo(rightList.get(j)) <= 0) {
                list.set(k++, leftList.get(i++));
            } else {
                list.set(k++, rightList.get(j++));
            }
        }
    
        while (i < n1) {
            list.set(k++, leftList.get(i++));
        }
    
        while (j < n2) {
            list.set(k++, rightList.get(j++));
        }
    }

    public void addBook(String name, String author, String genre) {
        Book book = new Book(name, author, genre);
        books.add(book);
        mergeSort(books, 0, books.size() - 1);
        System.out.println("Book added successfully: " + name);
        exportBooksToCSV();
    }

    public void issueBook(String bookName, String studentName, String studentId) {
        if (countBooksIssuedToStudent(studentId) >= 5) {
            System.out.println("Student has already borrowed 5 books. Cannot issue more.");
            return;
        }

        Book book = findBookBinarySearch(bookName);
        if (book != null && !book.isIssued) {
            book.issueBook(studentName, studentId);
            exportBooksToCSV();
        } else {
            System.out.println("Book is either not available or already issued.");
        }
    }

    public void returnBook(String bookName) {
        Book book = findBookBinarySearch(bookName);
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

        System.out.printf("\n%-35s %-20s %-25s %-10s %-20s %-10s %-30s %-30s\n",
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

    private Book findBookBinarySearch(String bookName) {
        int left = 0, right = books.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int cmp = books.get(mid).name.compareToIgnoreCase(bookName);
            if (cmp == 0) return books.get(mid);
            if (cmp < 0) left = mid + 1;
            else right = mid - 1;
        }
        return null;
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

    private boolean login() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n===== Admin Login =====");
        System.out.print("Enter Username: ");
        String username = sc.nextLine();
        System.out.print("Enter Password: ");
        String password = sc.nextLine();
        return username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD);
    }

    private void showAvailableBooks() {
        System.out.printf("\n%-35s %-20s %-25s\n", "Book Name", "Author", "Genre");
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

    public void exportBooksToCSV() {
        try (PrintWriter writer = new PrintWriter(new File(FILENAME))) {
            writer.println("Book Name,Author,Genre,Issued,Student Name,Student ID,Issue Date,Return Date");
            for (Book book : books) {
                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s\n",
                        book.name,
                        book.author,
                        book.genre,
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

    public void loadBooksFromCSV() {
        books.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            reader.readLine();
            String line;
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
            Collections.sort(books);
        } catch (IOException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
    }

    public void adminMenu(Scanner sc) {
        while (true) {
            System.out.println("\nAdmin Panel");
            System.out.println("1. Add Book");
            System.out.println("2. Issue Book");
            System.out.println("3. Return Book");
            System.out.println("4. Show All Books");
            System.out.println("5. Show Available Books");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt(); sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Book Name: ");
                    String name = sc.nextLine();
                    System.out.print("Author: ");
                    String author = sc.nextLine();
                    System.out.print("Genre: ");
                    String genre = sc.nextLine();
                    addBook(name, author, genre);
                }
                case 2 -> {
                    System.out.print("Book Name: ");
                    String bookName = sc.nextLine();
                    System.out.print("Student Name: ");
                    String studentName = sc.nextLine();
                    System.out.print("Student ID: ");
                    String studentId = sc.nextLine();
                    issueBook(bookName, studentName, studentId);
                }
                case 3 -> {
                    System.out.print("Book Name: ");
                    String bookName = sc.nextLine();
                    returnBook(bookName);
                }
                case 4 -> displayBooks();
                case 5 -> showAvailableBooks();
                case 6 -> {
                    System.out.println("Exiting Admin Panel...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    public void studentPortal(Scanner sc) {
        while (true) {
            System.out.println("\nStudent Portal");
            System.out.println("1. View Available Books");
            System.out.println("2. Search Book by Name");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt(); sc.nextLine();

            switch (choice) {
                case 1 -> showAvailableBooks();
                case 2 -> {
                    System.out.print("Enter Book Name: ");
                    String bookName = sc.nextLine();
                    Book book = findBookBinarySearch(bookName);
                    if (book != null) book.displayBook();
                    else System.out.println("Book not found.");
                }
                case 3 -> {
                    System.out.println("Exiting Student Portal...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    public static void main(String[] args) {
        LibraryManagementSystem library = new LibraryManagementSystem();
        Scanner sc = new Scanner(System.in);

        System.out.println("Library Management System");
        System.out.println("1. Admin Login");
        System.out.println("2. Student Portal");
        System.out.print("Choose your role: ");
        int role = sc.nextInt(); sc.nextLine();

        if (role == 1) {
            if (!library.login()) {
                System.out.println("Invalid login. Exiting...");
                return;
            }
            library.adminMenu(sc);
        } else if (role == 2) {
            library.studentPortal(sc);
        } else {
            System.out.println("Invalid choice.");
        }

        sc.close();
    }
}