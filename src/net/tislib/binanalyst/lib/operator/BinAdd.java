package net.tislib.binanalyst.lib.operator;

import net.tislib.binanalyst.lib.BinCalc;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;

import static net.tislib.binanalyst.lib.BitOps.xor;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

/**
 * Created by Taleh Ibrahimli on 2/6/18.
 * Email: me@talehibrahimli.com
 */
public class BinAdd {


    public static Bit[] add(BitOpsCalculator calculator, Bit[] a, Bit[] b) {
        a = flip(a);
        b = flip(b);
        int L = Math.max(a.length, b.length);
        Bit[] result = new Bit[L];

        result[0] = xor(calculator, a[0], b[0]);
        for (int i = 0; i < L - 1; i++) {
            Bit ai, bi, ci, ai1, bi1;

            ai = i < a.length ? a[i] : ZERO;
            ai1 = i < a.length - 1 ? a[i + 1] : ZERO;

            bi = i < b.length ? b[i] : ZERO;
            bi1 = i < b.length - 1 ? b[i + 1] : ZERO;

            ci = result[i];

            Bit r = BinCalc.getAddPosBit(calculator, ai, bi, ci, ai1, bi1);
            result[i + 1] = r;
        }

        return flip(result);
    }

    private static Bit[] flip(Bit[] bits) {
        Bit[] result = new Bit[bits.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = bits[bits.length - i - 1];
        }
        return result;
    }

    public static Bit[] add(BitOpsCalculator calculator, Bit[]... nums) {
        if (nums.length == 0) return new Bit[0];
        if (nums.length == 1) return nums[0];
        int L = maxLength(nums);
        Bit[] result = new Bit[L];
        for (int i = 0; i < result.length; i++) {
            result[i] = ZERO;
        }
        for (int i = 0; i < nums.length; i++) {
            result = add(calculator, result, nums[i]);
        }
        return result;
    }

    private static int maxLength(Bit[][] nums) {
        int max = -1;
        for (Bit[] L : nums) {
            if (max < L.length) max = L.length;
        }
        return max;
    }

}
