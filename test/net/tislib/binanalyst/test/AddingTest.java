package net.tislib.binanalyst.test;

import static junit.framework.TestCase.assertTrue;

import net.tislib.binanalyst.lib.BinCalc;
import net.tislib.binanalyst.lib.BinValueHelper;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
@RunWith(Parameterized.class)
public class AddingTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {4332323, 7843451}, {23121, 13454}, {5462, 232131}, {54543, 2565}, {67674, 2313}, {434345, 4545455}, {6, 82313123}
        });
    }

    private final long a;
    private final long b;

    public AddingTest(long a, long b){
        this.a = a;
        this.b = b;
    }

    @Test
    public void test() {
        int l = BinValueHelper.binLength(a);

        for (int i = 0; i < l - 1; i++) {
            assertTrue(check(a, b, i));
        }
    }

    private static boolean check(long a, long b, int i) {

        long c = a + b;

        byte ai, bi, ci, ai1, bi1, ci1;

        ai = BinValueHelper.getBit(a, i);
        bi = BinValueHelper.getBit(b, i);
        ci = BinValueHelper.getBit(c, i);
        ai1 = BinValueHelper.getBit(a, i + 1);
        bi1 = BinValueHelper.getBit(b, i + 1);
        ci1 = BinValueHelper.getBit(c, i + 1);

        byte cip = BinCalc.getAddPosBit(BitOpsCalculator.getDefault(), ai, bi, ci, ai1, bi1);
        return cip == ci1;

    }

}
