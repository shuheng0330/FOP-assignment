/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pricetracker;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSVDataReader {
    public static List<String[]> readCSV(String filePath) {
            List<String[]> data = new ArrayList<>();
            Pattern pattern = Pattern.compile(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    Matcher matcher = pattern.matcher(line);
                    List<String> fields = new ArrayList<>();
                    int count = 0;
                    while (matcher.find()) {
                        fields.add(line.substring(count, matcher.start()));
                        count = matcher.end();
                    }
                    fields.add(line.substring(count));

                    String[] fieldsArray = fields.toArray(new String[0]);
                    data.add(fieldsArray);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }
}
