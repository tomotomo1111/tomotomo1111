
public class SoftwarePatern {

    public static void main(String[] args) {
        
        GroupDistrict country = new GroupDistrict();
        GroupDistrict region = new GroupDistrict();
        GroupDistrict prefecture = new GroupDistrict();
        UnitDistrict city = new UnitDistrict();
        
        city.setName("長久手市");
        city.setPopulation(300000);
        prefecture.setName("愛知県");
        region.setName("中部地方");
        country.setName("日本");
        prefecture.add(city);
        region.add(prefecture);
        country.add(region);
        int x = country.getPopulation();
    }
}