package net.tislib.binanalyst.lib.operator;

import net.tislib.binanalyst.lib.BinOps;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;

/**
 * Created by Taleh Ibrahimli on 2/6/18.
 * Email: me@talehibrahimli.com
 */
public class BinMulRec {

    private static Bit[] multiplyByBit(BitOpsCalculator calculator, Bit[] b, Bit bit) {
        Bit[] res = new Bit[b.length];
        for (int i = 0; i < b.length; i++) {
            res[i] = calculator.and(b[i], bit);
        }
        return res;
    }


    public static Bit[] multiplyTree2Rec(BitOpsCalculator calculator, Bit[] aBits, Bit[] bBits, boolean addTree) {
        if (aBits.length == 1 || bBits.length == 1) {
            if (aBits.length == 1) {
                return multiplyByBit(calculator, bBits, aBits[0]);
            } else {
                return multiplyByBit(calculator, aBits, bBits[0]);
            }
        }
        DivideBinArray2 divideBinArray2 = new DivideBinArray2(aBits, bBits).invoke();
        int l = divideBinArray2.getL();
        Bit[] al = divideBinArray2.getAl();
        Bit[] ah = divideBinArray2.getAh();
        Bit[] bl = divideBinArray2.getBl();
        Bit[] bh = divideBinArray2.getBh();

//        BinValueHelper.printValues(aBits);
//        BinValueHelper.printValues(ah);
//        BinValueHelper.printValues(al);

        Bit[] c0 = multiplyTree2Rec(calculator, al, bl, addTree);
        Bit[] c1 = multiplyTree2Rec(calculator, al, bh, addTree);
        Bit[] c2 = multiplyTree2Rec(calculator, bl, ah, addTree);
        Bit[] c3 = multiplyTree2Rec(calculator, ah, bh, addTree);


//        System.out.println("al: " + BinValueHelper.toLong(al));
//        System.out.println("ah: " + BinValueHelper.toLong(ah));
//        System.out.println("bl: " + BinValueHelper.toLong(bl));
//        System.out.println("bh: " + BinValueHelper.toLong(bh));
//
//        System.out.println("c0: " + BinValueHelper.toLong(c0));
//        System.out.println("c1: " + BinValueHelper.toLong(c1));
//        System.out.println("c2: " + BinValueHelper.toLong(c2));
//        System.out.println("c3: " + BinValueHelper.toLong(c3));
//
//        if (BinValueHelper.toLong(c0).longValue() != ((BinValueHelper.toLong(aBits).longValue() % (1 << l)) * ((BinValueHelper.toLong(bBits).longValue() % (1 << l))) % (1 << l))) {
//            System.out.println("ERROR");
//        }

        if (addTree) {
            return BinAdd.add(calculator, c0, BinOps.shl(c1, l), BinOps.shl(c2, l), BinOps.shl(c3, l * 2));
        } else {
            return add2(calculator, add2(calculator, c0, BinOps.shl(c1, l)), add2(calculator, BinOps.shl(c2, l), BinOps.shl(c3, l * 2)));
        }
    }

    public static Bit[] add2(BitOpsCalculator calculator, Bit[] aBits, Bit[] bBits) {
        if (aBits.length == 1 || bBits.length == 1) {
            return BinAdd.add(calculator, aBits, bBits);
        }
        int l = Math.min(aBits.length / 2, bBits.length / 2);
        Bit[] al = new Bit[l];
        Bit[] ah = new Bit[aBits.length - l];
        Bit[] bl = new Bit[l];
        Bit[] bh = new Bit[bBits.length - l];

        System.arraycopy(aBits, ah.length, al, 0, al.length);
        System.arraycopy(aBits, 0, ah, 0, ah.length);

        System.arraycopy(bBits, bh.length, bl, 0, bl.length);
        System.arraycopy(bBits, 0, bh, 0, bh.length);

//        long aVal = BinValueHelper.toLong(aBits).longValue();
//        long bVal = BinValueHelper.toLong(bBits).longValue();
//        System.out.println( aVal + " " + bVal);

//        return BinAdd.add(calculator, al, bl, BinOps.shl(ah, l), BinOps.shl(bh, l));

        return BinAdd.add(calculator, BinOps.shl(add2(calculator, ah, bh), l), add2(calculator, al, bl));
    }

    private static class DivideBinArray2 {
        private Bit[] aBits;
        private Bit[] bBits;
        private int l;
        private Bit[] al;
        private Bit[] ah;
        private Bit[] bl;
        private Bit[] bh;

        public DivideBinArray2(Bit[] aBits, Bit... bBits) {
            this.aBits = aBits;
            this.bBits = bBits;
        }

        public int getL() {
            return l;
        }

        public Bit[] getAl() {
            return al;
        }

        public Bit[] getAh() {
            return ah;
        }

        public Bit[] getBl() {
            return bl;
        }

        public Bit[] getBh() {
            return bh;
        }

        public DivideBinArray2 invoke() {
            l = Math.min(aBits.length / 2, bBits.length / 2);
            al = new Bit[l];
            ah = new Bit[aBits.length - l];
            bl = new Bit[l];
            bh = new Bit[bBits.length - l];

            System.arraycopy(aBits, ah.length, al, 0, al.length);
            System.arraycopy(aBits, 0, ah, 0, ah.length);

            System.arraycopy(bBits, bh.length, bl, 0, bl.length);
            System.arraycopy(bBits, 0, bh, 0, bh.length);
            return this;
        }
    }
}
