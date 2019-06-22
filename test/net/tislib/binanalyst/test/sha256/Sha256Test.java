package net.tislib.binanalyst.test.sha256;

import static net.tislib.binanalyst.lib.WordOpsHelper.toHex;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import net.tislib.binanalyst.lib.WordOpsHelper;
import net.tislib.binanalyst.lib.algorithms.sha.sha256.Sha256;
import net.tislib.binanalyst.lib.algorithms.sha.sha256.Sha256Algorithm;
import net.tislib.binanalyst.lib.algorithms.sha.sha256.Sha256AlgorithmImpl;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.SimpleBitOpsCalculator;
import net.tislib.binanalyst.test.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class Sha256Test {
    private final byte[] data;
    private final Sha256Algorithm sha256Algorithm;

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws IOException {
        List<Object[]> data = TestData.testPairData();

        data = data.stream().map(item -> {
            return new Object[]{("" + item[0] + item[1]).getBytes()};
        }).collect(Collectors.toList());

        data.add(new Object[]{Files.readAllBytes(Paths.get(Sha256Test.class.getResource("randfile1").getPath()))});
        data.add(new Object[]{Files.readAllBytes(Paths.get(Sha256Test.class.getResource("randfile2").getPath()))});
        data.add(new Object[]{Files.readAllBytes(Paths.get(Sha256Test.class.getResource("randfile3").getPath()))});

        return data;
    }

    private final WordOpsHelper wordOpsHelper;
    private final SimpleBitOpsCalculator calculator;

    public Sha256Test(byte[] data) {
        calculator = new SimpleBitOpsCalculator();
        wordOpsHelper = new WordOpsHelper(calculator);
        this.sha256Algorithm = new Sha256AlgorithmImpl(calculator);
        this.data = data;
    }

    @Test
    public void simpleTest() {
        int[] hash = Sha256.hash(data);

        Bit[][] res = sha256Algorithm.hash(data);

        Assert.assertEquals(toHex(hash), wordOpsHelper.toHex(res));
    }

    @Test
    public void varBitTest() {
        int[] hash = Sha256.hash(data);

        Bit[][] constantDat = wordOpsHelper.toBitWordArray(wordOpsHelper.pad(data));
        Bit[][] bits = new Bit[constantDat.length][32];

        for (int i = 0; i < constantDat.length; i++) {
            for (int j = 0; j < constantDat[i].length; j++) {
                VarBit varBit = new VarBit("I[" + i + "][" + j + "]");
                varBit.setValue(constantDat[i][j].getValue());
                bits[i][j] = varBit;
            }
        }

        Bit[][] res = sha256Algorithm.hash(bits);

        Assert.assertEquals(toHex(hash), wordOpsHelper.toHex(res));
    }

}
