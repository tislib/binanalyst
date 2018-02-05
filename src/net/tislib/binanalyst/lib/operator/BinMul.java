package net.tislib.binanalyst.lib.operator;

import net.tislib.binanalyst.lib.Bit;
import net.tislib.binanalyst.lib.BitOps;

import static net.tislib.binanalyst.lib.ConstantBit.ZERO;

/**
 * Created by Taleh Ibrahimli on 2/6/18.
 * Email: me@talehibrahimli.com
 */
public class BinMul {


    public Bit mulAndGetBit(long a, long b, int index) {
        return null;
    }

    public static Bit[][] getMultiplicationMatrix(Bit[] a, Bit[] b) {
        int COL_SIZE = a.length + b.length;

        Bit[/*ROW*/][/*COLUMN*/] M = new Bit[a.length][COL_SIZE];
        for (int j = 0; j < b.length; j++) { // ROW
            for (int i = 0; i < COL_SIZE; i++) { // COLUMN
                M[j][i] = ZERO;
            }
        }

        for (int j = 0; j < b.length; j++) { // ROW
            for (int i = 0; i < a.length; i++) { // COLUMN
                int aIndex = a.length - 1 - i;
                int bIndex = b.length - 1 - j;
                M[j][COL_SIZE - i - 1 - j] = BitOps.and(b[bIndex], a[aIndex]);
            }
        }

        return M;
    }
}
