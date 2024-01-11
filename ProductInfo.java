/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pricetracker;

public class ProductInfo {
        protected String itemName;
        protected String premiseCode;
        protected double price;
        protected String shop;
        protected int quantity;
        protected String unit;


        public ProductInfo(String itemName, String premiseCode, double price, String shop, int quantity, String unit) {
            this.itemName = itemName;
            this.premiseCode = premiseCode;
            this.price = price;
            this.shop = shop;
            this.quantity = quantity;
            this.unit=unit;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getPremiseCode() {
            return premiseCode;
        }

        public void setPremiseCode(String premiseCode) {
            this.premiseCode = premiseCode;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getShop() {
            return shop;
        }

        public void setShop(String shop) {
            this.shop = shop;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        // Calculate total price based on current quantity and price
        public double calculateTotalPrice() {
            return quantity * price;
        }
        @Override
        public String toString() {
            return "ProductInfo{" +
                    "itemName='" + itemName + '\'' +
                    ", premiseCode='" + premiseCode + '\'' +
                    ", price=" + price +
                    ", shop='" + shop + '\'' +
                    ", quantity=" + quantity +
                    '}';
        }
}
