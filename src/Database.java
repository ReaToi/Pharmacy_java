import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
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

}

