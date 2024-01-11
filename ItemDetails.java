/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pricetracker;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;

public class ItemDetails {
    public String[] findItemDetails(String searchItem) {
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

    public String[] findItemDetailsByName(String searchItemName) {
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
    
    public void displayItemDetails(String itemName) {
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
    
}
