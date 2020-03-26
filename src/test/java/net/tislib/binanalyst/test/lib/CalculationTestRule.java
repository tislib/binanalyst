package net.tislib.binanalyst.test.lib;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import org.junit.rules.ErrorCollector;

import java.util.function.Function;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;
import static org.hamcrest.CoreMatchers.is;

public class CalculationTestRule extends ErrorCollector {
    private BitOpsGraphCalculator calculator;
    private Runnable onUpdate;

    public void checkTransformation(Function<Bit, Bit> operation, Function<Bit, Bit> expectedTransformation) {
        triggerOnUpdate();
        VarBit[] bits = VarBit.list("a", 1, ZERO);
        calculator.getInput().setBits(new VarBit[][]{bits});

        Bit result = operation.apply(bits[0]);

        Bit expected = expectedTransformation.apply(bits[0]);

        calculator.setOutputBits(new Bit[]{result});

        calculator.optimize();

        checkThat(result, is(expected));
    }

    private void triggerOnUpdate() {
        if (this.onUpdate != null) {
            this.onUpdate.run();
        }
    }

    public void checkTransformationResult(Function<Bit, Bit> operation, Function<Bit, String> expectedTransformationResult) {
        triggerOnUpdate();
        VarBit[] bits = VarBit.list("a", 1, ZERO);
        calculator.getInput().setBits(new VarBit[][]{bits});

        Bit result = operation.apply(bits[0]);

        CharSequence expected = expectedTransformationResult.apply(bits[0]);

        checkThat(result.toString(), is(expected));
    }

    public void setCalculator(BitOpsGraphCalculator calculator) {
        this.calculator = calculator;
    }

    public void setOnUpdate(Runnable onUpdate) {
        this.onUpdate = onUpdate;
    }

    public Runnable getOnUpdate() {
        return onUpdate;
    }
}
