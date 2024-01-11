/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pricetracker;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JOptionPane;

public class displayOption {
    private void displayShoppingResults(ArrayList<bestOption.Shop> selectedShops, double totalPrice) {
    StringBuilder message = new StringBuilder("Best Option:\n");

    for (bestOption.Shop shop : selectedShops) {
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

    System.out.println(message.toString()); // Check if the message is being generated correctly

    JOptionPane.showMessageDialog(null, message.toString(), "Best Option For You", JOptionPane.INFORMATION_MESSAGE);
}
        
    private void displayCheapestOptionAndPrice(bestOption.Shop cheapestShop, double cheapestPrice) {
    if (cheapestShop != null) {
        StringBuilder message = new StringBuilder("Cheapest Option:\n");
        message.append("Shop: ").append(cheapestShop.getName()).append("\n");
        message.append("Premise Code: ").append(cheapestShop.getPremiseCode()).append("\n");
        message.append("Address: ").append(cheapestShop.getAddress()).append("\n");
        message.append("District: ").append(cheapestShop.getDistrict()).append("\n");
        message.append("Total Price: $").append(cheapestPrice);

        JOptionPane.showMessageDialog(null, message.toString(), "Cheapest Option For You", JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(null, "No shops available.", "Cheapest Option", JOptionPane.INFORMATION_MESSAGE);
    }
}
    
}
