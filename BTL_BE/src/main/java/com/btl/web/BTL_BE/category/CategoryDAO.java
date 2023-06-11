package com.btl.web.BTL_BE.category;

import com.btl.web.BTL_BE.Book.Category;
import com.btl.web.BTL_BE.DAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.ResponseEntity;

public class CategoryDAO extends DAO {

    public static String SELECT_ALL_CATEGORY = "SELECT * FROM category";
    public static String SELECT_CATEGORY_BY_ID = "SELECT * FROM category WHERE id = ?";

    public ResponseEntity<?> selectAllCategory() {
        List<Category> categories = new ArrayList<>();
        try {
            try ( PreparedStatement ps = connection.prepareStatement(SELECT_ALL_CATEGORY)) {
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    categories.add(new Category(id, name));
                }
            }
            return ResponseEntity.ok().body(categories);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    public ResponseEntity<?> selectCategoryById(String id) {
        Category category = new Category();
        try ( PreparedStatement ps = connection.prepareStatement(SELECT_CATEGORY_BY_ID)) {
            ps.setString(1, id);
            try ( ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    category.setId(resultSet.getInt("id"));
                    category.setName(resultSet.getString("name"));
                }
                connection.close();
                ps.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ResponseEntity.ok().body(category);
    }
}
