package util;

import entities.MenuItem;

import java.util.LinkedHashMap;
import java.util.Map;

public class MenuLoader {
    public static Map<String , MenuItem> load() {
        Map<String, MenuItem> menu = new LinkedHashMap<>();
        menu.put("M1", new MenuItem("M1", "Veg Thali", 80.00));
        menu.put("C1", new MenuItem("C1", "Coffee", 30.00));
        menu.put("S1", new MenuItem("S1", "Sandwich", 60.00));
        return menu;
    }
}
