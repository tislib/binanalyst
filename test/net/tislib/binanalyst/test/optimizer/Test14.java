package net.tislib.binanalyst.test.optimizer;

import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import java.util.List;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.optimizer.LogicalOptimizer;
import net.tislib.binanalyst.lib.calc.graph.optimizer.SimpleOptimizer;
import net.tislib.binanalyst.lib.operator.BinMul;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public class Test14 {

    public static void main(String... args) {
        GraphBitOpsCalculator calculator = new GraphBitOpsCalculator();

        long a = 1331;
        long b = 1771;

        VarBit[] aBits = VarBit.list("a", 14, ZERO);
        VarBit[] bBits = VarBit.list("b", 14, ZERO);

        setVal(calculator, aBits, a);
        setVal(calculator, bBits, b);

        calculator.setInputBits(aBits, bBits);

        calculator.getOptimizers().add(new SimpleOptimizer());
        calculator.getOptimizers().add(new LogicalOptimizer());
//        calculator.getOptimizers().add(new CleanNotOptimizer());


        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        calculator.setOutputBits(r);

        calculator.optimize();

        System.out.println(calculator.getOperationCount());
        System.out.println("MIDDLE SIZE: " + calculator.getMiddle().getBits().size());
//        calculator.show();

        calculator.calculate();

        calculator.showResult();
        System.out.println(a * b);

        for (int i = 0; i < calculator.getInput().getBits().size(); i++) {
            System.out.println("checking bit:" + i);
            List<OperationalBit> nBits = calculator.getNegative().getBits();
            List<VarBit> iBits = calculator.getInput().getBits();

            nBits.get(i).setValue(iBits.get(i).getValue());

            calculator.calculate();

            calculator.showResult();
            System.out.println(a * b);
        }


//        calculator.showOutputFull();
    }
}
