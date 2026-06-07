public class Question1of1 {

    public static double findMaxElement(double[][] array) {
        double maxElement = array[0][0];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j] > maxElement) {
                    maxElement = array[i][j];
                }
            }
        }

        return maxElement;
    }

    public static void main(String[] args) {
        double[][] myArray = {
            {2.9, 3.2},
            {3.4, 7.3},
            {6.3, 8.5}
        };

        double maxElement = findMaxElement(myArray);
        System.out.println(maxElement);
    }
}
