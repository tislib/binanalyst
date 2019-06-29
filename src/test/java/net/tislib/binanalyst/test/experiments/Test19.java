package net.tislib.binanalyst.test.experiments;

import java.util.HashSet;
import java.util.Set;

public class Test19 {

    public static void main(String... args) {
        Set<String> variants = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                int c = i + j;
                if (c % 8 == 7 && (i % 2 == 1) && (j % 2 == 0)) {
                    variants.add((i % 8) + " " + (j % 8));
                }
            }
        }
        variants.forEach(System.out::println);
    }

    static void checkPrime(int n) {
        int i, m = 0, flag = 0;
        m = n / 2;
        if (n == 0 || n == 1) {
            System.out.println(n + " is not prime number");
        } else {
            for (i = 2; i <= m; i++) {
                if (n % i == 0) {
                    System.out.println(n + " is not prime number");
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                System.out.println(n + " is prime number");
            }
        }//end of else
    }
}
