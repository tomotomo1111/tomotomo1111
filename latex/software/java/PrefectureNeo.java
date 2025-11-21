import java.util.*;

public class PrefectureNeo {
    List<CityNeo> cities = new ArrayList<>();
    private String name;

    public PrefectureNeo(String name) {this.name = name;}
    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}
    public void add(CityNeo city) {cities.add(city);}
    public int getPopulation() {
        int result = cities.stream()
                            .mapToInt(CityNeo::getPopulation)
                            .sum();
        System.out.print(this.getName() + " : " + result + "人, \n");
        return result;
    }
}