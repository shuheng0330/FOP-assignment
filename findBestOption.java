/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pricetracker;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class findBestOption {
    static List<Map<String, Object>> findBestOption(List<ItemsInfo> itemsInfos) {
        StringBuilder messageBuilder = new StringBuilder("Here are the cheapest shops:\n\n");

        HashMap<String, Integer> itemQuantity = new HashMap<>();    //HashMap to store quantity of each item
        List<String> availableItem = itemsInfos.stream() //Lists to store unique item names
                .map(ItemsInfo::getItemName)
                .distinct()
                .collect(Collectors.toList());

        for (ItemsInfo items : itemsInfos) {                        // Populate itemQuantity map with item names and their quantities
            String itemName = items.getItemName();
            int cnt = items.getQuantity();
            itemQuantity.put(itemName, cnt);
        }

        List<String> shops = itemsInfos.stream() //List that contains particular premises (shops)
                .map(ItemsInfo::getPremise)
                .distinct()
                .collect(Collectors.toList());

        List<Map<String, Object>> result = new ArrayList<>();       // List to store the result(the best shop for each item)

        // Loop until all items are processed
        while (!availableItem.isEmpty()) {
            List<List<Map<String, Object>>> availableItemsPerShop = new ArrayList<>();
            //store items that should be removed from availableItem after processing each shop
            List<String> itemsToRemove = new ArrayList<>();

            // Iterate over each shop
            for (String shop : shops) {
                // Filter items for the current shop
                List<Map<String, Object>> shopItems = itemsInfos.stream()
                        .filter(info -> availableItem.contains(info.getItemName()) && shop.equals(info.getPremise()))
                        .collect(Collectors.groupingBy(
                                ItemsInfo::getItemName,
                                Collectors.minBy(Comparator.comparingDouble(ItemsInfo::getPrice))
                        ))
                        .values().stream()
                        .map(Optional::get)
                        .sorted(Comparator.<ItemsInfo, Integer>comparing(info -> itemQuantity.getOrDefault(info.getItemName(), 0)).reversed()
                                .thenComparingDouble(ItemsInfo::getPrice))
                        .map(item -> {
                            Map<String, Object> itemMap = new HashMap<>();
                            itemMap.put("item", item.getItemName());
                            itemMap.put("price", item.getPrice());
                            itemMap.put("unit", item.getUnit());
                            itemMap.put("shop", item.getPremise());
                            itemMap.put("quantity", itemQuantity.getOrDefault(item.getItemName(), 0));
                            return itemMap;
                        })
                        .collect(Collectors.toList());

                // Add the best items for the current shop to the list
                availableItemsPerShop.add(shopItems);
            }

            // Determine the best shop based on the quantity of items
            Map<String, Object> bestShopInfo = determineBestShop(availableItemsPerShop);

            // Sort items based on price for each shop
            availableItemsPerShop.forEach(items -> items.sort(Comparator.comparingDouble(i -> (double) i.get("price"))));

            // Get the best items from the best shop
            List<Map<String, Object>> bestShopItems = (List<Map<String, Object>>) bestShopInfo.get("items");

            // Append shop details and items to the message
            messageBuilder.append(String.format("------> Shop: %s%n\n", bestShopInfo.get("shop")));
            for (Map<String, Object> item : bestShopItems) {
                String itemName = (String) item.get("item");
                String unit = (String) item.get("unit");
                double price = (double) item.get("price");
                int count = itemQuantity.get(itemName);
                messageBuilder.append(String.format("Item : %s%nUnit: %s%nQuantity : %d%nPrice per unit : RM%.2f%n%n", itemName, unit, count, price));
                itemsToRemove.add(itemName);
            }

            availableItem.removeAll(itemsToRemove);
            // Add the best shop details and items to the result list
            result.add(bestShopInfo);
        }

        // Print the total price for all selected items after processing all shops
        messageBuilder.append("---------------------------------------------------\n");
        double totalPrice = result.stream()
                .mapToDouble(info -> calculateTotalPrices((List<Map<String, Object>>) info.get("items")))
                .sum();

        messageBuilder.append(String.format("Total price: RM%.2f%n", totalPrice));
        messageBuilder.append(String.format("\nNote that : \nIf the selected items are not displayed here, then the items are currently unavailable.",totalPrice));

        JTextArea textArea = new JTextArea(messageBuilder.toString());
        textArea.setEditable(false);  
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);  

        Font font = new Font("Segoe UI", Font.PLAIN, 13);
        textArea.setFont(font);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(300, 600));

        JOptionPane.showMessageDialog(null, scrollPane);

        return result;
    }

    //Method to find the shop that can buy the most items
    private static Map<String, Object> determineBestShop(List<List<Map<String, Object>>> availableItemsPerShop) {
        return availableItemsPerShop.stream()
                //Find the shop with the maximum total quantity of items
                .max(Comparator.comparingInt(items -> items.stream().mapToInt(item -> (int) item.get("quantity")).sum()))
                .orElseThrow(NoSuchElementException::new) //Throws an exception if the stream is empty (no shops are available).
                .stream()
                .findFirst() //Take the first item (there should be only one, as we selected the shop with the maximum total quantity)
                .map(item -> {  //Map the found item to a result map containing the shop and its items
                    Map<String, Object> result = new HashMap<>();
                    result.put("shop", item.get("shop"));
                    result.put("items", availableItemsPerShop.stream()
                            .flatMap(List::stream)
                            .filter(i -> i.get("shop").equals(item.get("shop")))
                            .collect(Collectors.toList()));
                    return result;
                })
                .orElseThrow(NoSuchElementException::new);
    }

    private static double calculateTotalPrices(List<Map<String, Object>> items) {
        return items.stream()
                //Maps each item to its price multiplied by its quantity
                .mapToDouble(item -> (double) item.get("price") * (int) item.get("quantity"))
                .sum();   //Calculate the sum of all the calculated prices
    }
}