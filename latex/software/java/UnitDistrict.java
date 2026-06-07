public class UnitDistrict extends District {
    private int population;

    public UnitDistrict(String name, int population) {
        this.name = name;
        this.population = population;
    }
    public int getPopulation() {
        System.out.print(this.getName() + " : " + this.population + "人, ");
        return this.population;
    }
    public void setPopulation(int p) { this.population = p; }
}