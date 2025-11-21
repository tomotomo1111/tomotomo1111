public class SoftwarePatern {

    public static void main(String[] args) {

        GroupDistrict country = new GroupDistrict("日本国");
        GroupDistrict region1 = new GroupDistrict("中部地方");
        GroupDistrict region2 = new GroupDistrict("近畿地方");
        GroupDistrict prefecture1 = new GroupDistrict("愛知県");
        GroupDistrict prefecture2 = new GroupDistrict("静岡県");
        GroupDistrict prefecture3 = new GroupDistrict("大阪府");
        GroupDistrict prefecture4 = new GroupDistrict("京都府");
        
        prefecture1.add(new UnitDistrict("名古屋市", 2328846));
        prefecture1.add(new UnitDistrict("豊田市", 425677));
        prefecture1.add(new UnitDistrict("岡崎市", 387835));
        region1.add(prefecture1);
        prefecture2.add(new UnitDistrict("浜松市", 797980));
        prefecture2.add(new UnitDistrict("静岡市", 704898));
        prefecture2.add(new UnitDistrict("富士市", 248399));
        region1.add(prefecture2);
        country.add(region1);

        prefecture3.add(new UnitDistrict("大阪市", 2756034));
        prefecture3.add(new UnitDistrict("堺市", 822671));
        prefecture3.add(new UnitDistrict("東大阪市", 489151));
        region2.add(prefecture3);
        prefecture4.add(new UnitDistrict("京都市", 1388807));
        prefecture4.add(new UnitDistrict("宇治市", 183510));
        prefecture4.add(new UnitDistrict("亀岡市", 87518));
        region2.add(prefecture4);
        country.add(region2);

        int x = country.getPopulation();
    }
}