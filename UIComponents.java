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
    private JButton clearButton;
    private JButton backButton;
    private String loggedInUsername;
    private UserData user;

    
    public UIComponents(){
        setTitle("Shopping Cart");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"Item Code", "Item Name", "Unit", "Quantity"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        cartTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
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
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(1000);
        cartTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(200);
        cartTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        cartTable.setFocusable(false); 
        cartTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); 

        

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new FlowLayout());

        itemField = new JTextField(15);
        addButton = new JButton("Add");
        removeButton = new JButton("Remove");
        confirmButton = new JButton("Confirm");
        checkOutButton = new JButton("Check Out");
        clearButton=new JButton("Clear");
        itemQuantityMap = new HashMap<>();
        
          JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        add(backButton, BorderLayout.SOUTH);
        
        ItemDetails itemDet = new ItemDetails();
        ShopDetails shopDet = new ShopDetails();



        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tableModel.getRowCount() >= 10) {
                    JOptionPane.showMessageDialog(null, "You have reached the maximum amount of 10 items in the shopping cart", "Maximum Items Reached", JOptionPane.WARNING_MESSAGE);
                    return; 
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
                        deleteItemFromDatabase(selectedItem);
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
                int[] selectedRows = cartTable.getSelectedRows(); 
                List<String> selectedItemCodes = new ArrayList<>(); // To store item codes for all selected rows

                for (int selectedRow : selectedRows) {
                    String selectedItemCode = tableModel.getValueAt(selectedRow, 0).toString();
                    selectedItemCodes.add(selectedItemCode);
                }

                JTextField districtField = new JTextField(15);
                JPanel inputPanel = new JPanel();
                inputPanel.add(new JLabel("Enter District:"));
                inputPanel.add(districtField);

                int result = JOptionPane.showConfirmDialog(null, inputPanel, "Enter District", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    String district = districtField.getText();

                    ArrayList<Double> premiseCodesByItem = new ArrayList<>();
                    for (String selectedItemCode : selectedItemCodes) {
                        premiseCodesByItem.addAll(shopDet.findPremiseCodesByItemCode(selectedItemCode));
                    }

                    ArrayList<String> premiseCodesByDistrict = shopDet.findPremiseCodesByDistrict(district);

                    ArrayList<Double> districtCodes = new ArrayList<>();
                    for (String code : premiseCodesByDistrict) {
                        districtCodes.add(Double.parseDouble(code));
                    }

                    // Filter the available premise based on item codes and district
                    ArrayList<Double> availablePremises = new ArrayList<>(premiseCodesByItem);
                    availablePremises.retainAll(districtCodes);
//                       

                    if (!availablePremises.isEmpty()) {
                        shopDet.showAvailablePremises(availablePremises); 

                        // Process items and create ItemsInfo instances
                        List<ItemsInfo> itemsInfos = new ArrayList<>();

                        for (int selectedRow : selectedRows) {
                            String selectedItemCode = tableModel.getValueAt(selectedRow, 0).toString();
                            String itemName = tableModel.getValueAt(selectedRow, 1).toString();
                            String unit = tableModel.getValueAt(selectedRow, 2).toString();
                            int quantity = Integer.parseInt(tableModel.getValueAt(selectedRow, 3).toString());

                            // Fetch all information for the selected itemCode
                            List<Double> prices = new ArrayList<>();
                            List<String> shops = new ArrayList<>();
                            List<String> premiseCodes = shopDet.getPremiseCodesFromCSV(selectedItemCode);

                            for (String premiseCode : premiseCodes) {
                                double parsePremiseCode = Double.parseDouble(premiseCode);
                                if (availablePremises.contains(parsePremiseCode)) {
                                    prices.add(shopDet.getPriceFromCSV(selectedItemCode, premiseCode));
                                    shops.add(shopDet.getPremiseFromCSV(parsePremiseCode));

                                    // Create an ItemsInfo instance and add it to the list
                                    ItemsInfo itemsInfo = new ItemsInfo(
                                            itemName,
                                            premiseCode,
                                            prices.get(prices.size() - 1),
                                            shops.get(shops.size() - 1),
                                            quantity,
                                            unit
                                    );
                                    itemsInfos.add(itemsInfo);
                                }
                            }
                        }

                        System.out.println("Finished processing items");                                          // checking debug statement

                        // Call the findBestOption method from the findBestOption class to find the cheapest seller
                        List<Map<String, Object>> bestOption = findBestOption.findBestOption(itemsInfos);
                        
                    } else {
                            JOptionPane.showMessageDialog(null, "Selected items are not available in the specified district.",
                                    "Items Not Available", JOptionPane.WARNING_MESSAGE);
                        }
                        }
            
                         else {
                    JOptionPane.showMessageDialog(null, "Please select an item to proceed.", "No Item Selected", JOptionPane.WARNING_MESSAGE);
                }}
        });
        
            clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear all items?", "Clear Items", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
            // Clear all items for the specific user
            clearItems(loggedInUsername);
            // Update the cart table
            updateCartTable();
        }}
});
            
        itemPanel.add(itemField);
        itemPanel.add(addButton);
        itemPanel.add(removeButton);
        itemPanel.add(checkOutButton);
        itemPanel.add(confirmButton);
        itemPanel.add(clearButton);
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
        setSize(1600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"Item Code", "Item Name", "Unit", "Quantity"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        cartTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
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
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(1000);
        cartTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(200);
        cartTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        cartTable.setFocusable(false); 
        cartTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new FlowLayout());

        itemField = new JTextField(15);
        addButton = new JButton("Add");
        removeButton = new JButton("Remove");
        confirmButton = new JButton("Confirm");
        checkOutButton = new JButton("Check Out");
        clearButton=new JButton("Clear");
        itemQuantityMap = new HashMap<>();
        
          JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        add(backButton, BorderLayout.SOUTH);
        
        ItemDetails itemDet = new ItemDetails();
        ShopDetails shopDet = new ShopDetails();


        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tableModel.getRowCount() >= 10) {
                    JOptionPane.showMessageDialog(null, "You have reached the maximum amount of 10 items in the shopping cart", "Maximum Items Reached", JOptionPane.WARNING_MESSAGE);
                    return; 
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
                        deleteItemFromDatabase(selectedItem);
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
                int[] selectedRows = cartTable.getSelectedRows(); 
                List<String> selectedItemCodes = new ArrayList<>(); 

                for (int selectedRow : selectedRows) {
                    String selectedItemCode = tableModel.getValueAt(selectedRow, 0).toString();
                    selectedItemCodes.add(selectedItemCode);
                }

                // Prompt user to enter district
                JTextField districtField = new JTextField(15);
                JPanel inputPanel = new JPanel();
                inputPanel.add(new JLabel("Enter District:"));
                inputPanel.add(districtField);

                int result = JOptionPane.showConfirmDialog(null, inputPanel, "Enter District", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    String district = districtField.getText();

                    // Fetch premise codes based on all selected item codes
                    ArrayList<Double> premiseCodesByItem = new ArrayList<>();
                    for (String selectedItemCode : selectedItemCodes) {
                        premiseCodesByItem.addAll(shopDet.findPremiseCodesByItemCode(selectedItemCode));
                    }

                    ArrayList<String> premiseCodesByDistrict = shopDet.findPremiseCodesByDistrict(district);

                    // Convert premiseCodesByDistrict to ArrayList<Double>
                    ArrayList<Double> districtCodes = new ArrayList<>();
                    for (String code : premiseCodesByDistrict) {
                        districtCodes.add(Double.parseDouble(code));
                    }

                    // Filter the available premise based on item codes and district
                    ArrayList<Double> availablePremises = new ArrayList<>(premiseCodesByItem);
                    availablePremises.retainAll(districtCodes);

                    if (!availablePremises.isEmpty()) {
                        shopDet.showAvailablePremises(availablePremises); 

                        // Process items and create ItemsInfo instances
                        List<ItemsInfo> itemsInfos = new ArrayList<>();

                        for (int selectedRow : selectedRows) {
                            String selectedItemCode = tableModel.getValueAt(selectedRow, 0).toString();
                            String itemName = tableModel.getValueAt(selectedRow, 1).toString();
                            String unit = tableModel.getValueAt(selectedRow, 2).toString();
                            int quantity = Integer.parseInt(tableModel.getValueAt(selectedRow, 3).toString());

                            // Fetch all information for the selected itemCode
                            List<Double> prices = new ArrayList<>();
                            List<String> shops = new ArrayList<>();
                            List<String> premiseCodes = shopDet.getPremiseCodesFromCSV(selectedItemCode);

                            for (String premiseCode : premiseCodes) {
                                double parsePremiseCode = Double.parseDouble(premiseCode);
                                if (availablePremises.contains(parsePremiseCode)) {
                                    prices.add(shopDet.getPriceFromCSV(selectedItemCode, premiseCode));
                                    shops.add(shopDet.getPremiseFromCSV(parsePremiseCode));

                                    // Create an ItemsInfo instance and add it to the list
                                    ItemsInfo itemsInfo = new ItemsInfo(
                                            itemName,
                                            premiseCode,
                                            prices.get(prices.size() - 1),
                                            shops.get(shops.size() - 1),
                                            quantity,
                                            unit
                                    );
                                    itemsInfos.add(itemsInfo);
                                }
                            }
                        }

                        System.out.println("Finished processing items");                                          // checking debug statement

                        // Call the findBestOption method from the findBestOption class to find the cheapest seller
                        List<Map<String, Object>> bestOption = findBestOption.findBestOption(itemsInfos);
                        
                    } else {
                            JOptionPane.showMessageDialog(null, "Selected items are not available in the specified district.",
                                    "Items Not Available", JOptionPane.WARNING_MESSAGE);
                        }
                        }
            
                         else {
                    JOptionPane.showMessageDialog(null, "Please select an item to proceed.", "No Item Selected", JOptionPane.WARNING_MESSAGE);
                }}
        });
        
            clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear all items?", "Clear Items", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            // Clear all items for the specific user
            clearItems(loggedInUsername);
            // Update the cart table
            updateCartTable();
        }}
});
            

        itemPanel.add(itemField);
        itemPanel.add(addButton);
        itemPanel.add(removeButton);
        itemPanel.add(checkOutButton);
        itemPanel.add(confirmButton);
        itemPanel.add(clearButton);
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
        fetchItemsForUser(loggedInUsername);
    }
        private void clearItems(String loggedInUsername) {
           itemQuantityMap.clear();
           tableModel.setRowCount(0);
           itemField.setText(""); // Clear the text field as well
           
      try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/combinedcart", "root", "Shuheng0330.")) {
        String query = "DELETE FROM cart WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, loggedInUsername);
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) deleted.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
           
        }
    private void deleteItemFromDatabase(String selectedItem) {
    try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/combinedcart", "root", "Shuheng0330.")) {
        String query = "DELETE FROM cart WHERE itemName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, selectedItem);
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) deleted.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
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
       
    ArrayList<String> getItemsFromTableModel() {
        ArrayList<String> itemList = new ArrayList<>();
        int rowCount = tableModel.getRowCount();

        for (int i = 0; i < rowCount; i++) {
            String itemCode = tableModel.getValueAt(i, 0).toString();
            itemList.add(itemCode);
        }
        return itemList; 
    }
    public void setUserData(String username) {
        this.loggedInUsername = username;
    }
    private void fetchItemsForUser(String loggedInUsername) {
        itemQuantityMap = new HashMap<>();

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

                // Create a UserData object to hold the retrieved data
                userData = new UserData(retrievedUsername,retrievedPassword,retrievedEmail,retrievedContact);
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
