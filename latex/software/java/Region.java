import java.util.*;

public class Region {
    List<Prefecture> prefectures = new ArrayList<>();
    private String name;

    public Region(String name) {this.name = name;}
    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}
    public void add(Prefecture prefecture) {prefectures.add(prefecture);}
    public int getPopulation() {
        int result = 0;
        for (int i = 0; i < prefectures.size(); i++) {
            Prefecture prefecture = (Prefecture) prefectures.get(i);
            result += prefecture.getPopulation();
        }
        System.out.print(this.getName() + " : " + result + "人, ");
        System.out.println("\n");
        return result;
    }
}