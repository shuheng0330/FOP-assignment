/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pricetracker;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CSVImporter extends JFrame {
    private static final String CSV_FILE_PATH = "src/lookup_item.csv";
    private UserData user;

    private DefaultTableModel tableModel;
    private JButton backToMainMenu;
    private JTextField searchField;
    private Map<String, Map<String, List<ShopItem>>> categories;
    private String loggedInUsername;
    private CombinedChartPriceGUI gui;

    public CSVImporter() {
        super("CSV File Importer");
        categories = new HashMap<>();
        tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().setBackground(new Color(255, 192, 203));

        table.setBackground(new Color(230, 230, 250));

        searchField = new JTextField(20);
        searchField.addActionListener(e -> searchItem()); 
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchItem());
        JButton backToMainMenuButton = new JButton("Main Menu");
        backToMainMenuButton.addActionListener(e -> backToMainMenu());

        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(new Color(230, 230, 250)); 
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(backToMainMenuButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(searchPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1800, 1600);
        setLocationRelativeTo(null);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    displayItemActions();
                }
            }
        });
    }


    private void backToMainMenu() {
        displayMainMenu();
    }

    void displayItemActions() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Set the background color of the frame to pink
        getContentPane().setBackground(new Color(255, 192, 203));

        UIManager.put("OptionPane.background", new Color(255, 255, 204));
        UIManager.put("Panel.background", new Color(255, 255, 204));

        String[] itemActions = {"Modify item details", "View top 5 cheapest seller", "View price trend", "Add to shopping cart", "Back to main menu"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Select an action:",
                "Item Actions",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                itemActions,
                itemActions[0] 
        );

        switch (choice) {
            case 0:
                JOptionPane.showMessageDialog(this, "Since you are only a user,so you can only modify item quantity.\nFor further information, please contact administrator Tay Qi Xiang\n tayqixiang@gmail.com", "Information", JOptionPane.ERROR_MESSAGE);
                UserData user = getUserData(loggedInUsername);
                UIComponents view = new UIComponents(loggedInUsername);
                view.setUserData(user.getUsername());
                view.setVisible(true);
                view.setLocationRelativeTo(null);
                break;
            case 1:
                SwingUtilities.invokeLater(() -> {
                    String priceCatcherFilePath = "src/pricecatcher_2023-08.csv";
                    String lookupPremiseFilePath = "src/lookup_premise.csv";
                    String lookupItemFilePath = "src/lookup_item.csv";

                    // Read three CSV files
                    List<String[]> priceCatcherData = CSVDataReader.readCSV(priceCatcherFilePath);
                    List<String[]> lookupPremiseData = CSVDataReader.readCSV(lookupPremiseFilePath);
                    List<String[]> lookupItemData = CSVDataReader.readCSV(lookupItemFilePath);

                    // Create an instance of Top5CheapestSeller
                    ItemVisualisation visualisation = new ItemVisualisation(priceCatcherData, lookupPremiseData, lookupItemData);
                    visualisation.setSize(1600, 500);
                    visualisation.setLocationRelativeTo(null);
                    visualisation.setVisible(true);
                });
                break;
            case 2:
                SwingUtilities.invokeLater(() -> {
                    String priceCatcherFilePath = "src/pricecatcher_2023-08.csv";
                    String lookupPremiseFilePath = "src/lookup_premise.csv";
                    String lookupItemFilePath = "src/lookup_item.csv";

                    List<String[]> priceCatcherData = CSVDataReader.readCSV(priceCatcherFilePath);
                    List<String[]> lookupPremiseData = CSVDataReader.readCSV(lookupPremiseFilePath);
                    List<String[]> lookupItemData = CSVDataReader.readCSV(lookupItemFilePath);

                    CombinedChartPriceGUI gui = new CombinedChartPriceGUI(priceCatcherData, lookupPremiseData, lookupItemData);
                    gui.setSize(1600, 500);
                    gui.setLocationRelativeTo(null);
                    gui.setVisible(true);

                });
                break;
            case 3:
                addToShoppingCart(loggedInUsername);
                break;

            case 4:
                CSVImporter importer = new CSVImporter();
                importer.setUserData(loggedInUsername);
                importer.setExtendedState(JFrame.MAXIMIZED_BOTH);
                importer.setVisible(true);
                importer.displayMainMenu();
                break;
            default:

                break;

        }
    }

    void displayMainMenu() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Set the background color of the frame to pink
        getContentPane().setBackground(new Color(255, 192, 203));

        UIManager.put("OptionPane.background", new Color(255, 255, 204));
        UIManager.put("Panel.background", new Color(255, 255, 204));

        String[] options = {"Import Data", "Browse by Categories", "Search for a Product", "View Shopping Cart", "Account Settings", "Exit"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Select an option:",
                "Main Menu",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0] 
        );

        switch (choice) {
            case 0:
                importData();
                break;

            case 1:
                browseByCategories();
                break;
            case 2:
                performSearch();
                break;
            case 3:
                viewShoppingCart(loggedInUsername);
                break;
            case 4:
                openAccountSettings(loggedInUsername);
                break;
            case 5:
                exitApplication();
                break;
            default:
                break;
        }
    }

    private void importData() {
    }

    private void browseByCategories() {

        String selectedCategory = getCategory();
        if (selectedCategory == null) {
            return;
        }

        String selectedSubcategory = null;
        if (!selectedCategory.equals("Back to Main Menu")) {
            selectedSubcategory = getSubcategory(selectedCategory);
            if (selectedSubcategory == null) {
                return;
            }
        }
        categories.clear(); 

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            br.readLine();
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                processCSVLine(selectedCategory, selectedSubcategory, line); // Filter based on selected category and subcategory
            }

            updateTable(categories);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error importing CSV file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTable(Map<String, Map<String, List<ShopItem>>> categories) {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        tableModel.addColumn("Item Code");
        tableModel.addColumn("Item Name");
        tableModel.addColumn("Unit");
        tableModel.addColumn("Item Group");
        tableModel.addColumn("Item Category");

        for (Map.Entry<String, Map<String, List<ShopItem>>> entry : categories.entrySet()) {
            String itemGroup = entry.getKey();
            Map<String, List<ShopItem>> itemGroups = entry.getValue();

            for (Map.Entry<String, List<ShopItem>> subEntry : itemGroups.entrySet()) {
                String itemCategory = subEntry.getKey();
                List<ShopItem> items = subEntry.getValue();

                for (ShopItem item : items) {
                    tableModel.addRow(item.getDataArray());
                }
            }
        }
    }

    private String getSubcategory(String mainCategory) {
        Set<String> uniqueSubcategories = new HashSet<>();
        List<String[]> csvData = CSVDataReader.readCSV(CSV_FILE_PATH);

        for (int i = 1; i < csvData.size(); i++) {
            String[] data = csvData.get(i);
            if (data.length >= 5 && data[3].trim().equalsIgnoreCase(mainCategory)) {
                uniqueSubcategories.add(data[4].trim());
            }
        }

        String[] subcategoriesArray = uniqueSubcategories.toArray(new String[0]);

        String selectedSubcategory = (String) JOptionPane.showInputDialog(
                this,
                "Select a Subcategory for " + mainCategory + ":",
                "Subcategory Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                subcategoriesArray,
                subcategoriesArray[0]
        );
        if (selectedSubcategory == null) {
            int option = JOptionPane.showConfirmDialog(
                    this,
                    "Do you want to return to the main menu?",
                    "Return to Main Menu",
                    JOptionPane.YES_NO_OPTION
            );

            if (option == JOptionPane.YES_OPTION) {
                displayMainMenu();
            } else {
                return null;
            }
        }

        JOptionPane.showMessageDialog(this, "You selected: " + selectedSubcategory);
        return selectedSubcategory;
    }

    private void processCSVLine(String selectedCategory, String selectedSubCategory, String line) {
        try {
            String[] data = line.split(",");
            if (data.length < 5) {
                throw new IllegalArgumentException("Invalid number of fields in line: " + line);
            }

            String itemCode = data[0].trim();
            String itemName = data[1].trim();
            String unit = data[2].trim();
            String itemGroup = data[3].trim();
            String itemCategory = data[4].trim();

            if (itemGroup.equalsIgnoreCase(selectedCategory)
                    && (selectedSubCategory == null || itemCategory.equalsIgnoreCase(selectedSubCategory))) {
                ShopItem item = new ShopItem(itemCode, itemName, unit, itemGroup, itemCategory);

                // Create categories and sub-categories
                categories
                        .computeIfAbsent(itemGroup, k -> new HashMap<>())
                        .computeIfAbsent(itemCategory, k -> new ArrayList<>())
                        .add(item);
            }
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            System.err.println("Skipping invalid line: " + line);
            e.printStackTrace();
        }
    }

    private void addToShoppingCart(String loggedInUsername) {
        JOptionPane.showMessageDialog(null, "Please ensure that you add no more than 10 items to your shopping cart. ^_^", "Limit Message", JOptionPane.INFORMATION_MESSAGE);

        UserData user = getUserData(loggedInUsername);
        combinedCart cartFrame = new combinedCart(user);
        cartFrame.setUserData(user.getUsername());
        cartFrame.setSize(1600, 500);
        cartFrame.setLocationRelativeTo(null);
        cartFrame.setVisible(true);

    }

    private void performSearch() {
        UIManager.put("OptionPane.background", new Color(255, 255, 204));
        String searchText = searchField.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        categories.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            br.readLine();
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                processCSVLineForSearch(line, searchText);
            }

            // Update the table with the imported data
            updateTable(categories);

            // Display items found or a message if no items match the search
            if (tableModel.getRowCount() > 0) {
                JOptionPane.showMessageDialog(this, "Items matching the search criteria found.");
            } else {
                JOptionPane.showMessageDialog(this, "No items found matching the search criteria.", "No Match", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error importing CSV file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processCSVLineForSearch(String line, String searchText) {
        try {
            String[] data = line.split(",");
            if (data.length < 5) {
                throw new IllegalArgumentException("Invalid number of fields in line: " + line);
            }

            String itemCode = data[0].trim();
            String itemName = data[1].trim();
            String unit = data[2].trim();
            String itemGroup = data[3].trim();
            String itemCategory = data[4].trim();

            if (itemName.toLowerCase().contains(searchText)
                    || itemGroup.toLowerCase().contains(searchText)
                    || itemCategory.toLowerCase().contains(searchText)
                    || levenshteinDistance(itemName, searchText) <= 3
                    || levenshteinDistance(itemGroup, searchText) <= 3
                    || levenshteinDistance(itemCategory, searchText) <= 3) {

                categories
                        .computeIfAbsent(itemGroup, k -> new HashMap<>())
                        .computeIfAbsent(itemCategory, k -> new ArrayList<>())
                        .add(new ShopItem(itemCode, itemName, unit, itemGroup, itemCategory));
            }
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            System.err.println("Skipping invalid line: " + line);
            e.printStackTrace();
        }
    }

    private void viewShoppingCart(String loggedInUsername) {
        UserData user = getUserData(loggedInUsername);
        UIComponents view = new UIComponents(loggedInUsername);
        view.setUserData(user.getUsername());
        view.setVisible(true);
        view.setLocationRelativeTo(null);
        
    }

    private UserData getUserData(String username) {
        UserData userData = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/javafx-video", "root", "Shuheng0330.");

            String query = "SELECT * FROM users WHERE username = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String retrievedUsername = resultSet.getString("Username");
                String retrievedPassword = resultSet.getString("Password");
                String retrievedEmail = resultSet.getString("Email");
                String retrievedContact = resultSet.getString("Contactno");

                userData = new UserData(retrievedUsername, retrievedPassword, retrievedEmail, retrievedContact);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }

        return userData;
    }

    public void setUser(String name) {
        this.loggedInUsername = name;
    }

    private void openAccountSettings(String loggedInUsername) {
        UserData user = getUserData(loggedInUsername);

        if (user != null) {
            AccountSettingsPage account = new AccountSettingsPage(user);
            account.setUserData(user.getUsername(), user.getPassword(), user.getEmail(), user.getContactno());
            account.setVisible(true);
            account.setLocationRelativeTo(null);

        } else {
            JOptionPane.showMessageDialog(this, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exitApplication() {
        int exitConfirmation = JOptionPane.showConfirmDialog(
                this,
                "Do you want to exit the app now?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (exitConfirmation == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private String getCategory() {
        Set<String> uniqueCategories = new HashSet<>();
        List<String[]> csvData = CSVDataReader.readCSV(CSV_FILE_PATH);

        for (int i = 1; i < csvData.size(); i++) {
            String[] data = csvData.get(i);
            if (data.length >= 5) {
                uniqueCategories.add(data[3].trim());
            }
        }

        String[] subcategoriesArray = uniqueCategories.toArray(new String[0]);

        String selectedCategory = (String) JOptionPane.showInputDialog(
                this,
                "Select a Category:",
                "Category Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                subcategoriesArray,
                subcategoriesArray[0]
        );

        if (selectedCategory == null) {
            int option = JOptionPane.showConfirmDialog(
                    this,
                    "Do you want to return to the main menu?",
                    "Return to Main Menu",
                    JOptionPane.YES_NO_OPTION
            );

            if (option == JOptionPane.YES_OPTION) {
                displayMainMenu();
            } else {
                return null;
            }
        } else {
            JOptionPane.showMessageDialog(this, "You selected: " + selectedCategory);
        }

        return selectedCategory;
    }

    private int levenshteinDistance(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = a.charAt(i - 1) == b.charAt(j - 1)
                            ? dp[i - 1][j - 1]
                            : 1 + Math.min(Math.min(dp[i][j - 1], dp[i - 1][j]), dp[i - 1][j - 1]);
                }
            }
        }

        return dp[a.length()][b.length()];
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CSVImporter importer = new CSVImporter();
            importer.setExtendedState(JFrame.MAXIMIZED_BOTH);
            importer.setVisible(true);
            importer.displayMainMenu();
        });
    }

    private void searchItem() {
        performSearch();
    }

    void setUserData(String username) {
        this.loggedInUsername = username;
    }

    // ShopItem class
    private static class ShopItem {

        private String itemCode;
        private String itemName;
        private String unit;
        private String itemGroup;
        private String itemCategory;

        public ShopItem(String itemCode, String itemName, String unit, String itemGroup, String itemCategory) {
            this.itemCode = itemCode;
            this.itemName = itemName;
            this.unit = unit;
            this.itemGroup = itemGroup;
            this.itemCategory = itemCategory;
        }

        public Object[] getDataArray() {
            return new Object[]{itemCode, itemName, unit, itemGroup, itemCategory};
        }

        public String getItem() {
            return itemName;
        }

        public boolean containsText(String searchText) {
            // Check if the item name, group, or category contains the search text
            return itemName.toLowerCase().contains(searchText)
                    || itemGroup.toLowerCase().contains(searchText)
                    || itemCategory.toLowerCase().contains(searchText);
        }
    }
}

