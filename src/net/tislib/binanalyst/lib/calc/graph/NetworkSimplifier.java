package net.tislib.binanalyst.lib.calc.graph;

import net.tislib.binanalyst.lib.bit.Bit;

/**
 * Created by Taleh Ibrahimli on 2/7/18.
 * Email: me@talehibrahimli.com
 */
public class NetworkSimplifier implements Simplifier {


    @Override
    public synchronized Bit[] simplify(Bit... bits) {
        this.init();
        return bits;
    }

    @Override
    public synchronized Bit simplify(Bit bit) {
        return null;
    }


    private void init() {

    }
}
