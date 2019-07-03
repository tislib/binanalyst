package net.tislib.binanalyst.lib.analyse;

import net.tislib.binanalyst.lib.analyse.lk.LogicKeeper;
import net.tislib.binanalyst.lib.analyse.lk.SimpleLogicKeeper;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;

public class GraphExpressionRootFinderLogicKeeper {
    private final BitOpsGraphCalculator calculator;

    private LogicKeeper rootLogicKeeper = new SimpleLogicKeeper();

    public GraphExpressionRootFinderLogicKeeper(BitOpsGraphCalculator calculator) {
        this.calculator = calculator;
    }

    public void analyse() {
        for (NamedBit operationalBit : calculator.getInput()) {
           rootLogicKeeper.keep(operationalBit);
        }
        for (OperationalBit operationalBit : calculator.getMiddle()) {
           rootLogicKeeper.keep(operationalBit);
        }
    }

    public void show() {
        rootLogicKeeper.showFormula(calculator.getOutput().getBits().get(0));
    }

}
