package net.tislib.binanalyst.lib;

/**
 * Created by Taleh Ibrahimli on 2/5/18.
 * Email: me@talehibrahimli.com
 */
public interface BitOpsCalculator {
    Bit xor(Bit[] bits);

    Bit and(Bit[] bits);

    Bit or(Bit[] bits);

    Bit not(Bit bit);

    Bit equal(Bit[] bits);

    Bit wrap(Number num);
}
