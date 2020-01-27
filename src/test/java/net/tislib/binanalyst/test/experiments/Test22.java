package net.tislib.binanalyst.test.experiments;

import net.tislib.binanalyst.lib.analyse.lk.BruteForceLogicKeeper;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.tools.GraphCalculatorTools;
import net.tislib.binanalyst.lib.calc.graph.tools.NormalFormulaGenerator;
import net.tislib.binanalyst.lib.operator.BinAdd;
import net.tislib.binanalyst.lib.operator.BinMul;
import net.tislib.binanalyst.test.examples.SimpleTestCalculators;

import java.util.Set;

public class Test22 {

    public static void main(String... args) {
        BitOpsGraphCalculator calculator = SimpleTestCalculators.nBitFunction(8, BinMul::multiply, "ANDOR", "BINDER");

        BruteForceLogicKeeper bruteForceLogicKeeper = new BruteForceLogicKeeper();

        calculator.show();

        for (NamedBit operationalBit : calculator.getInput()) {
            bruteForceLogicKeeper.keep(operationalBit);
        }

        for (OperationalBit operationalBit : calculator.getMiddle()) {
            bruteForceLogicKeeper.keep(operationalBit);
            System.out.println(operationalBit.getName() + " kept");
//            bruteForceLogicKeeper.showAllFormulas();
        }

        bruteForceLogicKeeper.showFormula(calculator.getOutput().getBitL(15));
    }
}
