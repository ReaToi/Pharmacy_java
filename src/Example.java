import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Example {
    // JDBC URL, username, and password of SQLite database
    private static final String url = "jdbc:postgresql://localhost:5432/pharmacy";
    private static final String user = "reatoi";
    private static final String password = "Beksultan-04";
    Statement statement = Database.db_connect(url, user, password);
    private JComboBox<String> categoryTypeComboBox;
    private JComboBox<String> categoryComboBox;
    private JComboBox<String> providerComboBox;

    private JFrame frame;
    private JTable table;
//    public JScrollPane scrollPane = new JScrollPane(table);
    private JButton addButton;
    private JButton addDrug;
    private JButton addCategoryButton;
    private JButton add_employee_button;
    private JButton update_drug;

    public Example() throws SQLException {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new Example().createAndShowGUI();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void createAndShowGUI() {
        addCategoryButton = new JButton("Add Category");
        addButton = new JButton("Add Provider");
        frame = new JFrame("Product Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
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
        frame.add(loginPanel, BorderLayout.CENTER);

        // Table to display products
//        table = new JTable();
//        JScrollPane scrollPane = new JScrollPane(table);
//        frame.add(scrollPane, BorderLayout.CENTER);
//
        frame.setVisible(true);

        // Action listener for login button
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (authenticate(username, password)) {
                main_page();
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
    private void main_page(){
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);

        frame.add(scrollPane, BorderLayout.CENTER);

        JButton logout = new JButton("Logout");
        JButton provisers = new JButton("Providers");
        JButton categories = new JButton("Categories");
        JButton drugs = new JButton("Drugs");
        JButton employee = new JButton("Employees");
        provisers.addActionListener(e -> showProducts());
        categories.addActionListener(e -> {
            try {
                show_categories();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        drugs.addActionListener(e -> show_drugs());
        employee.addActionListener(e -> show_employees());


        logout.addActionListener(e -> {
//            createAndShowGUI();
            frame.dispose();
        });

        JPanel panel = new JPanel(new FlowLayout());
        panel.add(categories);
        panel.add(provisers);
        panel.add(employee);
        panel.add(drugs);
        panel.add(logout);
        frame.add(panel, BorderLayout.PAGE_START);
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    private void show_categories() throws SQLException {
//        frame.remove(scrollPane); // Удаляем текущую панель с таблицей
//        table = new JTable(); // Создаем новую таблицу или обновляем существующую
//        JScrollPane newScrollPane = new JScrollPane(table); // Создаем новую панель прокрутки с таблицей
//        frame.add(newScrollPane, BorderLayout.CENTER); // Добавляем новую панель прокрутки в центр
//        frame.revalidate(); // Пересчитываем компоновку
//        frame.repaint();

//        Connection connection = null;
//        Statement statement = null;
//        ResultSet resultSet = null;

//            connection = DriverManager.getConnection(url, user, password);
//            statement = connection.createStatement();
//            String query = "SELECT category.category_name, category_types.category_type, category_type_id FROM category inner join category_types ON category.category_type_id=category_types.id"; // Example query, change it according to your database schema
//            resultSet = statement.executeQuery(query);
//            ResultSetMetaData metaData = resultSet.getMetaData();
        ResultSet resultSet = Database.select_category(statement);
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

        frame.remove(addButton);
        if(addDrug != null) frame.remove(addDrug);
        if (add_employee_button != null) frame.remove(add_employee_button);
        CategoryButton();
    }

    private void show_drugs(){
//        "select drugs.photo, drugs.drug_name, drugs.description, drugs.count, category.category_name, providers.provider_name from drugs inner join category on drugs.category_id = category.id inner join providers on drugs.provider_id = providers.id";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            String query = "select drugs.photo, drugs.drug_name, drugs.description, drugs.count, category.category_name, providers.provider_name from drugs inner join category on drugs.category_id = category.id inner join providers on drugs.provider_id = providers.id"; // Example query, change it according to your database schema
            resultSet = statement.executeQuery(query);
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
        if(addButton != null) frame.remove(addButton);
        if(addCategoryButton != null) frame.remove(addCategoryButton);
        if (add_employee_button != null) frame.remove(add_employee_button);
        show_add_drug_button();
    }
    private void show_employees(){
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            String query = "SELECT fullname, username, email, address FROM employees"; // Example query, change it according to your database schema
            resultSet = statement.executeQuery(query);
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
        if(addButton != null) frame.remove(addButton);
        if(addDrug != null) frame.remove(addDrug);
        if(addCategoryButton != null) frame.remove(addCategoryButton);
        add_employee_button();
    }

    private void showProducts() {
//        frame.remove(scrollPane); // Удаляем текущую панель с таблицей
//        table = new JTable(); // Создаем новую таблицу или обновляем существующую
//        JScrollPane newScrollPane = new JScrollPane(table); // Создаем новую панель прокрутки с таблицей
//        frame.add(newScrollPane, BorderLayout.CENTER); // Добавляем новую панель прокрутки в центр
//        frame.revalidate(); // Пересчитываем компоновку
//        frame.repaint();
//        CategoryButton();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Open a connection
            connection = DriverManager.getConnection(url, user, password);

            // Create a statement
            statement = connection.createStatement();
            String query = "SELECT id, provider_name, company_name FROM providers"; // Example query, change it according to your database schema
            resultSet = statement.executeQuery(query);

            // Get column names
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }
//            columnNames[columnCount] = "s";
//            columnCount++;

            // Populate table model with data
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            while (resultSet.next()) {
//                model.addColumn("");
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    System.out.println(resultSet.getObject(i));
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
        if(addDrug != null) frame.remove(addDrug);
        if (addCategoryButton != null) frame.remove(addCategoryButton);
        if (add_employee_button != null) frame.remove(add_employee_button);
        showAddProductButton();

    }
    private void showAddProductButton() {
        addButton = new JButton("Add Provider");
        addButton.addActionListener(e -> showAddProductForm());
        frame.add(addButton, BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();
    }
    private void show_add_drug_button(){
        addDrug = new JButton("Add Drug");
        addDrug.addActionListener(e -> show_add_drugs_form());
        frame.add(addDrug, BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();
//        if (addButton  !=null) frame.remove(addButton);
//        if (addCategoryButton  !=null) frame.remove(addCategoryButton);
//        if (add_employee_button != null) frame.remove(add_employee_button);
    }
    private void CategoryButton() {
        addCategoryButton = new JButton("Add Category");
        addCategoryButton.addActionListener(e -> add_category_form());
        frame.add(addCategoryButton, BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();
    }
    private void add_employee_button(){
        add_employee_button = new JButton("Add Employee");
        add_employee_button.addActionListener(e -> show_add_employees_form());
        frame.add(add_employee_button, BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();
//        frame.remove(addButton);
//        frame.remove(addCategoryButton);
//        if(addDrug != null) frame.remove(addDrug);

    }
    private void load_provider(){
        try(Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM providers")) {

            // Очищаем ComboBox перед добавлением новых элементов
            providerComboBox.removeAllItems();

            // Добавляем каждый тип категории в ComboBox
            while (resultSet.next()) {
                String categoryType = resultSet.getString("provider_name");
                providerComboBox.addItem(categoryType);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    private void load_category(){
        try(Connection connection = DriverManager.getConnection(url, user, password);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM category")) {

            // Очищаем ComboBox перед добавлением новых элементов
//            if
            categoryComboBox.removeAllItems();

            // Добавляем каждый тип категории в ComboBox
            while (resultSet.next()) {
                String categoryType = resultSet.getString("category_name");
                categoryComboBox.addItem(categoryType);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
    private void loadCategoryTypes() {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM category_types")) {

            // Очищаем ComboBox перед добавлением новых элементов
            categoryTypeComboBox.removeAllItems();

            // Добавляем каждый тип категории в ComboBox
            while (resultSet.next()) {
                String categoryType = resultSet.getString("category_type");
                categoryTypeComboBox.addItem(categoryType);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private int get_category_id(String category){
        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM category WHERE category_name = ?")) {
            statement.setString(1, category);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()) return resultSet.getInt("id");
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return -1;
    }
    private int get_provider_id(String provider){
        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM providers WHERE provider_name = ?")) {
            statement.setString(1, provider);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()) return resultSet.getInt("id");
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return -1;
    }
    private int getCategoryTypeId(String categoryType) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT id FROM category_types WHERE category_type = ?")) {
            statement.setString(1, categoryType);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1; // Если что-то пошло не так, возвращаем -1
    }
    private void add_category_form(){
        JFrame add_category_frame = new JFrame("Add category");
        add_category_frame.setSize(300, 200);
        add_category_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add_category_frame.setLocationRelativeTo(frame);
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField nameField = new JTextField();
        categoryTypeComboBox = new JComboBox<>();
        loadCategoryTypes();
        inputPanel.add(new JLabel("Name category:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Category type:"));
        inputPanel.add(categoryTypeComboBox);
        add_category_frame.add(inputPanel, BorderLayout.CENTER);
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String name = nameField.getText();
//            int category = Integer.valueOf(category_type_field.getText());
            String selectedCategoryType = (String) categoryTypeComboBox.getSelectedItem();
            int category = getCategoryTypeId(selectedCategoryType);

            if (add_category(name, category)) {
//                JOptionPane.showMessageDialog(addProductFrame, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                add_category_frame.dispose();
                try {
                    show_categories();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                JOptionPane.showMessageDialog(add_category_frame, "Error adding product", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add_category_frame.add(saveButton, BorderLayout.SOUTH);

        add_category_frame.setVisible(true);
    }
    private void showAddProductForm() {
        JFrame addProductFrame = new JFrame("Add Provider");
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
            String price = priceField.getText();

            if (addProduct(name, price)) {
//                JOptionPane.showMessageDialog(addProductFrame, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                addProductFrame.dispose();
                showProducts();
            } else {
                JOptionPane.showMessageDialog(addProductFrame, "Error adding product", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        addProductFrame.add(saveButton, BorderLayout.SOUTH);

        addProductFrame.setVisible(true);
    }

    private void show_add_employees_form(){
        JFrame add_employees_form = new JFrame("Add Employee");
        add_employees_form.setSize(600, 200);
        add_employees_form.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add_employees_form.setLocationRelativeTo(frame);
        JPanel input_panel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField fullname_field = new JTextField();
        JTextField email_field = new JTextField();
        JTextField username_field = new JTextField();
        JTextField password_field = new JTextField();
        JTextField address_field = new JTextField();
        input_panel.add(new JLabel("Fullname"));
        input_panel.add(fullname_field);

        input_panel.add(new JLabel("Email"));
        input_panel.add(email_field);
        input_panel.add(new JLabel("Username"));
        input_panel.add(username_field);
        input_panel.add(new JLabel("Password"));
        input_panel.add(password_field);
        input_panel.add(new JLabel("Address"));
        input_panel.add(address_field);
        add_employees_form.add(input_panel, BorderLayout.CENTER);
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String fullname = fullname_field.getText();
            String email = email_field.getText();
            String username = username_field.getText();
            String pswd = password_field.getText();
            String address = address_field.getText();

            if (add_employee(email, address, fullname, username, pswd, null)) {
//                JOptionPane.showMessageDialog(addProductFrame, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                add_employees_form.dispose();
                main_page();
            } else {
                JOptionPane.showMessageDialog(add_employees_form, "Error adding employee", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add_employees_form.add(saveButton, BorderLayout.SOUTH);
        add_employees_form.setVisible(true);

    }

    private void show_add_drugs_form(){
        JFrame add_add_drugs_form = new JFrame("Add Drugs");
        add_add_drugs_form.setSize(400, 200);
        add_add_drugs_form.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add_add_drugs_form.setLocationRelativeTo(frame);
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField nameField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField countField = new JTextField();
        categoryComboBox = new JComboBox<>();
        providerComboBox = new JComboBox<>();
        load_category();
        load_provider();
        inputPanel.add(new JLabel("Name category:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Count:"));
        inputPanel.add(countField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryComboBox);
        inputPanel.add(new JLabel("Provider"));
        inputPanel.add(providerComboBox);
        add_add_drugs_form.add(inputPanel, BorderLayout.CENTER);
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> add_add_drugs_form.dispose());
        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            String description = descriptionField.getText();
            int count = Integer.valueOf(countField.getText());
            String selectedCategory = (String) categoryComboBox.getSelectedItem();
            int category = get_category_id(selectedCategory);
            String selected_provider = (String) providerComboBox.getSelectedItem();
            int provider = get_provider_id(selected_provider);

            if (add_drugs(name, category, description, count, provider)) {
//                JOptionPane.showMessageDialog(addProductFrame, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                add_add_drugs_form.dispose();
                main_page();
            } else {
                JOptionPane.showMessageDialog(add_add_drugs_form, "Error adding product", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(saveButton);
        panel.add(cancelButton);
        add_add_drugs_form.add(panel, BorderLayout.SOUTH);
        add_add_drugs_form.setVisible(true);
    }

    private boolean add_employee(String email, String address, String fullname, String username, String pswd,
                                 Boolean is_admin){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            if(!Boolean.TRUE.equals(is_admin)) is_admin = false;
            connection = DriverManager.getConnection(url, user, password);
            String query = "INSERT INTO employees (email, fullname, username, password, address, is_admin) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, fullname);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, pswd);
            preparedStatement.setString(5, address);
            preparedStatement.setBoolean(6, is_admin);

            // Execute the statement
            preparedStatement.executeUpdate();

            return true;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }finally {
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
        }
    }

    private boolean add_category(String name, int category_type){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = DriverManager.getConnection(url, user, password);
            String query = "INSERT INTO category (category_name, category_type_id) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, category_type);

            // Execute the statement
            preparedStatement.executeUpdate();

            return true;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            return false;
    }finally {
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

    private boolean addProduct(String name, String price) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Open a connection
            connection = DriverManager.getConnection(url, user, password);

            // Create a prepared statement
            String query = "INSERT INTO providers (provider_name, company_name) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, price);

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

    private boolean add_drugs(String drug_name, int category, String descriptions, int counts, int provider){
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Open a connection
            connection = DriverManager.getConnection(url, user, password);

            // Create a prepared statement
            String query = "INSERT INTO drugs (drug_name, category_id, description, count, provider_id) " +
                    "VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, drug_name);
            preparedStatement.setInt(2, category);
            preparedStatement.setString(3, descriptions);
            preparedStatement.setInt(4, counts);
            preparedStatement.setInt(5, provider);

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
        }
    }
}