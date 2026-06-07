import java.util.ArrayList;
import java.util.List;

public class GroupDistrict extends District {
    List<District> districts = new ArrayList<>();
    public void add(District district) { districts.add(district); }
    public int getPopulation() {
        int result = 0;
        for (int i = 0; i < districts.size(); i++) {
            District district= (District)districts.get(i);
            result += district.getPopulation();
        }
        System.out.println();
        System.out.print(this.getName() + " : " + result + "人, ");
        return result;
    }
}