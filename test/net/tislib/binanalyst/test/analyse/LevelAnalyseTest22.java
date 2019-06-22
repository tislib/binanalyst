package net.tislib.binanalyst.test.analyse;

import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import java.util.Map;
import net.tislib.binanalyst.lib.BinValueHelper;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.BinderOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.UnusedBitOptimizerDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.XorAndCalculatorDecorator;
import net.tislib.binanalyst.lib.calc.graph.tools.OperationLevelMeasureAnalyser;
import net.tislib.binanalyst.lib.operator.BinMul;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public class LevelAnalyseTest22 {

    public static void main(String... args) {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();
        calculator = new BinderOptimizationDecorator(calculator);
        calculator = new XorAndCalculatorDecorator(calculator, true);
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


//        Bit[] r = BinMulRec.multiplyTree2Rec(calculator, aBits, bBits, true);
        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        //110001

        Bit[] truth = new Bit[6];
        truth[0] = r[2];
        truth[1] = r[3];
        truth[2] = calculator.not(r[4]);
        truth[3] = calculator.not(r[5]);
        truth[4] = calculator.not(r[6]);
        truth[5] = r[7];

        calculator.setOutputBits(new Bit[]{calculator.and(truth)});

        calculator.calculate();

        calculator.show();


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
