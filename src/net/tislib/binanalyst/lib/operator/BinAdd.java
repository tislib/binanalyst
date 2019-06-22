package net.tislib.binanalyst.lib.operator;

import static net.tislib.binanalyst.lib.BinOps.xor;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import net.tislib.binanalyst.lib.BinValueHelper;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;

/**
 * Created by Taleh Ibrahimli on 2/6/18.
 * Email: me@talehibrahimli.com
 */
@SuppressWarnings("Duplicates")
public class BinAdd {


    public static Bit[] add(BitOpsCalculator calculator, Bit[] a, Bit[] b) {
        int L = Math.max(a.length, b.length) + 1;
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
            } else if (ai == ZERO) {
                if (carryBit == ZERO) {
                    result[i] = bi;
                } else {
                    result[i] = xor(calculator, bi, carryBit);
                    carryBit = calculator.and(bi, carryBit);
                }
            } else if (bi == ZERO) {
                if (carryBit == ZERO) {
                    result[i] = ai;
                } else {
                    result[i] = xor(calculator, ai, carryBit);
                    carryBit = calculator.and(ai, carryBit);
                }
            } else {

                result[i] = addBits(calculator, carryBit, ai, bi);

                // ai and bi is prev values
//            carryBit = calculator.and(calculator.or(ai, bi), calculator.or(calculator.and(ai, bi), calculator.not(result[i])));
//            carryBit = calculator.or(
//                    calculator.and(calculator.not(result[i]), calculator.or(ai, bi)),
//                    calculator.and(ai, bi)
//            );
                if (carryBit == ZERO) {
                    carryBit = calculator.and(ai, bi);
                } else {
//                carryBit = calculator.or(
//                        calculator.and(calculator.not(calculator.xor(carryBit, ai, bi)), calculator.or(ai, bi)),
//                        calculator.and(ai, bi)
//                );

                    carryBit = calculator.or(
                            calculator.and(ai, bi),
                            calculator.and(ai, carryBit),
                            calculator.and(bi, carryBit)
                    );
                }
            }
        }

        return result;
    }

    public static Bit getAddStageBit(BitOpsCalculator calculator, Bit[] bits, int dv) {
        if (bits.length < 1 << dv) {
            return ZERO;
        }
        Bit[] xl = new Bit[bits.length / 2];
        Bit[] ml = new Bit[bits.length / 2];
        for (int i = 0; i < bits.length; i += 2) {
            Bit m = calculator.and(bits[i], bits[i + 1]);
            Bit x = calculator.xor(bits[i], bits[i + 1]);
            ml[i / 2] = m;
            xl[i / 2] = x;
        }

        Bit result = calculator.xor(ml);
        if (dv > 0) {
            result = calculator.xor(result, getAddStageBit(calculator, xl, dv - 1));
        }
        return result;
    }


    public static Bit addBits(BitOpsCalculator calculator, Bit... bits) {
        return calculator.xor(bits);
    }

    public static Bit[] add(BitOpsCalculator calculator, Bit[]... nums) {
        if (nums.length == 0) return new Bit[0];
        if (nums.length == 1) return nums[0];
        Bit[] result = nums[0];
        for (int i = 1; i < nums.length; i++) {
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
