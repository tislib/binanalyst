package net.tislib.binanalyst.lib.calc.graph.decorator;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Layer;
import net.tislib.binanalyst.lib.calc.graph.Operation;

import java.util.List;
import java.util.function.Function;

public class AbstractBitOpsGraphCalculatorDecorator implements BitOpsGraphCalculator {

    private final BitOpsGraphCalculator calculator;

    public AbstractBitOpsGraphCalculatorDecorator(BitOpsGraphCalculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public Layer<VarBit> getInput() {
        return calculator.getInput();
    }

    @Override
    public Layer<OperationalBit> getMiddle() {
        return calculator.getMiddle();
    }

    @Override
    public Layer<NamedBit> getOutput() {
        return calculator.getOutput();
    }

    @Override
    public void setInputBits(VarBit[]... bits) {
        calculator.setInputBits(bits);
    }

    @Override
    public void setInputBits(VarBit... bits) {
        calculator.setInputBits(bits);
    }

    @Override
    public void setOutputBits(Bit[]... bits) {
        calculator.setOutputBits(bits);
    }

    public Bit operation(Operation operation, NamedBit... bits) {
        switch (operation) {
            case NOT:
                return not(bits[0]);
            case AND:
                return and(bits);
            case OR:
                return or(bits);
            case XOR:
                return xor(bits);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int getOperationCount() {
        return calculator.getOperationCount();
    }

    @Override
    public void show() {
        calculator.show();
    }

    @Override
    public void calculate() {
        calculator.calculate();
    }

    @Override
    public long getReusedBitsCount() {
        return calculator.getReusedBitsCount();
    }

    @Override
    public void remake() {
        calculator.remake();
    }

    @Override
    public void replaceBit(NamedBit namedBit, NamedBit varBit) {
        calculator.replaceBit(namedBit, varBit);
    }

    @Override
    public void show(boolean showValues) {
        calculator.show(showValues);
    }

    @Override
    public void optimize() {
        calculator.optimize();
    }

    @Override
    public Bit locate(String name) {
        return calculator.locate(name);
    }

    @Override
    public void replace(Bit from, Bit to) {
        calculator.replace(from, to);
    }

    @Override
    public void remove(OperationalBit operationalBit) {
        calculator.remove(operationalBit);
    }

    @Override
    public void remove(List<OperationalBit> operationalBit) {
        calculator.remove(operationalBit);
    }

    @Override
    public BitOpsGraphCalculator copy() {
        return calculator.copy();
    }

    @Override
    public Bit xor(Bit... bits) {
        return calculator.xor(bits);
    }

    @Override
    public Bit and(Bit... bits) {
        return calculator.and(bits);
    }

    @Override
    public Bit or(Bit... bits) {
        return calculator.or(bits);
    }

    @Override
    public Bit not(Bit bit) {
        return calculator.not(bit);
    }

    @Override
    public Bit wrap(Number num) {
        return calculator.wrap(num);
    }

    public static BitOpsGraphCalculator chain(BitOpsGraphCalculator calculator, Function<BitOpsGraphCalculator, AbstractBitOpsGraphCalculatorDecorator>... decorators) {
        BitOpsGraphCalculator result = calculator;
        for (Function<BitOpsGraphCalculator, AbstractBitOpsGraphCalculatorDecorator> decorator : decorators) {
            result = decorator.apply(result);
        }
        return result;
    }

    public BitOpsGraphCalculator getOriginal() {
        if (calculator instanceof AbstractBitOpsGraphCalculatorDecorator) {
            return ((AbstractBitOpsGraphCalculatorDecorator) calculator).getOriginal();
        }
        return calculator;
    }
}
