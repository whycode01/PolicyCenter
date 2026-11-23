package app;

import java.util.function.Function;
import java.util.stream.Collectors;

public class StreamPractice {

    public static boolean isAnagram(String s1, String s2) {
        var map1 = s1.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()));

        var map2 = s2.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()));

        return map1.equals(map2);
    }

    public static void main(String[] args){

        System.out.println(isAnagram("listen", "silent")); // true
        System.out.println(isAnagram("hello", "world")); // false
    }
}