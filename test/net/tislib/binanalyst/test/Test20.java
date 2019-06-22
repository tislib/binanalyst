package net.tislib.binanalyst.test;

import static net.tislib.binanalyst.lib.BinValueHelper.formulaToString;
import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import net.tislib.binanalyst.lib.BinValueHelper;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.BinderOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.UnusedBitOptimizerDecorator;

public class Test20 {

    public static void main(String... args) {
        Set<Integer> truth0 = new TreeSet<>();
        Set<Integer> truth1 = new TreeSet<>();
        Set<Integer> truth2 = new TreeSet<>();
        int n = 3;
        int L = 1 << n;

        Predicate<Integer> predicate0 = i -> ((i) % 2) == 1;
        Predicate<Integer> predicate1 = i -> ((i / 2) % 2) == 1;
        Predicate<Integer> predicate2 = i -> ((i / 4) % 2) == 1;

        for (int i = 0; i < L; i++) {
            for (int j = 0; j < L; j++) {
                int c = i * j;
                if (predicate0.test(i) && predicate0.test(j)) {
                    truth0.add(c);
                }
                if (predicate1.test(i) && predicate1.test(j)) {
                    truth1.add(c);
                }
                if (predicate2.test(i) && predicate2.test(j)) {
                    truth2.add(c);
                }
            }

        }
        for (Integer t : truth0) {
            BinValueHelper.print(t);
        }
        System.out.println(truth0.size());
        for (Integer t : truth1) {
            BinValueHelper.print(t);
        }
        System.out.println(truth1.size());
        for (Integer t : truth2) {
            BinValueHelper.print(t);
        }
        System.out.println(truth2.size());

        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();
        calculator = new BinderOptimizationDecorator(calculator);
        calculator = new SimpleOptimizationDecorator(calculator);
        calculator = new UnusedBitOptimizerDecorator(calculator);

        VarBit[] input = VarBit.list("c", truth0.size(), ZERO);
        calculator.setInputBits(input);

        Bit[] output = new Bit[3];
        output[0] = truthToCalcOutput(truth0, calculator, 0);
        output[1] = truthToCalcOutput(truth1, calculator, 1);
        output[2] = truthToCalcOutput(truth2, calculator, 2);

        calculator.setOutputBits(output);

        setVal(input, 12);

        calculator.calculate();

        calculator.show();

        System.out.println(formulaToString(output[0]));
        System.out.println(formulaToString(output[1]));

    }

    private static Bit truthToCalcOutput(Set<Integer> truth, BitOpsGraphCalculator calculator, int num) {
        Bit output = null;
        int i = 0;
        for (int c : truth) {
            Bit val = adjustCalc(calculator, c);
            if (output == null) {
                output = val;
            } else {
                output = calculator.or(output, val);
            }
        }
        return output;
    }

    private static Bit adjustCalc(BitOpsGraphCalculator calculator, final int c) {
        int cx = c;
        Bit res = null;
        int i = 1;
        while (cx > 0) {
            boolean bv = cx % 2 == 0;
            cx = cx / 2;
            Bit val;
            if (bv) {
                val = calculator.getInput().getBitL(i);
            } else {
                val = calculator.not(calculator.getInput().getBitL(i));
            }
            if (res == null) {
                res = val;
            } else {
                res = calculator.and(res, val);
            }
            i++;
        }
        return res;
    }

    static boolean checkPrime(int n) {
        int i, m = 0, flag = 0;
        m = n / 2;
        if (n == 0 || n == 1) {
            return false;
        } else {
            for (i = 2; i <= m; i++) {
                if (n % i == 0) {
                    return false;
                }
            }
            return true;
        }//end of else
    }
}
