package org.example;

import java.sql.*;

public class Repository implements AutoCloseable {

    private Connection con;

    // Конструктор для установки соединения с БД
    public Repository(String username, String password) throws SQLException {
        this.con = DriverManager.getConnection(
            "jdbc:sqlite:data.db",  // Замените на ваш URL базы данных
            username,
            password);
    }

    // Метод для создания таблиц, если они еще не существуют
    public void createTables() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
            "user_id INT PRIMARY KEY, " +
            "username VARCHAR(255) NOT NULL)";

        String createGameStatsTable = "CREATE TABLE IF NOT EXISTS game_stats (" +
            "game_id INT PRIMARY KEY, " +
            "user_id INT, " +
            "total_time INT, " +
            "move_count INT, " +
            "game_date DATE, " +
            "FOREIGN KEY (user_id) REFERENCES users(user_id))";

        try (Statement stmt = con.createStatement()) {
            // Создаем таблицу пользователей
            stmt.executeUpdate(createUsersTable);
            System.out.println("Table 'users' created or already exists.");

            // Создаем таблицу статистики по играм
            stmt.executeUpdate(createGameStatsTable);
            System.out.println("Table 'game_stats' created or already exists.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Метод для вывода всех пользователей с их статистикой по играм
    public void displayAllUsersWithGameStats() {
        String query = "SELECT u.user_id, u.username, gs.game_id, gs.total_time, gs.move_count, gs.game_date " +
            "FROM users u LEFT JOIN game_stats gs ON u.user_id = gs.user_id";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String username = rs.getString("username");
                int gameId = rs.getInt("game_id");
                int totalTime = rs.getInt("total_time");
                int moveCount = rs.getInt("move_count");
                Date gameDate = rs.getDate("game_date");

                System.out.println("User ID: " + userId + ", Username: " + username);
                if (gameId != 0) {
                    System.out.println("Game ID: " + gameId + ", Time: " + totalTime + ", Moves: " + moveCount + ", Date: " + gameDate);
                } else {
                    System.out.println("No game statistics available.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Метод для добавления записи в таблицу game_stats
    public void addGameStat(int userId, int totalTime, int moveCount, Date gameDate) {
        String query = "INSERT INTO game_stats (user_id, total_time, move_count, game_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, totalTime);
            pstmt.setInt(3, moveCount);
            pstmt.setDate(4, gameDate);
            pstmt.executeUpdate();
            System.out.println("Game stat added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Метод для добавления записи в таблицу users и связывания с записью в game_stats
    public void addUserWithGameStat(String username, int totalTime, int moveCount, Date gameDate) {
        String insertUserQuery = "INSERT INTO users (username) VALUES (?)";
        String insertGameStatQuery = "INSERT INTO game_stats (user_id, total_time, move_count, game_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmtUser = con.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement pstmtGameStat = con.prepareStatement(insertGameStatQuery)) {

            // Добавляем пользователя
            pstmtUser.setString(1, username);
            pstmtUser.executeUpdate();

            // Получаем ID только что вставленного пользователя
            ResultSet rs = pstmtUser.getGeneratedKeys();
            if (rs.next()) {
                int userId = rs.getInt(1);

                // Добавляем статистику для этого пользователя
                pstmtGameStat.setInt(1, userId);
                pstmtGameStat.setInt(2, totalTime);
                pstmtGameStat.setInt(3, moveCount);
                pstmtGameStat.setDate(4, gameDate);
                pstmtGameStat.executeUpdate();
                System.out.println("User and associated game stats added successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Метод для удаления записи из таблицы game_stats по game_id
    public void deleteGameStat(int gameId) {
        String deleteQuery = "DELETE FROM game_stats WHERE game_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(deleteQuery)) {
            pstmt.setInt(1, gameId);

            // Проверка на связанные записи в таблице users
            String checkQuery = "SELECT user_id FROM game_stats WHERE game_id = ?";
            try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, gameId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    System.out.println("Game stat is associated with user_id: " + userId);
                    System.out.println("Are you sure you want to delete this game stat? (Y/N)");
                    // Пример запроса от пользователя, можно интегрировать с вводом через консоль.
                    // Здесь предполагается, что пользователь подтвердит или отклонит удаление.
                }
            }
            pstmt.executeUpdate();
            System.out.println("Game stat deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Метод для удаления записи пользователя по user_id
    public void deleteUser(int userId) {
        String deleteUserQuery = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(deleteUserQuery)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            System.out.println("User deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Реализация метода AutoCloseable для автоматического закрытия соединения
    @Override
    public void close() throws Exception {
        if (con != null && !con.isClosed()) {
            con.close();
            System.out.println("Connection closed.");
        }
    }
}
