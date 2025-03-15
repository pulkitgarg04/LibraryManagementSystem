# Library Management System

A Java-based application for managing a library's book inventory, including issuing and returning books. This project demonstrates core object-oriented programming principles, file handling, and console-based interaction.

---

## Features

- Add new books to the library inventory.
- Issue books to students with their details.
- Return issued books and update their status.
- View all books in the library, including their current status.
- Save and load data from CSV for persistent storage.

---

## Prerequisites

Ensure you have the following installed:

- [Java JDK](https://www.oracle.com/java/technologies/javase-downloads.html) (version 8 or above)

---

## How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/pulkitgarg04/LibraryManagementSystem.git
   cd LibraryManagementSystem
   ```

2. Compile the Java files:
    ```bash
    javac LibraryManagementSystem.java
    ```

3. Run the application:
    ```bash
    java LibraryManagementSystem
    ```

---

## Usage
1. Add Book: Input the book name, author, and genre to add a new book.
2. Issue Book: Provide the book name, student name, and student ID to issue a book.
3. Return Book: Specify the book name to mark it as returned.
4. Display Books: View all books in the library, including issued and available books.
5. Exit: Close the application.

---

## File Storage
- CSV File: Data is stored in books.csv in the following format:
```csv
name,author,genre,isIssued,studentName,studentId,issueDate,returnDate
```
- The system reads this file at startup and writes to it upon any changes.

---

## Contribution
Feel free to fork the repository and submit pull requests to enhance the functionality of the script.

#### Steps to Contribute
1. Fork the Repository:
    - Click the Fork button at the top right of the repository page to create a copy of the repository in your GitHub account.

2. Clone Your Fork:
    - Clone your forked repository to your local machine:

    ```bash
    git clone https://github.com/pulkitgarg04/LibraryManagementSystem.git
    cd LibraryManagementSystem
    ```

3. Create a New Branch:
    - Create a new branch for your changes:
    ```bash
    git checkout -b feature/your-feature-name
    ```

4. Make Your Changes:
    - Edit the script or add new features. Make sure your changes are well-tested.

5. Commit Your Changes:
    - After making changes, commit them with a clear and descriptive message:
    ```bash
    git add .
    git commit -m "Add a detailed description of your changes"
    ```

6. Push Your Changes:
- Push the changes to your forked repository:
    ```bash
    git push origin feature/your-feature-name
    ```

7. Open a Pull Request (PR):
- Go to the original repository on GitHub.
- Click on the Pull Requests tab.
- Click New Pull Request and choose your branch from your fork as the source.
- Provide a descriptive title and explanation of your changes.

---

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.