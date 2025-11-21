public class SoftwareNotPaternNeo {
    
    public static void main(String[] args) {
        
        CountryNeo country = new CountryNeo("日本国");
        RegionNeo region1 = new RegionNeo("中部地方");
        RegionNeo region2 = new RegionNeo("近畿地方");
        PrefectureNeo prefecture1 = new PrefectureNeo("愛知県");
        PrefectureNeo prefecture2 = new PrefectureNeo("静岡県");
        PrefectureNeo prefecture3 = new PrefectureNeo("大阪府");
        PrefectureNeo prefecture4 = new PrefectureNeo("京都府");
        
        prefecture1.add(new CityNeo("名古屋市", 2328846));
        prefecture1.add(new CityNeo("豊田市", 425677));
        prefecture1.add(new CityNeo("岡崎市", 387835));
        region1.add(prefecture1);
        prefecture2.add(new CityNeo("浜松市", 797980));
        prefecture2.add(new CityNeo("静岡市", 704898));
        prefecture2.add(new CityNeo("富士市", 248399));
        region1.add(prefecture2);
        country.add(region1);

        prefecture3.add(new CityNeo("大阪市", 2756034));
        prefecture3.add(new CityNeo("堺市", 822671));
        prefecture3.add(new CityNeo("東大阪市", 489151));
        region2.add(prefecture3);
        prefecture4.add(new CityNeo("京都市", 1388807));
        prefecture4.add(new CityNeo("宇治市", 183510));
        prefecture4.add(new CityNeo("亀岡市", 87518));
        region2.add(prefecture4);
        country.add(region2);

        int x = country.getPopulation();
    }
}
