package net.tislib.binanalyst.lib.operator;

import static net.tislib.binanalyst.lib.BitOps.xor;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;

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
        Bit carryBit = calculator.and(a[0], b[0]);
        for (int i = 1; i < L; i++) {
            Bit ai = i < a.length ? a[i] : ZERO;

            Bit bi = i < b.length ? b[i] : ZERO;

            if (ai == ZERO && bi == ZERO) {
                if (carryBit == ZERO) {
                    result[i] = ZERO;
                } else {
                    result[i] = carryBit;
                    carryBit = ZERO;
                }
                continue;
            } else if (ai == ZERO) {
                if (carryBit == ZERO) {
                    result[i] = bi;
                } else {
                    result[i] = xor(calculator, bi, carryBit);
                    carryBit = calculator.and(bi, carryBit);
                }
                continue;
            } else if (bi == ZERO) {
                if (carryBit == ZERO) {
                    result[i] = ai;
                } else {
                    result[i] = xor(calculator, ai, carryBit);
                    carryBit = calculator.and(ai, carryBit);
                }
                continue;
            }

            result[i] = addBits(calculator, carryBit, ai, bi);

            // ai and bi is prev values
//            carryBit = calculator.and(calculator.or(ai, bi), calculator.or(calculator.and(ai, bi), calculator.not(result[i])));
            carryBit = calculator.or(calculator.and(calculator.not(result[i]), calculator.or(ai, bi)), calculator.and(ai, bi));
        }

        return flip(result);
    }

    public static Bit addBits(BitOpsCalculator calculator, Bit... bits) {
        return calculator.xor(bits);
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
