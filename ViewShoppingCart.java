/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pricetracker;
import javax.swing.*;
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
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.lang.String;
import java.util.Arrays;
import java.util.Set;

public class ViewShoppingCart extends JFrame {
    private DefaultTableModel tableModel;
    private JTable cartTable;
    private JTextField itemField;
    private JButton addButton;
    private JButton removeButton;
    private JButton selectButton;
    private Map<String, Integer> itemQuantityMap;

    private JButton increaseButton;
    private JButton decreaseButton;
    private JButton confirmButton;
    private JButton checkOutButton;
    private String loggedInUsername;
    
    private String selectedItemCode;
    private ArrayList<String> itemsToPick; // List of items to be picked
    private ArrayList<Shop> shopsList; // List of shops
    private ArrayList<Double> availablePremises;
    private UserData user;

    
    public ViewShoppingCart() {
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
        confirmButton = new JButton("Confirm");
        selectButton = new JButton("Select"); ///
        checkOutButton = new JButton("Check Out");
        itemQuantityMap = new HashMap<>();
        itemsToPick = new ArrayList<>();

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

//        JButton selectButton = new JButton("Select Item");
        selectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectItems(); // Call the selectItem method when the button is clicked
            }
            
            private void selectItems() {
                int[] selectedRows = cartTable.getSelectedRows();
                if (selectedRows.length > 0) {
                    StringBuilder message = new StringBuilder("Selected Items:\n");
                for (int row : selectedRows) {
                    String selectedCode = tableModel.getValueAt(row, 0).toString(); // Assuming item code is in the first column
                    String selectedName = tableModel.getValueAt(row, 1).toString(); // Assuming item name is in the second column
                    message.append(selectedName).append(" (").append(selectedCode).append(")\n");
                }
            JOptionPane.showMessageDialog(null, message.toString(), "Selected Items", JOptionPane.INFORMATION_MESSAGE);
            } else {
            JOptionPane.showMessageDialog(null, "Please select items.", "No Items Selected", JOptionPane.WARNING_MESSAGE);
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
                        ArrayList<Double> premiseCodesByItem = findPremiseCodesByItemCode(selectedItemCode);
                        ArrayList<String> premiseCodesByDistrict = findPremiseCodesByDistrict(district);

                        handleCheckOut1(district);


                        String resultMessage = "Here is the best option for you"; 

                        JOptionPane.showMessageDialog(null, resultMessage, "Shopping Results", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an item to proceed.", "No Item Selected", JOptionPane.WARNING_MESSAGE);
                }
            }
            

        });

        itemPanel.add(itemField);
        itemPanel.add(addButton);
        itemPanel.add(removeButton);
        itemPanel.add(selectButton);
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
                    displayItemDetails(itemName);
                }
            }
        });


        setVisible(true);
        
        
    }
    public ViewShoppingCart(String loggedInUsername){
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
        confirmButton = new JButton("Confirm");
        selectButton = new JButton("Select"); ///
        checkOutButton = new JButton("Check Out");
        itemQuantityMap = new HashMap<>();
        itemsToPick = new ArrayList<>();

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

//        JButton selectButton = new JButton("Select Item");
        selectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectItems(); // Call the selectItem method when the button is clicked
            }
            
            private void selectItems() {
                int[] selectedRows = cartTable.getSelectedRows();
                if (selectedRows.length > 0) {
                    StringBuilder message = new StringBuilder("Selected Items:\n");
                for (int row : selectedRows) {
                    String selectedCode = tableModel.getValueAt(row, 0).toString(); // Assuming item code is in the first column
                    String selectedName = tableModel.getValueAt(row, 1).toString(); // Assuming item name is in the second column
                    message.append(selectedName).append(" (").append(selectedCode).append(")\n");
                }
            JOptionPane.showMessageDialog(null, message.toString(), "Selected Items", JOptionPane.INFORMATION_MESSAGE);
            } else {
            JOptionPane.showMessageDialog(null, "Please select items.", "No Items Selected", JOptionPane.WARNING_MESSAGE);
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
                        ArrayList<Double> premiseCodesByItem = findPremiseCodesByItemCode(selectedItemCode);
                        ArrayList<String> premiseCodesByDistrict = findPremiseCodesByDistrict(district);

                        handleCheckOut1(district);


                        String resultMessage = "Here is the best option for you"; 

                        JOptionPane.showMessageDialog(null, resultMessage, "Shopping Results", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an item to proceed.", "No Item Selected", JOptionPane.WARNING_MESSAGE);
                }
            }
            

        });

        itemPanel.add(itemField);
        itemPanel.add(addButton);
        itemPanel.add(removeButton);
        itemPanel.add(selectButton);
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
                    displayItemDetails(itemName);
                }
            }
        });


        setVisible(true);
        fetchItemsForUser(loggedInUsername);

        
    
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
    public void setUserData(String username) {
        this.loggedInUsername = username;
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
                String[] fields = line.split(","); // Assuming the CSV file uses a comma as the delimiter

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
                new ViewShoppingCart();
            }
        });
       
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
    

// Define a Shop class to hold shop information
    class Shop {
        private String premiseCode;
        private String name;
        private String address;
        private String premiseType;
        private String state;
        private String district;
        private Map<String, Double> availableItems; // <ItemCode, Price>
        
    // Setters
    public void setPremiseCode(String premiseCode) {
        this.premiseCode = premiseCode;
    }
    
    public void setAvailableItems(Map<String, Double> availableItems) {
        this.availableItems = availableItems;
    }

    public void setName(String name) {
        this.name = name;
    }
        
    public void setAddress(String address) {
        this.address = address;
    }

    public void setPremiseType(String premiseType) {
        this.premiseType = premiseType;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setDistrict(String district) {
        this.district = district;
    }    

    // Getters
    public String getPremiseCode() {
        return premiseCode;
    }

    public Map<String, Double> getAvailableItems() {
        return availableItems;
    }
    
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPremiseType() {
        return premiseType;
    }

    public String getState() {
        return state;
    }

    public String getDistrict() {
        return district;
    }
    
    
    
    private Shop findShopByPremiseCode(ArrayList<Shop> shopList, String premiseCode) {
        for (Shop shop : shopList) {
            if (shop.getPremiseCode().equals(premiseCode)) {
                return shop;
            }
        }
        return null;
    }
    }

    //Method to find premise code in pricecatcher CSV based on item code
    private ArrayList<Double> findPremiseCodesByItemCode(String ItemCode) {
        ArrayList<Double> premiseCodes = new ArrayList<>();

        if (ItemCode == null) {
            System.out.println("Selected item code is null.");
            return premiseCodes;
        }

        String csvFile = "src/pricecatcher_2023-08.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");

                // fields[2] is location item code
                if (fields.length > 2 && fields[2].trim().equals(ItemCode.trim())) {
                    try {
                        // Parse the premise code to double and add to the list
                        double premiseCode = Double.parseDouble(fields[1].trim());
                        if (!premiseCodes.contains(premiseCode)) {
                            premiseCodes.add(premiseCode);
                        }
                    } catch (NumberFormatException e) {
                        // Handle parsing errors if the premise code is not a valid double
                        System.err.println("Invalid premise code format: " + fields[1].trim());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return premiseCodes;
    }

    private String[] findPremiseDetailsByCode(String premiseCode) {
    String csvFile = "src/lookup_item.csv";
    String[] premiseDetails = null;

    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        String line;

        while ((line = br.readLine()) != null) {
            String[] fields = line.split(","); // Assuming the CSV file uses a comma as the delimiter

            if (fields.length > 0 && fields[0].equals(premiseCode)) {
                premiseDetails = fields;
                break;
            }
        }

        if (premiseDetails == null) {
            System.out.println("Premise details not found for premise code: " + premiseCode);
        }

    } catch (IOException e) {
        e.printStackTrace();
    }

    return premiseDetails;
}
//    Method to find premise code in lookup_premise CSV to find premise in specific district enter by user
    private ArrayList<String> findPremiseCodesByDistrict(String district) {
        ArrayList<String> premiseCodes = new ArrayList<>();
        String csvFile = "src/lookup_item.csv"; //

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (fields.length > 5 && fields[5].trim().equalsIgnoreCase(district.trim())) {
                    premiseCodes.add(fields[0].trim()); // premise code is at index 0
                }
            }

            // Debugging print statement
//            System.out.println("Premise codes found for district " + district + ": " + premiseCodes);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (premiseCodes.isEmpty()) {
            System.out.println("No matching premise codes found for the district: " + district);
        }

        return premiseCodes;
    }
    
//    // Method to build a list of items to be picked
//    private ArrayList<String> buildItemList() {
//         ArrayList<String> itemList = new ArrayList<>();
//         int rowCount = tableModel.getRowCount();
//
//    for (int i = 0; i < rowCount; i++) {
//        String itemCodes = tableModel.getValueAt(i, 0).toString();
//        // take the itemCode stored in the first column of tableModel 
//        itemList.add(itemCodes);
//    }
//
//    return itemList; 
//    }

    private void handleCheckOut1(String selectedDistrict) {
        int selectedRow = cartTable.getSelectedRow(); 
        if (selectedRow != -1) { // Ensure a row is selected
            String itemCodes = tableModel.getValueAt(selectedRow, 0).toString();

            ArrayList<Double> premiseCodesByItem = findPremiseCodesByItemCode(itemCodes);
            ArrayList<String> premiseCodesByDistrict = findPremiseCodesByDistrict(selectedDistrict);         
            
            // Rebuild the list based on items not available in the selected premise codes
            ArrayList<String> remainingItems = new ArrayList<>(itemsToPick);
            
            for (Double premiseCode : premiseCodesByItem) {
                String premiseCodeStr = String.valueOf(premiseCode);
                if (!premiseCodesByDistrict.contains(premiseCodeStr)) {
                    remainingItems.removeIf(item -> {
                        ArrayList<Double> premiseCodes = findPremiseCodesByItemCode(item);
                        return premiseCodes.contains(premiseCode);
                    });
                }
            }

            System.out.println("Premise codes by item: " + premiseCodesByItem);
            System.out.println("Premise codes by district: " + premiseCodesByDistrict);

            // Convert premiseCodesByDistrict to ArrayList<Double>
            ArrayList<Double> districtCodes = new ArrayList<>();
            for (String code : premiseCodesByDistrict) {
                districtCodes.add(Double.parseDouble(code));
            }

            ArrayList<Double> availablePremises = new ArrayList<>(premiseCodesByItem);
            availablePremises.retainAll(districtCodes);

            if (!availablePremises.isEmpty()) {
                showAvailablePremises(availablePremises);
                
            // Calculate the best option result and display it
            startShopping(remainingItems, shopsList);    
            } else {
            JOptionPane.showMessageDialog(null, "Some selected items are not available in the specified district.",
                    "Items Not Available", JOptionPane.WARNING_MESSAGE);
        }
        } else {}
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



    //改这里
    private void showAvailablePremises(ArrayList<Double> premiseCodes) {
    ArrayList<String[]> premiseData = new ArrayList<>();

    String csvFile = "C:\\Users\\User2022\\Downloads\\lookup_item.csv";
    boolean skipHeader = true; // Flag to skip the first line (header)

    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        String line;
        while ((line = br.readLine()) != null) {
            if (skipHeader) {
                skipHeader = false;
                continue; // Skip the header row
            }

            String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

            if (fields.length >= 6) {
                double premiseCode;
                try {
                    premiseCode = Double.parseDouble(fields[0].trim());
                } catch (NumberFormatException e) {
                    continue; // Skip lines that don't start with a number
                }

                if (premiseCodes.contains(premiseCode)) {
                    premiseData.add(new String[]{
                            fields[0].trim(), fields[1].trim(), fields[2].trim(),
                            fields[3].trim(), fields[4].trim(), fields[5].trim()
                    });
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    displayPremiseDetails(premiseData);

    JOptionPane optionPane = new JOptionPane();
    JDialog dialog = optionPane.createDialog("Available Premises");
    dialog.setAlwaysOnTop(true); // Ensures dialog remains visible
    dialog.setVisible(true);

    GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    Rectangle bounds = env.getMaximumWindowBounds();
    dialog.setSize(bounds.width, bounds.height);
    dialog.setLocation(0, 0); // Ensure dialog starts from the top-left corner
}

    //////////
    
    private Map<String, String> parseItemCSV(String csvFilePath) {
        Map<String, String> itemMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 2) {
                    String itemCode = fields[0].trim();
                    String itemName = fields[1].trim();
                    itemMap.put(itemCode, itemName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return itemMap;
    }

    private ArrayList<Shop> parseShopCSV(String csvFilePath) {
        ArrayList<Shop> shopList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 6) {
                    String premiseCode = fields[0].trim();
                    String name = fields[1].trim();
                    String address = fields[2].trim();
                    String premiseType = fields[3].trim();
                    String state = fields[4].trim();
                    String district = fields[5].trim();

                    Shop shop = new Shop();
                    shop.setPremiseCode(premiseCode);
                    shop.setName(name);
                    shop.setAddress(address);
                    shop.setPremiseType(premiseType);
                    shop.setState(state);
                    shop.setDistrict(district);

                    shopList.add(shop);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return shopList;
    }
    
    // build a list of items to be picked
    private ArrayList<String> buildItemList() {
         ArrayList<String> itemList = new ArrayList<>();
         int rowCount = tableModel.getRowCount();

    for (int i = 0; i < rowCount; i++) {
        String itemCode = tableModel.getValueAt(i, 0).toString();
        // Assuming itemCode is stored in the first column of your tableModel
        itemList.add(itemCode);
    }
    return itemList; 
    }

    private void updateShopPrices(String csvFilePath, ArrayList<Shop> shopList, Map<String, String> itemMap) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 3) {
                    String premiseCode = fields[0].trim();
                    String itemCode = fields[1].trim();
                    double price = Double.parseDouble(fields[2].trim());

                    // Find the shop in shopList based on premiseCode
                    Shop shop = shopList.stream().filter(s -> s.getPremiseCode().equals(premiseCode)).findFirst().orElse(null);

                    if (shop != null && itemMap.containsKey(itemCode)) {
                        Map<String, Double> availableItems = shop.getAvailableItems();
                        if (availableItems == null) {
                            availableItems = new HashMap<>();
                        }
                        availableItems.put(itemMap.get(itemCode), price);
                        shop.setAvailableItems(availableItems);
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    

    
        private void startShopping(ArrayList<String> itemsToPick, ArrayList<Shop> shopsList) {
            ArrayList<Shop> selectedShops = new ArrayList<>();

            while (!itemsToPick.isEmpty() && !shopsList.isEmpty()) {
                Shop bestShop = findBestShop(itemsToPick, shopsList);
                if (bestShop != null) {
                    selectedShops.add(bestShop);
                    removePickedItems(itemsToPick, bestShop);
                    shopsList.remove(bestShop);
                } else {
                    break;
                }
            }

            double totalPrice = CalculateTotalPrice(selectedShops);
            displayShoppingResults(selectedShops, totalPrice);
        }


    
        

        private Shop findBestShop(ArrayList<String> itemsToPick, ArrayList<Shop> shopsList) {
            Shop bestShop = null;
            int maxItemsFound = 0;
            double bestTotalPrice = Double.MAX_VALUE;

            for (Shop shop : shopsList) {
                Map<String, Double> availableItems = shop.getAvailableItems();

                ArrayList<String> commonItems = new ArrayList<>(availableItems.keySet());
                commonItems.retainAll(itemsToPick);

                int commonItemCount = commonItems.size();
                double totalPrice = calculateTotalPriceForShop(commonItems, availableItems);

                // Logic to select the best shop based on the number of items and total price
                if (commonItemCount > maxItemsFound || (commonItemCount == maxItemsFound && totalPrice < bestTotalPrice)) {
                    bestShop = shop;
                    maxItemsFound = commonItemCount;
                    bestTotalPrice = totalPrice;
                }
            }

            return bestShop;
        }

        
        
        private void removePickedItems(ArrayList<String> itemsToPick, Shop bestShop) {
                Map<String, Double> availableItems = bestShop.getAvailableItems();

                for (String item : availableItems.keySet()) {
                    if (itemsToPick.contains(item)) {
                        itemsToPick.remove(item);
                    }
                }

                // Remove the picked items from the bestShop's available items
                availableItems.keySet().retainAll(itemsToPick);
            }


        private double calculateTotalPriceForShop(ArrayList<String> commonItems, Map<String, Double> availableItems) {
            double totalPrice = 0.0;

            for (String item : commonItems) {
                double price = availableItems.getOrDefault(item, 0.0); // Get the price for the item or default to 0.0
                totalPrice += price;
            }

            return totalPrice;
        }

        
        private void displayPremiseDetails(ArrayList<String[]> premiseData) {
            DefaultTableModel premiseTableModel = new DefaultTableModel(
                    new String[]{"Premise Code", "Premise", "Address", "Premise Type", "State", "District"}, 0);

            for (String[] data : premiseData) {
                premiseTableModel.addRow(data);
            }

            JTable premiseTable = new JTable(premiseTableModel);
            JScrollPane premiseScrollPane = new JScrollPane(premiseTable);

            JOptionPane.showMessageDialog(null, premiseScrollPane, "Available Premises", JOptionPane.INFORMATION_MESSAGE);
        }
        
        private void displayShoppingResults(ArrayList<Shop> selectedShops, double totalPrice) {
            StringBuilder message = new StringBuilder("Best Option:\n");

            for (Shop shop : selectedShops) {
                message.append("Shop: ").append(shop.getName()).append("\n");
                message.append("Premise Code: ").append(shop.getPremiseCode()).append("\n");
                message.append("Address: ").append(shop.getAddress()).append("\n");
                message.append("District: ").append(shop.getDistrict()).append("\n");

                Map<String, Double> availableItems = shop.getAvailableItems();
                if (!availableItems.isEmpty()) {
                    message.append("Available Items:\n");
                    for (Map.Entry<String, Double> entry : availableItems.entrySet()) {
                        String itemName = entry.getKey();
                        double price = entry.getValue();
                        message.append("- ").append(itemName).append(" ($").append(price).append(")\n");
                    }
                } else {
                    message.append("No items available in this shop.\n");
                }

                message.append("\n");
            }

            message.append("Total Price: $").append(totalPrice);

            JOptionPane.showMessageDialog(null, message.toString(), "Best Option For You", JOptionPane.INFORMATION_MESSAGE);
        }
}
//    private DefaultTableModel tableModel;
//    private JTable cartTable;
//    private JTextField itemField;
//    private JButton addButton;
//    private JButton removeButton;
//    private JButton confirmButton;
//    private Map<String, Integer> itemQuantityMap;
//    private ChartPrice chartPrice;
//    private UserData user;
//    private String loggedInUsername;
//
//
//    private JButton increaseButton;
//    private JButton decreaseButton;
//
//    public ViewShoppingCart() {
//        setTitle("Shopping Cart");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(1000, 500);
//        setLayout(new BorderLayout());
//
//        tableModel = new DefaultTableModel(new String[]{"Item Code", "Item Name", "Unit", "Quantity"}, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false; // This makes all cells in the table non-editable
//            }
//        };
//        cartTable = new JTable(tableModel) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false; // This makes all cells in the table non-editable
//            }
//        };
//        cartTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
//            {
//                setHorizontalAlignment(SwingConstants.CENTER);
//            }
//        });
//        cartTable.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
//            {
//                setHorizontalAlignment(SwingConstants.CENTER);
//            }
//        });
//        cartTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
//            {
//                setHorizontalAlignment(SwingConstants.CENTER);
//            }
//        });
//
//        JScrollPane scrollPane = new JScrollPane(cartTable);
//
//        cartTable.getColumnModel().getColumn(0).setPreferredWidth(90);
//        cartTable.getColumnModel().getColumn(1).setPreferredWidth(500);
//        cartTable.getColumnModel().getColumn(2).setPreferredWidth(100);
//        cartTable.getColumnModel().getColumn(3).setPreferredWidth(110);
//        cartTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        cartTable.setFocusable(false); // Prevents focus on table cells
//
//        JPanel inputPanel = new JPanel();
//        inputPanel.setLayout(new BorderLayout());
//
//        JPanel itemPanel = new JPanel();
//        itemPanel.setLayout(new FlowLayout());
//
//        itemField = new JTextField(15);
//        addButton = new JButton("Add");
//        removeButton = new JButton("Remove");
//        confirmButton = new JButton("Confirm");
//        itemQuantityMap = new HashMap<>();
//        
//        JButton backButton = new JButton("Back");
//        backButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                // Dispose the current JFrame (combinedCart)
//                dispose();
//                
//                // Create and display the CSVImporter frame
//                CSVImporter csvImporter = new CSVImporter(chartPrice);
//                csvImporter.setVisible(true);
//                csvImporter.displayMainMenu();
//            }
//        });
//        add(backButton, BorderLayout.SOUTH);
//
//        addButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                String newItem = itemField.getText().trim();
//                if (!newItem.isEmpty()) {
//                    String[] itemDetails = findItemDetails(newItem);
//                    if (itemDetails != null) {
//                        String itemName = itemDetails[1];
//                        String itemCode = itemDetails[0];
//                        String unit = itemDetails[2];
//
//                        if (itemQuantityMap.containsKey(itemName)) {
//                            int currentQuantity = itemQuantityMap.get(itemName);
//                            itemQuantityMap.put(itemName, currentQuantity + 1);
//                        } else {
//                            itemQuantityMap.put(itemName, 1);
//                            tableModel.addRow(new Object[]{itemCode, itemName, unit, 1});
//                        }
//                        updateCartTable();
//                    } else {
//                        JOptionPane.showMessageDialog(null, "Item code not found.", "Error", JOptionPane.ERROR_MESSAGE);
//                    }
//                    itemField.setText("");
//                }
//            }
//        });
//
//        removeButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                int selectedIndex = cartTable.getSelectedRow();
//                if (selectedIndex != -1) {
//                    String selectedItem = tableModel.getValueAt(selectedIndex, 1).toString();
//                    int currentQuantity = itemQuantityMap.get(selectedItem);
//                    if (currentQuantity > 1) {
//                        itemQuantityMap.put(selectedItem, currentQuantity - 1);
//                    } else {
//                        itemQuantityMap.remove(selectedItem);
//                        tableModel.removeRow(selectedIndex);
//                    }
//                    updateCartTable();
//                }
//            }
//        });
//
//        confirmButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                int selectedIndex = cartTable.getSelectedRow();
//              if (selectedIndex != -1) {
//            String itemCode = tableModel.getValueAt(selectedIndex, 0).toString();
//            String itemName = tableModel.getValueAt(selectedIndex, 1).toString();
//            String unit = tableModel.getValueAt(selectedIndex, 2).toString();
//            int quantity = Integer.parseInt(tableModel.getValueAt(selectedIndex, 3).toString());
//
//            // Store the data in the MySQL database
//            UserData user = getUserData(loggedInUsername);
//            if (user != null) {
//                updateDatabase(itemCode, itemName, unit, quantity, user);
//            } else {
//                System.out.println("User data not found for username: " + loggedInUsername);
//            }
//            tableModel.setValueAt(quantity, selectedIndex, 3);
//
//        }
//            }
//        });
//        
//        itemPanel.add(itemField);
//        itemPanel.add(addButton);
//        itemPanel.add(removeButton);
//        itemPanel.add(confirmButton);
//        inputPanel.add(itemPanel, BorderLayout.WEST);
//
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
//
//        increaseButton = new JButton("+");
//        decreaseButton = new JButton("-");
//
//        increaseButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                int selectedIndex = cartTable.getSelectedRow();
//                if (selectedIndex != -1) {
//                    String selectedItem = tableModel.getValueAt(selectedIndex, 1).toString();
//                    int currentQuantity = itemQuantityMap.get(selectedItem);
//                    itemQuantityMap.put(selectedItem, currentQuantity + 1);
//                    updateCartTable();
//                }
//            }
//        });
//
//        decreaseButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                int selectedIndex = cartTable.getSelectedRow();
//                if (selectedIndex != -1) {
//                    String selectedItem = tableModel.getValueAt(selectedIndex, 1).toString();
//                    int currentQuantity = itemQuantityMap.get(selectedItem);
//                    if (currentQuantity > 1) {
//                        itemQuantityMap.put(selectedItem, currentQuantity - 1);
//                    } else {
//                        itemQuantityMap.remove(selectedItem);
//                        tableModel.removeRow(selectedIndex);
//                    }
//                    updateCartTable();
//                }
//            }
//        });
//
//        buttonPanel.add(Box.createVerticalGlue());
//        buttonPanel.add(increaseButton);
//        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
//        buttonPanel.add(decreaseButton);
//        buttonPanel.add(Box.createVerticalGlue());
//        inputPanel.add(buttonPanel, BorderLayout.EAST);
//
//        add(inputPanel, BorderLayout.NORTH);
//        add(scrollPane, BorderLayout.CENTER);
//
//        // Adding double-click functionality to display item details
//        cartTable.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if (e.getClickCount() == 2) {
//                    JTable target = (JTable) e.getSource();
//                    int row = target.getSelectedRow();
//                    String itemName = tableModel.getValueAt(row, 1).toString();
//                    displayItemDetails(itemName);
//                }
//            }
//        });
//        setVisible(true);
//    }
//    public ViewShoppingCart(String loggedInUsername) {
//        setTitle("Shopping Cart");
//        this.loggedInUsername=loggedInUsername;
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(1000, 500);
//        setLayout(new BorderLayout());
//
//        tableModel = new DefaultTableModel(new String[]{"Item Code", "Item Name", "Unit", "Quantity"}, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false; // This makes all cells in the table non-editable
//            }
//        };
//        cartTable = new JTable(tableModel) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false; // This makes all cells in the table non-editable
//            }
//        };
//        cartTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
//            {
//                setHorizontalAlignment(SwingConstants.CENTER);
//            }
//        });
//        cartTable.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
//            {
//                setHorizontalAlignment(SwingConstants.CENTER);
//            }
//        });
//        cartTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
//            {
//                setHorizontalAlignment(SwingConstants.CENTER);
//            }
//        });
//
//        JScrollPane scrollPane = new JScrollPane(cartTable);
//
//        cartTable.getColumnModel().getColumn(0).setPreferredWidth(90);
//        cartTable.getColumnModel().getColumn(1).setPreferredWidth(500);
//        cartTable.getColumnModel().getColumn(2).setPreferredWidth(100);
//        cartTable.getColumnModel().getColumn(3).setPreferredWidth(110);
//        cartTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        cartTable.setFocusable(false); // Prevents focus on table cells
//
//        JPanel inputPanel = new JPanel();
//        inputPanel.setLayout(new BorderLayout());
//
//        JPanel itemPanel = new JPanel();
//        itemPanel.setLayout(new FlowLayout());
//
//        itemField = new JTextField(15);
//        addButton = new JButton("Add");
//        removeButton = new JButton("Remove");
//        confirmButton = new JButton("Confirm");
//        itemQuantityMap = new HashMap<>();
//        
//        JButton backButton = new JButton("Back");
//        backButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                // Dispose the current JFrame (combinedCart)
//                dispose();
//                
//                // Create and display the CSVImporter frame
//                CSVImporter csvImporter = new CSVImporter(chartPrice);
//                csvImporter.setVisible(true);
//                csvImporter.displayMainMenu();
//            }
//        });
//        add(backButton, BorderLayout.SOUTH);
//
//        addButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                String newItem = itemField.getText().trim();
//                if (!newItem.isEmpty()) {
//                    String[] itemDetails = findItemDetails(newItem);
//                    if (itemDetails != null) {
//                        String itemName = itemDetails[1];
//                        String itemCode = itemDetails[0];
//                        String unit = itemDetails[2];
//
//                        if (itemQuantityMap.containsKey(itemName)) {
//                            int currentQuantity = itemQuantityMap.get(itemName);
//                            itemQuantityMap.put(itemName, currentQuantity + 1);
//                        } else {
//                            itemQuantityMap.put(itemName, 1);
//                            tableModel.addRow(new Object[]{itemCode, itemName, unit, 1});
//                        }
//                        updateCartTable();
//                    } else {
//                        JOptionPane.showMessageDialog(null, "Item code not found.", "Error", JOptionPane.ERROR_MESSAGE);
//                    }
//                    itemField.setText("");
//                }
//            }
//        });
//
//        removeButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                int selectedIndex = cartTable.getSelectedRow();
//                if (selectedIndex != -1) {
//                    String selectedItem = tableModel.getValueAt(selectedIndex, 1).toString();
//                    int currentQuantity = itemQuantityMap.get(selectedItem);
//                    if (currentQuantity > 1) {
//                        itemQuantityMap.put(selectedItem, currentQuantity - 1);
//                    } else {
//                        itemQuantityMap.remove(selectedItem);
//                        tableModel.removeRow(selectedIndex);
//                    }
//                    updateCartTable();
//                }
//            }
//        });
//
//        confirmButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//              int selectedIndex = cartTable.getSelectedRow();
//              if (selectedIndex != -1) {
//            String itemCode = tableModel.getValueAt(selectedIndex, 0).toString();
//            String itemName = tableModel.getValueAt(selectedIndex, 1).toString();
//            String unit = tableModel.getValueAt(selectedIndex, 2).toString();
//            int quantity = Integer.parseInt(tableModel.getValueAt(selectedIndex, 3).toString());
//            UserData user = getUserData(loggedInUsername);
//            if (user != null) {
//                updateDatabase(itemCode, itemName, unit, quantity, user);
//            } else {
//               System.out.println("User data not found for username: " + loggedInUsername);
//             }
//        }
//            if (selectedIndex != -1) {
//                tableModel.removeRow(selectedIndex);
//            // Additional logic, if needed, after removing the row
//        }
//            }
//        });
//        
//        itemPanel.add(itemField);
//        itemPanel.add(addButton);
//        itemPanel.add(removeButton);
//        itemPanel.add(confirmButton);
//        inputPanel.add(itemPanel, BorderLayout.WEST);
//
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
//
//        increaseButton = new JButton("+");
//        decreaseButton = new JButton("-");
//
//        increaseButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                int selectedIndex = cartTable.getSelectedRow();
//                if (selectedIndex != -1) {
//                    String selectedItem = tableModel.getValueAt(selectedIndex, 1).toString();
//                    int currentQuantity = itemQuantityMap.get(selectedItem);
//                    itemQuantityMap.put(selectedItem, currentQuantity + 1);
//                    updateCartTable();
//                }
//            }
//        });
//
//        decreaseButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                int selectedIndex = cartTable.getSelectedRow();
//                if (selectedIndex != -1) {
//                    String selectedItem = tableModel.getValueAt(selectedIndex, 1).toString();
//                    int currentQuantity = itemQuantityMap.get(selectedItem);
//                    if (currentQuantity > 1) {
//                        itemQuantityMap.put(selectedItem, currentQuantity - 1);
//                    } else {
//                        itemQuantityMap.remove(selectedItem);
//                        tableModel.removeRow(selectedIndex);
//                    }
//                    updateCartTable();
//                }
//            }
//        });
//
//        buttonPanel.add(Box.createVerticalGlue());
//        buttonPanel.add(increaseButton);
//        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
//        buttonPanel.add(decreaseButton);
//        buttonPanel.add(Box.createVerticalGlue());
//        inputPanel.add(buttonPanel, BorderLayout.EAST);
//
//        add(inputPanel, BorderLayout.NORTH);
//        add(scrollPane, BorderLayout.CENTER);
//
//        // Adding double-click functionality to display item details
//        cartTable.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if (e.getClickCount() == 2) {
//                    JTable target = (JTable) e.getSource();
//                    int row = target.getSelectedRow();
//                    String itemName = tableModel.getValueAt(row, 1).toString();
//                    displayItemDetails(itemName);
//                }
//            }
//        });
//        fetchItemsForUser(loggedInUsername);
//        setVisible(true);
//    }
//    public void setUserData(String username) {
//        this.loggedInUsername = username;
//    }   
//    
//    private UserData getUserData(String username) {
//        UserData userData = null;
//        Connection connection = null;
//        PreparedStatement statement = null;
//        ResultSet resultSet = null;
//
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/javafx-video", "root", "Shuheng0330.");
//
//            String query = "SELECT * FROM users WHERE username = ?";
//            statement = connection.prepareStatement(query);
//            statement.setString(1, username);
//
//            resultSet = statement.executeQuery();
//
//            if (resultSet.next()) {
//                String retrievedUsername = resultSet.getString("Username");
//                String retrievedPassword = resultSet.getString("Password");
//                String retrievedEmail=resultSet.getString("Email");
//                String retrievedContact=resultSet.getString("Contactno");
//                // You can retrieve other user information similarly
//
//                // Create a UserData object to hold the retrieved data
//                userData = new UserData(retrievedUsername,retrievedPassword,retrievedEmail,retrievedContact);
//                // Add other retrieved information to the UserData object
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//             try {
//        if (resultSet != null) {
//            resultSet.close();
//        }
//        if (statement != null) {
//            statement.close();
//        }
//        if (connection != null) {
//            connection.close();
//        }
//    } catch (SQLException e) {
//        System.out.println("Error closing resources: " + e.getMessage());
//    }
//        }
//
//        return userData;
//    }
//private void updateDatabase(String itemCode, String itemName, String unit, int quantity, UserData user) {
//    String url = "jdbc:mysql://127.0.0.1:3306/combinedcart";
//    String usernamedb = "root";
//    String password = "Shuheng0330.";
//
//    try (Connection connection = DriverManager.getConnection(url, usernamedb, password)) {
//        String query = "UPDATE cart SET quantity = ? WHERE username = ? AND itemName = ?";
//        PreparedStatement preparedStatement = connection.prepareStatement(query);
//
//        preparedStatement.setInt(1, quantity);
//        preparedStatement.setString(2, user.getUsername());
//        preparedStatement.setString(3, itemName);
//
//        int rowsAffected = preparedStatement.executeUpdate();
//
//        if (rowsAffected > 0) {
//            JOptionPane.showMessageDialog(null, "Item quantity updated successfully!");
//        } else {
//            JOptionPane.showMessageDialog(null, "Failed to update item quantity.");
//        }
//    } catch (SQLException e) {
//        e.printStackTrace();
//    }
//}
//
////    private void updateDatabase(String itemCode, String itemName, String unit, int quantity, UserData user) {
////    String url = "jdbc:mysql://127.0.0.1:3306/combinedcart";
////    String usernamedb = "root";
////    String password = "Shuheng0330.";
////
////    try (Connection connection = DriverManager.getConnection(url, usernamedb, password)) {
////        String query = "INSERT INTO cart (username, itemCode, itemName, unit, quantity) VALUES (?, ?, ?, ?, ?)";
////        PreparedStatement preparedStatement = connection.prepareStatement(query);
////
////        preparedStatement.setString(1, user.getUsername()); // Assuming getUsername() gets the username from UserData
////        preparedStatement.setString(2, itemCode);
////        preparedStatement.setString(3, itemName);
////        preparedStatement.setString(4, unit);
////        preparedStatement.setInt(5, quantity);
////
////        int rowsAffected = preparedStatement.executeUpdate();
////
////        if (rowsAffected > 0) {
////            JOptionPane.showMessageDialog(null, "Item added to cart successfully!");
////        } else {
////            JOptionPane.showMessageDialog(null, "Failed to add item to cart.");
////        }
////    } catch (SQLException e) {
////        e.printStackTrace();
////    }
////}
////
////    
//
//    private void updateCartTable() {
//        tableModel.setRowCount(0);
//        for (Map.Entry<String, Integer> entry : itemQuantityMap.entrySet()) {
//            String itemName = entry.getKey();
//            int quantity = entry.getValue();
//            String[] itemDetails = findItemDetailsByName(itemName);
//            if (itemDetails != null) {
//                String itemCode = itemDetails[0];
//                String unit = itemDetails[2];
//                tableModel.addRow(new Object[]{itemCode, itemName, unit, quantity});
//            }
//        }
//    }
//
//    private void displayItemDetails(String itemName) {
//        String[] itemDetails = findItemDetailsByName(itemName);
//        if (itemDetails != null) {
//            String itemCode = itemDetails[0];
//            String unit = itemDetails[2];
//            String message = "Item Name: " + itemName + "\n"
//                    + "Item Code: " + itemCode + "\n"
//                    + "Unit: " + unit;
//            JOptionPane.showMessageDialog(this, message, "Item Details", JOptionPane.INFORMATION_MESSAGE);
//        } else {
//            JOptionPane.showMessageDialog(this, "Item details not found.", "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private String[] findItemDetails(String searchItem) {
//        String csvFile = "C:\\Users\\User2022\\Downloads\\lookup_item.csv"; // Replace with the actual path to your CSV file
//        String[] itemDetails = null;
//
//        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
//            String line;
//            boolean found = false;
//
//            while ((line = br.readLine()) != null) {
//                String[] fields = line.split(","); // Assuming the CSV file uses a comma as the delimiter
//
//                if (fields.length > 0 && fields[0].equals(searchItem)) {
//                    found = true;
//                    itemDetails = fields;
//                    break;
//                }
//            }
//
//            if (!found) {
//                System.out.println("Item code not found.");
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return itemDetails;
//    }
//
//    private String[] findItemDetailsByName(String searchItemName) {
//        String csvFile = "C:\\Users\\User2022\\Downloads\\lookup_item.csv"; // Replace with the actual path to your CSV file
//        String[] itemDetails = null;
//
//        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
//            String line;
//
//            while ((line = br.readLine()) != null) {
//                String[] fields = line.split(","); // Assuming the CSV file uses a comma as the delimiter
//
//                if (fields.length > 1 && fields[1].equals(searchItemName)) {
//                    itemDetails = fields;
//                    break;
//                }
//            }
//
//            if (itemDetails == null) {
//                System.out.println("Item name not found.");
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return itemDetails;
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                new ViewShoppingCart();
//            }
//        });
//    }
//
//
//    private void fetchItemsForUser(String loggedInUsername) {
//        itemQuantityMap = new HashMap<>();
//        // Connect to your database and fetch items for the specified user
//        // Modify the database connection details as needed
//
//        String url = "jdbc:mysql://127.0.0.1:3306/combinedcart";
//        String usernamedb = "root";
//        String password = "Shuheng0330.";
//
//        try (Connection connection = DriverManager.getConnection(url, usernamedb, password)) {
//            String query = "SELECT * FROM cart WHERE username = ?";
//            PreparedStatement preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setString(1, loggedInUsername);
//
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                String itemCode = resultSet.getString("itemCode");
//                String itemName = resultSet.getString("itemName");
//                String unit = resultSet.getString("unit");
//                int quantity = resultSet.getInt("quantity");
//
//                // Populate the table with fetched items
//                itemQuantityMap.put(itemName, quantity);
//                tableModel.addRow(new Object[]{itemCode, itemName, unit, quantity});
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}
