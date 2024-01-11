/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pricetracker;
import java.awt.BorderLayout;
import java.awt.Font;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.*;
/**
 *
 * @author User2022
 */
public class CombinedChartPriceGUI extends JFrame {
    private List<String[]> priceCatcherData;
    private List<String[]> lookupPremiseData;
    private List<String[]> lookupItemData;
    private List<String[]> joinedTables;

    private final String ITEM_CODE_COLUMN = "item_code";
    private final String PRICE_COLUMN = "price";
    private final String DATE_COLUMN = "date";

    private JTextArea outputTextArea;
    
    public List<String[]> getPriceCatcherData() {
        return this.priceCatcherData;
    }
    public List<String[]> getLookupPremiseData() {
        return this.lookupPremiseData;
    }
    public List<String[]> getLookupItemData() {
        return this.lookupItemData;
    }
    public CombinedChartPriceGUI(List<String[]> priceCatcherData, List<String[]> lookupPremiseData, List<String[]> lookupItemData) {
        this.priceCatcherData = priceCatcherData;
        this.lookupPremiseData = lookupPremiseData;
        this.lookupItemData = lookupItemData;

        this.joinedTables = getJoinTables();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Price Trend Chart");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        outputTextArea = new JTextArea();
        outputTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        outputTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        JLabel itemLabel = new JLabel("Enter a particular item: ");
        JTextField itemTextField = new JTextField(20);
        JLabel unitLabel = new JLabel("Enter the unit: ");
        JTextField unitTextField = new JTextField(10);
        JButton generateChartButton = new JButton("Generate Chart");

        inputPanel.add(itemLabel);
        inputPanel.add(itemTextField);
        inputPanel.add(unitLabel);
        inputPanel.add(unitTextField);
        inputPanel.add(generateChartButton);

        add(inputPanel, BorderLayout.NORTH);

        generateChartButton.addActionListener(e -> {
            String itemName = itemTextField.getText();
            String unit = unitTextField.getText();

            StringBuilder chartText = new StringBuilder();
            chartText.append("Price Trend Chart for ").append(itemName).append("\n\n");
            chartText.append("Days\t|\tPrice\n");
            chartText.append("-------------------------------------------------\n");

            for (String date : getUniqueDates()) {
                double itemAveragePrice = getAveragePriceForItemCode(date, getItemCode(itemName, unit));
                String printPattern = getScale(itemAveragePrice);
                String formattedItemAveragePrice = String.format("%.2f", itemAveragePrice);
                chartText.append(formatDateString(date, "dd/MM/yyyy", "dd-MM-yyyy"))
                        .append("\t| ").append(printPattern).append("\t").append(formattedItemAveragePrice)
                        .append("\n");
            }

            chartText.append("\nScale :\n");
            chartText.append("$ = RM 0.10 (for cent only)");

            outputTextArea.setText(chartText.toString());
        });

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String priceCatcherFilePath = "src/pricecatcher_2023-08.csv";
            String lookupPremiseFilePath = "src/lookup_premise.csv";
            String lookupItemFilePath = "src/lookup_item.csv";

            List<String[]> priceCatcherData = CSVDataReader.readCSV(priceCatcherFilePath);
            List<String[]> lookupPremiseData = CSVDataReader.readCSV(lookupPremiseFilePath);
            List<String[]> lookupItemData = CSVDataReader.readCSV(lookupItemFilePath);

            CombinedChartPriceGUI gui = new CombinedChartPriceGUI(priceCatcherData, lookupPremiseData, lookupItemData);
            gui.setSize(1600,500);
            gui.setLocationRelativeTo(null);
            gui.setVisible(true);
        });
    }
    
    String getItemCode(String itemName, String unit) {
        for (String[] item : lookupItemData) {
            if (item[1].equalsIgnoreCase(itemName) && item[2].equalsIgnoreCase(unit)) {
                return item[0];
            }
        }
        return null;
    }

    String getScale(double price) {
        double avprice=price-(int)price;
        int scale = (int) (avprice / 0.10);
        StringBuilder scaleString = new StringBuilder();
        for (int i = 0; i < scale; i++) {
            scaleString.append("$");
        }
        return scaleString.toString();
    }
    
    static String formatDateString(String date, String inputDateFormat, String outputDateFormat) {
        Date parsedInputDate = null;
        try {
            parsedInputDate = new SimpleDateFormat(inputDateFormat).parse(date);
            return new SimpleDateFormat(outputDateFormat).format(parsedInputDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    List<String> getUniqueDates() {
        
    Set<String> uniqueDates = new HashSet<>();
        int dateColumnIndex = getColumnNameWithIndex(priceCatcherData).get(DATE_COLUMN);

        // Iterate through the list and add values from the specified column to the set
        for (String[] row : priceCatcherData) {
            if (row.length > dateColumnIndex) {
                if (!row[dateColumnIndex].equals(DATE_COLUMN)) {
                    uniqueDates.add(row[dateColumnIndex]);
                }
            }
        }

        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // Convert strings to Date objects
        List<Date> dateList = new ArrayList<>();
        for (String dateString : uniqueDates) {
            try {
                Date date = inputDateFormat.parse(dateString);
                dateList.add(date);
            } catch (ParseException e) {
                System.err.println("Error parsing date string: " + dateString);
                e.printStackTrace(); // Handle parsing exception
            }
        }


        // Sort the Date objects
        dateList.sort(Comparator.naturalOrder());
        List<String> sortedDateString = new ArrayList<>();
        for (Date date : dateList) {
            sortedDateString.add(outputDateFormat.format(date));
        }
            
        return sortedDateString;
    }


    Double getAveragePriceForItemCode(String date, String itemCode) {
        Map<String, Integer> joinedTablesColumnNameWithIndex = getColumnNameWithIndex(joinedTables);
        List<String[]> filteredData = joinedTables.stream()
                .filter(row -> row[joinedTablesColumnNameWithIndex.get("date")].equals(date))
                .filter(row -> row[joinedTablesColumnNameWithIndex.get("item_code")].equals(itemCode))
                .collect(Collectors.toList());

        // Calculate the average price by summing up the total prices for each premise
        double sumOfPrices = filteredData.stream()
                .mapToDouble(row -> Double.parseDouble(row[joinedTablesColumnNameWithIndex.get("price")]))
                .sum();

        long distinctPremiseCodeCount= filteredData.stream()
                .map(row -> row[joinedTablesColumnNameWithIndex.get("premise_code")])
                .distinct()
                .count();

        return sumOfPrices / distinctPremiseCodeCount;
    }


    public Map<String, Integer> getColumnNameWithIndex(List<String[]> csvDataList) {
        Map<String, Integer> columnNameWithIndex = new HashMap<>();
        String[] csvHeader = csvDataList.get(0);
        for (int i=0; i<csvHeader.length; i++) {
            String columnName = csvHeader[i];
            columnNameWithIndex.put(columnName, i);
        }
        return columnNameWithIndex;
    }

    private List<String[]> getJoinTables() {
        if (this.joinedTables != null) {
            return this.joinedTables;
        }
        this.joinedTables = new ArrayList<>();
        Map<String, Integer> priceTrackerDataColumnNameWithIndex = getColumnNameWithIndex(priceCatcherData);
        Map<String, Integer> lookupItemDataColumnNameWithIndex = getColumnNameWithIndex(lookupItemData);
        int itemCodeIndex_priceCatcher = priceTrackerDataColumnNameWithIndex.get("item_code");
        int itemCodeIndex_lookupItem = lookupItemDataColumnNameWithIndex.get("item_code");
        Map<String, List<String[]>> lookupItemMap = lookupItemData.stream()
                .collect(Collectors.groupingBy(row -> row[itemCodeIndex_lookupItem]));
        List<String> headers = new ArrayList<>();
        headers.addAll(Arrays.asList(priceCatcherData.get(0)));
        headers.addAll(Arrays.asList(lookupItemData.get(0)));

        this.joinedTables.add(headers.toArray(new String[0]));
        List<String[]> joinedTablesResults = priceCatcherData.stream().skip(1)
                .map(rowA -> {
                    String itemCode = rowA[itemCodeIndex_priceCatcher];
                    List<String[]> matchingRowsB = lookupItemMap.getOrDefault(itemCode, List.of());

                    return matchingRowsB.stream()
                            .map(rowB -> {
                                // Assuming you want to concatenate rows from tableA and tableB
                                String[] joinedRow = new String[rowA.length + rowB.length];
                                System.arraycopy(rowA, 0, joinedRow, 0, rowA.length);
                                System.arraycopy(rowB, 0, joinedRow, rowA.length, rowB.length);
                                return joinedRow;
                            })
                            .collect(Collectors.toList());
                })
                .flatMap(List::stream)
                .toList();
        this.joinedTables.addAll(joinedTablesResults);
        return this.joinedTables;
    }
}
