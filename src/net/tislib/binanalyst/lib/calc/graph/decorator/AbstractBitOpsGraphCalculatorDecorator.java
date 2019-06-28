package net.tislib.binanalyst.lib.calc.graph.decorator;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Layer;
import net.tislib.binanalyst.lib.calc.graph.Operation;

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

    @Override
    public Bit operation(Operation operation, NamedBit... bits) {
        return calculator.operation(operation, bits);
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
}
