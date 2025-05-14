import java.io.*;
import java.util.*;

class Book implements Comparable<Book> {
    private static int counter = 1;
    String id;
    String name;
    String author;
    String genre;
    int quantity;
    String studentName;
    String studentId;
    boolean isIssued;
    String issueDate;
    String returnDate;

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

    public void addBook(String name, String author, String genre, int quantity) {
        Book book = new Book(name, author, genre, quantity);
        books.add(book);
        mergeSort(books, 0, books.size() - 1);
        System.out.println("Book added successfully: " + name);
        exportBooksToCSV();
    }

    public void issueBook(String bookIdOrName, String studentName, String studentId) {
        if (countBooksIssuedToStudent(studentId) >= 5) {
            System.out.println("Student has already borrowed 5 books. Cannot issue more.");
            return;
        }

        Book book = findBookByIdOrName(bookIdOrName);
        if (book != null && book.quantity > 0) {
            book.issueBook(studentName, studentId);
            exportBooksToCSV();
        } else {
            System.out.println("Book is either not available or all copies are issued.");
        }
    }

    public void returnBook(String bookIdOrName) {
        Book book = findBookByIdOrName(bookIdOrName);
        if (book != null && book.isIssued) {
            book.returnBook();
            exportBooksToCSV();
        } else {
            System.out.println("No issued book found with this ID/Name.");
        }
    }

    public void displayBooks() {
        if (books.isEmpty()) {
            System.out.println("No books are available.");
            return;
        }

        System.out.printf("\n%-5s %-30s %-20s %-20s %-5s %-8s %-25s %-10s %-30s\n",
                "Id", "Book Name", "Author", "Genre", "Quantity", "Issued", "Student Name", "ID", "Issue Date");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        for (Book book : books) {
            System.out.printf("%5s %-30s %-20s %-20s %-5s %-8s %-25s %-10s %-30s\n",
                    book.id,
                    book.name,
                    book.author,
                    book.genre,
                    book.quantity,
                    book.isIssued ? "Yes" : "No",
                    book.studentName != null ? book.studentName : "-",
                    book.studentId != null ? book.studentId : "-",
                    book.issueDate != null ? book.issueDate : "-");
        }
    }

    private Book findBookByIdOrName(String idOrName) {
        for (Book book : books) {
            if (book.id.equalsIgnoreCase(idOrName) || book.name.equalsIgnoreCase(idOrName)) {
                return book;
            }
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
        System.out.println("--------------------------------------------------------------------------------------------");

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

    public void loadBooksFromCSV() {
        books.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
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

    private Book findBookBinarySearch(String name) {
        int left = 0, right = books.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            Book midBook = books.get(mid);
            int comparison = name.compareToIgnoreCase(midBook.name);
            if (comparison == 0) {
                return midBook;
            } else if (comparison < 0) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return null;
    }

    public void adminMenu(Scanner sc) {
        while (true) {
            System.out.println("\n===== Admin Menu =====");
            System.out.println("1. Add Book");
            System.out.println("2. Issue Book");
            System.out.println("3. Return Book");
            System.out.println("4. Display All Books");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt(); sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.println("Enter Book Name: ");
                    String name = sc.nextLine();
                    System.out.println("Enter Author Name: ");
                    String author = sc.nextLine();
                    System.out.println("Enter Genre: ");
                    String genre = sc.nextLine();
                    System.out.println("Enter Quantity: ");
                    int quantity = sc.nextInt();
                    sc.nextLine();
                    addBook(name, author, genre, quantity);
                }
                case 2 -> {
                    System.out.println("Enter Book ID or Name: ");
                    String bookIdOrName = sc.nextLine();
                    System.out.println("Enter Student Name: ");
                    String studentName = sc.nextLine();
                    System.out.println("Enter Student ID: ");
                    String studentId = sc.nextLine();
                    issueBook(bookIdOrName, studentName, studentId);
                }
                case 3 -> {
                    System.out.println("Enter Book ID or Name: ");
                    String bookIdOrName = sc.nextLine();
                    returnBook(bookIdOrName);
                }
                case 4 -> displayBooks();
                case 5 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    public void studentPortal(Scanner sc) {
        while (true) {
            System.out.println("\n===== Student Portal =====");
            System.out.println("1. Show Available Books");
            System.out.println("2. Search for a Book");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt(); sc.nextLine();

            switch (choice) {
                case 1 -> showAvailableBooks();
                case 2 -> {
                    System.out.println("Enter Book Name to Search: ");
                    String bookName = sc.nextLine();
                    Book book = findBookBinarySearch(bookName);
                    if (book != null) {
                        book.displayBook();
                    } else {
                        System.out.println("Book not found.");
                    }
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

        while (true) {
            System.out.println("\n===== Library Management System =====");
            System.out.println("1. Admin Login");
            System.out.println("2. Student Portal");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt(); sc.nextLine();

            switch (choice) {
                case 1 -> {
                    if (library.login()) {
                        library.adminMenu(sc);
                    } else {
                        System.out.println("Invalid credentials.");
                    }
                }
                case 2 -> library.studentPortal(sc);
                case 3 -> {
                    System.out.println("Exiting Library Management System...");
                    sc.close();
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}