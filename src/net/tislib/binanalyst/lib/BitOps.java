package net.tislib.binanalyst.lib;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;

/**
 * Created by Taleh Ibrahimli on 2/5/18.
 * Email: me@talehibrahimli.com
 */
public class BitOps {

//    private static GraphBitOpsOldCalculator calculator = new GraphBitOpsOldCalculator();

    public static Bit xor(BitOpsCalculator calculator, Bit... bits) {
        return calculator.xor(bits);
    }

    public static Bit and(BitOpsCalculator calculator, Bit... bits) {
        return calculator.and(bits);
    }

    public static Bit or(BitOpsCalculator calculator, Bit... bits) {
        return calculator.or(bits);
    }

    public static Bit not(BitOpsCalculator calculator, Bit bit) {
        return calculator.not(bit);
    }

    public static Bit wrap(BitOpsCalculator calculator, byte num) {
        return calculator.wrap(num);
    }

    public static Bit[] wrap(BitOpsCalculator calculator, byte... nums) {
        Bit bits[] = new Bit[nums.length];
        for (int i = 0; i < nums.length; i++) {
            bits[i] = wrap(calculator, nums[i]);
        }
        return bits;
    }

    public static byte convert(Bit bit) {
        return bit.getValue().toByteValue();
    }

    public static byte[] convert(Bit... bits) {
        byte nums[] = new byte[bits.length];
        for (int i = 0; i < bits.length; i++) {
            nums[i] = convert(bits[i]);
        }
        return nums;
    }

}
