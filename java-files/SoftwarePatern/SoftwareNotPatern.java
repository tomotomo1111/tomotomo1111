import java.util.*;

public class SoftwareNotPatern {
    
    public static void main(String[] args) {
        
        Country country = new Country("日本");
        Region region = new Region("中部");
        Prefecture prefecture = new Prefecture("愛知県");
        City city = new City("長久手市", 300000);
        
        prefecture.add(city);
        region.add(prefecture);
        country.add(region);
        int x = country.getPopulation();
    }
}
