package net.tislib.binanalyst.lib.calc.graph.sat;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.ConstantOperationRemoverOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.Logical2OptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.UnusedBitOptimizerDecorator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TSatBuilder {

    boolean hasSolution(BitOpsGraphCalculator satCalculator) {
        return SimpleSatTester.hasSolution(satCalculator);
    }

    public boolean hasSolution(BitOpsGraphCalculator initialCalc, long r) {
        BitOpsGraphCalculator calc = buildSat(initialCalc, r);

        return hasSolution(calc);
    }

    public BitOpsGraphCalculator buildSat(BitOpsGraphCalculator initialCalc, long r) {
        BitOpsGraphCalculator calc = SimpleSatTester.buildSat(initialCalc, r);

        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();

        calculator = new Logical2OptimizationDecorator(calculator);
        calculator = new ConstantOperationRemoverOptimizationDecorator(calculator);
        calculator = new SimpleOptimizationDecorator(calculator);
        calculator = new UnusedBitOptimizerDecorator(calculator);

        // collect inputs
        Map<String, VarBit> inputMap = new HashMap<>();

        List<Bit> main = new ArrayList<>();

        for (OperationalBit operationalBit : calc.getMiddle()) {
            inputMap.put(operationalBit.getName(), fromBit(operationalBit));
            for (NamedBit namedBit : operationalBit.getBits()) {
                inputMap.put(namedBit.getName(), fromBit(namedBit));
            }
        }

        calculator.setInputBits(inputMap.values().toArray(new VarBit[0]));

        for (OperationalBit operationalBit : calc.getMiddle()) {
            NamedBit[] bits = new NamedBit[operationalBit.getBits().length];
            for (int i = 0; i < operationalBit.getBits().length; i++) {
                bits[i] = inputMap.get(operationalBit.getBits()[i].getName());
            }
            Bit newOp = calculator.operation(operationalBit.getOperation(), bits);

            main.add(calculator.xor(calculator.not(inputMap.get(operationalBit.getName())), newOp));
        }
        main.add(inputMap.get(calc.getOutput().getBitL(0).getName()));

        calculator.setOutputBits(new Bit[]{calculator.and(main.toArray(new Bit[0]))});

        return calculator;
    }

    private static VarBit fromBit(NamedBit namedBit) {
        VarBit varBit = new VarBit();
        varBit.setName(namedBit.getName() + "R");
        return varBit;
    }
}
