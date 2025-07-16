package ui;

import service.LibraryService;
import service.CSVService;
import java.util.Scanner;

public class AdminMenu {
    public static void show(Scanner sc, LibraryService libraryService, CSVService csvService) {
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
                    libraryService.addBook(name, author, genre, quantity);
                    csvService.exportBooksToCSV(libraryService.getBooks());
                    System.out.println("Book added successfully: " + name);
                }
                case 2 -> {
                    System.out.println("Enter Book ID or Name: ");
                    String bookIdOrName = sc.nextLine();
                    System.out.println("Enter Student Name: ");
                    String studentName = sc.nextLine();
                    System.out.println("Enter Student ID: ");
                    String studentId = sc.nextLine();
                    libraryService.issueBook(bookIdOrName, studentName, studentId);
                    csvService.exportBooksToCSV(libraryService.getBooks());
                }
                case 3 -> {
                    System.out.println("Enter Book ID or Name: ");
                    String bookIdOrName = sc.nextLine();
                    libraryService.returnBook(bookIdOrName);
                    csvService.exportBooksToCSV(libraryService.getBooks());
                }
                case 4 -> libraryService.displayBooks();
                case 5 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
} 