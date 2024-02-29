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

    public static Connection db_connect(String url, String  user, String pwd) throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }
    public static ResultSet select_category(Connection connect) throws SQLException {
        String query = "SELECT category.category_name, category_types.category_type, category_type_id FROM category inner join category_types ON category.category_type_id=category_types.id"; // Example query, change it according to your database schema
//        ResultSet resultSet = connect.executeQuery(query);
        Statement statement = connect.createStatement();
        return statement.executeQuery(query);
    }
    public static ResultSet select_providers(Connection connect) throws SQLException{
        String query = "SELECT id, provider_name, company_name FROM providers";
        Statement statement = connect.createStatement();
        return statement.executeQuery(query);
    }

    public static ResultSet select_drugs(Connection connect) throws SQLException{
        String query = "select drugs.id, drugs.photo, drugs.drug_name, drugs.description, drugs.count, category.category_name, providers.provider_name from drugs inner join category on drugs.category_id = category.id inner join providers on drugs.provider_id = providers.id"; // Example query, change it according to your database schema
        Statement statement = connect.createStatement();
        return statement.executeQuery(query);
    }

    public static ResultSet select_employee(Connection connect) throws SQLException{
        String query = "SELECT id, fullname, username, email, address FROM employees"; // Example query, change it according to your database schema
        Statement statement = connect.createStatement();
        return statement.executeQuery(query);
    }

    public static ResultSet select_category_types(Connection connect) throws SQLException{
        String query = "SELECT * FROM category_types";
        Statement statement = connect.createStatement();
        return statement.executeQuery(query);
    }

    public static ResultSet get_auth_employee(Connection connect, String users, String pswd) throws SQLException{
        String query = "SELECT id, fullname, username, email, address FROM employees WHERE username=? AND password=?";
        PreparedStatement preparedStatement = connect.prepareStatement(query);
        preparedStatement.setString(1, users);
        preparedStatement.setString(2, pswd);
        Statement statement = connect.createStatement();
        System.out.println(statement.executeQuery(query).next());
        return statement.executeQuery(query);
    }

    public static int insert_providers(Connection connect, String name, String price) throws SQLException{
        String query = "INSERT INTO providers (provider_name, company_name) VALUES (?, ?)";
        PreparedStatement preparedStatement = connect.prepareStatement(query);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, price);
        return preparedStatement.executeUpdate();
    }

    public static int insert_categories(Connection connect, String name, int category_type) throws SQLException{
        String query = "INSERT INTO category (category_name, category_type_id) VALUES (?, ?)";
        PreparedStatement preparedStatement = connect.prepareStatement(query);
        preparedStatement.setString(1, name);
        preparedStatement.setInt(2, category_type);
        return preparedStatement.executeUpdate();
    }

    public static int insert_drugs(Connection connect, String drug_name, int category, String descriptions, int counts, int provider) throws SQLException{
        String query = "INSERT INTO drugs (drug_name, category_id, description, count, provider_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connect.prepareStatement(query);
        preparedStatement.setString(1, drug_name);
        preparedStatement.setInt(2, category);
        preparedStatement.setString(3, descriptions);
        preparedStatement.setInt(4, counts);
        preparedStatement.setInt(5, provider);

        // Execute the statement
        return preparedStatement.executeUpdate();
    }

    public static int insert_employee(Connection connect, String email, String address, String fullname, String username, String pswd,
                                      Boolean is_admin) throws SQLException{
        if(!Boolean.TRUE.equals(is_admin)) is_admin = false;

        String query = "INSERT INTO employees (email, fullname, username, pswd, address, is_admin) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connect.prepareStatement(query);
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, fullname);
        preparedStatement.setString(3, username);
        preparedStatement.setString(4, pswd);
        preparedStatement.setString(5, address);
        preparedStatement.setBoolean(6, is_admin);

        // Execute the statement
        return preparedStatement.executeUpdate();
    }
}

