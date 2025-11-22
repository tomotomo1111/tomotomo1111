
public class UnitDistrict extends District {
    private int population;
    public int getPopulation() {
        System.out.print(this.getName() + " : " + this.population + "人, ");
        return population;
    }
    public void setPopulation(int p) { this.population = p; }
}