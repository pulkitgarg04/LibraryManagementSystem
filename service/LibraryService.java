package service;

import model.Book;
import java.util.*;

public class LibraryService {
    private List<Book> books;

    public LibraryService(List<Book> books) {
        this.books = books;
    }

    public void addBook(String name, String author, String genre, int quantity) {
        Book book = new Book(name, author, genre, quantity);
        books.add(book);
        mergeSort(books, 0, books.size() - 1);
    }

    public void issueBook(String bookIdOrName, String studentName, String studentId) {
        if (countBooksIssuedToStudent(studentId) >= 5) {
            System.out.println("Student has already borrowed 5 books. Cannot issue more.");
            return;
        }
        Book book = findBookByIdOrName(bookIdOrName);
        if (book != null && book.quantity > 0) {
            book.issueBook(studentName, studentId);
        } else {
            System.out.println("Book is either not available or all copies are issued.");
        }
    }

    public void returnBook(String bookIdOrName) {
        Book book = findBookByIdOrName(bookIdOrName);
        if (book != null && book.isIssued) {
            book.returnBook();
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

    public Book findBookByIdOrName(String idOrName) {
        for (Book book : books) {
            if (book.id.equalsIgnoreCase(idOrName) || book.name.equalsIgnoreCase(idOrName)) {
                return book;
            }
        }
        return null;
    }

    public int countBooksIssuedToStudent(String studentId) {
        int count = 0;
        for (Book book : books) {
            if (book.isIssued && studentId.equals(book.studentId)) {
                count++;
            }
        }
        return count;
    }

    public Book findBookBinarySearch(String name) {
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

    public List<Book> getBooks() {
        return books;
    }
} 