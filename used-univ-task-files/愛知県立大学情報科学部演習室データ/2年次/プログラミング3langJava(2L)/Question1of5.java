public class Question1of5 {
    public static int findMaxDifference(int[][][] arr) {
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
		for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                for (int k = 0; k < arr[i][j].length; k++) {
                    int value = arr[i][j][k];
                    max = Math.max(max, value);
                    min = Math.min(min, value);
                }
            }
        }
        
        return max - min;
    }
    
    public static void main(String[] args) {
        int[][][] array = {{{6, 7}, {8, 5}},{{9, 10}, {11, 8}}};
        int result = findMaxDifference(array);
        System.out.println("•Ï”‚Ì·‚ªÅ‚à‘å‚«‚¢”Žš‚Í: " + result);
    }
}
