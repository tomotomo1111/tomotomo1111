
public class CurrentMino {
    public int[][][] arr;
    public int[] pos = {5, 0};
    public int mode = 0;
    public int label;
    public int onFloor = 0;
    public int geneTime;
    public boolean collision = false;
    
    // label
    // 2 z red
    // 3 j blue
    // 4 s green
    // 5 o yellow
    // 6 t purple
    // 7 i cyan
    // 8 l orange

    public CurrentMino(int[][][] arr, int mode, int label, int geneTime) {
        this.arr = arr;
        this.mode = mode;
        this.label = label;
        this.geneTime = geneTime;
    }

    //ラベルは一桁目でのみ判別していて、二桁目以降は色彩の連結を防ぐため.
    public int getLabelColorNum() {
        return this.label % 10;
    }
}