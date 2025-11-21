import java.util.ArrayList;
import java.util.List;

public class GroupDistrict extends District {
    List<District> districts = new ArrayList<>();

    public GroupDistrict(String name) {
        this.name = name;
    }
    public void add(District district) {districts.add(district);}
    public int getPopulation() {
        int result = districts.stream()
                                .mapToInt(District::getPopulation)
                                .sum();
        System.out.print(this.getName() + " : " + result + "人, " + "\n");
        return result;
    }
}