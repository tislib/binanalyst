package net.tislib.binanalyst.test.sha256;

import static net.tislib.binanalyst.lib.WordOpsHelper.toHex;

import java.util.Collection;
import net.tislib.binanalyst.lib.WordOpsHelper;
import net.tislib.binanalyst.lib.algorithms.sha.sha256.Sha256;
import net.tislib.binanalyst.lib.algorithms.sha.sha256.Sha256Algorithm;
import net.tislib.binanalyst.lib.algorithms.sha.sha256.Sha256AlgorithmImpl;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.SimpleBitOpsCalculator;
import net.tislib.binanalyst.test.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class Sha256Test {
    private final int a;
    private final int b;
    private final Sha256Algorithm sha256Algorithm;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return TestData.testPairData();
    }

    private final WordOpsHelper wordOpsHelper;
    private final SimpleBitOpsCalculator calculator;

    public Sha256Test(long a, long b) {
        calculator = new SimpleBitOpsCalculator();
        wordOpsHelper = new WordOpsHelper(calculator);
        this.sha256Algorithm = new Sha256AlgorithmImpl(calculator);

        this.a = (int) a;
        this.b = (int) b;
    }

    @Test
    public void testAlgorithm() {
        String str = ("" + a + b);
        int[] hash = Sha256.hash(str.getBytes());

        Bit[][] res = sha256Algorithm.hash(str.getBytes());

        Assert.assertEquals(toHex(hash), wordOpsHelper.toHex(res));
    }

}
