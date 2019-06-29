package net.tislib.binanalyst.lib.calc.graph.expression;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.tislib.binanalyst.lib.bit.BinaryValue;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.Layer;
import net.tislib.binanalyst.lib.calc.graph.Operation;

/**
 * Created by Taleh Ibrahimli on 2/10/18.
 * Email: me@talehibrahimli.com
 */
public class GraphExpression {

    private Layer<OperationalBit> middle = new Layer<>("middle");
    private Layer<VarBit> root = new Layer<>("root");

    private OperationalBit truth;

    public void setCalculation(GraphBitOpsCalculator calculator, VarBit[] result) {
        this.root = calculator.getInput().copy();
        NamedBit[] tBits = new NamedBit[result.length];
        List<NamedBit> oBits = calculator.getOutput().getBits();
        for (int i = 0; i < oBits.size(); i++) {
            tBits[i] = result[i].getValue().isTrue() ? oBits.get(i) : (NamedBit) calculator.not(oBits.get(i));
        }
        this.middle = calculator.getMiddle().copy();
        truth = new OperationalBit(Operation.AND, tBits);
        truth.setName("T");
        truth.setValue(BinaryValue.TRUE);
        middle.register(truth);
    }

    public Layer<OperationalBit> getMiddle() {
        return middle;
    }

    public Layer<VarBit> getRoot() {
        return root;
    }

    public void show() {
        show(false);
    }

    public void show(boolean showValues) {
        root.show(showValues);
        middle.show(showValues);
    }

    private void calculate() {
        for (OperationalBit bit : middle) {
            bit.calculate();
        }
    }

    public boolean check() {
        this.calculate();
        List<NamedBit> corruptedBits = Arrays.stream(truth.getBits()).filter(item -> item.getValue().isFalse()).collect(Collectors.toList());
        return corruptedBits.size() == 0;
    }

    public OperationalBit getTruth() {
        return truth;
    }
}
