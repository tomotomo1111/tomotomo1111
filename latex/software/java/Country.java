import java.util.*;

public class Country {
    List<Region> regions = new ArrayList<>();
    private String name;

    public Country(String name) {this.name = name;}
    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}
    public void add(Region region) {regions.add(region);}
    public int getPopulation() {
        int result = 0;
        for (int i = 0; i < regions.size(); i++) {
            Region region = (Region) regions.get(i);
            result += region.getPopulation();
        }
        System.out.print(this.getName() + " : " + result + "人, ");
        System.out.println("\n");
        return result;
    }
}