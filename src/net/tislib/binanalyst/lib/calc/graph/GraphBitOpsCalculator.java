package net.tislib.binanalyst.lib.calc.graph;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.CompositeBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

/**
 * Created by Taleh Ibrahimli on 2/5/18.
 * Email: me@talehibrahimli.com
 */
public class GraphBitOpsCalculator implements BitOpsCalculator {

    private final Layer input = new Layer("input");
    private final Layer middle = new Layer("middle");
    private final Layer output = new Layer("input");

    private int operationCount = 0;

    public void setInputBits(VarBit[]... bits) {
        input.setBits(bits);
    }

    public void setOutputBits(Bit... bits) {

    }

    @Override
    public Bit xor(Bit... bits) {
        operationCount += 5;
        for (Bit bit : bits) {
            Integer id = getBitId(bit);
        }
        CompositeBit compositeBit = new CompositeBit();
        for (Bit bit : bits) {
            if (bit.getValue()) compositeBit.setValue(!compositeBit.getValue());
        }
        return compositeBit;
    }

    private Integer getBitId(Bit bit) {
        Integer id = input.getBitId(bit);
        return null;
    }

    @Override
    public Bit and(Bit... bits) {
        operationCount++;
        for (Bit bit : bits) {
            if (bit == null) {
                throw new RuntimeException();
            }
            if (!bit.getValue()) {
                return ZERO;
            }
        }
        return ONE;
    }

    @Override
    public Bit or(Bit... bits) {
        operationCount++;
        for (Bit bit : bits) {
            if (bit.getValue()) {
                return ONE;
            }
        }
        return ZERO;
    }

    @Override
    public Bit not(Bit bit) {
        operationCount++;
        return new CompositeBit(!bit.getValue());
    }

    @Override
    public Bit equal(Bit... bits) {
        operationCount += 5;
        for (Bit bit : bits) {
            if (xor(bits[0], bit).getValue()) return ZERO;
        }
        return ONE;
    }

    @Override
    public Bit wrap(Number num) {
        return num.longValue() == 0 ? ZERO : ONE;
    }

    public void saveState(OutputStream outputStream) throws JAXBException {
        GraphCalculatorState state = getState();

        JAXBContext jc = JAXBContext.newInstance(GraphCalculatorState.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(state, outputStream);
    }

    private GraphCalculatorState getState() {
        GraphCalculatorState graphCalculatorState = new GraphCalculatorState();
        for (Bit bit : input) {
            VarBit varBit = (VarBit) bit;
            GraphCalculatorState.BitInfo bitInfo = new GraphCalculatorState.BitInfo();
            bitInfo.setName(bit.toString());
            graphCalculatorState.getInput().getBit().add(bitInfo);
        }
        return graphCalculatorState;
    }

    public void loadState(InputStream inputStream) {

    }

    public int getOperationCount() {
        return operationCount;
    }
}
