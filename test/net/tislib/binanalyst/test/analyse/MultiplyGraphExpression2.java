package net.tislib.binanalyst.test.analyse;

import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;
import net.tislib.binanalyst.lib.calc.graph.decorator.AndOrCalculatorDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.BinderOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.UnusedBitOptimizerDecorator;
import net.tislib.binanalyst.lib.calc.graph.tools.GraphCalculatorTools;
import net.tislib.binanalyst.lib.operator.BinMul;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public class MultiplyGraphExpression2 {

    public static void main(String... args) throws JsonProcessingException {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();
        calculator = new BinderOptimizationDecorator(calculator);
        calculator = new AndOrCalculatorDecorator(calculator, true);
//        calculator = new XorAndCalculatorDecorator(calculator, true);
        calculator = new SimpleOptimizationDecorator(calculator);
//        calculator = new ConstantOperationRemoverOptimizationDecorator(calculator);
        calculator = new UnusedBitOptimizerDecorator(calculator);

        long a = 7;
        long b = 5;

        //32532325, 23403244

        VarBit[] aBits = VarBit.list("a", 3, ZERO);
        VarBit[] bBits = VarBit.list("b", 3, ZERO);

        setVal(aBits, a);
        setVal(bBits, b);

        prepareCommonOps(calculator);

        calculator.setInputBits(aBits, bBits);

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        Bit[] truth = GraphCalculatorTools.getTruthBit(calculator, r, 35);

        Bit result = calculator.and(truth);

        calculator.setOutputBits(new Bit[]{result});

        ObjectMapper objectMapper = new ObjectMapper();

//        String data = objectMapper.writeValueAsString(GraphCalculatorTools.serializeCalculator(calculator));

//        System.out.println(data);

        calculator.calculate();
        calculator.show();

//        print(r);
//        System.out.println(result.getValue().isTrue());

        traverse(calculator);


    }

    private static void traverse(BitOpsGraphCalculator calculator) {
        NamedBit truth = calculator.getOutput().getBits().get(0);
        String val = traverse(truth);
        System.out.println(val);
        System.out.println(val.length());
    }

    private static String traverse(NamedBit bit) {
        if (bit instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) bit;
            if (operationalBit.getOperation() == Operation.AND) {
                StringBuilder stringBuilder = new StringBuilder();
                NamedBit[] bits = operationalBit.getBits();
                for (int i = 0, bitsLength = bits.length; i < bitsLength; i++) {
                    stringBuilder.append(traverse(bits[i]));
                    if (i < bitsLength - 1) {
                        stringBuilder.append(",");
                    }
                }
                return stringBuilder.toString();
            } else if (operationalBit.getOperation() == Operation.OR) {
                return traverse(operationalBit.getBits()[0]);
            } else if (operationalBit.getOperation() == Operation.NOT) {
                return "!" + traverse(operationalBit.getBits()[0]);
            } else {
                throw new RuntimeException();
            }
        } else {
            return bit.getName();
        }
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
