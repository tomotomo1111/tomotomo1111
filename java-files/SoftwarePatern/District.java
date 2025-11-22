
public abstract class District {
    private String name;
    public abstract int getPopulation();
    public String getName () { return name; }
    public void setName(String n) { this.name = n; }
}