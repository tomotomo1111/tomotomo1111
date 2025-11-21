import java.util.*;

public class RegionNeo {
    List<PrefectureNeo> prefectures = new ArrayList<>();
    private String name;

    public RegionNeo(String name) {this.name = name;}
    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}
    public void add(PrefectureNeo prefecture) {prefectures.add(prefecture);}
    public int getPopulation() {
        int result = prefectures.stream()
                            .mapToInt(PrefectureNeo::getPopulation)
                            .sum();
        System.out.print(this.getName() + " : " + result + "人, \n");
        return result;
    }
}