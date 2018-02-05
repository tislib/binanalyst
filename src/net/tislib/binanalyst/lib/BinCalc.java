package net.tislib.binanalyst.lib;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
public class BinCalc {


    public static byte getAddPosBit(byte ai, byte bi, byte si, byte ai1, byte bi1) {
        return (byte) (ai1 ^ bi1 ^ ((ai | bi) & ((ai & bi) | ~si)));
    }

     public static Bit getAddPosBit(Bit ai, Bit bi, Bit si, Bit ai1, Bit bi1) {

//        return  (ai1 ^ bi1 ^ ((ai | bi) & ((ai & bi) | ~si)));
         return null;
    }




    public static byte[] getAddMultiPosBit(byte ri[], byte[] si, byte ri1[]) {
        if (ri.length != ri1.length) {
            throw new RuntimeException("incorrect parameter length");
        }
        if (ri.length == 2) {
            byte res = getAddPosBit(ri[0], ri[1], si[0], ri1[0], ri1[1]);
            return new byte[]{res};
        }
        byte xi[] = new byte[ri.length - 1];


        xi[0] = getAddPosBit(ri[0], ri[1], si[0], ri1[0], ri1[1]);


        for (int i = 0; i < ri.length - 2; i++) {
            xi[i + 1] = getAddPosBit(si[i], ri[i + 2], si[i + 1], xi[i], ri1[i + 2]);
        }

        return xi;
    }
}