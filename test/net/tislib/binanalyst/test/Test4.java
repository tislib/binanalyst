package net.tislib.binanalyst.test;

import net.tislib.binanalyst.lib.BinValueHelper;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;

import static net.tislib.binanalyst.lib.BitOps.*;
import static net.tislib.binanalyst.lib.bit.ConstantBit.*;

/**
 * Created by Taleh Ibrahimli on 2/5/18.
 * Email: me@talehibrahimli.com
 */
public class Test4 {

    public static void main(String... args) {

        BinValueHelper.print(and(BitOpsCalculator.getDefault(), ONE, ZERO, ONE));
        BinValueHelper.print(and(BitOpsCalculator.getDefault(), ONE, ONE, ONE));
        BinValueHelper.print(or(BitOpsCalculator.getDefault(), ONE, ZERO, ONE));
        BinValueHelper.print(xor(BitOpsCalculator.getDefault(), ONE, ZERO, ONE));
        BinValueHelper.print(xor(BitOpsCalculator.getDefault(), ONE, ZERO, ZERO));
        BinValueHelper.print(or(BitOpsCalculator.getDefault(), ZERO, ZERO, ZERO));


    }
}
