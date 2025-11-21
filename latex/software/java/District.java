public abstract class District {
    protected String name;

    public abstract int getPopulation();
    public String getName() { return this.name; }
    public void setName(String n) { this.name = n; }
}