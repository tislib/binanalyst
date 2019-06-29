package net.tislib.binanalyst.lib.calc.graph.optimizer;

import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.Layer;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public interface Transformer {
    void transform(Layer<? extends NamedBit> layer, GraphBitOpsCalculator.LayerType layerType);
}
