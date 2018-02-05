package net.tislib.binanalyst.lib;

import java.util.ArrayList;
import java.util.List;

import static net.tislib.binanalyst.lib.BitOps.wrap;
import static net.tislib.binanalyst.lib.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.ConstantBit.ZERO;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
public class BinValueHelper {


    private static final int BIT_LENGTH = 64;

    public static byte[] getBinArray(long num) {
        List<Byte> container = new ArrayList<>();
        while (num > 0) {
            container.add((byte) (num % 2));
            num >>= 1;
        }
        byte[] binRep = new byte[BIT_LENGTH];
        for (int i = 0; i < BIT_LENGTH - container.size(); i++) {
            binRep[i] = 0;
        }
        for (int i = 0; i < container.size(); i++) {
            binRep[BIT_LENGTH - i - 1] = container.get(i);
        }
        return binRep;
    }

    public static int binLength(long num) {
        return getBinArray(num).length;
    }

    public static void print(long a, int length) {
        byte[] binArr = getBinArray(a);
        String pad = new String(new char[length - binArr.length]).replace("\0", "0");
        System.out.print("0x" + pad);
        for (Byte bit : binArr) {
            System.out.print(bit);
        }
        System.out.print(" : " + a);
        System.out.println();
    }

    public static void print(long a) {
        print(a, BIT_LENGTH);
    }

    public static byte getBit(long a, int i) {
        byte[] binArr = getBinArray(a);
        int index = binArr.length - i - 1;
        return binArr[index];
    }

    public static Bit getConstBit(long a, int i) {
        return getBit(a, i) == 0 ? ZERO : ONE;
    }

    public static byte xor(byte ai, byte bi) {
        return (byte) ((ai + bi) % 2);
    }

    public static boolean nand(byte ai, byte bi) {
        return (ai & bi) == 0;
    }


    public static long toLong(byte[] bin) {
        long num = 0;
        for (int i = 0; i < bin.length; i++) {
            num += bin[bin.length - i - 1] * (1 << i);
        }
        return num;
    }

    public static long toLong(Bit[] bits) {
        long num = 0;
        for (int i = 0; i < bits.length; i++) {
            num += (bits[bits.length - i - 1].getValue() ? 1 : 0) * (1 << i);
        }
        return num;
    }

    public static void print(byte[] binArr) {
        System.out.print("0x");
        for (int i = 0; i < binArr.length; i++) {
            System.out.print(binArr[i]);
        }
        System.out.print(" : " + toLong(binArr));
        System.out.println();
    }

    public static void printError(byte[] res, long s) {
        byte[] binArr = getBinArray(s);
        if (binArr.length != res.length) {
            System.out.println("different length!");
            return;
        }
        for (int i = 0; i < binArr.length; i++) {
            if (res[i] != binArr[i]) {
                System.out.println("First error on: " + i);
                return;
            }
        }
        System.out.println("SAME!");
    }

    public static void printError(Bit[] res, long s) {
        res = trim(res);
        Bit[] bitArr = trim(wrap(getBinArray(s)));
        if (bitArr.length != res.length) {
            System.out.println("different length!");
            return;
        }
        for (int i = 0; i < bitArr.length; i++) {
            if (res[i].getValue() != bitArr[i].getValue()) {
                System.out.println("First error on: " + i);
                return;
            }
        }
        System.out.println("SAME!");
    }

    public static void print(Bit... bits) {
        System.out.print("0x");
        for (int i = 0; i < bits.length; i++) {
            System.out.print(bits[i].toString());
        }
        System.out.print(" : " + toLong(bits));
        System.out.println();
    }

    public static Bit[] getBits(long num) {
        return wrap(getBinArray(num));
    }

    public static void print(Bit[][] m) {
        for (Bit[] line : m) {
            print(line);
        }
    }

    public static Bit[] trim(Bit[] bits) {
        int i = 0;
        for (; i < bits.length; i++) {
            if (bits[i].getValue()) {
                break;
            }
        }
        Bit[] newBits = new Bit[bits.length - i];
        for (int j = 0; j < bits.length - i; j++) {
            newBits[j] = bits[i + j];
        }
        return newBits;
    }
}
