package com.btl.web.BTL_BE.Book;

import com.btl.web.BTL_BE.DAO;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookDAO extends DAO {

    private static final String SELECT_ALL_BOOKS = "SELECT * FROM book";
    private static final String SELECT_BOOK_BY_ID = "SELECT * FROM book WHERE id = ?";

    private static final String SELECT_ALL_CATEGORIES = "SELECT * FROM category";
    private static final String SELECT_CATEGORY_BY_ID = "SELECT * FROM category WHERE id = ?";

    private static final String INSERT_BOOK = "INSERT INTO book (title, author, categoryId, releaseDate, pageNumber, soldNumber, cover, description) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_BOOK_BY_ID = "UPDATE book SET title = ?, author = ?, categoryId = ?, releaseDate = ?, pageNumber = ?, soldNumber = ?, cover = ?, description = ? WHERE id = ?";
    private static final String DELETE_BOOK_BY_ID = "DELETE FROM book WHERE id = ?";

    public ResponseEntity<?> selectAllBooks() {
        List<Book> books = new ArrayList<>();
        try {
            try ( PreparedStatement ps = connection.prepareStatement(SELECT_ALL_BOOKS)) {
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String author = resultSet.getString("author");
                    int categoryId = resultSet.getInt("categoryId");
                    String category = getCategoryById(categoryId);
                    Date releaseDate = resultSet.getDate("releaseDate");
                    int pageNumber = resultSet.getInt("pageNumber");
                    int soldNumber = resultSet.getInt("soldNumber");
                    String cover = resultSet.getString("cover");
                    String description = resultSet.getString("description");
                    books.add(new Book(id, title, author, categoryId, category, releaseDate, pageNumber, soldNumber, cover, description));
                }
            }
            return ResponseEntity.ok().body(books);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> selectBookById(String id) {
        Book book = new Book();
        try ( PreparedStatement ps = connection.prepareStatement(SELECT_BOOK_BY_ID)) {
            ps.setString(1, id);
            try ( ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    book.setId(resultSet.getInt("id"));
                    book.setTitle(resultSet.getString("title"));
                    book.setAuthor(resultSet.getString("author"));
                    book.setCategoryId(resultSet.getInt("categoryId"));
                    book.setCategory(getCategoryById(resultSet.getInt("categoryId")));
                    book.setReleaseDate(resultSet.getDate("releaseDate"));
                    book.setPageNumber(resultSet.getInt("pageNumber"));
                    book.setSoldNumber(resultSet.getInt("soldNumber"));
                    book.setCover(resultSet.getString("cover"));
                    book.setDescription(resultSet.getString("description"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.ok().body(book);
    }

    public ResponseEntity<?> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        try {
            try ( PreparedStatement ps = connection.prepareStatement(SELECT_ALL_CATEGORIES)) {
                ResultSet result = ps.executeQuery();
                while (result.next()) {
                    int id = result.getInt("id");
                    String title = result.getString("name");
                    categories.add(new Category(id, title));
                }
            }
            return ResponseEntity.ok().body(categories);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public String getCategoryById(int id) {
        try ( PreparedStatement ps = connection.prepareStatement(SELECT_CATEGORY_BY_ID)) {
            ps.setInt(1, id);
            try ( ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    return resultSet.getString("name");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "none";
    }

    public Book addBook(Book newBook) {
        try {
            PreparedStatement ps = connection.prepareStatement(INSERT_BOOK, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, newBook.getTitle());
            ps.setString(2, newBook.getAuthor());
            ps.setInt(3, newBook.getCategoryId());
            ps.setDate(4, newBook.getReleaseDate());
            ps.setInt(5, newBook.getPageNumber());
            ps.setInt(6, newBook.getSoldNumber());
            ps.setString(7, newBook.getCover());
            ps.setString(8, newBook.getDescription());

            // set id cho book (auto increasement)
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    newBook.setId(generatedId);
                    return newBook;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateBook(String id, Book updatedBook) {
        try {
            PreparedStatement ps = connection.prepareStatement(UPDATE_BOOK_BY_ID);
            ps.setString(1, updatedBook.getTitle());
            ps.setString(2, updatedBook.getAuthor());
            ps.setInt(3, updatedBook.getCategoryId());
            ps.setDate(4, updatedBook.getReleaseDate());
            ps.setInt(5, updatedBook.getPageNumber());
            ps.setInt(6, updatedBook.getSoldNumber());
            ps.setString(7, updatedBook.getCover());
            ps.setString(8, updatedBook.getDescription());
            ps.setString(9, id);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBook(String id) {
        try ( PreparedStatement ps = connection.prepareStatement(DELETE_BOOK_BY_ID)) {
            ps.setString(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
