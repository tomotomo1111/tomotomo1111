import java.util.*;

public class SoftwareNotPatern {
    
    public static void main(String[] args) {
        
        Country country = new Country("日本国");
        Region region1 = new Region("中部地方");
        Region region2 = new Region("近畿地方");
        Prefecture prefecture1 = new Prefecture("愛知県");
        Prefecture prefecture2 = new Prefecture("静岡県");
        Prefecture prefecture3 = new Prefecture("大阪府");
        Prefecture prefecture4 = new Prefecture("京都府");
        
        prefecture1.add(new City("名古屋市", 2328846));
        prefecture1.add(new City("豊田市", 425677));
        prefecture1.add(new City("岡崎氏", 387835));
        region1.add(prefecture1);
        prefecture2.add(new City("浜松市", 797980));
        prefecture2.add(new City("静岡市", 704898));
        prefecture2.add(new City("富士市", 248399));
        region1.add(prefecture2);

        prefecture3.add(new City("大阪市", 2756034));
        prefecture3.add(new City("堺市", 822671));
        prefecture3.add(new City("東大阪市", 489151));
        region2.add(prefecture3);
        prefecture4.add(new City("京都市", 1388807));
        prefecture4.add(new City("宇治氏", 183510));
        prefecture4.add(new City("亀岡市", 87518));
        region2.add(prefecture4);

        country.add(region1);
        country.add(region2);
        int x = country.getPopulation();
    }
}
