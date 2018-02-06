package net.tislib.binanalyst.lib;

import net.tislib.binanalyst.lib.bit.Bit;

import static net.tislib.binanalyst.lib.BitOps.*;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
public class BinCalc {


    public static byte getAddPosBit(byte ai, byte bi, byte si, byte ai1, byte bi1) {
        return convert(getAddPosBit(wrap(ai), wrap(bi), wrap(si), wrap(ai1), wrap(bi1)));
    }

    public static Bit getAddPosBit(Bit ai, Bit bi, Bit si, Bit ai1, Bit bi1) {
        return xor(ai1, bi1, and(or(ai, bi), (or(and(ai, bi), not(si)))));
    }


    public static byte[] getAddMultiPosBit(byte ri[], byte[] si, byte ri1[]) {
        return convert(getAddMultiPosBit(wrap(ri), wrap(si), wrap(ri1)));
    }

    public static Bit[] getAddMultiPosBit(Bit ri[], Bit[] si, Bit ri1[]) {
        if (ri.length != ri1.length) {
            throw new RuntimeException("incorrect parameter length");
        }
        if (ri.length == 2) {
            Bit res = getAddPosBit(ri[0], ri[1], si[0], ri1[0], ri1[1]);
            return new Bit[]{res};
        }
        Bit xi[] = new Bit[ri.length - 1];


        xi[0] = getAddPosBit(ri[0], ri[1], si[0], ri1[0], ri1[1]);


        for (int i = 0; i < ri.length - 2; i++) {
            xi[i + 1] = getAddPosBit(si[i], ri[i + 2], si[i + 1], xi[i], ri1[i + 2]);
        }

        return xi;
    }
}
