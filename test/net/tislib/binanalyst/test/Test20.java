package net.tislib.binanalyst.test;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class Test20 {

    public static void main(String... args) {
        Set<String> variants = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            if (checkPrime(i))
                for (int j = 0; j < 1000; j++) {
                    if (checkPrime(j)) {
                        Function<Integer, Integer> func = a -> a / 8 % 2;
                        int c = i / 4;
                        System.out.println(c + " " + (func.apply(i) | func.apply(j)));
                    }
                }
        }
        variants.forEach(System.out::println);
    }

    static boolean checkPrime(int n) {
        int i, m = 0, flag = 0;
        m = n / 2;
        if (n == 0 || n == 1) {
            return false;
        } else {
            for (i = 2; i <= m; i++) {
                if (n % i == 0) {
                    return false;
                }
            }
            return true;
        }//end of else
    }
}
