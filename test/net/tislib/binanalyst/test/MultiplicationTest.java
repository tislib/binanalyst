package net.tislib.binanalyst.test;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;
import net.tislib.binanalyst.lib.calc.SimpleBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.operator.BinMul;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static net.tislib.binanalyst.lib.BinValueHelper.*;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
@RunWith(Parameterized.class)
public class MultiplicationTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {23234, 34345}, {455, 65632}, {3, 23443244}, {32332324, 4}, {2, 3}, {5, 6}, {2, 422}
        });
    }

    private final long a;
    private final long b;

    public MultiplicationTest(long a, long b) {
        this.a = a;
        this.b = b;
    }

    @Test
    public void simpleCalc() {
        SimpleBitOpsCalculator calculator = new SimpleBitOpsCalculator();
        Bit[] r = BinMul.multiply(calculator, trim(getBits(calculator, a)), trim(getBits(calculator, b)));
        assertEquals(toLong(r), a * b);
    }

    @Test
    public void graphCalc() {
        GraphBitOpsCalculator calculator = new GraphBitOpsCalculator();

        VarBit[] aBits = VarBit.list("a", 3, ZERO);
        VarBit[] bBits = VarBit.list("b", 3, ZERO);

        setVal(calculator, aBits, a);
        setVal(calculator, bBits, b);

        calculator.setInputBits(aBits, bBits);

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        System.out.println(calculator.getOperationCount());

        calculator.show();

        assertEquals(a * b, toLong(r));
    }


}
