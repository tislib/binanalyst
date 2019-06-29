package net.tislib.binanalyst.test.experiments;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;

public class Test21 {

    public static void main(String... args) {
        int n = 8;
        int L = 1 << n;

        Function<Integer, Integer> predicate0 = i -> ((i) % 2);
        Function<Integer, Integer> predicate1 = i -> ((i / 2) % 2);
        Function<Integer, Integer> predicate2 = i -> ((i / 4) % 2);

        Map<String, String> set = new HashMap<>();

        for (int i = 0; i < L; i++) {
            for (int j = i + 1; j < L; j++) {
                if (!checkPrime(i) || !checkPrime(j)) {
                    continue;
                }
                int c = i * j;

                Integer c0 = predicate0.apply(c);
                Integer c1 = predicate1.apply(c);
                Integer ir = i / 4;
                Integer jr = j / 4;
                String key = "" + ir + " " + jr + " " + c0 + " " + c1;
                if (set.containsKey(key)) {
                    System.out.println(key + " " + i + " " + j + " prev " + set.get(key));
                    System.out.println(key + " " + i + " " + j + " now " + c);
                }
                set.put(key, i + " " + j);
            }

        }


    }

    private static Bit truthToCalcOutput(Set<Integer> truth, BitOpsGraphCalculator calculator, int num) {
        Bit output = null;
        int i = 0;
        for (int c : truth) {
            Bit val = adjustCalc(calculator, c);
            if (output == null) {
                output = val;
            } else {
                output = calculator.or(output, val);
            }
        }
        return output;
    }

    private static Bit adjustCalc(BitOpsGraphCalculator calculator, final int c) {
        int cx = c;
        Bit res = null;
        int i = 1;
        while (cx > 0) {
            boolean bv = cx % 2 == 0;
            cx = cx / 2;
            Bit val;
            if (bv) {
                val = calculator.getInput().getBitL(i);
            } else {
                val = calculator.not(calculator.getInput().getBitL(i));
            }
            if (res == null) {
                res = val;
            } else {
                res = calculator.and(res, val);
            }
            i++;
        }
        return res;
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
