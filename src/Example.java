import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Example {
    // JDBC URL, username, and password of SQLite database
    private static final String url = "jdbc:postgresql://localhost:5432/pharmacy";
    private static final String user = "reatoi";
    private static final String password = "Beksultan-04";

    private JFrame frame;
    private JTable table;
    private JButton addButton;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Example().createAndShowGUI());
    }

    private void createAndShowGUI() {
        frame = new JFrame("Product Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);

        // Login panel
        JPanel loginPanel = new JPanel(new FlowLayout());
        JTextField usernameField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);
        JButton loginButton = new JButton("Login");
        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        frame.add(loginPanel, BorderLayout.NORTH);

        // Table to display products
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);

        // Action listener for login button
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (authenticate(username, password)) {
                showProducts();
                loginPanel.setVisible(false); // Hide login panel after successful authentication
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private boolean authenticate(String username, String password) {
        // Add your authentication logic here
        // For simplicity, let's assume it always returns true
        return true;
    }

    private void showProducts() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Open a connection
            connection = DriverManager.getConnection(url, user, password);

            // Create a statement
            statement = connection.createStatement();
            String query = "SELECT * FROM providers"; // Example query, change it according to your database schema
            resultSet = statement.executeQuery(query);

            // Get column names
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

            // Populate table model with data
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                model.addRow(rowData);
            }

            // Set table model
            table.setModel(model);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
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
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        showAddProductButton();
    }
    private void showAddProductButton() {
        addButton = new JButton("Add Product");
        addButton.addActionListener(e -> showAddProductForm());
        frame.add(addButton, BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();
    }
    private void showAddProductForm() {
        JFrame addProductFrame = new JFrame("Add Product");
        addProductFrame.setSize(300, 150);
        addProductFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addProductFrame.setLocationRelativeTo(frame);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        addProductFrame.add(inputPanel, BorderLayout.CENTER);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());

            if (addProduct(name, price)) {
                JOptionPane.showMessageDialog(addProductFrame, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                addProductFrame.dispose();
                showProducts();
            } else {
                JOptionPane.showMessageDialog(addProductFrame, "Error adding product", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        addProductFrame.add(saveButton, BorderLayout.SOUTH);

        addProductFrame.setVisible(true);
    }

    private boolean addProduct(String name, double price) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Open a connection
            connection = DriverManager.getConnection(url, user, password);

            // Create a prepared statement
            String query = "INSERT INTO providers (provider_name, company_name) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, price);

            // Execute the statement
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }}
}