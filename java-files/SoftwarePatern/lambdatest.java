import java.util.function.BiFunction;

interface MYBI<T> {Integer calculate(T x, T y);}

public class lambdatest {

    public static void main(String[] args) {
        BiFunction<Integer, Integer, Integer> function = lambdatest::add;
        func(function);
        func((Integer x, Integer y) -> {return x * y;});

        MYBI<Integer> mybi = new MYBI<Integer>(){
            public Integer calculate(Integer x, Integer y) {
                return x * y;
            }
        };
        func(mybi);
    }

    public static Integer add(Integer x, Integer y) {
        return x + y;
    }

    public static void func(BiFunction<Integer, Integer, Integer> function) {
        System.out.println("func : " + function.apply(2, 5));
    }

    public static void func(MYBI<Integer> x) {
        System.out.println("func : " +x.calculate(5, 17));
    }
}