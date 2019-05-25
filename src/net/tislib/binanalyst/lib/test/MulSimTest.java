package net.tislib.binanalyst.lib.test;

import java.util.HashSet;
import java.util.Set;
import net.tislib.binanalyst.lib.BinValueHelper;

public class MulSimTest {

    public static void main(String... args) { //221
        int num = 1321 * 4493;
//        Set<Integer> values = getBinSim(221, 4);
//        Set<Integer> values = getBinSim(num, 6);
//        Set<Integer> values = getNormalSim(num, 15);
        Set<Integer> values = getSuperSim(num, 15);
        System.out.println("____");
        System.out.println(values);
        System.out.println(values.size());
    }

    private static Set<Integer> getBinSim(int number, int suffixLength) {
        int mod = 2 << suffixLength;
        return getNormalSim(number, mod);
    }

    private static Set<Integer> getNormalSim(int number, int mod) {
        System.out.println(mod);
        BinValueHelper.print(number);
        Set<Integer> aL = new HashSet<>();
        for (int a = 1; a < mod; a++) {
            for (int b = a; b < mod; b++) {
                int c = a * b;
                if (c % mod == number % mod) {
                    int va = a % mod;
                    int vb = b % mod;
                    if (!aL.contains(va)) {
                        aL.add(va);
                        System.out.println(va + " * " + vb + " = " + (va * vb) + " => " + c);
                        BinValueHelper.print(c);
                    }
                }
            }
        }
        return aL;
    }

    private static Set<Integer> getSuperSim(int number, int xMod) {
        System.out.println(xMod);
        BinValueHelper.print(number);
        Set<Integer> aL = new HashSet<>();
        int bound = (int) Math.sqrt(number);
        for (int mod = xMod; mod > 1; mod--) {
            for (int a = 2; a < bound; a++) {
                for (int b = a; b < bound; b++) {
                    int c = a * b;
                    boolean isOk = true;
                    for (long rMod = 2; rMod <= mod; rMod++) {
                        if (number % rMod != c % rMod) {
                            isOk = false;
                            break;
                        }
                    }
                    if (isOk) {
                        int va = a % mod;
                        if (!aL.contains(va)) {
                            aL.add(va);
                            System.out.println(a + " * " + b + " => " + c);
                            BinValueHelper.print(c);
                        }
                    }
                }
            }
            if (aL.size() > 0) {
                System.out.println(mod);
                break;
            }
        }
        return aL;
    }
}
