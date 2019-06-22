package net.tislib.binanalyst.lib;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;

public class WordOpsHelper {
    private final BitOpsCalculator calculator;

    public WordOpsHelper(BitOpsCalculator calculator) {
        this.calculator = calculator;
    }

    public Bit[] wordToBits(int word) {
        Bit[] res = new Bit[32];
        for (int i = res.length - 1; i >= 0; i--) {
            res[i] = calculator.wrap(word >> i & 1);
        }
        return res;
    }

    public int bitsToWord(Bit[] bits) {
        int res = 0;
        for (int i = 0; i < Math.min(bits.length, 33); i++) {
            if (i > 0) {
                res = res << 1;
            }
            res += bits[bits.length - i - 1].intVal();
        }
        return res;
    }

    public int[] bitsArrToWordArr(Bit[][] bits) {
        int[] res = new int[bits.length];
        for (int i = 0; i < bits.length; i++) {
            res[i] = bitsToWord(bits[i]);
        }
        return res;
    }

    public String toHex(Bit[]... state) {
        return toHex(bitsArrToWordArr(state));
    }

    final private static char[] encoding = "0123456789ABCDEF".toCharArray();

    public static String toHex(int... arr) {
        char[] encodedChars = new char[arr.length * 4 * 2];
        for (int i = 0; i < arr.length; i++) {
            int v = arr[i];
            int idx = i * 4 * 2;
            for (int j = 0; j < 8; j++) {
                encodedChars[idx + j] = encoding[(v >>> ((7 - j) * 4)) & 0x0F];
            }
        }
        return new String(encodedChars);
    }

    public Bit[] add(Bit[]... bits) {
        int[] ints = bitsArrToWordArr(bits);
        int res = ints[0];
        for (int i = 1; i < ints.length; i++) {
            res = res + ints[i];
        }
        return wordToBits(res);
    }

    public Bit[] and(Bit[]... bits) {
        int[] ints = bitsArrToWordArr(bits);
        int res = ints[0];
        for (int i = 1; i < ints.length; i++) {
            res = res & ints[i];
        }
        return wordToBits(res);
    }

    public Bit[] or(Bit[]... bits) {
        int[] ints = bitsArrToWordArr(bits);
        int res = ints[0];
        for (int i = 1; i < ints.length; i++) {
            res = res | ints[i];
        }
        return wordToBits(res);
    }

    public Bit[] xor(Bit[]... bits) {
        int[] ints = bitsArrToWordArr(bits);
        int res = ints[0];
        for (int i = 1; i < ints.length; i++) {
            res = res ^ ints[i];
        }
        return wordToBits(res);
    }

    public Bit[] not(Bit[] bits) {
        int word = bitsToWord(bits);
        int res = ~word;
        return wordToBits(res);
    }

    public Bit[] rotateRight(Bit[] bits, int distance) {
        int word = bitsToWord(bits);
        int res = Integer.rotateRight(word, distance);
        return wordToBits(res);
    }

    public Bit[] shr(Bit[] bits, int length) {
        int word = bitsToWord(bits);
        int res = word >> length;
        return wordToBits(res);
    }
}
