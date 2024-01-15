/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pricetracker;

public class ItemsInfo {
        protected String itemName;
        protected String premiseCode;
        protected double price;
        protected String premise;
        protected int quantity;
        protected String unit;


        public ItemsInfo(String itemName, String premiseCode, double price, String premise, int quantity, String unit) {
            this.itemName = itemName;
            this.premiseCode = premiseCode;
            this.price = price;
            this.premise = premise;
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

        public String getPremise() {
            return premise;
        }

        public void setShop(String premise) {
            this.premise = premise;
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
            return "ItemsInfo{" +
                    "itemName='" + itemName + '\'' +
                    ", premiseCode='" + premiseCode + '\'' +
                    ", price=" + price +
                    ", shop='" + premise + '\'' +
                    ", quantity=" + quantity +
                    '}';
        }
}

