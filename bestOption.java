/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pricetracker;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.lang.String;

public class bestOption {
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

    }
    
    public List<Shop> getShopsWithItems(List<String> itemsToBePicked, List<Shop> allShops) {
    List<Shop> shopsWithItems = new ArrayList<>();

    for (Shop shop : allShops) {
        Map<String, Double> shopItems = shop.getAvailableItems();
        boolean shopHasItems = false;

        for (String item : itemsToBePicked) {
            if (shopItems.containsKey(item)) {
                shopHasItems = true;
                break;
            }
        }

        if (shopHasItems) {
            shopsWithItems.add(shop);
        }
    }

    return shopsWithItems;
}

    
    public class ShopItems {
        private Map<String, Map<String, Double>> shopItems = new HashMap<>();

        public void readShopItemsFromCSV(String priceCatcherFilePath) {
//            UIComponents ui = new UIComponents();
//            ArrayList<String> itemList = ui.getItemsFromTableModel();
            try (BufferedReader br = new BufferedReader(new FileReader(priceCatcherFilePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] fields = line.split(",");
                    if (fields.length >= 3) {
                        String premiseCode = fields[1].trim();
                        String itemCode = fields[2].trim();
                        double price = Double.parseDouble(fields[3].trim());

                        // If the shop exists in the map, retrieve its item map, else create a new one
                        Map<String, Double> itemsMap = shopItems.computeIfAbsent(premiseCode, k -> new HashMap<>());
                        // Add the item and its price to the shop's item map
                        itemsMap.put(itemCode, price);
                    }
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
       }
    
    public Map<String, Map<String, Double>> getShopItems() {
        return shopItems;
    }
    }
    
    

    public Shop findBestShop(List<String> itemsToBePicked, List<Shop> shopsList) {
        Shop bestShop = null;
        int maxItemsFound = 0;

        for (Shop shop : shopsList) {
            Map<String, Double> availableItems = shop.getAvailableItems();

            List<String> commonItems = new ArrayList<>(availableItems.keySet());
            commonItems.retainAll(itemsToBePicked);

            if (commonItems.size() > maxItemsFound) {
                bestShop = shop;
                maxItemsFound = commonItems.size();
            }
        }

        return bestShop;
    }
    
    //method to find the Cheapest Shops among the shop map
    public List<Shop> findCheapestShops(List<String> itemsToBePicked, List<Shop> shopsList) {
        List<Shop> selectedShops = new ArrayList<>();
        List<String> remainingItems = new ArrayList<>(itemsToBePicked);

        while (!remainingItems.isEmpty() && !shopsList.isEmpty()) {
            Shop bestShop = findBestShop(remainingItems, selectedShops);

            if (bestShop != null) {
                selectedShops.add(bestShop);
                removeCommonItems(remainingItems, bestShop);
                shopsList.remove(bestShop);
            }
        }

        return selectedShops;
    }

    private void removeCommonItems(List<String> itemsToBePicked, Shop bestShop) {
        Map<String, Double> availableItems = bestShop.getAvailableItems();

        itemsToBePicked.removeIf(availableItems::containsKey);
    }
    
    private double calculateTotalPrice(List<Shop> selectedShops) {
        double totalPrice = 0.0;

        for (Shop shop : selectedShops) {
            Map<String, Double> availableItems = shop.getAvailableItems();
            totalPrice += availableItems.values().stream().mapToDouble(Double::doubleValue).sum();
        }

        return totalPrice;
    }

    public void startShopping(List<String> itemList, List<Shop> allShops) {
        List<Shop> shopsWithItems = getShopsWithItems(itemList, allShops);
        List<Shop> cheapestShops = findCheapestShops(itemList, new ArrayList<>(shopsWithItems));
        double totalPrice = calculateTotalPrice(cheapestShops);
        displaySelectedShops(cheapestShops);
        displayTotalPrice(totalPrice);
    }
    
    // Helper method to display selected shops in UI 
    private void displaySelectedShops(List<Shop> cheapestShops) {
        System.out.println("Selected Shops:");
        for (Shop shop : cheapestShops) {
            System.out.println(shop.getName() + " - " + shop.getAddress());
        }
    }

    // Helper method to display total price in UI 
    private void displayTotalPrice(double totalPrice) {
        System.out.println("Total Price: " + totalPrice);
    }
    
}
