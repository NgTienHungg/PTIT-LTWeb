package com.btl.web.BTL_BE.User;

import com.btl.web.BTL_BE.DAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserDAO extends DAO {

    private final String INSERT_USER = "INSERT INTO user (username, password, email, role) VALUES (?, ?, ?, ?)";
    private final String SELECT_USER_BY_USERNAME = "SELECT * FROM user WHERE username = ?";
    private final String SELECT_USER_BY_USERNAME_AND_PASSWORD = "SELECT * FROM user WHERE username = ? AND password = ?";

    public ResponseEntity<?> selectUser(String username, String password) {
        User user = new User();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_USERNAME_AND_PASSWORD);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(resultSet.getInt("role"));
                user.setEmail(resultSet.getString("email"));
            }
            return ResponseEntity.ok().body(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    public void addUser(String username, String password, String email) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.setInt(4, 0); // Đặt vai trò mặc định là 0
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);

                System.out.println("User added successfully with ID: " + generatedId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isUserExists(String username) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_USERNAME);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // Trả về true nếu ResultSet có ít nhất một hàng
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean authentication(String username, String password) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_USERNAME_AND_PASSWORD);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getRole(String username) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_USERNAME);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
