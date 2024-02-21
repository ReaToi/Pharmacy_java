import javax.swing.*;
import java.sql.*;


public class Test {
    private static final String url = "jdbc:postgresql://localhost:5432/pharmacy";
    private static final String user = "reatoi";
    private static final String password = "Beksultan-04";

    public static void main(String[] args) {
        Connection connection = null;
        try {

            connection = DriverManager.getConnection(url, user, password);

            if (connection != null) {
                System.out.println("Успешное подключение к базе данных!");
                // Здесь вы можете выполнять операции с базой данных
                // Например, вызывать методы вашего DAO для доступа к данным
            } else {
                System.out.println("Не удалось подключиться к базе данных.");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при подключении к базе данных: " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
    }
}

