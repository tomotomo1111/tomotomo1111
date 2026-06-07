import java.util.*;
import java.awt.*;

public class iteratortest {

    public static void main(String[] args) {
        Set<Integer> hlist = new HashSet<Integer>(Arrays.asList(1, 2, 3, 4, 5));
        say(hlist.iterator());

        Integer integer = 12;
        say(integer);
        say("14");

        hlist.add(6);
        hlist.add(7);
        say(hlist.iterator());

        hlist.forEach(n -> System.out.println(n));

        LinkedList<Integer> llist = new LinkedList<Integer>(Arrays.asList(6, 7, 8, 9));
        llist.add(19);
        llist.add(10);
        say(llist.iterator());

        Map<String, Integer> hmap = new HashMap<String, Integer>();
        hmap.put("January", 1);
        hmap.put("February", 2);
        System.out.println("s" + hmap.keySet() + ", n" + hmap.values() + ", t" + hmap.entrySet());
        hmap.forEach((String s, Integer n)-> {System.out.print(s + " : " + n + '\n');});
    }

    public static void say(Iterator<Integer> a) {
        while (a.hasNext()) {
            System.out.println(a.next());
        }
    }

    public static void say(Integer i) {
        System.out.println(i.intValue());
    }

    public static void say(String s) {
        System.out.println(Integer.valueOf(s).intValue());
    }

    public static void msay(Map map, Integer i) {
        if (map.containsValue(i)) {
            System.out.println("Exists");
        } else {

        }
    }
}