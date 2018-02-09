package net.tislib.binanalyst.lib.calc.graph;

import net.tislib.binanalyst.lib.bit.*;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

import static net.tislib.binanalyst.lib.BinValueHelper.print;
import static net.tislib.binanalyst.lib.BinValueHelper.printValues;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

/**
 * Created by Taleh Ibrahimli on 2/5/18.
 * Email: me@talehibrahimli.com
 */
public class GraphBitOpsCalculator implements BitOpsCalculator {

    private final Layer<VarBit> input = new Layer<>("Input");
    private final Layer<OperationalBit> middle = new Layer<>("Middle");
    private final Layer<NamedBit> output = new Layer<>("Output");

    private final VarBit ONE;
    private final VarBit ZERO;

    public GraphBitOpsCalculator() {
        ONE = new VarBit("ONE");
        ZERO = new VarBit("ZERO");
        ONE.setValue(true);
        ZERO.setValue(false);
    }

    private int operationCount = 0;

    public void setInputBits(VarBit[]... bits) {
        input.setBits(bits);
        input.addBits(ZERO, ONE);
    }

    public void init() {

    }

    public void setOutputBits(Bit[]... bitsArray) {
        NamedBit newBitsArray[][] = new NamedBit[bitsArray.length][];
        for(int i=0;i<bitsArray.length;i++){
            NamedBit[] result = resolveBits(bitsArray[i]);
            newBitsArray[i] = result;
        }
        output.setBits(newBitsArray);
    }

    private NamedBit[] resolveBits(Bit... bits) {
        NamedBit[] result = new NamedBit[bits.length];
        for (int i = 0; i < bits.length; i++) {
            if (bits[i] instanceof ConstantBit) {
                result[i] = bits[i].getValue() ? ONE : ZERO;
            } else if (bits[i] instanceof NamedBit) {
                result[i] = (NamedBit) bits[i];
            } else {
                throw new UnsupportedOperationException("unsupported bit type");
            }
        }
        return result;
    }

    public Bit operation(Operation operation, NamedBit... bits) {
//        switch (operation) {
//            case AND:
//                if (contains(bits, ZERO)) return ZERO;
//                if (contains(bits, ONE)) {
//                    return and(remove(bits, ONE));
//                }
//            case OR:
//                if (contains(bits, ONE)) return ONE;
//                if (contains(bits, ZERO)) {
//                    return and(remove(bits, ZERO));
//                }
//            case XOR:
//                if (contains(bits, ZERO)) {
//                    return and(remove(bits, ZERO));
//                }
//            case NOT:
//                if (contains(bits, ZERO)) {
//                    return ONE;
//                }
//                if (contains(bits, ONE)) {
//                    return ZERO;
//                }
//        }
        operationCount++;
        OperationalBit result = new OperationalBit(operation, bits);
        middle.register(result);
        return result;
    }

    private Bit[] remove(NamedBit[] bits, VarBit bit) {
        List<Bit> namedBits = new ArrayList<>();
        for (NamedBit namedBit : bits) {
            if (!namedBit.equals(bit)) namedBits.add(namedBit);
        }
        return namedBits.toArray(new Bit[]{});
    }

    private boolean contains(NamedBit[] bits, Bit bit) {
        for (NamedBit namedBit : bits) {
            if (namedBit.equals(bit)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Bit xor(Bit... bits) {
        if (bits.length == 0) {
            throw new UnsupportedOperationException("empty operation");
        }
        if (bits.length == 1) {
            return bits[0];
        }
        return operation(Operation.XOR, resolveBits(bits));
    }

    @Override
    public Bit and(Bit... bits) {
        if (bits.length == 0) {
            return ZERO;
        }
        if (bits.length == 1) {
            return bits[0];
        }
        return operation(Operation.AND, resolveBits(bits));
    }

    @Override
    public Bit or(Bit... bits) {
        if (bits.length == 0) {
            return ONE;
        }
        if (bits.length == 1) {
            return bits[0];
        }
        return operation(Operation.OR, resolveBits(bits));
    }

    @Override
    public Bit not(Bit bit) {
        Bit result = operation(Operation.NOT, resolveBits(bit));
        return result;
    }

    @Override
    public Bit wrap(Number num) {
        throw new UnsupportedOperationException("wrapping is not supported for " + this.getClass().getName());
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

    public void show() {
        System.out.println("INPUT:");
        for (NamedBit bit : input) {
            System.out.println(bit.toString() + " => " + bit.getValue());
        }
        System.out.println("MIDDLE:");
        for (NamedBit bit : middle) {
            System.out.println(bit.toString() + " => " + bit.getValue());
        }
        System.out.println("OUTPUT:");
        for (NamedBit bit : output) {
            System.out.println(bit.toString() + " => " + bit.getValue());
        }
        System.out.println("RESULT");
        printValues(output.getBits().toArray(new NamedBit[]{}));
    }

    public void calculate() {
        for (OperationalBit bit : middle) {
            bit.calculate();
        }
    }
}
