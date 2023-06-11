package com.btl.web.BTL_BE.Order;

import com.btl.web.BTL_BE.DAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;

public class OrderDAO extends DAO {

    public static final String SELECT_ALL_ORDERS = "SELECT * FROM orders";
    public static final String SELECT_ORDER_BY_ID = "SELECT * FROM orders WHERE id = ?";
    public static final String SELECT_ORDER_BY_USER_ID = "SELECT * FROM orders WHERE idUser = ?";

    public static final String INSERT_ODER = "INSERT INTO orders (idUser, idBook, sum) VALUES (?, ?, ?)";
    public static final String DELETE_ORDER_BY_ID = "DELETE FROM orders WHERE id = ?";
    public static final String UPDATE_ORDER_BY_ID = "UPDATE orders SET idUser = ?, idBook = ?, sum = ? WHERE id = ?";

    public ResponseEntity<?> selectAllOrders() {
        List<Order> orders = new ArrayList<>();
        try {
            try ( PreparedStatement ps = connection.prepareStatement(SELECT_ALL_ORDERS)) {
                ResultSet result = ps.executeQuery();
                while (result.next()) {
                    int id = result.getInt("id");
                    int idUser = result.getInt("idUser");
                    int idBook = result.getInt("idBook");
                    int sum = result.getInt("sum");
                    orders.add(new Order(id, idUser, idBook, sum));
                }
            }
            return ResponseEntity.ok().body(orders);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> selectOrderById(String id) {
        Order order = null;
        try {
            try ( PreparedStatement ps = connection.prepareStatement(SELECT_ORDER_BY_ID)) {
                ps.setString(1, id);
                ResultSet result = ps.executeQuery();
                if (result.next()) {
                    int idUser = result.getInt("idUser");
                    int idBook = result.getInt("idBook");
                    int sum = result.getInt("sum");
                    order = new Order(Integer.parseInt(id), idUser, idBook, sum);
                }
            }
            if (order != null) {
                return ResponseEntity.ok().body(order);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public boolean deleteOrderById(String id) {
        try {
            int rowsAffected;
            try ( PreparedStatement ps = connection.prepareStatement(DELETE_ORDER_BY_ID)) {
                ps.setString(1, id);
                rowsAffected = ps.executeUpdate();
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Order addOrder(Order newOrder) {
        try {
            PreparedStatement ps = connection.prepareStatement(INSERT_ODER, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, newOrder.getIdUser());
            ps.setInt(2, newOrder.getIdBook());
            ps.setInt(3, newOrder.getSum());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    newOrder.setId(generatedId);
                    return newOrder;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateOrder(String id, Order updatedOrder) {
        try {
            int rowsAffected;
            try ( PreparedStatement ps = connection.prepareStatement(UPDATE_ORDER_BY_ID)) {
                ps.setInt(1, updatedOrder.getIdUser());
                ps.setInt(2, updatedOrder.getIdBook());
                ps.setInt(3, updatedOrder.getSum());
                ps.setString(4, id);
                rowsAffected = ps.executeUpdate();
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResponseEntity<?> getOrderByUserId(int id) {
        List<Order> orders = new ArrayList<>();
        try {
            try ( PreparedStatement ps = connection.prepareStatement(SELECT_ORDER_BY_USER_ID)) {
                ps.setInt(1, id);
                ResultSet result = ps.executeQuery();
                while (result.next()) {
                    int idOrder = result.getInt("id");
                    int idUser = result.getInt("idUser");
                    int idBook = result.getInt("idBook");
                    int sum = result.getInt("sum");
                    orders.add(new Order(idOrder, idUser, idBook, sum));
                }
            }
            return ResponseEntity.ok().body(orders);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
