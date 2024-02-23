import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private JComboBox<String> categoryTypeComboBox;
    private JComboBox<String> categoryComboBox;
    private JComboBox<String> providerComboBox;

    private JFrame frame;
    private JTable table;
    private JButton addButton;
    private JButton addDrug;
    private JButton addCategoryButton;

    // Списки статических данных
    private List<Category> categories = new ArrayList<>();
    private List<Provider> providers = new ArrayList<>();
    private List<Drug> drugs = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().createAndShowGUI());
    }

    private void createAndShowGUI() {
        // Инициализация статических данных
        initializeStaticData();

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

        frame.setVisible(true);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (authenticate(username, password)) {
                mainPage();
                loginPanel.setVisible(false); // Hide login panel after successful authentication
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void initializeStaticData() {
        // Пример заполнения статических данных
        categories.add(new Category(1, "Category 1", 1));
        categories.add(new Category(2, "Category 2", 2));
        categories.add(new Category(3, "Category 3", 1));

        providers.add(new Provider(1, "Provider 1", "Company A"));
        providers.add(new Provider(2, "Provider 2", "Company B"));
        providers.add(new Provider(3, "Provider 3", "Company C"));

        drugs.add(new Drug(1, "Drug A", 1, "Description 1", 100, 1));
        drugs.add(new Drug(2, "Drug B", 2, "Description 2", 200, 2));
        drugs.add(new Drug(3, "Drug C", 3, "Description 3", 300, 3));
    }

    private boolean authenticate(String username, String password) {
        // Добавьте здесь вашу логику аутентификации
        // Для упрощения предположим, что всегда возвращаем true
        return true;
    }

    private void mainPage() {
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        JButton logout = new JButton("Logout");
        JButton providersButton = new JButton("Providers");
        JButton categoriesButton = new JButton("Categories");
        JButton addDrugButton = new JButton("Add Drug");
        providersButton.addActionListener(e -> showProviders());
        categoriesButton.addActionListener(e -> showCategories());
        addDrugButton.addActionListener(e -> showAddDrugForm());

        logout.addActionListener(e -> frame.dispose());

        JPanel panel = new JPanel(new FlowLayout());
        panel.add(categoriesButton);
        panel.add(providersButton);
        panel.add(logout);
        panel.add(addDrugButton);

        frame.add(panel, BorderLayout.PAGE_START);
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    private void showCategories() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name", "Category Type ID"}, 0);
        for (Category category : categories) {
            model.addRow(new Object[]{category.getId(), category.getName(), category.getCategoryTypeId()});
        }
        table.setModel(model);
    }

    private void showProviders() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name", "Company Name"}, 0);
        for (Provider provider : providers) {
            model.addRow(new Object[]{provider.getId(), provider.getName(), provider.getCompanyName()});
        }
        table.setModel(model);
    }

    private void showAddDrugForm() {
        JFrame addDrugFrame = new JFrame("Add Drug");
        addDrugFrame.setSize(300, 150);
        addDrugFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addDrugFrame.setLocationRelativeTo(frame);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField nameField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField countField = new JTextField();
        categoryComboBox = new JComboBox<>();
        providerComboBox = new JComboBox<>();

        for (Category category : categories) {
            categoryComboBox.addItem(category.getName());
        }

        for (Provider provider : providers) {
            providerComboBox.addItem(provider.getName());
        }

        inputPanel.add(new JLabel("Name category:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Count:"));
        inputPanel.add(countField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryComboBox);
        inputPanel.add(new JLabel("Provider"));
        inputPanel.add(providerComboBox);
        addDrugFrame.add(inputPanel, BorderLayout.CENTER);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            String description = descriptionField.getText();
            int count = Integer.parseInt(countField.getText());
            int categoryId = categories.get(categoryComboBox.getSelectedIndex()).getId();
            int providerId = providers.get(providerComboBox.getSelectedIndex()).getId();

            if (addDrug(name, categoryId, description, count, providerId)) {
                addDrugFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(addDrugFrame, "Error adding drug", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        addDrugFrame.add(saveButton, BorderLayout.SOUTH);
        addDrugFrame.setVisible(true);
    }

    private boolean addDrug(String name, int categoryId, String description, int count, int providerId) {
        // Ваш код добавления лекарства
        return true; // Временный возврат успеха
    }}