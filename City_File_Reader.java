import java.io.*;
import java.util.ArrayList;

public class City_File_Reader {
    public static ArrayList<String> readCitiesFromFile() {
        ArrayList<String> cities = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("us_cities.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                cities.add(line);  // Add each city to the list
            }
            System.out.println("Cities read from us_cities.txt.");
        } catch (IOException e) {
            System.out.println("An error occurred while reading cities from file.");
            e.printStackTrace();
        }
        return cities;
    }

    public static void main(String[] args) {
        ArrayList<String> cities = readCitiesFromFile();
        System.out.println("Cities loaded: " + cities.size());
        for (String city : cities) {
            System.out.println(city);  // Print each city to the console
        }
    }
}