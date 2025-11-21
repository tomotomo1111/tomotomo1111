import java.util.*;

public class CountryNeo {
    List<RegionNeo> regions = new ArrayList<>();
    private String name;

    public CountryNeo(String name) {this.name = name;}
    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}
    public void add(RegionNeo region) {regions.add(region);}
    public int getPopulation() {
        int result = regions.stream()
                            .mapToInt(RegionNeo::getPopulation)
                            .sum();
        System.out.print(this.getName() + " : " + result + "人, \n");
        return result;
    }
}