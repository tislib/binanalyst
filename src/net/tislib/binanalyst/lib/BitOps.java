package net.tislib.binanalyst.lib;

import net.tislib.binanalyst.lib.calc.GraphBitOpsCalculator;

/**
 * Created by Taleh Ibrahimli on 2/5/18.
 * Email: me@talehibrahimli.com
 */
public class BitOps {

    private static GraphBitOpsCalculator calculator = new GraphBitOpsCalculator();

    public static Bit xor(Bit... bits) {
        return calculator.xor(bits);
    }

    public static Bit and(Bit... bits) {
        return calculator.and(bits);
    }

    public static Bit or(Bit... bits) {
        return calculator.or(bits);
    }

    public static Bit not(Bit bit) {
        return calculator.not(bit);
    }

    public static Bit equal(Bit... bits) {
        return calculator.equal(bits);
    }

    public static Bit wrap(byte num) {
        return calculator.wrap(num);
    }

    public static Bit[] wrap(byte... nums) {
        Bit bits[] = new Bit[nums.length];
        for (int i = 0; i < nums.length; i++) {
            bits[i] = wrap(nums[i]);
        }
        return bits;
    }

    public static byte convert(Bit bit) {
        return (byte) (bit.getValue() ? 1 : 0);
    }

    public static byte[] convert(Bit... bits) {
        byte nums[] = new byte[bits.length];
        for (int i = 0; i < bits.length; i++) {
            nums[i] = convert(bits[i]);
        }
        return nums;
    }

}
