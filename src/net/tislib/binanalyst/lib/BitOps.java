package net.tislib.binanalyst.lib;

/**
 * Created by Taleh Ibrahimli on 2/5/18.
 * Email: me@talehibrahimli.com
 */
public class BitOps {

    private static BitOpsCalculator calculator = new SimpleBitOpsCalculator();

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

}
