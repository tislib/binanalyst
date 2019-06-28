package net.tislib.binanalyst.lib.operator;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import net.tislib.binanalyst.lib.BinOps;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;

/**
 * Created by Taleh Ibrahimli on 2/6/18.
 * Email: me@talehibrahimli.com
 */
public class BinMul {

    public static Bit[][] getMultiplicationMatrix(BitOpsCalculator calculator, Bit[] a, Bit[] b) {
        int COL_SIZE = a.length + b.length;

        Bit[/*ROW*/][/*COLUMN*/] M = new Bit[b.length][COL_SIZE];
        for (int j = 0; j < b.length; j++) { // ROW
            for (int i = 0; i < COL_SIZE; i++) { // COLUMN
                try {
                    M[j][i] = ZERO;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        for (int j = 0; j < b.length; j++) { // ROW
            for (int i = 0; i < a.length; i++) { // COLUMN
                int aIndex = a.length - 1 - i;
                int bIndex = b.length - 1 - j;
                M[j][COL_SIZE - i - 1 - j] = BinOps.and(calculator, b[bIndex], a[aIndex]);
            }
        }

        return M;
    }

    public static Bit[] multiply(BitOpsCalculator calculator, Bit[] a, Bit[] b) {
        if (a.length == 1) {
            return multiplyByBit(calculator, b, a[0]);
        }
        if (b.length == 1) {
            return multiplyByBit(calculator, a, b[0]);
        }
        return BinAdd.add(calculator, getMultiplicationMatrix(calculator, a, b));
    }

    private static Bit[][] flip(Bit[][] matrix) {
        Bit[][] res = new Bit[matrix.length][];

        for (int i = 0; i < matrix.length; i++) {
            res[i] = flip(matrix[i]);
        }

        return res;
    }

    private static Bit[] flip(Bit[] bits) {
        Bit[] result = new Bit[bits.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = bits[bits.length - i - 1];
        }
        return result;
    }

    private static Bit[] multiplyByBit(BitOpsCalculator calculator, Bit[] b, Bit bit) {
        Bit[] res = new Bit[b.length];
        for (int i = 0; i < b.length; i++) {
            res[i] = calculator.and(b[i], bit);
        }
        return res;
    }

    public static Bit[] multiply2(BitOpsCalculator calculator, Bit[] a, Bit[] b) {
        return BinAdd2.add(calculator, getMultiplicationMatrix(calculator, a, b));
    }
}
