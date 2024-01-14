/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pricetracker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ShopDetails {
    public ArrayList<Double> findPremiseCodesByItemCode(String ItemCode) {
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

                // fields[2] is location of item code
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

//    Method to find premise code in lookup_premise CSV to find premise in specific district enter by user
    public ArrayList<String> findPremiseCodesByDistrict(String district) {
        ArrayList<String> premiseCodes = new ArrayList<>();
        String csvFile = "src/lookup_premise.csv"; 

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (fields.length > 5 && fields[5].trim().equalsIgnoreCase(district.trim())) {
                    premiseCodes.add(fields[0].trim()); // premise code is at index 0
                }
            }
            //System.out.println("Premise codes found for district " + district + ": " + premiseCodes);
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (premiseCodes.isEmpty()) {
            System.out.println("No matching premise codes found for the district: " + district);
        }

        return premiseCodes;
    }
    
    public String[] findPremiseDetailsByCode(String premiseCode) {
        String csvFile = "src/lookup_premise.csv";
        String[] premiseDetails = null;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(","); 

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
    
    public void showAvailablePremises(ArrayList<Double> premiseCodes) {
        ArrayList<String[]> premiseData = new ArrayList<>();

        String csvFile = "src/lookup_premise.csv";
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
    }
    
    private void displayPremiseDetails(ArrayList<String[]> premiseData) {
            DefaultTableModel premiseTableModel = new DefaultTableModel(
                    new String[]{"Premise Code", "Premise", "Address", "Premise Type", "State", "District"}, 0);

            for (String[] data : premiseData) {
                premiseTableModel.addRow(data);
            }

            JTable premiseTable = new JTable(premiseTableModel);
            JScrollPane premiseScrollPane = new JScrollPane(premiseTable);

            JOptionPane.showMessageDialog(null, premiseScrollPane, "Available shops to buy items in shopping cart", JOptionPane.INFORMATION_MESSAGE);
        }
    
    
    private static final String CSV_PRICECATHCER = "src/pricecatcher_2023-08.csv";
    
    public double getPriceFromCSV(String itemCode) {
        double price = 0.0; // Default value if item code is not found
        
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PRICECATHCER))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(","); // Split CSV by comma
                if (fields.length > 0 && fields[2].trim().equals(itemCode.trim())) {
                    price = Double.parseDouble(fields[3].trim()); 
                    break; // Exit loop once found
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        
        return price;
    }
    
    public String getPremiseCodeFromCSV(String itemCode) {
        String premiseCode = ""; // Default value if item code is not found
        
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PRICECATHCER))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(","); // Split CSV by comma 
                if (fields.length > 0 && fields[2].trim().equals(itemCode.trim())) {
                    premiseCode = fields[1].trim(); 
                    break; // Exit loop once found
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception accordingly
        }
        
        return premiseCode;
    }
    
    public String getPremiseFromCSV(double premiseCode) {
        String premise = ""; // Default value if premise code is not found
        String csvFile = "src/lookup_premise.csv";
        boolean skipHeader = true;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
                        System.out.println("PremiseCode: "+premiseCode);
            while ((line = br.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue; // Skip the header row
                }

                String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                if (fields.length >= 6) {
                    double code;
                    try {
                        code = Double.parseDouble(fields[0].trim());
                    } catch (NumberFormatException e) {
                        continue; // Skip lines that don't start with a number
                    }
                    System.out.println("field[0]"+fields[0].trim());

                    if (code == premiseCode) {
                        premise = fields[1].trim();
                        System.out.println("premise get from csv: "+premise);   
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return premise;
    }
}
