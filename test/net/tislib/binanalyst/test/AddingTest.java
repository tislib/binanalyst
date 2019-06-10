package net.tislib.binanalyst.test;

import static junit.framework.TestCase.assertTrue;
import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.BinValueHelper.toLong;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import net.tislib.binanalyst.lib.BinCalc;
import net.tislib.binanalyst.lib.BinValueHelper;
import net.tislib.binanalyst.lib.bit.ConstantBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;
import net.tislib.binanalyst.lib.operator.BinAdd;
import net.tislib.binanalyst.lib.operator.BinAdd2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
@RunWith(Parameterized.class)
public class AddingTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {4332323, 7843451, 12313, 324234},
                {23121, 13454, 324234, 12321312},
                {5462, 232131, 4562626, 5464562},
                {54543, 2565, 234234, 53451341},
                {67674, 2313, 435435, 24324},
                {434345, 4545455, 345345, 6546456},
                {6, 82313123, 345345435, 234243}
        });
    }

    private final long a;
    private final long b;
    private final long c;
    private final long d;

    public AddingTest(long a, long b, long c, long d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Test
    public void test() {
        int l = BinValueHelper.binLength(a);

        for (int i = 0; i < l - 1; i++) {
            assertTrue(check(a, b, i));
        }
    }

    @Test
    public void binCalcAdd() {
        int l = BinValueHelper.binLength(a) + BinValueHelper.binLength(b) + BinValueHelper.binLength(c) + BinValueHelper.binLength(d);
        VarBit[] aBits = VarBit.list("a", l, ConstantBit.ZERO);
        VarBit[] bBits = VarBit.list("a", l, ConstantBit.ZERO);
        VarBit[] cBits = VarBit.list("c", l, ConstantBit.ZERO);
        VarBit[] dBits = VarBit.list("d", l, ConstantBit.ZERO);

        setVal(aBits, a);
        setVal(bBits, b);
        setVal(cBits, c);
        setVal(dBits, d);

        assertEquals(toLong(BinAdd.add(BitOpsCalculator.getDefault(), aBits, bBits, cBits, dBits)).longValue(), a + b + c + d);
    }

    @Test
    public void binCalcAdd2() {
        int l = BinValueHelper.binLength(a) + BinValueHelper.binLength(b) + BinValueHelper.binLength(c) + BinValueHelper.binLength(d);
        VarBit[] aBits = VarBit.list("a", l, ConstantBit.ZERO);
        VarBit[] bBits = VarBit.list("a", l, ConstantBit.ZERO);
        VarBit[] cBits = VarBit.list("c", l, ConstantBit.ZERO);
        VarBit[] dBits = VarBit.list("d", l, ConstantBit.ZERO);

        setVal(aBits, a);
        setVal(bBits, b);
        setVal(cBits, c);
        setVal(dBits, d);

        assertEquals(toLong(BinAdd2.add(BitOpsCalculator.getDefault(), aBits, bBits, cBits, dBits)).longValue(), a + b + c + d);
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
