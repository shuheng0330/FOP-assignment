/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pricetracker;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UIComponents extends JFrame {
    private DefaultTableModel tableModel;
    private JTable cartTable;
    private JTextField itemField;
    private JButton addButton;
    private JButton removeButton;
    private Map<String, Integer> itemQuantityMap;

    private JButton increaseButton;
    private JButton decreaseButton;
    private JButton confirmButton;
    private JButton checkOutButton;
    private String loggedInUsername;
    private UserData user;

    
    private String selectedItemCode;
    private ArrayList<Double> availablePremises;
    
    
    public UIComponents(){
        setTitle("Shopping Cart");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
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

        cartTable.getColumnModel().getColumn(0).setPreferredWidth(90);
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(500);
        cartTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(110);
        cartTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        cartTable.setFocusable(false); // Prevents focus on table cells
        cartTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); //select multiple item 


        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new FlowLayout());

        itemField = new JTextField(15);
        addButton = new JButton("Add");
        removeButton = new JButton("Remove");
        confirmButton = new JButton("Confirm");//////
        checkOutButton = new JButton("Check Out");
        itemQuantityMap = new HashMap<>();
        
        ItemDetails itemDet = new ItemDetails();
        ShopDetails shopDet = new ShopDetails();
        

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tableModel.getRowCount() >= 10) {
                    JOptionPane.showMessageDialog(null, "You have reached the maximum amount of 10 items in the shopping cart", "Maximum Items Reached", JOptionPane.WARNING_MESSAGE);
                    return; // Prevent further addition of items if the limit is reached
                }

                String newItem = itemField.getText().trim();
                if (!newItem.isEmpty()) {
                    String[] itemDetails = itemDet.findItemDetails(newItem);
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
            int selectedIndex = cartTable.getSelectedRow();
              if (selectedIndex != -1) {
            String itemCode = tableModel.getValueAt(selectedIndex, 0).toString();
            String itemName = tableModel.getValueAt(selectedIndex, 1).toString();
            String unit = tableModel.getValueAt(selectedIndex, 2).toString();
            int quantity = Integer.parseInt(tableModel.getValueAt(selectedIndex, 3).toString());

            UserData user = getUserData(loggedInUsername);
            if (user != null) {
                updateDatabase(itemCode, itemName, unit, quantity, user);
            } else {
                System.out.println("User data not found for username: " + loggedInUsername);
            }
            tableModel.setValueAt(quantity, selectedIndex, 3);

        }
            
                JOptionPane.showMessageDialog(null, "All the items in your shopping cart is cleared now!");
            }
        });

        
        checkOutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = cartTable.getSelectedRow(); // Get the selected row from the cartTable
                if (selectedRow != -1) { // Ensure a row is selected
                    selectedItemCode = tableModel.getValueAt(selectedRow, 0).toString(); // Get the selected item code

                    JTextField districtField = new JTextField(15);
                    JPanel inputPanel = new JPanel();
                    inputPanel.add(new JLabel("Enter District:"));
                    inputPanel.add(districtField);

                    int result = JOptionPane.showConfirmDialog(null, inputPanel, "Enter District", JOptionPane.OK_CANCEL_OPTION);

                    if (result == JOptionPane.OK_OPTION) {
                        String district = districtField.getText();
                        
                        ArrayList<Double> premiseCodesByItem = shopDet.findPremiseCodesByItemCode(selectedItemCode);
                        ArrayList<String> premiseCodesByDistrict = shopDet.findPremiseCodesByDistrict(district);
                        
                        //checking for debug
                        System.out.println("Premise codes by item: " + premiseCodesByItem);
                        System.out.println("Premise codes by district: " + premiseCodesByDistrict);
                        
                        // Convert premiseCodesByDistrict to ArrayList<Double>
                            ArrayList<Double> districtCodes = new ArrayList<>();
                            for (String code : premiseCodesByDistrict) {
                                districtCodes.add(Double.parseDouble(code));
                            }
                        //filter the available premise based on itemCode and district
                            ArrayList<Double> availablePremises = new ArrayList<>(premiseCodesByItem);
                            availablePremises.retainAll(districtCodes);
                            
                            if (!availablePremises.isEmpty()) {
                                shopDet.showAvailablePremises(availablePremises);   // show available shop in that district that can buy items selected 
                            

        List<ItemsInfo> itemsInfos = new ArrayList<>();
        

// Iterate through the rows of the table model
for (int i = 0; i < tableModel.getRowCount(); i++) {
    // Get data from each column of the current row
    String itemCode = tableModel.getValueAt(i, 0).toString();
    String itemName = tableModel.getValueAt(i, 1).toString();
    String unit = tableModel.getValueAt(i, 2).toString();
    int quantity = Integer.parseInt(tableModel.getValueAt(i, 3).toString());

    // Fetch price, premiseCode, premise from the CSV file using methods from ShopDetails
    double price = shopDet.getPriceFromCSV(itemCode); // Implement this method to read price from the CSV file
    String premiseCode = shopDet.getPremiseCodeFromCSV(itemCode); // Implement this method to read premiseCode from the CSV file
    double parsePremiseCode = Double.parseDouble(premiseCode);
    String shop = shopDet.getPremiseFromCSV(parsePremiseCode); // Implement this method to read shop from the CSV file

    // Create a ItemsInfo instance and add it to the list
    ItemsInfo itemsInfo = new ItemsInfo(itemName, premiseCode, price, shop, quantity, unit);
    itemsInfos.add(itemsInfo);
}

// Call the findBestPackages method from the findBestPackages class
List<Map<String, Object>> bestOption = findBestOption.findBestOption(itemsInfos);

                            } else {
                            JOptionPane.showMessageDialog(null, "Some selected items are not available in the specified district.",
                                    "Items Not Available", JOptionPane.WARNING_MESSAGE);
                        }
                        } else {}
            
                        } else {
                    JOptionPane.showMessageDialog(null, "Please select an item to proceed.", "No Item Selected", JOptionPane.WARNING_MESSAGE);
                }}
        });
                

        itemPanel.add(itemField);
        itemPanel.add(addButton);
        itemPanel.add(removeButton);
        itemPanel.add(checkOutButton);
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
                    ItemDetails itemDet = new ItemDetails();
                    itemDet.displayItemDetails(itemName);
                }
            }
        });
        setVisible(true);
    }
    public UIComponents(String loggedInUsername){
        setTitle("Shopping Cart");
        this.loggedInUsername=loggedInUsername;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
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

        cartTable.getColumnModel().getColumn(0).setPreferredWidth(90);
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(500);
        cartTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(110);
        cartTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        cartTable.setFocusable(false); // Prevents focus on table cells
        cartTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); //select multiple item 


        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new FlowLayout());

        itemField = new JTextField(15);
        addButton = new JButton("Add");
        removeButton = new JButton("Remove");
        confirmButton = new JButton("Confirm");//////
        checkOutButton = new JButton("Check Out");
        itemQuantityMap = new HashMap<>();
        
        ItemDetails itemDet = new ItemDetails();
        ShopDetails shopDet = new ShopDetails();
        

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tableModel.getRowCount() >= 10) {
                    JOptionPane.showMessageDialog(null, "You have reached the maximum amount of 10 items in the shopping cart", "Maximum Items Reached", JOptionPane.WARNING_MESSAGE);
                    return; // Prevent further addition of items if the limit is reached
                }

                String newItem = itemField.getText().trim();
                if (!newItem.isEmpty()) {
                    String[] itemDetails = itemDet.findItemDetails(newItem);
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
            int selectedIndex = cartTable.getSelectedRow();
              if (selectedIndex != -1) {
            String itemCode = tableModel.getValueAt(selectedIndex, 0).toString();
            String itemName = tableModel.getValueAt(selectedIndex, 1).toString();
            String unit = tableModel.getValueAt(selectedIndex, 2).toString();
            int quantity = Integer.parseInt(tableModel.getValueAt(selectedIndex, 3).toString());

            UserData user = getUserData(loggedInUsername);
            if (user != null) {
                updateDatabase(itemCode, itemName, unit, quantity, user);
            } else {
                System.out.println("User data not found for username: " + loggedInUsername);
            }
            tableModel.setValueAt(quantity, selectedIndex, 3);

        
            }
            }
        });

        
        checkOutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = cartTable.getSelectedRow(); // Get the selected row from the cartTable
                if (selectedRow != -1) { // Ensure a row is selected
                    selectedItemCode = tableModel.getValueAt(selectedRow, 0).toString(); // Get the selected item code

                    JTextField districtField = new JTextField(15);
                    JPanel inputPanel = new JPanel();
                    inputPanel.add(new JLabel("Enter District:"));
                    inputPanel.add(districtField);

                    int result = JOptionPane.showConfirmDialog(null, inputPanel, "Enter District", JOptionPane.OK_CANCEL_OPTION);

                    if (result == JOptionPane.OK_OPTION) {
                        String district = districtField.getText();
                        
                        ArrayList<Double> premiseCodesByItem = shopDet.findPremiseCodesByItemCode(selectedItemCode);
                        ArrayList<String> premiseCodesByDistrict = shopDet.findPremiseCodesByDistrict(district);
                        
                        //checking for debug
                        System.out.println("Premise codes by item: " + premiseCodesByItem);
                        System.out.println("Premise codes by district: " + premiseCodesByDistrict);
                        
                        // Convert premiseCodesByDistrict to ArrayList<Double>
                            ArrayList<Double> districtCodes = new ArrayList<>();
                            for (String code : premiseCodesByDistrict) {
                                      districtCodes.add(Double.parseDouble(code));
                            }
                        //filter the available premise based on itemCode and district
                            ArrayList<Double> availablePremises = new ArrayList<>(premiseCodesByItem);
                            availablePremises.retainAll(districtCodes);
                            
                            if (!availablePremises.isEmpty()) {
                                shopDet.showAvailablePremises(availablePremises);   // show available shop in that district that can buy items selected 
                            

        List<ItemsInfo> itemsInfos = new ArrayList<>();
        

// Iterate through the rows of the table model
for (int i = 0; i < tableModel.getRowCount(); i++) {
    // Get data from each column of the current row
    String itemCode = tableModel.getValueAt(i, 0).toString();
    String itemName = tableModel.getValueAt(i, 1).toString();
    String unit = tableModel.getValueAt(i, 2).toString();
    int quantity = Integer.parseInt(tableModel.getValueAt(i, 3).toString());

    // Fetch price, premiseCode, premise from the CSV file using methods from ShopDetails
    double price = shopDet.getPriceFromCSV(itemCode); // Implement this method to read price from the CSV file
    String premiseCode = shopDet.getPremiseCodeFromCSV(itemCode); // Implement this method to read premiseCode from the CSV file
    double parsePremiseCode = Double.parseDouble(premiseCode);
    String shop = shopDet.getPremiseFromCSV(parsePremiseCode); // Implement this method to read shop from the CSV file

    // Create a ItemsInfo instance and add it to the list
    ItemsInfo itemsInfo = new ItemsInfo(itemName, premiseCode, price, shop, quantity, unit);
    itemsInfos.add(itemsInfo);
}

// Call the findBestPackages method from the findBestPackages class
List<Map<String, Object>> bestOption = findBestOption.findBestOption(itemsInfos);

                            } else {
                            JOptionPane.showMessageDialog(null, "Some selected items are not available in the specified district.",
                                    "Items Not Available", JOptionPane.WARNING_MESSAGE);
                        }
                        } else {}
            
                        } else {
                    JOptionPane.showMessageDialog(null, "Please select an item to proceed.", "No Item Selected", JOptionPane.WARNING_MESSAGE);
                }}
        });
                

        itemPanel.add(itemField);
        itemPanel.add(addButton);
        itemPanel.add(removeButton);
        itemPanel.add(checkOutButton);
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
                    ItemDetails itemDet = new ItemDetails();
                    itemDet.displayItemDetails(itemName);
                }
            }
        });
        setVisible(true);
        fetchItemsForUser(loggedInUsername);  // Fetch and display items for the logged-in user

    }
    private void updateCartTable() {
        tableModel.setRowCount(0);
        for (Map.Entry<String, Integer> entry : itemQuantityMap.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();    
            ItemDetails itemDet = new ItemDetails();
            String[] itemDetails = itemDet.findItemDetailsByName(itemName);
            if (itemDetails != null) {
                String itemCode = itemDetails[0];
                String unit = itemDetails[2];
                tableModel.addRow(new Object[]{itemCode, itemName, unit, quantity});
            }
        }
    }
    public void setUserData(String username) {
        this.loggedInUsername = username;
    }
    private void fetchItemsForUser(String loggedInUsername) {
        itemQuantityMap = new HashMap<>();
        // Connect to your database and fetch items for the specified user
        // Modify the database connection details as needed

        String url = "jdbc:mysql://127.0.0.1:3306/combinedcart";
        String usernamedb = "root";
        String password = "Shuheng0330.";

        try (Connection connection = DriverManager.getConnection(url, usernamedb, password)) {
            String query = "SELECT * FROM cart WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, loggedInUsername);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String itemCode = resultSet.getString("itemCode");
                String itemName = resultSet.getString("itemName");
                String unit = resultSet.getString("unit");
                int quantity = resultSet.getInt("quantity");

                // Populate the table with fetched items
                itemQuantityMap.put(itemName, quantity);
                tableModel.addRow(new Object[]{itemCode, itemName, unit, quantity});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
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
                String retrievedEmail=resultSet.getString("Email");
                String retrievedContact=resultSet.getString("Contactno");
                // You can retrieve other user information similarly

                // Create a UserData object to hold the retrieved data
                userData = new UserData(retrievedUsername,retrievedPassword,retrievedEmail,retrievedContact);
                // Add other retrieved information to the UserData object
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


            private void updateDatabase(String itemCode, String itemName, String unit, int quantity, UserData user) {
                    String url = "jdbc:mysql://127.0.0.1:3306/combinedcart";
    String usernamedb = "root";
    String password = "Shuheng0330.";

    try (Connection connection = DriverManager.getConnection(url, usernamedb, password)) {
        String query = "UPDATE cart SET quantity = ? WHERE username = ? AND itemName = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, quantity);
        preparedStatement.setString(2, user.getUsername());
        preparedStatement.setString(3, itemName);

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Item quantity updated successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "Failed to update item quantity.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
            }

       
    ArrayList<String> getItemsFromTableModel() {
        ArrayList<String> itemList = new ArrayList<>();
        int rowCount = tableModel.getRowCount();

        for (int i = 0; i < rowCount; i++) {
            String itemCode = tableModel.getValueAt(i, 0).toString();
            itemList.add(itemCode);
        }
        return itemList; 
    }

          public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                UIComponents view=new UIComponents();
                view.setVisible(true);
                view.setLocationRelativeTo(null);
            }
        });
    }
}
