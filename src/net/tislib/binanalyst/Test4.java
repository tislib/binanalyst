package net.tislib.binanalyst;

import net.tislib.binanalyst.lib.BinValueHelper;

import static net.tislib.binanalyst.lib.BitOps.*;
import static net.tislib.binanalyst.lib.ConstantBit.*;

/**
 * Created by Taleh Ibrahimli on 2/5/18.
 * Email: me@talehibrahimli.com
 */
public class Test4 {

    public static void main(String... args) {

        BinValueHelper.print(and(ONE, ZERO, ONE));
        BinValueHelper.print(and(ONE, ONE, ONE));
        BinValueHelper.print(or(ONE, ZERO, ONE));
        BinValueHelper.print(xor(ONE, ZERO, ONE));
        BinValueHelper.print(xor(ONE, ZERO, ZERO));
        BinValueHelper.print(or(ZERO, ZERO, ZERO));


    }
}
