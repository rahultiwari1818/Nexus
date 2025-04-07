package src;

public abstract class Book {
    protected String title;
    protected String author;
    protected String isbn;
    protected boolean isAvailable;
    protected int totalCopies;
    protected int lentCopies;

    public Book(String title, String author, String isbn, int totalCopies, int lentCopies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.totalCopies = totalCopies;
        this.lentCopies = lentCopies;
        this.isAvailable = (totalCopies > lentCopies);
    }

    public abstract String getFormat();

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public boolean isAvailable() {
        return totalCopies > lentCopies;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public int getLentCopies() {
        return lentCopies;
    }

    public void lendBook() {
        if (isAvailable()) {
            lentCopies++;
        } else {
            throw new IllegalStateException("No copies available to lend.");
        }
    }

    public void returnBook() {
        if (lentCopies > 0) {
            lentCopies--;
        } else {
            throw new IllegalStateException("No borrowed copies to return.");
        }
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", totalCopies=" + totalCopies +
                ", lentCopies=" + lentCopies +
                ", available=" + isAvailable() +
                ", format=" + getFormat() +
                '}';
    }
}
