package net.tislib.binanalyst.lib.operator;

import static net.tislib.binanalyst.lib.BitOps.xor;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;

/**
 * Created by Taleh Ibrahimli on 2/6/18.
 * Email: me@talehibrahimli.com
 */
@SuppressWarnings("Duplicates")
public class BinAdd2 {


    public static Bit[] chain(BitOpsCalculator calculator, Bit[] a, Bit[] b) {
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
        Bit[][] numsTransposed = transpose(nums);

        Bit[][] chainMatrix = new Bit[nums.length][nums.length];
        Bit[][] resultMatrix = new Bit[nums.length][nums.length];
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums.length; j++) {
                chainMatrix[i][j] = numsTransposed[i][j];
                resultMatrix [i][j] = numsTransposed[i][j];
            }
        }
        for (int i = 1; i < nums.length; i++) {
            for (int j = 0; j < nums.length; j++) {
                Bit ri = resultMatrix[i - 1][j];
                chainMatrix[i][j] = chain(calculator, chainMatrix[i - 1][j], numsTransposed[i][j], ri);
            }
        }
        return xorMatrix(calculator, chainMatrix);
    }

    private static Bit chain(BitOpsCalculator calculator, Bit ai, Bit bi, Bit ri) {
        return calculator.or(calculator.and(calculator.not(ri), calculator.or(ai, bi)), calculator.and(ai, bi));
    }

    private static Bit[][] transpose(Bit[][] nums) {
        int L = maxLength(nums);
        Bit[][] numsTransposed = new Bit[L][nums.length];
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums[i].length; j++) {
                Bit r = nums[i][j];
                numsTransposed[j][i] = r;
            }
        }
        return numsTransposed;
    }

    private static Bit[] xorMatrix(BitOpsCalculator calculator, Bit[][] chainMatrix) {
        Bit[] result = new Bit[chainMatrix.length];
        for (int i = 0; i < chainMatrix.length; i++) {
            Bit[] vert = new Bit[chainMatrix[i].length];
            for (int j = 0; j < chainMatrix[i].length; j++) {
                vert[j] = chainMatrix[i][j];
            }
            result[i] = xor(calculator, vert);
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
