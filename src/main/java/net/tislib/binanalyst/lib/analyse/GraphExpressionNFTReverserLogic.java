package net.tislib.binanalyst.lib.analyse;

import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import java.util.ArrayList;
import java.util.List;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.Layer;
import net.tislib.binanalyst.lib.calc.graph.decorator.BinderOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.ConstantOperationRemoverOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.UnusedBitOptimizerDecorator;
import net.tislib.binanalyst.lib.calc.graph.expression.BooleanExpression;
import net.tislib.binanalyst.lib.calc.graph.tools.GraphCalculatorTools;
import net.tislib.binanalyst.lib.calc.reverse.SingleBitReverser;

@SuppressWarnings("Duplicates")
public class GraphExpressionNFTReverserLogic {

    private final BitOpsGraphCalculator calculator;

    private BitOpsGraphCalculator innerCalculator = new GraphBitOpsCalculator();
    private NamedBit truth;
    private boolean usageOnly;
    private List<NamedBit> output;
    private VarBit[] replacedOutput;

    public GraphExpressionNFTReverserLogic(BitOpsGraphCalculator calculator) {
        this.calculator = calculator;

        innerCalculator.getMiddle().setLabelPrefix("I");

        innerCalculator = new SimpleOptimizationDecorator(innerCalculator);
        innerCalculator = new BinderOptimizationDecorator(innerCalculator);
        innerCalculator = new ConstantOperationRemoverOptimizationDecorator(innerCalculator);
        innerCalculator = new UnusedBitOptimizerDecorator(innerCalculator);

        init();
    }

    private void init() {
        if (usageOnly) {
            for (VarBit varBit : this.calculator.getInput()) {
                List<OperationalBit> usage = GraphCalculatorTools.findUsages(calculator, varBit);
                output.addAll(usage);
            }
        } else {
            this.output = calculator.getOutput().getBits();
        }


        this.replacedOutput = VarBit.list("MR", calculator.getOutput().size(), ZERO);

        innerCalculator.getInput().addBits(replacedOutput);

        this.truth = (NamedBit) ONE;

        for (int i = 0; i < output.size(); i++) {
            truth = (NamedBit) innerCalculator.and(truth, innerCalculator.xor(innerCalculator.not(output.get(i)), replacedOutput[i]));
        }
    }

    public static SingleBitReverser reverser() {
        return (calculator, bitName, truth) -> {
            GraphExpressionNFTReverserLogic graphExpressionNFTReverserLogic = new GraphExpressionNFTReverserLogic(calculator);
            graphExpressionNFTReverserLogic.usageOnly = false;
            if (truth) {
                graphExpressionNFTReverserLogic.truth = graphExpressionNFTReverserLogic.locateAndReplace(graphExpressionNFTReverserLogic.truth, calculator.getInput().locate(bitName), ONE);
            } else {
                graphExpressionNFTReverserLogic.truth = graphExpressionNFTReverserLogic.locateAndReplace(graphExpressionNFTReverserLogic.truth, calculator.getInput().locate(bitName), ZERO);
            }
            graphExpressionNFTReverserLogic.getInnerCalculator().show();
            graphExpressionNFTReverserLogic.analyse();
            VarBit[] input = graphExpressionNFTReverserLogic.getInnerCalculator().getInput().getBits().toArray(new VarBit[0]);

            System.out.println(formulaToString(graphExpressionNFTReverserLogic.truth));
            return (BooleanExpression) value -> {
                setVal(input, value);
                String bn = bitName;
                boolean bt = truth;
                graphExpressionNFTReverserLogic.getInnerCalculator().calculate();
                boolean res = graphExpressionNFTReverserLogic.truth.getValue().isTrue();
                return res;
            };
        };
    }

    public void analyse() {
        locateTruth(innerCalculator);

        innerCalculator.optimize();
    }

    private void locateTruth(BitOpsGraphCalculator innerCalculator) {
        for (VarBit varBit : calculator.getInput()) {
            this.truth = (NamedBit) innerCalculator.or(
                    locateAndReplace(truth, varBit, ONE),
                    locateAndReplace(truth, varBit, ZERO)
            );
        }

        innerCalculator.getOutput().setSingleBit(truth);
    }

    private NamedBit locateAndReplace(NamedBit truth, VarBit varBit, Bit bit) {
        if (truth instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) truth;
            NamedBit[] newBits = new NamedBit[operationalBit.getBits().length];

            boolean changed = false;
            for (int i = 0; i < operationalBit.getBits().length; i++) {
                newBits[i] = locateAndReplace(operationalBit.getBits()[i], varBit, bit);
                if (newBits[i] != operationalBit.getBits()[i]) {
                    changed = true;
                }
            }
            if (changed) {
                return (NamedBit) innerCalculator.operation(operationalBit.getOperation(), newBits);
            } else {
                return truth;
            }

        } else if (truth.getName().equals(varBit.getName())) {
            return (NamedBit) bit;
        }
        return truth;
    }

    public void show() {
        showState();
    }

    public void showState() {
        System.out.println("Done");
//        System.out.println(formulaToString(truth));
    }

    public static String formulaToString(Bit bit) {
        if (bit instanceof OperationalBit) {
            return ((OperationalBit) bit)
                    .showFull(false);
        } else {
            return bit.toString();
        }
    }

    public BitOpsGraphCalculator getInnerCalculator() {
        return innerCalculator;
    }
}
