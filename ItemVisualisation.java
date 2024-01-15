/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pricetracker;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ItemVisualisation extends JFrame {
    private List<String[]> priceCatcherData;
    private List<String[]> lookupPremiseData;
    private List<String[]> lookupItemData;

    private JTextField itemNameField;
    private JTextField unitField;
    private JTextArea outputArea;

    public List<String[]> getPriceCatcherData() {
        return this.priceCatcherData;
    }

    public List<String[]> getLookupPremiseData() {
        return this.lookupPremiseData;
    }

    public List<String[]> getLookupItemData() {
        return this.lookupItemData;
    }

    public ItemVisualisation(List<String[]> priceCatcherData, List<String[]> lookupPremiseData, List<String[]> lookupItemData) {
        this.priceCatcherData = priceCatcherData;
        this.lookupPremiseData = lookupPremiseData;
        this.lookupItemData = lookupItemData;

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Top 5 Cheapest Sellers");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout());

        JLabel itemNameLabel = new JLabel("Enter the item name: ");
        itemNameField = new JTextField(20);
        JLabel unitLabel = new JLabel("Enter the unit: ");
        unitField = new JTextField(10);
        JButton displayButton = new JButton("Display Top 5 Cheapest Sellers");

        inputPanel.add(itemNameLabel);
        inputPanel.add(itemNameField);
        inputPanel.add(unitLabel);
        inputPanel.add(unitField);
        inputPanel.add(displayButton);

        add(inputPanel, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemName = itemNameField.getText();
                String unit = unitField.getText();

                StringBuilder result = new StringBuilder();
                result.append("Top 5 Cheapest Sellers for ").append(itemName).append(" (").append(unit).append(")\n\n");

                String itemCode = getItemCode(itemName, unit);
                if (itemCode != null) {
                    List<String[]> sellers = new ArrayList<>();
                    for (String[] row : priceCatcherData) {
                        if (row[2].equals(itemCode)) {
                            sellers.add(row);
                        }
                    }

                    if (sellers.isEmpty()) {
                        result.append("No sellers found for ").append(itemName).append(" (").append(unit).append(") in pricecatcherData.\n");
                    } else {
                        sellers.sort(Comparator.comparingDouble(row -> Double.parseDouble(row[3])));

                        int count = 0;
                        List<String> displayedPremises = new ArrayList<>();
                        for (String[] seller : sellers) {
                            if (count >= 5) {
                                break;
                            }
                            String premiseCode = seller[1];
                            String premiseDetails = getPremiseDetails(premiseCode);
                            if (premiseDetails != null && !displayedPremises.contains(premiseDetails)) {
                                result.append((count + 1)).append(". ").append(premiseDetails).append("\nPrice: $").append(seller[3]).append("\n\n");
                                displayedPremises.add(premiseDetails);
                                count++;
                            }
                        }
                    }
                } else {
                    result.append("Item not found!");
                }

                outputArea.setText(result.toString());
            }
        });

        JButton backButton = new JButton("Back");
        inputPanel.add(backButton);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    private String getItemCode(String itemName, String unit) {
        for (String[] item : lookupItemData) {
            if (item[1].equalsIgnoreCase(itemName) && item[2].equalsIgnoreCase(unit)) {
                return item[0];
            }
        }
        return null;
    }

    private String getPremiseDetails(String premiseCode) {
        for (String[] premise : lookupPremiseData) {
            if (premise[0].equals(premiseCode)) {
                return premise[1] + "\nAddress: " + premise[2];
            }
        }
        return null;
    }

    public static void main(String[] args) {
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
            visualisation.setSize(1600, 600);
            visualisation.setLocationRelativeTo(null);
            visualisation.setVisible(true);
        });
    }
}
