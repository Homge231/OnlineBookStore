package Coursework;

import java.util.Arrays;
import java.util.Scanner;

// BOOK CLASS
class Book {
    private final String title;
    private final String author;
    private final double price;
    private int stock;

    public Book(String title, String author, double price, int stock) {
        if (price < 0 || stock < 0) throw new IllegalArgumentException("Price and stock must be non-negative.");
        this.title = title;
        this.author = author;
        this.price = price;
        this.stock = stock;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public void reduceStock(int quantity) {
        if (quantity <= 0 || quantity > stock) throw new IllegalArgumentException("Invalid quantity.");
        stock -= quantity;
    }
    public boolean isInStock(int quantity) { return stock >= quantity; }

    @Override
    public String toString() {
        return getTitle() + " by " + getAuthor() + " - $" + getPrice() + " | Stock: " + getStock();
    }
}

// LINKED QUEUE ADT
class LinkedQueueADT<E> implements AbstractLinkedQueue<E> {
    private class Node {
        E element;
        Node next;
        Node(E element) { this.element = element; this.next = null; }
    }
    private Node head;
    private Node tail;
    private int size;
    public LinkedQueueADT() {
        head = tail = null;
        size = 0;
    }
    @Override
    public void offer(E element) {
        if (element == null) throw new IllegalArgumentException("Element cannot be null.");
        Node newNode = new Node(element);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }
    @Override
    public E poll() {
        if (isEmpty()) throw new IllegalStateException("Queue is empty.");
        E element = head.element;
        head = head.next;
        if (head == null) tail = null;
        size--;
        return element;
    }
    @Override
    public E peek() {
        if (isEmpty()) throw new IllegalStateException("Queue is empty.");
        return head.element;
    }
    @Override
    public int size() { return size; }
    @Override
    public boolean isEmpty() { return size == 0; }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node temp = head;
        while (temp != null) {
            sb.append(temp.element);
            if (temp.next != null) sb.append(", ");
            temp = temp.next;
        }
        sb.append("]");
        return sb.toString();
    }
}

// ARRAYLIST ADT
class ArrayListADT<E> implements AbstractList<E> {
    private static final int DEFAULT_CAPACITY = 5;
    private Object[] elements;
    private int nextIndex;

    public ArrayListADT() {
        elements = new Object[DEFAULT_CAPACITY];
        nextIndex = 0;
    }
    @Override
    public boolean add(E element) {
        if (nextIndex == elements.length) {
            elements = Arrays.copyOf(elements, elements.length * 2);
        }
        elements[nextIndex] = element;
        nextIndex++;
        return true;
    }
    @Override
    public boolean add(int index, E element) {
        if (index < 0 || index > nextIndex) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        if (nextIndex == elements.length) {
            elements = Arrays.copyOf(elements, elements.length * 2);
        }
        for (int i = nextIndex; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        nextIndex++;
        return true;
    }
    @SuppressWarnings("unchecked")
    @Override
    public E get(int index) {
        if (index < 0 || index >= nextIndex) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return (E) elements[index];
    }
    @SuppressWarnings("unchecked")
    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= nextIndex) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        E oldElement = (E) elements[index];
        elements[index] = element;
        return oldElement;
    }
    @SuppressWarnings("unchecked")
    @Override
    public E remove(int index) {
        if (index < 0 || index >= nextIndex) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        E removedElement = (E) elements[index];
        for (int i = index; i < nextIndex - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[nextIndex - 1] = null;
        nextIndex--;
        if (nextIndex < elements.length / 3 && elements.length > DEFAULT_CAPACITY) {
            elements = Arrays.copyOf(elements, elements.length / 2);
        }
        return removedElement;
    }
    @Override
    public int size() { return nextIndex; }
    @Override
    public int indexOf(E element) {
        for (int i = 0; i < nextIndex; i++) {
            if (elements[i] == element) return i;
        }
        return -1;
    }
    @Override
    public boolean contains(E element) {
        for (int i = 0; i < nextIndex; i++) {
            if (elements[i] == element) return true;
        }
        return false;
    }
    @Override
    public boolean isEmpty() { return nextIndex == 0; }
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < nextIndex; i++) {
            result.append(elements[i]);
            if (i < nextIndex - 1) result.append(", ");
        }
        result.append("]");
        return result.toString();
    }
}

// ORDER CLASS
class Order {
    private final String customerName;
    private final String shippingAddress;
    private String status; // "Processing", "Processed"
    private final LinkedQueueADT<BookEntry> books;

    public static class BookEntry {
        private final Book book;
        private final int quantity;

        BookEntry(Book book, int quantity) {
            this.book = book;
            this.quantity = quantity;
        }

        public Book getBook() { return book; }
        public int getQuantity() { return quantity; }
    }

    public Order(String customerName, String shippingAddress) {
        if (customerName.isEmpty() || shippingAddress.isEmpty())
            throw new IllegalArgumentException("Customer name and shipping address cannot be empty.");
        this.customerName = customerName;
        this.shippingAddress = shippingAddress;
        this.status = "Processing";
        this.books = new LinkedQueueADT<>();
    }

    public void addBook(Book book, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (!book.isInStock(quantity)) {
            throw new IllegalArgumentException("Not enough stock for book: " + book.getTitle());
        }
        books.offer(new BookEntry(book, quantity));
    }

    public void sortBooks() {
        if (books.size() <= 1) return;
        BookEntry[] array = new BookEntry[books.size()];
        LinkedQueueADT<BookEntry> temp = new LinkedQueueADT<>();
        int index = 0;
        while (!books.isEmpty()) {
            BookEntry entry = books.poll();
            array[index++] = entry;
            temp.offer(entry);
        }
        while (!temp.isEmpty()) books.offer(temp.poll());
        BookQuickSort.sort(array, 0, array.length - 1);
        while (!books.isEmpty()) books.poll();
        for (BookEntry entry : array) {
            books.offer(entry);
        }
    }

    public void process() {
        if (status.equals("Processed")) {
            System.out.println("Order already processed.");
            return;
        }
        LinkedQueueADT<BookEntry> temp = new LinkedQueueADT<>();
        while (!books.isEmpty()) {
            BookEntry entry = books.poll();
            entry.getBook().reduceStock(entry.getQuantity());
            temp.offer(entry);
        }
        while (!temp.isEmpty()) books.offer(temp.poll());
        status = "Processed";
    }

    public String getCustomerName() { return customerName; }
    public String getShippingAddress() { return shippingAddress; }
    public String getStatus() { return status; }
    public LinkedQueueADT<BookEntry> getBooks() { return books; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Order for " + customerName + "\nShipping Address: " + shippingAddress + "\nBooks:\n");
        LinkedQueueADT<BookEntry> temp = new LinkedQueueADT<>();
        while (!books.isEmpty()) {
            BookEntry entry = books.poll();
            sb.append(entry.getQuantity()).append(" x ").append(entry.getBook().getTitle()).append("\n");
            temp.offer(entry);
        }
        while (!temp.isEmpty()) books.offer(temp.poll());
        sb.append("Status: ").append(status);
        return sb.toString();
    }
}

// BOOK QUICK SORT
class BookQuickSort {
    public static void sort(Order.BookEntry[] array, int start, int end) {
        if (start >= end) return;
        int boundary = partition(array, start, end);
        sort(array, start, boundary - 1);
        sort(array, boundary + 1, end);
    }

    private static int partition(Order.BookEntry[] array, int start, int end) {
        Order.BookEntry pivot = array[end];
        int boundary = start - 1;
        for (int i = start; i <= end; i++) {
            if (array[i].getBook().getTitle().compareToIgnoreCase(pivot.getBook().getTitle()) <= 0) {
                boundary++;
                Order.BookEntry temp = array[i];
                array[i] = array[boundary];
                array[boundary] = temp;
            }
        }
        return boundary;
    }
}

// MAIN APPLICATION: ONLINE BOOKSTORE
public class OnlineBookstore {
    private static final AbstractList<Book> books = new ArrayListADT<>();
    private static final AbstractLinkedQueue<Order> pendingOrders = new LinkedQueueADT<>();
    private static final AbstractLinkedQueue<Order> processedOrders = new LinkedQueueADT<>();

    static {
        books.add(new Book("My Life", "Me", 19.99, 10));
        books.add(new Book("The Art of Suffering while Coding", "John", 39.99, 5));
        books.add(new Book("How to make Ak47 at Home", "5 Minute War", 29.99, 8));
        books.add(new Book("MrBeast Burger", "Mit to bit", 45.99, 6));
    }

    private static int getValidIntInput(Scanner scanner, String prompt, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("exit")) {
                return -1; // Báo hiệu hủy
            }
            try {
                if (input.isEmpty()) throw new NumberFormatException();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(errorMessage);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== ONLINE BOOKSTORE SYSTEM ===");
            System.out.println("1. Customer");
            System.out.println("2. Admin");
            System.out.println("3. Exit");
            int choice = getValidIntInput(scanner, "Choose your role (1/2/3): ", "Invalid input. Please enter a valid number.");
            if (choice == -1) choice = 3; // "exit" tương đương với thoát
            switch (choice) {
                case 1: customerMenu(scanner); break;
                case 2: adminMenu(scanner); break;
                case 3: System.out.println("Exiting..."); System.exit(0);
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private static void customerMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n=== CUSTOMER MENU ===");
            System.out.println("1. Display Books");
            System.out.println("2. Add Order");
            System.out.println("3. Search Books");
            System.out.println("4. Back to Main Menu");
            int choice = getValidIntInput(scanner, "Choose an option: ", "Invalid input. Please enter a valid number.");
            if (choice == -1) return; // Thoát nếu nhập "exit"
            switch (choice) {
                case 1: displayBooks(); break;
                case 2: addOrder(scanner); break;
                case 3: searchBooks(scanner); break;
                case 4: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private static void adminMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n=== ADMIN MENU ===");
            System.out.println("1. Process Next Order");
            System.out.println("2. View Orders");
            System.out.println("3. Search Orders");
            System.out.println("4. Add Book");
            System.out.println("5. Back to Main Menu");
            int choice = getValidIntInput(scanner, "Choose an option: ", "Invalid input. Please enter a valid number.");
            if (choice == -1) return; // Thoát nếu nhập "exit"
            switch (choice) {
                case 1: processNextOrder(); break;
                case 2: viewOrders(); break;
                case 3: searchOrders(scanner); break;
                case 4: addBook(scanner); break;
                case 5: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private static void displayBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }
        System.out.println("Available Books:");
        for (int i = 0; i < books.size(); i++) {
            System.out.println((i + 1) + ". " + books.get(i));
        }
    }

    private static void addOrder(Scanner scanner) {
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter shipping address: ");
        String address = scanner.nextLine().trim();

        Order order;
        try {
            order = new Order(name, address);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        displayBooks();
        while (true) {
            int bookIndex = getValidIntInput(scanner, "Enter book number (0 to finish): ", "Invalid input. Please enter a valid number.") - 1;
            if (bookIndex == -2) return; // Thoát nếu nhập "exit" (-1 - 1 = -2)
            if (bookIndex == -1) break; // Thoát vòng lặp nếu nhập 0
            if (bookIndex < 0 || bookIndex >= books.size()) {
                System.out.println("Invalid book number.");
                continue;
            }
            Book book = books.get(bookIndex);
            int quantity = getValidIntInput(scanner, "Enter quantity: ", "Invalid input. Please enter a valid number.");
            if (quantity == -1) continue; // Bỏ qua nếu nhập "exit"
            try {
                order.addBook(book, quantity);
                System.out.println("Book added to order: " + book.getTitle());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        if (order.getBooks().isEmpty()) {
            System.out.println("Order not added because no books were selected.");
            return;
        }
        order.sortBooks();
        pendingOrders.offer(order);
        System.out.println("Order added successfully!");
    }

    private static void searchBooks(Scanner scanner) {
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }
        System.out.print("Enter keyword (title or author): ");
        String keyword = scanner.nextLine().trim().toLowerCase();
        if (keyword.isEmpty()) {
            System.out.println("Keyword cannot be empty.");
            return;
        }
        System.out.println("Search Results:");
        boolean found = false;
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            if (book.getTitle().toLowerCase().contains(keyword) || book.getAuthor().toLowerCase().contains(keyword)) {
                System.out.println((i + 1) + ". " + book);
                found = true;
            }
        }
        if (!found) System.out.println("No books matched your search.");
    }

    private static void processNextOrder() {
        if (pendingOrders.isEmpty()) {
            System.out.println("No pending orders to process.");
            return;
        }
        Order order = pendingOrders.poll();
        order.process();
        processedOrders.offer(order);
        System.out.println("Order for " + order.getCustomerName() + " has been processed.");
    }

    private static void viewOrders() {
        if (pendingOrders.isEmpty() && processedOrders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }
        printOrderTableHeader();
        int orderNum = 1;

        LinkedQueueADT<Order> temp = new LinkedQueueADT<>();
        while (!pendingOrders.isEmpty()) {
            Order order = pendingOrders.poll();
            printOrderTableRow(order, orderNum++);
            temp.offer(order);
        }
        while (!temp.isEmpty()) pendingOrders.offer(temp.poll());

        while (!processedOrders.isEmpty()) {
            Order order = processedOrders.poll();
            printOrderTableRow(order, orderNum++);
            temp.offer(order);
        }
        while (!temp.isEmpty()) processedOrders.offer(temp.poll());

        printOrderTableFooter();
    }

    private static void searchOrders(Scanner scanner) {
        System.out.print("Enter customer name to search: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Search name cannot be empty.");
            return;
        }
        boolean found = false;
        System.out.println("\nSearch Results for Customer: " + name);
        printOrderTableHeader();

        LinkedQueueADT<Order> temp = new LinkedQueueADT<>();
        int orderNum = 1;
        while (!pendingOrders.isEmpty()) {
            Order order = pendingOrders.poll();
            if (order.getCustomerName().equalsIgnoreCase(name)) {
                printOrderTableRow(order, orderNum++);
                found = true;
            }
            temp.offer(order);
        }
        while (!temp.isEmpty()) pendingOrders.offer(temp.poll());

        while (!processedOrders.isEmpty()) {
            Order order = processedOrders.poll();
            if (order.getCustomerName().equalsIgnoreCase(name)) {
                printOrderTableRow(order, orderNum++);
                found = true;
            }
            temp.offer(order);
        }
        while (!temp.isEmpty()) processedOrders.offer(temp.poll());

        printOrderTableFooter();
        if (!found) System.out.println("No orders found for " + name);
    }

    private static void addBook(Scanner scanner) {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) {
            System.out.println("Title cannot be empty.");
            return;
        }
        System.out.print("Enter author name: ");
        String author = scanner.nextLine().trim();
        if (author.isEmpty()) {
            System.out.println("Author cannot be empty.");
            return;
        }
        double price = getValidDoubleInput(scanner);
        if (price < 0) {
            System.out.println("Price cannot be negative.");
            return;
        }
        int stock = getValidIntInput(scanner, "Enter stock quantity: ", "Invalid input. Please enter a valid number.");
        if (stock == -1) return; // Thoát nếu nhập "exit"
        if (stock < 0) {
            System.out.println("Stock cannot be negative.");
            return;
        }
        books.add(new Book(title, author, price, stock));
        System.out.println("Book added successfully!");
    }

    private static double getValidDoubleInput(Scanner scanner) {
        while (true) {
            System.out.print("Enter price: ");
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) throw new NumberFormatException();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private static void printOrderTableHeader() {
        System.out.println("\n+------+--------------------+--------------------+----------------------+-------------+");
        System.out.println("| No.  | Customer Name      | Shipping Address   | Books                | Status      |");
        System.out.println("+------+--------------------+--------------------+----------------------+-------------+");
    }

    private static void printOrderTableRow(Order order, int orderNum) {
        String customerName = truncateOrPad(order.getCustomerName(), 18);
        String shippingAddress = truncateOrPad(order.getShippingAddress(), 18);
        String status = truncateOrPad(order.getStatus(), 11);

        LinkedQueueADT<Order.BookEntry> books = order.getBooks();
        LinkedQueueADT<Order.BookEntry> temp = new LinkedQueueADT<>();
        String firstBook = "";
        boolean first = true;
        while (!books.isEmpty()) {
            Order.BookEntry entry = books.poll();
            if (first) {
                firstBook = truncateOrPad(entry.getQuantity() + " x " + entry.getBook().getTitle(), 20);
                first = false;
                System.out.printf("| %-4d | %-18s | %-18s | %-20s | %-11s |\n", orderNum, customerName, shippingAddress, firstBook, status);
            } else {
                String bookLine = truncateOrPad(entry.getQuantity() + " x " + entry.getBook().getTitle(), 20);
                System.out.printf("|      |                    |                    | %-20s |             |\n", bookLine);
            }
            temp.offer(entry);
        }
        while (!temp.isEmpty()) books.offer(temp.poll());

        System.out.println("+------+--------------------+--------------------+----------------------+-------------+");
    }

    private static void printOrderTableFooter() {}

    private static String truncateOrPad(String text, int length) {
        if (text == null) text = "";
        if (text.length() > length) return text.substring(0, length - 3) + "...";
        return String.format("%-" + length + "s", text);
    }
}

