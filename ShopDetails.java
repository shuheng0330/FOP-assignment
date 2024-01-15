/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pricetracker;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

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

                if (fields.length > 2 && fields[2].trim().equals(ItemCode.trim())) {
                    try {
                        double premiseCode = Double.parseDouble(fields[1].trim());
                        if (!premiseCodes.contains(premiseCode)) {
                            premiseCodes.add(premiseCode);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid premise code format: " + fields[1].trim());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return premiseCodes;
    }

    public ArrayList<String> findPremiseCodesByDistrict(String district) {
        ArrayList<String> premiseCodes = new ArrayList<>();
        String csvFile = "src/lookup_premise.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (fields.length > 5 && fields[5].trim().equalsIgnoreCase(district.trim())) {
                    premiseCodes.add(fields[0].trim()); 
                }
            }
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
        boolean skipHeader = true; 

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue; 
                }

                String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                if (fields.length >= 6) {
                    double premiseCode;
                    try {
                        premiseCode = Double.parseDouble(fields[0].trim());
                    } catch (NumberFormatException e) {
                        continue;
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

        TableColumnModel columnModel = premiseTable.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(80);  
        columnModel.getColumn(1).setPreferredWidth(300);
        columnModel.getColumn(2).setPreferredWidth(900);
        columnModel.getColumn(3).setPreferredWidth(200);
        columnModel.getColumn(4).setPreferredWidth(70);
        columnModel.getColumn(5).setPreferredWidth(70);

        JScrollPane premiseScrollPane = new JScrollPane(premiseTable);
        premiseScrollPane.setPreferredSize(new Dimension(1200, 500));  
        
        JOptionPane.showMessageDialog(null, premiseScrollPane, "Available shops to buy items in cart", JOptionPane.INFORMATION_MESSAGE);
    }

    private static final String CSV_PRICECATHCER = "src/pricecatcher_2023-08.csv";

    private static Set<String> processedPremiseCodes = new HashSet<>();

    public List<String> getPremiseCodesFromCSV(String itemCode) {
        List<String> premiseCodes = new ArrayList<>();
        Set<String> processedPremiseCodes = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PRICECATHCER))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length > 0 && fields[2].trim().equals(itemCode.trim())) {
                    String premiseCode = fields[1].trim();
                    // check if the same premiseCode is fetched before
                    if (!processedPremiseCodes.contains(premiseCode)) {
                        processedPremiseCodes.add(premiseCode);
                        premiseCodes.add(premiseCode);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return premiseCodes;
    }

    public double getPriceFromCSV(String itemCode, String premiseCode) {
        double price = 0.0; 

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PRICECATHCER))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(","); 
                if (fields.length >= 4 && fields[2].trim().equals(itemCode.trim()) && fields[1].trim().equals(premiseCode.trim())) {
                    price = Double.parseDouble(fields[3].trim());
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return price;
    }

    public String getPremiseFromCSV(double premiseCode) {
        String premise = ""; 
        String csvFile = "src/lookup_premise.csv";
        boolean skipHeader = true;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue; 
                }

                String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                if (fields.length >= 6) {
                    double code;
                    try {
                        code = Double.parseDouble(fields[0].trim());
                    } catch (NumberFormatException e) {
                        continue; 
                    }

                    if (code == premiseCode) {
                        premise = fields[1].trim();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return premise;
    }
}