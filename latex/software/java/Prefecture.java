import java.util.*;

public class Prefecture {
    List<City> cities = new ArrayList<>();
    private String name;

    public Prefecture(String name) {this.name = name;}
    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}
    public void add(City city) {cities.add(city);}
    public int getPopulation() {
        int result = 0;
        for (int i = 0; i < cities.size(); i++) {
            City city = (City) cities.get(i);
            result += city.getPopulation();
        }
        System.out.print(this.getName() + " : " + result + "人, ");
        System.out.println();
        return result;
    }
}