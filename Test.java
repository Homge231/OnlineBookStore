package Coursework;

public class Test {
    public static void main(String[] args) {
        // Create custom data structures
        ArrayListADT<Book> books = new ArrayListADT<>();
        LinkedQueueADT<Order> orders = new LinkedQueueADT<>();

        // --- Test ArrayListADT: Add books ---
        long start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            books.add(new Book("Book " + i, "Author " + i, 19.99, 10));
        }
        long end = System.nanoTime();
        System.out.println("Time to add 10,000 books: " + (end - start) / 1_000_000.0 + " ms");

        // --- Test ArrayListADT: Get book by index ---
        start = System.nanoTime();
        Book sample = books.get(5000);
        end = System.nanoTime();
        System.out.println("Time to access book at index 5000: " + (end - start) + " ns");

        // --- Test ArrayListADT: Remove book from middle ---
        start = System.nanoTime();
        books.remove(5000);
        end = System.nanoTime();
        System.out.println("Time to remove book at index 5000: " + (end - start) / 1_000_000.0 + " ms");

        // --- Test Linear Search for Book Title ---
        String searchTitle = "Book 9000";
        start = System.nanoTime();
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getTitle().equals(searchTitle)) break;
        }
        end = System.nanoTime();
        System.out.println("Time to search for a book title: " + (end - start) / 1_000_000.0 + " ms");

        // --- Test LinkedQueueADT: Enqueue orders ---
        Order order;
        start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            order = new Order("Customer " + i, "Address " + i);
            order.addBook(new Book("Book " + i, "Author " + i, 19.99, 10), 1);
            orders.offer(order);
        }
        end = System.nanoTime();
        System.out.println("Time to enqueue 10,000 orders: " + (end - start) / 1_000_000.0 + " ms");

        // --- Test LinkedQueueADT: Dequeue one order ---
        start = System.nanoTime();
        orders.poll();
        end = System.nanoTime();
        System.out.println("Time to dequeue one order: " + (end - start) + " ns");

        // --- Test QuickSort on Order Books ---
        // Create an order with multiple books for sorting
        Order testOrder = new Order("TestCustomer", "TestAddress");
        for (int i = 0; i < 1000; i++) {
            testOrder.addBook(new Book("Book " + (1000 - i), "Author " + i, 19.99, 10), 1); // Reverse order to test sorting
        }

        start = System.nanoTime();
        testOrder.sortBooks(); // Uses BookQuickSort internally
        end = System.nanoTime();
        System.out.println("Time to QuickSort 1,000 books in an order: " + (end - start) / 1_000_000.0 + " ms");
    }
}