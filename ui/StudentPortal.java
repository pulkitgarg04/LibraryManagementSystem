package ui;

import service.LibraryService;
import java.util.Scanner;

public class StudentPortal {
    public static void show(Scanner sc, LibraryService libraryService) {
        while (true) {
            System.out.println("\n===== Student Portal =====");
            System.out.println("1. Show Available Books");
            System.out.println("2. Search for a Book");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt(); sc.nextLine();

            switch (choice) {
                case 1 -> showAvailableBooks(libraryService);
                case 2 -> {
                    System.out.println("Enter Book Name to Search: ");
                    String bookName = sc.nextLine();
                    var book = libraryService.findBookBinarySearch(bookName);
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

    private static void showAvailableBooks(LibraryService libraryService) {
        System.out.printf("\n%-35s %-20s %-25s\n", "Book Name", "Author", "Genre");
        System.out.println("--------------------------------------------------------------------------------------------");
        boolean hasAvailableBooks = false;
        for (var book : libraryService.getBooks()) {
            if (!book.isIssued) {
                System.out.printf("%-35s %-20s %-25s\n", book.name, book.author, book.genre);
                hasAvailableBooks = true;
            }
        }
        if (!hasAvailableBooks) {
            System.out.println("No books are currently available.");
        }
    }
} 