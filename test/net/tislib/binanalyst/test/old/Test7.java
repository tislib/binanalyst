package net.tislib.binanalyst.test.old;

import net.tislib.binanalyst.lib.BinCalc;
import net.tislib.binanalyst.lib.BinValueHelper;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static net.tislib.binanalyst.lib.BitOps.xor;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
public class Test7 {

    public static void main(String... args) {

        long a = 433235823;
        long b = 784871251;
        long c = 243434878;
        long d = 136553421;
        long s = a + b + c + d;

        BinValueHelper.print(a);
        BinValueHelper.print(b);
        BinValueHelper.print(c);
        BinValueHelper.print(d);
        System.out.print("Res:");
        BinValueHelper.print(s);
        System.out.print("Pre:");

        int l = BinValueHelper.binLength(d);

        Bit[] res = new Bit[l];
        for (int i = 0; i < l; i++) {
            res[l - i - 1] = checkRecursive(new long[]{
                    a, b, c, d
            }, i);
        }
        BinValueHelper.print(res);
        BinValueHelper.printError(BitOpsCalculator.getDefault(), res, s);
        System.out.println(cache.size() + " OPS");

    }

    private static Map<Holder, Bit> cache = new HashMap<>();

    private static Bit checkRecursive(long num[], int i) {
        Holder holder = new Holder();
        holder.num = num;
        holder.i = i;
        if (cache.containsKey(holder)) return cache.get(holder);

        int N = num.length;
        Bit r[], ri[], si[] = new Bit[N - 1];

        ri = getConstBits(num, i);

        if (i == 0) {
            return xor(BitOpsCalculator.getDefault(), ri);
        }

        r = getConstBits(num, i - 1);

        si[0] = checkRecursive(new long[]{num[0], num[1]}, i - 1);
        if (si.length > 1)
            si[1] = checkRecursive(new long[]{num[0], num[1], num[2]}, i - 1);
        if (si.length > 2)
            si[2] = checkRecursive(new long[]{num[0], num[1], num[2], num[3]}, i - 1);


        Bit result = BinCalc.getAddMultiPosBit(BitOpsCalculator.getDefault(), r, si, ri)[N - 2];
        cache.put(holder, result);
        return result;
    }


    public static Bit[] getConstBits(long nums[], int index) {
        Bit[] bits = new Bit[nums.length];
        for (int i = 0; i < nums.length; i++) {
            bits[i] = BinValueHelper.getConstBit(nums[i], index);
        }
        return bits;
    }

    static class Holder {
        public long num[];
        public int i;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Holder holder = (Holder) o;
            return i == holder.i &&
                    Arrays.equals(num, holder.num);
        }

        @Override
        public int hashCode() {

            int result = Objects.hash(i);
            result = 31 * result + Arrays.hashCode(num);
            return result;
        }
    }

}
