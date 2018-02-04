package net.tislib.binanalyst;

import net.tislib.binanalyst.lib.BinCalc;
import net.tislib.binanalyst.lib.BinValueHelper;

import java.util.HashMap;
import java.util.Map;

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

    private static Map<Long, Byte> cache = new HashMap<>();

    private static byte checkRecursive(long num[], int i) {
//        if (cache.containsKey(sum(num))) return cache.get(sum(num));
        int N = num.length;
        byte r[], ri[], si[] = new byte[N - 1];

        ri = getBits(num, i);

        if (i == 0) {
            return BinValueHelper.getBit(xor(ri), i);
        }

        r = getBits(num, i - 1);
//        si[0] = checkRecursive(new long[]{
//                num[0], num[1]
//        }, i - 1);
//        si[1] = checkRecursive(new long[]{
//                num[0], num[1], num[2]
//        }, i - 1);
//        si[2] = checkRecursive(new long[]{
//                num[0], num[1], num[2], num[3]
//        }, i - 1);

        si[0] = BinValueHelper.getBit(num[0] + num[1], i);
        si[1] = BinValueHelper.getBit(num[0] + num[1] + num[2], i);
        si[2] = BinValueHelper.getBit(num[0] + num[1] + num[2] + num[3], i);


        byte result = BinCalc.getAddMultiPosBit(r, si, ri)[N - 2];
        cache.put(sum(num), result);
        return result;
    }

    private static long xor(byte[] nums) {
        long s = 0;
        for (int i = 0; i < nums.length; i++) {
            s ^= nums[i];
        }
        return s;
    }

    private static Long sum(long[] nums) {
        long s = 0;
        for (int i = 0; i < nums.length; i++) {
            s += nums[i];
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

}
