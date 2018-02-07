package net.tislib.binanalyst.test;

import net.tislib.binanalyst.lib.BinCalc;
import net.tislib.binanalyst.lib.BinValueHelper;
import net.tislib.binanalyst.lib.BitOps;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;
import net.tislib.binanalyst.lib.calc.SimpleBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.operator.BinMul;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static net.tislib.binanalyst.lib.BinValueHelper.getBits;
import static net.tislib.binanalyst.lib.BinValueHelper.trim;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
@RunWith(Parameterized.class)
public class MultiplicationTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {232, 343}, {455, 656}, {344, 234}, {34, 54}, {2, 3}, {5, 6}, {2, 422}
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
        assertEquals(BinValueHelper.toLong(r), a * b);
    }

    @Test
    public void graphCalc() {
        GraphBitOpsCalculator calculator = new GraphBitOpsCalculator();
        Bit[] r = BinMul.multiply(calculator, trim(getBits(calculator, a)), trim(getBits(calculator, b)));
        assertEquals(BinValueHelper.toLong(r), a * b);
    }


}
