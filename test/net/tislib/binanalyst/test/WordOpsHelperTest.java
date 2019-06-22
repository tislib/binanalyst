package net.tislib.binanalyst.test;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import net.tislib.binanalyst.lib.BinValueHelper;
import net.tislib.binanalyst.lib.WordOpsHelper;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.SimpleBitOpsCalculator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class WordOpsHelperTest {

    private final int a;
    private final int b;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return TestData.testPairData();
    }

    private final WordOpsHelper wordOpsHelper;
    private final SimpleBitOpsCalculator calculator;

    public WordOpsHelperTest(long a, long b) {
        calculator = new SimpleBitOpsCalculator();
        wordOpsHelper = new WordOpsHelper(calculator);

        this.a = (int) a;
        this.b = (int) b;
    }

    @Test
    public void wordBitConverter() {
        int c = a + b;

        int res = wordOpsHelper.bitsToWord(wordOpsHelper.wordToBits(c));

        assertEquals(c, res);
    }

    @Test
    public void testAdd() {
        int c = a + b;

        Bit[] resBits = wordOpsHelper.add(wordOpsHelper.wordToBits(a), wordOpsHelper.wordToBits(b));

        int res = wordOpsHelper.bitsToWord(resBits);

        assertEquals(c, res);
    }
}
