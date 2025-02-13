import java.util.ArrayList;
import java.util.List;

public class CityDatabase {
    private static final List<String> cities = new ArrayList<>();

    static {
        // Initialize the list with cities
        cities.add("Adrian");
        cities.add("Alma");
        cities.add("Ann Arbor");
        cities.add("Battle Creek");
        cities.add("Bay City");
        cities.add("Benton Harbor");
        cities.add("Bloomfield Hills");
        cities.add("Cadillac");
        cities.add("Charlevoix");
        cities.add("Cheboygan");
        cities.add("Dearborn");
        cities.add("Detroit");
        cities.add("East Lansing");
        cities.add("Eastpointe");
        cities.add("Ecorse");
        cities.add("Escanaba");
        cities.add("Flint");
        cities.add("Grand Haven");
        cities.add("Grand Rapids");
        cities.add("Grayling");
        cities.add("Grosse Pointe");
        cities.add("Hancock");
        cities.add("Highland Park");
        cities.add("Holland");
        cities.add("Houghton");
        cities.add("Interlochen");
        cities.add("Iron Mountain");
        cities.add("Ironwood");
        cities.add("Ishpeming");
        cities.add("Jackson");
        cities.add("Kalamazoo");
        cities.add("Lansing");
        cities.add("Livonia");
        cities.add("Ludington");
        cities.add("Mackinaw City");
        cities.add("Manistee");
        cities.add("Marquette");
        cities.add("Menominee");
        cities.add("Midland");
        cities.add("Monroe");
        cities.add("Mount Clemens");
        cities.add("Mount Pleasant");
        cities.add("Muskegon");
        cities.add("Niles");
        cities.add("Petoskey");
        cities.add("Pontiac");
        cities.add("Port Huron");
        cities.add("Royal Oak");
        cities.add("Saginaw");
        cities.add("Saint Ignace");
        cities.add("Saint Joseph");
        cities.add("Sault Sainte Marie");
        cities.add("Traverse City");
        cities.add("Trenton");
        cities.add("Warren");
        cities.add("Wyandotte");
        cities.add("Ypsilanti");
        cities.add("Montgomery");
        cities.add("Juneau");
        cities.add("Phoenix");
        cities.add("Little Rock");
        cities.add("Sacramento");
        cities.add("Denver");
        cities.add("Hartford");
        cities.add("Dover");
        cities.add("Tallahassee");
        cities.add("Atlanta");
        cities.add("Honolulu");
        cities.add("Boise");
        cities.add("Springfield");
        cities.add("Indianapolis");
        cities.add("Des Moines");
        cities.add("Topeka");
        cities.add("Frankfort");
        cities.add("Baton Rouge");
        cities.add("Augusta");
        cities.add("Annapolis");
        cities.add("Boston");
        cities.add("Lansing");
        cities.add("Saint Paul");
        cities.add("Jackson");
        cities.add("Jefferson City");
        cities.add("Helena");
        cities.add("Lincoln");
        cities.add("Carson City");
        cities.add("Concord");
        cities.add("Trenton");
        cities.add("Santa Fe");
        cities.add("Albany");
        cities.add("Raleigh");
        cities.add("Bismarck");
        cities.add("Columbus");
        cities.add("Oklahoma City");
        cities.add("Salem");
        cities.add("Harrisburg");
        cities.add("Providence");
        cities.add("Columbia");
        cities.add("Pierre");
        cities.add("Nashville");
        cities.add("Austin");
        cities.add("Salt Lake City");
        cities.add("Montpelier");
        cities.add("Richmond");
        cities.add("Olympia");
        cities.add("Charleston");
        cities.add("Madison");
        cities.add("Cheyenne");
    }

    // Get the list of cities
    public static List<String> getCities() {
        return new ArrayList<>(cities); // Return a copy to prevent modification
    }
}
