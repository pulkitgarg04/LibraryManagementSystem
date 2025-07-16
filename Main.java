import model.Book;
import service.LibraryService;
import ui.AdminMenu;
import ui.StudentPortal;
import service.CSVService;
import service.AuthService;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        final String FILENAME = "books.csv";
        List<Book> books = new ArrayList<>();
        CSVService csvService = new CSVService(FILENAME);
        csvService.loadBooksFromCSV(books);
        LibraryService libraryService = new LibraryService(books);
        AuthService authService = new AuthService();
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
                    System.out.println("\n===== Admin Login =====");
                    System.out.print("Enter Username: ");
                    String username = sc.nextLine();
                    System.out.print("Enter Password: ");
                    String password = sc.nextLine();
                    if (authService.login(username, password)) {
                        AdminMenu.show(sc, libraryService, csvService);
                    } else {
                        System.out.println("Invalid credentials.");
                    }
                }
                case 2 -> StudentPortal.show(sc, libraryService);
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