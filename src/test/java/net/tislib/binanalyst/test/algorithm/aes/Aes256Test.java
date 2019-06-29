package net.tislib.binanalyst.test.algorithm.aes;

import static net.tislib.binanalyst.lib.WordOpsHelper.toHex;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import net.tislib.binanalyst.lib.WordOpsHelper;
import net.tislib.binanalyst.lib.algorithms.aes.AES;
import net.tislib.binanalyst.lib.algorithms.aes.AesAlgorithm;
import net.tislib.binanalyst.lib.algorithms.aes.AesAlgorithmImpl;
import net.tislib.binanalyst.lib.algorithms.sha.sha256.Sha256;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.SimpleBitOpsCalculator;
import net.tislib.binanalyst.test.TestData;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
@Ignore
public class Aes256Test {
    private static final String ENC_KEY = "dk2390dk9023d0923kd908h5";
    private final byte[] data;
    private final AesAlgorithm aesAlgorithmImpl;
    private final WordOpsHelper wordOpsHelper;
    private final SimpleBitOpsCalculator calculator;

    public Aes256Test(byte[] data) {
        calculator = new SimpleBitOpsCalculator();
        wordOpsHelper = new WordOpsHelper(calculator);
        this.aesAlgorithmImpl = new AesAlgorithmImpl(calculator);
        this.data = data;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws IOException {
        List<Object[]> data = TestData.testPairData();

        data = data.stream().map(item -> new Object[]{("" + item[0] + item[1]).getBytes()}).collect(Collectors.toList());

        data.add(new Object[]{Files.readAllBytes(Paths.get(Aes256Test.class.getClassLoader().getResource("randfile1").getPath()))});
        data.add(new Object[]{Files.readAllBytes(Paths.get(Aes256Test.class.getClassLoader().getResource("randfile2").getPath()))});
        data.add(new Object[]{Files.readAllBytes(Paths.get(Aes256Test.class.getClassLoader().getResource("randfile3").getPath()))});

        return data;
    }

    @Test
    public void simpleTest() {
        AES aes = new AES();
        aes.setKey(ENC_KEY);

        byte[] encData = aes.encrypt(data);

        Bit[][] res = aesAlgorithmImpl.encrypt(data);

        Assert.assertEquals(toHex(encData), wordOpsHelper.toHex(res));
    }
}
