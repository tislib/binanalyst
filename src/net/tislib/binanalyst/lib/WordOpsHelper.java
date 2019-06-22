package net.tislib.binanalyst.lib;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import java.nio.ByteBuffer;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;
import net.tislib.binanalyst.lib.operator.BinAdd;

public class WordOpsHelper {
    private final BitOpsCalculator calculator;

    public WordOpsHelper(BitOpsCalculator calculator) {
        this.calculator = calculator;
    }

    public Bit[] wordToBits(int word) {
        char[] chars = Integer.toBinaryString(word).toCharArray();
        Bit[] res = new Bit[32];
        for (int i = 0; i < res.length; i++) {
            res[i] = ZERO;
        }
        for (int i = 0; i < chars.length; i++) {
            res[i + (res.length - chars.length)] = calculator.wrap(chars[i] == '1' ? 1 : 0);
        }
        return res;
    }

    public int bitsToWord(Bit[] bits) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < bits.length; i++) {
            str.append((byte) bits[i].intVal());
        }
        return (int) Long.parseLong(str.toString(), 2);
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
        Bit[] z2 = BinAdd.add(calculator, bits);
        Bit[] zx = new Bit[bits[0].length];
        System.arraycopy(z2, z2.length - zx.length, zx, 0, zx.length);

        return zx;
    }

    public Bit[] and(Bit[]... bits) {
        return BinOps.and(calculator, bits);
    }

    public Bit[] or(Bit[]... bits) {
        return BinOps.or(calculator, bits);
    }

    public Bit[] xor(Bit[]... bits) {
        return BinOps.xor(calculator, bits);
    }

    public Bit[] not(Bit[] bits) {
        return BinOps.not(calculator, bits);
    }

    public Bit[] rotateRight(Bit[] bits, int distance) {
        return or(
                and(
                        shr(bits, distance),
                        not(shl(wordToBits(-1), (bits.length - distance)))
                ),
                shl(bits, bits.length - distance)
        );
    }

    public Bit[] shru(Bit[] bh, int l) {
        Bit[] newBits = new Bit[bh.length];
        for (int i = 0; i < newBits.length; i++) {
            newBits[i] = ZERO;
        }
        System.arraycopy(bh, 0, newBits, l, bh.length - l);
        return newBits;
    }

    public static Bit[] shl(Bit[] bh, int l) {
        Bit[] newBits = new Bit[bh.length];
        for (int i = 0; i < newBits.length; i++) {
            newBits[i] = ZERO;
        }
        System.arraycopy(bh, l, newBits, 0, bh.length - l);
        return newBits;
    }

    public Bit[] shr(Bit[] bh, int l) {
        Bit[] newBits = new Bit[bh.length];
        for (int i = 0; i < newBits.length; i++) {
            newBits[i] = bh[bh.length - 1];
        }
        System.arraycopy(bh, 0, newBits, l, bh.length - l);
        return newBits;
    }


    public Bit[][] toBitWordArray(byte[] pad) {
        int[] intArr = toIntArray(pad);
        Bit[][] res = new Bit[intArr.length][8];
        for (int i = 0; i < intArr.length; i++) {
            res[i] = wordToBits(intArr[i]);
        }
        return res;
    }


    public byte[] pad(byte[] message) {
        final int blockBits = 512;
        final int blockBytes = blockBits / 8;

        // new message length: original + 1-bit and padding + 8-byte length
        int newMessageLength = message.length + 1 + 8;
        int padBytes = blockBytes - (newMessageLength % blockBytes);
        newMessageLength += padBytes;

        // copy message to extended array
        final byte[] paddedMessage = new byte[newMessageLength];
        System.arraycopy(message, 0, paddedMessage, 0, message.length);

        // write 1-bit
        paddedMessage[message.length] = (byte) 0b10000000;

        // skip padBytes many bytes (they are already 0)

        // write 8-byte integer describing the original message length
        int lenPos = message.length + 1 + padBytes;
        ByteBuffer.wrap(paddedMessage, lenPos, 8).putLong(message.length * 8);

        return paddedMessage;
    }

    public int[] toIntArray(byte[] bytes) {
        if (bytes.length % Integer.BYTES != 0) {
            throw new IllegalArgumentException("byte array length");
        }

        ByteBuffer buf = ByteBuffer.wrap(bytes);

        int[] result = new int[bytes.length / Integer.BYTES];
        for (int i = 0; i < result.length; ++i) {
            result[i] = buf.getInt();
        }

        return result;
    }
}
