/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pricetracker;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class combinedCart extends JFrame {
    private DefaultTableModel tableModel;
    private JTable cartTable;
    private JTextField itemField;
    private JButton addButton;
    private JButton removeButton;
    private JButton confirmButton;
    private Map<String, Integer> itemQuantityMap;
    private UserData user;
    private String loggedInUsername;

    private JButton increaseButton;
    private JButton decreaseButton;

    public combinedCart() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "Please ensure that you add no more than 10 items to your shopping cart. ^_^", "Limit Message", JOptionPane.INFORMATION_MESSAGE);

        String[] itemActions = {"Modify item details", "View top 5 cheapest seller", "View price trend", "Add to shopping cart", "Back to main menu"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Select an action:",
                "Item Actions",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                itemActions,
                itemActions[0] // Default selection is the first option
        );
        setTitle("Shopping Cart");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"Item Code", "Item Name", "Unit", "Quantity"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // This makes all cells in the table non-editable
            }
        };
        cartTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // This makes all cells in the table non-editable
            }
        };
        cartTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(SwingConstants.CENTER);
            }
        });
        cartTable.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(SwingConstants.CENTER);
            }
        });
        cartTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(SwingConstants.CENTER);
            }
        });

        JScrollPane scrollPane = new JScrollPane(cartTable);

        cartTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(850);
        cartTable.getColumnModel().getColumn(2).setPreferredWidth(300);
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(200);
        cartTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        cartTable.setFocusable(false); // Prevents focus on table cells

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new FlowLayout());

        itemField = new JTextField(15);
        addButton = new JButton("Add");
        removeButton = new JButton("Remove");
        confirmButton = new JButton("Confirm");
        itemQuantityMap = new HashMap<>();

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Dispose the current JFrame (combinedCart)
                dispose();

                // Create and display the CSVImporter frame
                CSVImporter csvImporter = new CSVImporter();
                csvImporter.setVisible(true);
                csvImporter.displayMainMenu();
            }
        });
        add(backButton, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tableModel.getRowCount() >= 10) {
                    JOptionPane.showMessageDialog(null, "You have reached the maximum amount of 10 items in the shopping cart", "Maximum Items Reached", JOptionPane.WARNING_MESSAGE);
                    return; // Prevent further addition of items if the limit is reached
                }

                String newItem = itemField.getText().trim();
                if (!newItem.isEmpty()) {
                    String[] itemDetails = findItemDetails(newItem);
                    if (itemDetails != null) {
                        String itemName = itemDetails[1];
                        String itemCode = itemDetails[0];
                        String unit = itemDetails[2];

                        if (itemQuantityMap.containsKey(itemName)) {
                            int currentQuantity = itemQuantityMap.get(itemName);
                            itemQuantityMap.put(itemName, currentQuantity + 1);
                        } else {
                            itemQuantityMap.put(itemName, 1);
                            tableModel.addRow(new Object[]{itemCode, itemName, unit, 1});
                        }
                        updateCartTable();
                    } else {
                        JOptionPane.showMessageDialog(null, "Item code not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    itemField.setText("");
                }
            }
        });

        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = cartTable.getSelectedRow();
                if (selectedIndex != -1) {
                    String selectedItem = tableModel.getValueAt(selectedIndex, 1).toString();
                    int currentQuantity = itemQuantityMap.get(selectedItem);
                    if (currentQuantity > 1) {
                        itemQuantityMap.put(selectedItem, currentQuantity - 1);
                    } else {
                        itemQuantityMap.remove(selectedItem);
                        tableModel.removeRow(selectedIndex);
                    }
                    updateCartTable();
                }
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                 int rowCount = tableModel.getRowCount();

                for (int i = 0; i < rowCount; i++) {
                    String itemCode = tableModel.getValueAt(i, 0).toString();
                    String itemName = tableModel.getValueAt(i, 1).toString();
                    String unit = tableModel.getValueAt(i, 2).toString();
                    int quantity = Integer.parseInt(tableModel.getValueAt(i, 3).toString());

                    // Store the data in the MySQL database
                    storeInDatabase(itemCode, itemName, unit, quantity, user);
                }

                // Clear the cart after storing items in the database
                tableModel.setRowCount(0);
                itemQuantityMap.clear();
                updateCartTable();
                
                 JOptionPane.showMessageDialog(null, "Items added to cart successfully!");
            }
        });
        
        itemPanel.add(itemField);
        itemPanel.add(addButton);
        itemPanel.add(removeButton);
        itemPanel.add(confirmButton);
        inputPanel.add(itemPanel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        increaseButton = new JButton("+");
        decreaseButton = new JButton("-");

        increaseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = cartTable.getSelectedRow();
                if (selectedIndex != -1) {
                    String selectedItem = tableModel.getValueAt(selectedIndex, 1).toString();
                    int currentQuantity = itemQuantityMap.get(selectedItem);
                    itemQuantityMap.put(selectedItem, currentQuantity + 1);
                    updateCartTable();
                }
            }
        });

        decreaseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = cartTable.getSelectedRow();
                if (selectedIndex != -1) {
                    String selectedItem = tableModel.getValueAt(selectedIndex, 1).toString();
                    int currentQuantity = itemQuantityMap.get(selectedItem);
                    if (currentQuantity > 1) {
                        itemQuantityMap.put(selectedItem, currentQuantity - 1);
                    } else {
                        itemQuantityMap.remove(selectedItem);
                        tableModel.removeRow(selectedIndex);
                    }
                    updateCartTable();
                }
            }
        });

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(increaseButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(decreaseButton);
        buttonPanel.add(Box.createVerticalGlue());
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Adding double-click functionality to display item details
        cartTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    String itemName = tableModel.getValueAt(row, 1).toString();
                    displayItemDetails(itemName);
                }
            }
        });
        setVisible(true);
        setSize(1600, 500);
        setLocationRelativeTo(null);
    }

    public combinedCart(UserData user) {
        setTitle("Shopping Cart");
        this.user = user;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"Item Code", "Item Name", "Unit", "Quantity"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // This makes all cells in the table non-editable
            }
        };
        cartTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // This makes all cells in the table non-editable
            }
        };
        cartTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(SwingConstants.CENTER);
            }
        });
        cartTable.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(SwingConstants.CENTER);
            }
        });
        cartTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(SwingConstants.CENTER);
            }
        });

        JScrollPane scrollPane = new JScrollPane(cartTable);

        cartTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(850);
        cartTable.getColumnModel().getColumn(2).setPreferredWidth(300);
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(200);
        cartTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        cartTable.setFocusable(false); // Prevents focus on table cells

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new FlowLayout());

        itemField = new JTextField(15);
        addButton = new JButton("Add");
        removeButton = new JButton("Remove");
        confirmButton = new JButton("Confirm");
        itemQuantityMap = new HashMap<>();

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        add(backButton, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tableModel.getRowCount() >= 10) {
                    JOptionPane.showMessageDialog(null, "You have reached the maximum amount of 10 items in the shopping cart", "Maximum Items Reached", JOptionPane.WARNING_MESSAGE);
                    return; // Prevent further addition of items if the limit is reached
                }

                String newItem = itemField.getText().trim();
                if (!newItem.isEmpty()) {
                    String[] itemDetails = findItemDetails(newItem);
                    if (itemDetails != null) {
                        String itemName = itemDetails[1];
                        String itemCode = itemDetails[0];
                        String unit = itemDetails[2];

                        if (itemQuantityMap.containsKey(itemName)) {
                            int currentQuantity = itemQuantityMap.get(itemName);
                            itemQuantityMap.put(itemName, currentQuantity + 1);
                        } else {
                            itemQuantityMap.put(itemName, 1);
                            tableModel.addRow(new Object[]{itemCode, itemName, unit, 1});
                        }
                        updateCartTable();
                    } else {
                        JOptionPane.showMessageDialog(null, "Item code not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    itemField.setText("");
                }
            }
        });

        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = cartTable.getSelectedRow();
                if (selectedIndex != -1) {
                    String selectedItem = tableModel.getValueAt(selectedIndex, 1).toString();
                    int currentQuantity = itemQuantityMap.get(selectedItem);
                    if (currentQuantity > 1) {
                        itemQuantityMap.put(selectedItem, currentQuantity - 1);
                    } else {
                        itemQuantityMap.remove(selectedItem);
                        tableModel.removeRow(selectedIndex);
                    }
                    updateCartTable();
                }
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int rowCount = tableModel.getRowCount();

                for (int i = 0; i < rowCount; i++) {
                    String itemCode = tableModel.getValueAt(i, 0).toString();
                    String itemName = tableModel.getValueAt(i, 1).toString();
                    String unit = tableModel.getValueAt(i, 2).toString();
                    int quantity = Integer.parseInt(tableModel.getValueAt(i, 3).toString());

                    // Store the data in the MySQL database
                    storeInDatabase(itemCode, itemName, unit, quantity, user);
                }

                // Clear the cart after storing items in the database
                tableModel.setRowCount(0);
                itemQuantityMap.clear();
                updateCartTable();
                
                 JOptionPane.showMessageDialog(null, "Items added to cart successfully!");
            }
        });
        
        itemPanel.add(itemField);
        itemPanel.add(addButton);
        itemPanel.add(removeButton);
        itemPanel.add(confirmButton);
        inputPanel.add(itemPanel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        increaseButton = new JButton("+");
        decreaseButton = new JButton("-");

        increaseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = cartTable.getSelectedRow();
                if (selectedIndex != -1) {
                    String selectedItem = tableModel.getValueAt(selectedIndex, 1).toString();
                    int currentQuantity = itemQuantityMap.get(selectedItem);
                    itemQuantityMap.put(selectedItem, currentQuantity + 1);
                    updateCartTable();
                }
            }
        });

        decreaseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = cartTable.getSelectedRow();
                if (selectedIndex != -1) {
                    String selectedItem = tableModel.getValueAt(selectedIndex, 1).toString();
                    int currentQuantity = itemQuantityMap.get(selectedItem);
                    if (currentQuantity > 1) {
                        itemQuantityMap.put(selectedItem, currentQuantity - 1);
                    } else {
                        itemQuantityMap.remove(selectedItem);
                        tableModel.removeRow(selectedIndex);
                    }
                    updateCartTable();
                }
            }
        });

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(increaseButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(decreaseButton);
        buttonPanel.add(Box.createVerticalGlue());
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Adding double-click functionality to display item details
        cartTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    String itemName = tableModel.getValueAt(row, 1).toString();
                    displayItemDetails(itemName);
                }
            }
        });
        setVisible(true);
        setSize(1600, 500);
        setLocationRelativeTo(null);

    }

    public void setUserData(String username) {
        this.loggedInUsername = username;
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

                // Create a UserData object to hold the retrieved data
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

    private void storeInDatabase(String itemCode, String itemName, String unit, int quantity, UserData user) {
        String url = "jdbc:mysql://127.0.0.1:3306/combinedcart";
        String usernamedb = "root";
        String password = "Shuheng0330.";

        try (Connection connection = DriverManager.getConnection(url, usernamedb, password)) {
            String query = "INSERT INTO cart (username, itemCode, itemName, unit, quantity) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, itemCode);
            preparedStatement.setString(3, itemName);
            preparedStatement.setString(4, unit);
            preparedStatement.setInt(5, quantity);
            
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateCartTable() {
        tableModel.setRowCount(0);
        for (Map.Entry<String, Integer> entry : itemQuantityMap.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            String[] itemDetails = findItemDetailsByName(itemName);
            if (itemDetails != null) {
                String itemCode = itemDetails[0];
                String unit = itemDetails[2];
                tableModel.addRow(new Object[]{itemCode, itemName, unit, quantity});
            }
        }
    }

    private void displayItemDetails(String itemName) {
        String[] itemDetails = findItemDetailsByName(itemName);
        if (itemDetails != null) {
            String itemCode = itemDetails[0];
            String unit = itemDetails[2];
            String message = "Item Name: " + itemName + "\n"
                    + "Item Code: " + itemCode + "\n"
                    + "Unit: " + unit;
            JOptionPane.showMessageDialog(this, message, "Item Details", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Item details not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String[] findItemDetails(String searchItem) {
        String csvFile = "src/lookup_item.csv";
        String[] itemDetails = null;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean found = false;

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");

                if (fields.length > 0 && fields[0].equals(searchItem)) {
                    found = true;
                    itemDetails = fields;
                    break;
                }
            }

            if (!found) {
                System.out.println("Item code not found.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return itemDetails;
    }

    private String[] findItemDetailsByName(String searchItemName) {
        String csvFile = "src/lookup_item.csv";
        String[] itemDetails = null;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");

                if (fields.length > 1 && fields[1].equals(searchItemName)) {
                    itemDetails = fields;
                    break;
                }
            }

            if (itemDetails == null) {
                System.out.println("Item name not found.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return itemDetails;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new combinedCart();
            }
        });
    }
}


