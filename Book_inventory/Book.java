
public class Book {
    public String title, isbn, genre, author;
    public double price;

    // Default Constructor
    public Book() {
        this.title = "Default Title";
        this.price = 0.0;
        this.isbn = "000-000";
        this.genre = "Fiction";
        this.author = "Unknown";
    }

    // Parameterized Constructor with strict validation
    public Book(String title, double price, String isbn, String genre, String author) 
           throws MissingDataException, InvalidPriceException, InvalidGenreException { 
        
        if (title == null || title.trim().isEmpty()) {
            throw new MissingDataException("Input Error: Title cannot be left blank.");
        }
        
        if (price < 0) {
            throw new InvalidPriceException("Price Error: Value " + price + " is invalid. Books cannot have a negative price.");
        }
        
        String g = genre.toLowerCase().trim();
        if (!(g.equals("fiction") || g.equals("non-fiction") || g.equals("sci-fi") || g.equals("mystery"))) {
            throw new InvalidGenreException("Genre Error: '" + genre + "' is invalid.");
        }

        this.title = title;
        this.price = price;
        this.isbn = isbn;
        this.genre = genre;
        this.author = author;
    }

    // Copy Constructor
    public Book(Book other) {
        this.title = other.title;
        this.price = other.price;
        this.isbn = other.isbn;
        this.genre = other.genre;
        this.author = other.author;
    }
}