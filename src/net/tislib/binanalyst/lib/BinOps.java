package net.tislib.binanalyst.lib;

import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import java.util.function.Consumer;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;

/**
 * Created by Taleh Ibrahimli on 2/5/18.
 * Email: me@talehibrahimli.com
 */
public class BinOps {

//    private static GraphBitOpsOldCalculator calculator = new GraphBitOpsOldCalculator();

    public static Bit xor(BitOpsCalculator calculator, Bit... bits) {
        return calculator.xor(bits);
    }

    public static Bit[] xor(BitOpsCalculator calculator, Bit[]... bits) {
        if (bits.length == 0) return new Bit[0];
        if (bits.length == 1) {
            return bits[0];
        }

        Bit[] res = copy(bits[0]);
        for (int i = 1; i < bits.length; i++) {
            for (int j = 0; j < bits[i].length; j++) {
                res[j] = xor(calculator, res[j], bits[i][j]);
            }
        }
        return res;
    }

    public static Bit[] or(BitOpsCalculator calculator, Bit[]... bits) {
        if (bits.length == 0) return new Bit[0];
        if (bits.length == 1) {
            return bits[0];
        }

        Bit[] res = copy(bits[0]);

        for (int i = 1; i < bits.length; i++) {
            for (int j = 0; j < bits[i].length; j++) {
                res[j] = or(calculator, res[j], bits[i][j]);
            }
        }
        return res;
    }

    public static Bit[] and(BitOpsCalculator calculator, Bit[]... bits) {
        if (bits.length == 0) return new Bit[0];
        if (bits.length == 1) {
            return bits[0];
        }

        Bit[] res = copy(bits[0]);

        for (int i = 1; i < bits.length; i++) {
            for (int j = 0; j < bits[i].length; j++) {
                res[j] = and(calculator, res[j], bits[i][j]);
            }
        }
        return res;
    }

    public static Bit[] not(BitOpsCalculator calculator, Bit[] bits) {
        Bit[] res = new Bit[bits.length];
        for (int i = 0; i < bits.length; i++) {
            res[i] = calculator.not(bits[i]);
        }
        return res;
    }

    public static Bit[][] copy(Bit[][] state) {
        Bit[][] res = new Bit[state.length][];
        for (int i = 0; i < state.length; i++) {
            res[i] = copy(state[i]);
        }
        return res;
    }

    public static Bit[] copy(Bit[] bits) {
        Bit[] res = new Bit[bits.length];
        for (int i = 0; i < bits.length; i++) {
            res[i] = bits[i];
        }
        return res;
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

    public static Bit[] wrap(BitOpsCalculator calculator, Number... nums) {
        Bit bits[] = new Bit[nums.length];
        for (int i = 0; i < nums.length; i++) {
            bits[i] = wrap(calculator, nums[i].byteValue());
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

    public static void variate(int length, Consumer<Bit[]> consumer) {
        VarBit[] varBits = VarBit.list("v", length, ZERO);
        consumer.accept(varBits);
        for (int i = 0; i < 1 << length; i++) {
            setVal(varBits, i);
            consumer.accept(varBits);
        }
    }

    public static Bit[] shl(Bit[] bh, int l) {
        Bit[] newBits = new Bit[bh.length + l];
        for (int i = 0; i < newBits.length; i++) {
            newBits[i] = ZERO;
        }
        System.arraycopy(bh, 0, newBits, 0, bh.length);
        return newBits;
    }

    public static Bit[] shr(Bit[] bh, int l) {
        Bit[] newBits = new Bit[bh.length + l];
        for (int i = 0; i < newBits.length; i++) {
            newBits[i] = ZERO;
        }
        System.arraycopy(bh, 0, newBits, l, bh.length);
        return newBits;
    }
}
