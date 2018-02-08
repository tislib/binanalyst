package net.tislib.binanalyst.lib.calc.graph;

import net.tislib.binanalyst.lib.bit.Bit;

/**
 * Created by Taleh Ibrahimli on 2/7/18.
 * Email: me@talehibrahimli.com
 */
public interface Simplifier {

    Bit[] simplify(Bit... bits);

    Bit simplify(Bit bit);

}
