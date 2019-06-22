package net.tislib.binanalyst.test.sha256;

import static net.tislib.binanalyst.lib.BinValueHelper.print;
import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import net.tislib.binanalyst.lib.WordOpsHelper;
import net.tislib.binanalyst.lib.algorithms.sha.sha256.Sha256;
import net.tislib.binanalyst.lib.algorithms.sha.sha256.Sha256AlgorithmImpl;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import org.junit.Assert;
import org.junit.Test;

public class Sha256TestTemp {
    private final Sha256AlgorithmImpl sha256Algorithm;
    private final GraphBitOpsCalculator calculator;
    private final WordOpsHelper wordHelper;

    public Sha256TestTemp() {
        this.calculator = new GraphBitOpsCalculator();
        this.sha256Algorithm = new Sha256AlgorithmImpl(calculator);
        this.wordHelper = new WordOpsHelper(calculator);
    }

    @Test
    public void testAlgorithm() {
        int[] hash = Sha256.hash("".getBytes());

        Bit[][] res = sha256Algorithm.hash("".getBytes());

        Bit[] hashBits = new Bit[8 * 32];
        int i = 0;
        for (int word : hash) {
            for (Bit bit : wordHelper.wordToBits(word)) {
                hashBits[i++] = bit;
            }
        }

        print(res);
        print(hashBits);
//        Assert.assertEquals(BinValueHelper.toHexString(res), "d14a028c2a3a2bc9476102bb288234c415a2b01f828ea62ac5b3e42f");
    }

    @Test
    public void testAlgorithm1x() {
        int val = 123789129;
        int d = 2;

        Bit[] bits = wordHelper.wordToBits(val);

        Bit[] res = wordHelper.rotateRight(bits, d);

        System.out.println("Integer " + Integer.rotateRight(val, d));
        print(wordHelper.wordToBits(Integer.rotateRight(val, d)));
        System.out.println();
        System.out.println();

        System.out.println("xx " + wordHelper.bitsToWord(res));
        print(res);
    }

    @Test
    public void testAlgorithm3x() {
        int a = -45860535;
        int b = 1340744138;
        int c = -2027339864;

        int c1 = a ^ b ^ c;

        int c3 = wordHelper.bitsToWord(wordHelper.wordToBits(c1));

        int c2 = wordHelper.bitsToWord(wordHelper.xor(wordHelper.wordToBits(a), wordHelper.wordToBits(b), wordHelper.wordToBits(c)));

        Assert.assertEquals(c1, c3);
        Assert.assertEquals(c1, c2);
    }

    @Test
    public void testAlgorithm2x() {
        int val1 = 123789129;
        int val2 = 127781108;

        VarBit[] varBit1 = VarBit.list("a", 32, ZERO);
        VarBit[] varBit2 = VarBit.list("a", 32, ZERO);
        setVal(varBit1, val1);
        setVal(varBit2, val2);
        Bit[] res = wordHelper.or(varBit1, varBit2);
        print(varBit1);
        print(varBit2);

        int r2 = val1 | val2;
        System.out.println("Integer " + r2);
        print(wordHelper.wordToBits(r2));
        System.out.println();
        System.out.println();

        System.out.println("xx " + wordHelper.bitsToWord(res));
        print(res);
    }

    @Test
    public void testAlgorithm1() {
        int[] hash = Sha256.hash("".getBytes());

        Bit[] hashBits = new Bit[8 * 32];
        int i = 0;
        for (int word : hash) {
            for (Bit bit : wordHelper.wordToBits(word)) {
                hashBits[i++] = bit;
            }
        }

        print(hashBits);
    }

    @Test
    public void testAlgorithm2() {

        Bit[][] res = sha256Algorithm.hash("".getBytes());

        print(res);
    }
}
