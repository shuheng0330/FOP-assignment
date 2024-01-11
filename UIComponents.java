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
    private JButton selectButton;
    private Map<String, Integer> itemQuantityMap;

    private JButton increaseButton;
    private JButton decreaseButton;
    private JButton confirmButton;
    private JButton checkOutButton;
    
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
        confirmButton = new JButton("Confirm");
        selectButton = new JButton("Select"); ///
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
                JOptionPane.showMessageDialog(null, "All the items in your shopping cart is cleared now!");
            }
        });

        selectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectItems();
            }
            
            private void selectItems() {
                int[] selectedRows = cartTable.getSelectedRows();
                if (selectedRows.length > 0) {
                    StringBuilder message = new StringBuilder("Selected Items:\n");
                for (int row : selectedRows) {
                    String selectedCode = tableModel.getValueAt(row, 0).toString(); //item code is in the first column
                    String selectedName = tableModel.getValueAt(row, 1).toString(); //item name is in the second column
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

                            ArrayList<Double> availablePremises = new ArrayList<>(premiseCodesByItem);
                            availablePremises.retainAll(districtCodes);
                            
                            if (!availablePremises.isEmpty()) {
                                shopDet.showAvailablePremises(availablePremises);   // show available premises in that district that can buy items selected (but not best option)
                            
////                            UIComponents ui = new UIComponents();
////                            ArrayList<String> itemsToBePicked = ui.getItemsFromTableModel();//这里
//                            bestOption best = new bestOption();
//                            
//                                
//
////                                // Calculate the best option result and display it
//
//                                best.startShopping(selectedItemCode, allShops);//？？？？？
//                                
//                                
//                                displayOption display = new displayOption(); 
//                                display.displayShoppingResults(selectedShops, totalPrice);
//                                display.displayCheapestOptionAndPrice(cheapestShop, cheapestPrice);
        List<ProductInfo> productInfos = new ArrayList<>();
//        List<Map<String, Object>> bestPackages = findBestPackages.findBestPackages(productInfos);

// Call the findBestPackages method from the findBestPackages class
List<Map<String, Object>> bestPackages = findBestPackages.findBestPackages(productInfos);

// Show bestPackages information in a message dialog
StringBuilder packageDetails = new StringBuilder("Best Packages:\n");
for (Map<String, Object> packageInfo : bestPackages) {
    // Append package information to the StringBuilder
    // Customize this part according to how you want to display the information
    packageDetails.append("Shop: ").append(packageInfo.get("shop")).append("\n");
    // ... Other details you want to display
    packageDetails.append("-----------------\n");
}

// Show the message dialog with the best package details
JOptionPane.showMessageDialog(null, packageDetails.toString(), "Best Packages", JOptionPane.INFORMATION_MESSAGE);



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
                    ItemDetails itemDet = new ItemDetails();
                    itemDet.displayItemDetails(itemName);
                }
            }
        });
        setVisible(true);
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
    

}
