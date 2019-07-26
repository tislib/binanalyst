package net.tislib.binanalyst.lib.calc.graph;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;

public interface BitOpsGraphCalculator extends BitOpsCalculator {
    Layer<VarBit> getInput();

    Layer<OperationalBit> getMiddle();

    Layer<NamedBit> getOutput();

    void setInputBits(VarBit[]... bits);

    void setInputBits(VarBit... bits);

    void setOutputBits(Bit[]... bitsArray);

    Bit operation(Operation operation, NamedBit... bits);

    int getOperationCount();

    void show();

    void calculate();

    long getReusedBitsCount();

    void remake();

    void replaceBit(NamedBit namedBit, NamedBit varBit);

    void show(boolean showValues);

    void optimize();
}
