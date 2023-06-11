package com.btl.web.BTL_BE.Rating;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import com.btl.web.BTL_BE.DAO;
import java.sql.SQLException;

public class RatingDAO extends DAO {

    public static String SELECT_ALL_RATING = "SELECT * FROM rating";

    public ResponseEntity<?> selectAllRating() {
        List<Rating> ratings = new ArrayList<>();
        try {
            try ( PreparedStatement ps = connection.prepareStatement(SELECT_ALL_RATING)) {
                ResultSet result = ps.executeQuery();
                while (result.next()) {
                    int id = result.getInt("id");
                    int idBook = result.getInt("idBook");
                    int idUser = result.getInt("idUser");
                    int starCnt = result.getInt("starCnt");
                    String comment = result.getString("comment");
                    String userName = this.getName(idUser);
                    ratings.add(new Rating(id, idBook, idUser, starCnt, comment, userName));
                }
            }
            return ResponseEntity.ok().body(ratings);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> selectRating(String id) {
        Rating rating = null;
        try {
            String query = "SELECT * FROM rating WHERE id = ?";
            try ( PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, id);
                ResultSet result = ps.executeQuery();
                if (result.next()) {
                    int idBook = result.getInt("idBook");
                    int idUser = result.getInt("idUser");
                    int starCnt = result.getInt("starCnt");
                    String comment = result.getString("comment");
                    String userName = this.getName(idUser);
                    rating = new Rating(Integer.parseInt(id), idBook, idUser, starCnt, comment, userName);
                }
            }
            if (rating != null) {
                return ResponseEntity.ok().body(rating);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public boolean deleteRating(String id) {
        try {
            String query = "DELETE FROM rating WHERE id = ?";
            int rowsAffected;
            try ( PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, id);
                rowsAffected = ps.executeUpdate();
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Rating addRating(Rating rating) {
        try {
            String query = "INSERT INTO rating(idBook, idUser, starCnt, comment) VALUES(?, ?, ?, ?)";
            try ( PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, rating.getIdBook());
                ps.setInt(2, rating.getIdUser());
                ps.setInt(3, rating.getStarCnt());
                ps.setString(4, rating.getComment());
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    ResultSet result = ps.getGeneratedKeys();
                    if (result.next()) {
                        int id = result.getInt(1);
                        rating.setId(id);
                        rating.setUserName(getName(rating.getIdUser()));
                        System.out.println(": " + rating.userName);
                        return rating;
                    }
                }
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateRating(String id, Rating rating) {
        try {
            String query = "UPDATE rating SET idBook = ?, idUser = ?, starCnt = ?, comment = ? WHERE id = ?";
            int rowsAffected;
            try ( PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, rating.getIdBook());
                ps.setInt(2, rating.getIdUser());
                ps.setInt(3, rating.getStarCnt());
                ps.setString(4, rating.getComment());
                ps.setString(5, id);
                rowsAffected = ps.executeUpdate();
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResponseEntity<?> selectRatingByBookIdAndUserId(String idBook, String idUser) {
        List<Rating> ratings = new ArrayList<>();
        try {
            String query = "SELECT * FROM rating WHERE idBook = ? AND idUser = ?";
            try ( PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, idBook);
                ps.setString(2, idUser);
                ResultSet result = ps.executeQuery();
                while (result.next()) {
                    int id = result.getInt("id");
                    int idBook1 = result.getInt("idBook");
                    int idUser1 = result.getInt("idUser");
                    int starCnt = result.getInt("starCnt");
                    String comment = result.getString("comment");
                    String userName = this.getName(idUser1);
                    ratings.add(new Rating(id, idBook1, idUser1, starCnt, comment, userName));
                }
            }
            return ResponseEntity.ok().body(ratings);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> selectRatingByBookId(String idBook) {
        List<Rating> ratings = new ArrayList<>();
        try {
            String query = "SELECT * FROM rating WHERE idBook = ?";
            try ( PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, idBook);
                ResultSet result = ps.executeQuery();
                while (result.next()) {
                    int id = result.getInt("id");
                    int idBook1 = result.getInt("idBook");
                    int idUser = result.getInt("idUser");
                    int starCnt = result.getInt("starCnt");
                    String comment = result.getString("comment");
                    String userName = this.getName(idUser);
                    ratings.add(new Rating(id, idBook1, idUser, starCnt, comment, userName));
                }
            }
            return ResponseEntity.ok().body(ratings);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public String getName(int id) {
        try {
            String query = "select * from user where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("username");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
