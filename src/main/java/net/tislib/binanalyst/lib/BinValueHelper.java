package net.tislib.binanalyst.lib;

import static net.tislib.binanalyst.lib.BinOps.wrap;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import net.tislib.binanalyst.lib.bit.BinaryValue;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.ConstantBit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;
import net.tislib.binanalyst.lib.calc.SimpleBitOpsCalculator;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
public class BinValueHelper {

    private static final int BIT_LENGTH = 64;
    static BitOpsCalculator simpleCalculator = new SimpleBitOpsCalculator();

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
        int i = 0;
        while (num > 0) {
            num /= 2;
            i++;
        }
        return i;
    }

    public static void print(long a, int length) {
        byte[] binArr = getBinArray(a);
        String pad = new String(new char[length - binArr.length]).replace("\0", "0");
        System.out.print("0b" + pad);
        for (Byte bit : binArr) {
            System.out.print(bit);
        }
        System.out.print(" : " + a);
        System.out.println();
    }

    public static void printHex(long a, int length) {
        byte[] binArr = getBinArray(a);
        String pad = new String(new char[length - binArr.length / 4]).replace("\0", "0");
        System.out.print("0x" + pad);
        for (int i = 0; i < binArr.length - 4; i++) {
            Byte bit0 = binArr[i];
            Byte bit1 = binArr[i + 1];
            Byte bit2 = binArr[i + 2];
            Byte bit3 = binArr[i + 3];
            System.out.print(bit4ToHexChar(bit0, bit1, bit2, bit3));
        }
        System.out.print(" : " + a);
        System.out.println();
    }

    public static Character bit4ToHexChar(Byte bit0, Byte bit1, Byte bit2, Byte bit3) {
        return '!';
    }

    public static void print(long a) {
        print(a, BIT_LENGTH);
    }

    public static void printHex(long a) {
        printHex(a, BIT_LENGTH / 4);
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

    public static BigInteger toLong(Bit[] bits) {
        BigInteger num = BigInteger.ZERO;
        for (int i = 0; i < bits.length; i++) {
            int main = (bits[bits.length - i - 1].getValue().toByteValue());
            BigInteger power10 = BigInteger.ONE.add(BigInteger.ONE).pow(i);
            num = num.add(power10.multiply(BigInteger.valueOf(main)));
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

    public static void printError(BitOpsCalculator calculator, Bit[] res, long s) {
        res = trim(res);
        Bit[] bitArr = trim(wrap(calculator, getBinArray(s)));
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
            System.out.print(bits[i].intVal());
        }
        System.out.print(" : " + toLong(bits));
        System.out.println();
    }

    public static void printSpaced(Bit... bits) {
        for (int i = 0; i < bits.length; i++) {
            System.out.println(i + ":" + bits[i].toString());
        }
    }

    public static void printValues(Bit... bits) {
        System.out.print("0x");
        for (int i = 0; i < bits.length; i++) {
            System.out.print(bits[i].getValue().toString());
        }
        System.out.print(" : " + toLong(bits));
        System.out.println();
    }

    public static Bit[] getBits(BitOpsCalculator calculator, long num) {
        return wrap(calculator, getBinArray(num));
    }

    public static void print(Bit[][] m) {
        for (Bit[] line : m) {
            print(line);
        }
    }

    public static Bit[] trim(Bit[] bits) {
        int i = 0;
        for (; i < bits.length; i++) {
            if (bits[i].getValue().isTrue()) {
                break;
            }
        }
        Bit[] newBits = new Bit[bits.length - i];
        for (int j = 0; j < bits.length - i; j++) {
            newBits[j] = bits[i + j];
        }
        return newBits;
    }

    public static void setVal(VarBit[] bits, long value) {
        for (VarBit bit : bits) {
            bit.setValue(BinaryValue.FALSE);
        }
        Bit[] bitArr = trim(wrap(simpleCalculator, getBinArray(value)));
        for (int i = 0; i < Math.min(bitArr.length, bits.length); i++) {
            BinaryValue val = bitArr[bitArr.length - 1 - i].getValue();
            bits[bits.length - 1 - i].setValue(val);
        }
    }

    public static VarBit[] concat(VarBit[] aBits, VarBit[] bBits) {
        VarBit[] result = new VarBit[aBits.length + bBits.length];
        int i;
        for (i = 0; i < aBits.length; i++) {
            result[i] = aBits[i];
        }

        for (int j = 0; j < bBits.length; j++) {
            result[i + j] = bBits[j];
        }
        return result;
    }

    public static VarBit[] reverse(VarBit[] bits) {
        VarBit[] result = new VarBit[bits.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = bits[bits.length - i - 1];
        }
        return result;
    }

    public static VarBit[] clone(VarBit[] bits) {
        VarBit[] result = new VarBit[bits.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = new VarBit(bits[i].getName());
            result[i].setValue(bits[i].getValue());
        }
        return result;
    }

    public static Bit[] rasterize(Bit[] resultingBits) {
        Bit[] result = new ConstantBit[resultingBits.length];
        for (int i = 0; i < resultingBits.length; i++) {
            result[i] = rasterize(resultingBits[i]);
        }
        return result;
    }

    private static Bit rasterize(Bit bit) {
        if (bit instanceof ConstantBit) return (ConstantBit) bit;
        return bit.getValue().isTrue() ? ONE : ZERO;
    }

    public static VarBit[] setLength(VarBit[] cBits, int length) {
        VarBit[] result = new VarBit[length];
        for (int i = 0; i < length; i++) {
            result[i] = new VarBit();
            result[i].setValue(BinaryValue.FALSE);
        }
        int diff = length - cBits.length;

        if (length - diff >= 0) System.arraycopy(cBits, 0, result, diff, length - diff);
        return result;
    }

    public static String formulaToString(Bit bit) {
        if (bit instanceof OperationalBit) {
            if (((OperationalBit) bit).testBits(item -> !(item instanceof OperationalBit))) {
                return ((OperationalBit) bit).getName();
            }
            return ((OperationalBit) bit).showFull(false);
        } else {
            return bit.toString();
        }
    }

    public static void printFormula(NamedBit bit) {
        System.out.println(bit.getName() + " = " + formulaToString(bit));
    }

    public static long toHexString(Bit[] res) {
        return 0;
    }
}
