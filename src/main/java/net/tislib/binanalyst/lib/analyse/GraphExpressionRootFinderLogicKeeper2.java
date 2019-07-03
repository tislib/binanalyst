package net.tislib.binanalyst.lib.analyse;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.tislib.binanalyst.lib.analyse.lk.LogicKeeper;
import net.tislib.binanalyst.lib.analyse.lk.SimpleLogicKeeper;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.tools.GraphCalculatorTools;

public class GraphExpressionRootFinderLogicKeeper2 {
    private final BitOpsGraphCalculator calculator;

    private LogicKeeper rootLogicKeeper = new SimpleLogicKeeper();

    public GraphExpressionRootFinderLogicKeeper2(BitOpsGraphCalculator calculator) {
        this.calculator = calculator;
    }

    public void analyse() {
        List<OperationalBit> bits = calculator.getMiddle().getBits();
        for (int i = 0, bitsSize = bits.size(); i < bitsSize; i++) {
            OperationalBit operationalBit = bits.get(i);
            Set<String> res = findConflicts(operationalBit);
            if (res.size() > 0) {
                System.out.println(operationalBit.getName() + ": " + res);
            }
        }
    }

    private Set<String> findConflicts(OperationalBit operationalBit) {
        Set<NamedBit> bits = GraphCalculatorTools.findDependencyBits(operationalBit);

        Set<String> res = AnalyserUtil.findConflicts(bits);

        for (String conflictedBit : res) {
            VarBit locatedBit = calculator.getInput().locate(conflictedBit);
            operationalBit = GraphCalculatorTools.normalFormSeparatePerBit(calculator, operationalBit, locatedBit);
            break;
        }

        return res;
    }

    public void show() {

    }

}
