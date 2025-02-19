import java.io.*;
import java.util.ArrayList;

public class City_File_Writer {
    public static void writeCitiesToFile(ArrayList<String> cities) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("us_cities.txt"))) {
            for (String city : cities) {
                writer.write(city);
                writer.newLine();  // Write each city on a new line
            }
            System.out.println("Cities written to us_cities.txt.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing cities to file.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ArrayList<String> cities = CityList.getCities();
        writeCitiesToFile(cities);
    }
}
