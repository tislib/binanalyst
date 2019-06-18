package net.tislib.binanalyst.test.analyse;

import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import java.util.Map;
import net.tislib.binanalyst.lib.BinValueHelper;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.BinderOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.UnusedBitOptimizerDecorator;
import net.tislib.binanalyst.lib.calc.graph.tools.GraphCalculatorTools;
import net.tislib.binanalyst.lib.calc.graph.tools.OperationLevelMeasureAnalyser;
import net.tislib.binanalyst.lib.operator.BinMul;
import net.tislib.binanalyst.lib.operator.BinMulRec;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public class LevelAnalyseTest {

    public static void main(String... args) {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();
//        calculator = new BinderOptimizationDecorator(calculator);
        calculator = new SimpleOptimizationDecorator(calculator);
        calculator = new UnusedBitOptimizerDecorator(calculator);

        OperationLevelMeasureAnalyser operationLevelMeasureAnalyser = new OperationLevelMeasureAnalyser(calculator);

        long a = 3;
        long b = 2;

        //32532325, 23403244

        VarBit[] aBits = VarBit.list("a", 3, ZERO);
        VarBit[] bBits = VarBit.list("b", 3, ZERO);


        setVal(aBits, a);
        setVal(bBits, b);

        prepareCommonOps(calculator);

        calculator.setInputBits(aBits, bBits);


//        Bit[] r = BinMul.multiply(calculator, aBits, bBits);
        Bit[] r = BinMulRec.multiplyTree2Rec(calculator, aBits, bBits, false);


//        int i = 0;
//        VarBit[][] M = new VarBit[aBits.length][bBits.length];
//        for (Bit bit1 : aBits) {
//            int j = 0;
//            for (Bit bit2 : bBits) {
//                M[i][j] = (VarBit) calculator.and(bit1, bit2);
//                j++;
//            }
//            i++;
//        }
//
//
//        calculator.setInputBits(M);

        calculator.setOutputBits(r);

        calculator.calculate();

        System.out.println(BinValueHelper.toLong(r));

        operationLevelMeasureAnalyser.analyse();
//        operationLevelMeasureAnalyser.reLabel();
        calculator.show();
//        operationLevelMeasureAnalyser.show();
        Map<Integer, Long> stats = operationLevelMeasureAnalyser.stats();
        System.out.println(stats);


//        printValues(r);
        System.out.println("MIDDLE SIZE: " + calculator.getMiddle().getBits().size());
        System.out.println("DEPTH: " + GraphCalculatorTools.getMaxDepth(calculator));
//
//        for (int i = 0; i < r.length; i++) {
//            BinValueHelper.printFormula((NamedBit) r[i]);
//        }
    }

    private static void prepareCommonOps(BitOpsGraphCalculator calculator) {
        for (Bit bit : calculator.getInput()) {
            calculator.not(bit);
        }
        int i = 0;
        int l = calculator.getInput().getBits().size();
        Bit[][] M = new Bit[l][l];
        for (Bit bit1 : calculator.getInput()) {
            int j = 0;
            for (Bit bit2 : calculator.getInput()) {
                M[i][j] = calculator.and(bit1, bit2);
                j++;
            }
            i++;
        }

        for (int k1 = 0; k1 < l; k1++) {
            for (int k2 = 0; k2 < l; k2++) {
                for (int k3 = 0; k3 < l; k3++) {
                    for (int k4 = 0; k4 < l; k4++) {
                        calculator.xor(M[k1][k2], M[k3][k4]);
                    }
                }
            }
        }

        for (int k1 = 0; k1 < l; k1++) {
            for (int k2 = 0; k2 < l; k2++) {
                for (int k3 = 0; k3 < l; k3++) {
                    for (int k4 = 0; k4 < l; k4++) {
                        calculator.and(M[k1][k2], M[k3][k4]);
                    }
                }
            }
        }
    }

}
