package net.tislib.binanalyst.lib.operator;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.BitOps;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

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
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        for (int j = 0; j < b.length; j++) { // ROW
            for (int i = 0; i < a.length; i++) { // COLUMN
                int aIndex = a.length - 1 - i;
                int bIndex = b.length - 1 - j;
                M[j][COL_SIZE - i - 1 - j] = BitOps.and(calculator, b[bIndex], a[aIndex]);
            }
        }

        return M;
    }

    public static Bit[] multiply(BitOpsCalculator calculator, Bit[] a, Bit[] b) {
        return BinAdd.add(calculator, getMultiplicationMatrix(calculator, a, b));
    }

    public static Bit[] multiply2(BitOpsCalculator calculator, Bit[] a, Bit[] b) {
        return BinAdd.add2(calculator, getMultiplicationMatrix(calculator, a, b));
    }
}
