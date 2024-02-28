//import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;

public class Database {
    private static final String url = "jdbc:postgresql://localhost:5432/pharmacy";
    private static final String user = "reatoi";
    private static final String password = "Beksultan-04";
//    public static void

    public static void main(String[] args) {
        // Параметры подключения к базе данных
        String url = "jdbc:postgresql://localhost:5432/pharmacy";
        String user = "reatoi";
        String password = "Beksultan-04";

        // Переменные для соединения и выполнения запроса
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Установление соединения с базой данных
            connection = DriverManager.getConnection(url, user, password);

            // Создание объекта для выполнения SQL-запросов
            statement = connection.createStatement();

            // SQL-запрос для выборки данных
            String query = "SELECT * FROM providers";

            // Выполнение запроса и получение результатов
            resultSet = statement.executeQuery(query);

            // Обработка результатов запроса
            while (resultSet.next()) {
                // Чтение данных из результата
                int id = resultSet.getInt("id");
                String provider_name = resultSet.getString("provider_name");
                // Вывод данных (здесь можно провести другую обработку)
                System.out.println("ID: " + id + ", Name: " + provider_name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Закрытие ресурсов
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Statement db_connect(String url, String  user, String pwd) throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection.createStatement();
    }
    public static ResultSet select_category(Statement connect) throws SQLException {
        String query = "SELECT category.category_name, category_types.category_type, category_type_id FROM category inner join category_types ON category.category_type_id=category_types.id"; // Example query, change it according to your database schema
//        ResultSet resultSet = connect.executeQuery(query);
        return connect.executeQuery(query);
    }


}

