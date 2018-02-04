package net.tislib.binanalyst;

import net.tislib.binanalyst.lib.BinCalc;
import net.tislib.binanalyst.lib.BinValueHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
public class Test3 {

    public static void main(String... args) {

        long a = 433235423;
        long b = 784341251;
        long c = 243434878;
        long d = 134653421;
        long s = a + b + c + d;

        BinValueHelper.print(a);
        BinValueHelper.print(b);
        BinValueHelper.print(c);
        BinValueHelper.print(d);
        System.out.print("Res:");
        BinValueHelper.print(s);
        System.out.print("Pre:");

        int l = BinValueHelper.binLength(d);

        byte[] res = new byte[l];
        for (int i = 0; i < l; i++) {
            res[l - i - 1] = checkRecursive(new long[]{
                    a, b, c, d
            }, i);
        }
        BinValueHelper.print(res);
        BinValueHelper.printError(res, s);

    }

    private static Map<Holder, Byte> cache = new HashMap<>();

    private static byte checkRecursive(long num[], int i) {
        Holder holder = new Holder();
        holder.num = num;
        holder.i = i;
        if (cache.containsKey(holder)) return cache.get(holder);

        int N = num.length;
        byte r[], ri[], si[] = new byte[N - 1];

        ri = getBits(num, i);

        if (i == 0) {
            return BinValueHelper.getBit(xor(ri), i);
        }

        r = getBits(num, i - 1);

        si[0] = checkRecursive(new long[]{num[0], num[1]}, i - 1);
        if (si.length > 1)
            si[1] = checkRecursive(new long[]{num[0], num[1], num[2]}, i - 1);
        if (si.length > 2)
            si[2] = checkRecursive(new long[]{num[0], num[1], num[2], num[3]}, i - 1);


        byte result = BinCalc.getAddMultiPosBit(r, si, ri)[N - 2];
        cache.put(holder, result);
        return result;
    }

    private static long xor(byte[] nums) {
        long s = 0;
        for (int i = 0; i < nums.length; i++) {
            s ^= nums[i];
        }
        return s;
    }



    public static byte[] getBits(long nums[], int index) {
        byte[] bits = new byte[nums.length];
        for (int i = 0; i < nums.length; i++) {
            bits[i] = BinValueHelper.getBit(nums[i], index);
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
