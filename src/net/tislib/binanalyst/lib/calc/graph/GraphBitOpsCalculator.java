package net.tislib.binanalyst.lib.calc.graph;

import net.tislib.binanalyst.lib.bit.*;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.optimizer.ConstantCleaner;
import net.tislib.binanalyst.lib.calc.graph.optimizer.Optimizer;
import net.tislib.binanalyst.lib.calc.graph.optimizer.Transformer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static net.tislib.binanalyst.lib.BinValueHelper.printValues;

/**
 * Created by Taleh Ibrahimli on 2/5/18.
 * Email: me@talehibrahimli.com
 */
public class GraphBitOpsCalculator implements BitOpsCalculator {

    private final Layer<VarBit> input = new Layer<>("Input");
    private final Layer<OperationalBit> middle = new Layer<>("Middle");
    private final Layer<NamedBit> output = new Layer<>("Output");

    public final VarBit ZERO;

    public GraphBitOpsCalculator() {
        ZERO = new VarBit("ZERO");
        ZERO.setValue(false);
    }

    public Layer<VarBit> getInput() {
        return input;
    }

    public Layer<OperationalBit> getMiddle() {
        return middle;
    }

    public Layer<NamedBit> getOutput() {
        return output;
    }

    private List<Optimizer> optimizers = new ArrayList<>();

    private int operationCount = 0;

    public void setInputBits(VarBit[]... bits) {
        input.setBits(bits);
        input.addBits(ZERO);
    }

    public void setOutputBits(Bit[]... bitsArray) {
        NamedBit newBitsArray[][] = new NamedBit[bitsArray.length][];
        for (int i = 0; i < bitsArray.length; i++) {
            NamedBit[] result = resolveBits(bitsArray[i]);
            newBitsArray[i] = result;
        }
        output.setBits(newBitsArray, false);
    }

    private NamedBit[] resolveBits(Bit... bits) {
        NamedBit[] result = new NamedBit[bits.length];
        for (int i = 0; i < bits.length; i++) {
            if (bits[i] instanceof ConstantBit) {
                result[i] = bits[i].getValue() ? (NamedBit) not(ZERO) : ZERO;
            } else if (bits[i] instanceof NamedBit) {
                result[i] = (NamedBit) bits[i];
            } else {
                throw new UnsupportedOperationException("unsupported bit type");
            }
        }
        return result;
    }

    public Bit operation(Operation operation, NamedBit... bits) {
        NamedBit result = null;
        for (int i = 0; i < 10; i++) {
            if (result != null && result instanceof OperationalBit) {
                operation = ((OperationalBit) result).getOperation();
                bits = ((OperationalBit) result).getBits();
            }
            for (Optimizer optimizer : this.optimizers) {
                result = optimizer.optimize(this, operation, bits, result);
            }
        }
        if (result == null) {
            operationCount++;
            result = new OperationalBit(operation, bits);
            return middle.register((OperationalBit) result);
        }
        return result;
    }

    @Override
    public Bit xor(Bit... bits) {
        return operation(Operation.XOR, resolveBits(bits));
    }

    @Override
    public Bit and(Bit... bits) {
        return operation(Operation.AND, resolveBits(bits));
    }

    @Override
    public Bit or(Bit... bits) {
        return operation(Operation.OR, resolveBits(bits));
    }

    @Override
    public Bit not(Bit bit) {
        return operation(Operation.NOT, resolveBits(bit));
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
        input.show(false);
        middle.show(false);
        output.show(false);
    }

    public void showResult() {
        System.out.println("RESULT");
        printValues(output.getBits().toArray(new NamedBit[]{}));
    }

    public void calculate() {
        for (OperationalBit bit : middle) {
            bit.calculate();
        }
    }

    public void transform(Transformer transformer) {
        transformer.transform(input, LayerType.INPUT);
        transformer.transform(middle, LayerType.MIDDLE);
        transformer.transform(output, LayerType.OUTPUT);
    }

    public List<Optimizer> getOptimizers() {
        return optimizers;
    }

    public void clean() {
        //clean for constants
        for (OperationalBit bit : this.getMiddle().getBits()) {
            ConstantCleaner.clean(this, bit);
        }
        // clean for unused middles
        int removeCount;
        do {
            removeCount = 0;
            for (int i = 0; i < this.getMiddle().getBits().size(); i++) {
                OperationalBit bit = this.getMiddle().getBits().get(i);
                int usage = getUsage(this.getMiddle().getBits(), bit);
                if (!this.getOutput().contains(bit) && usage == 0) {
                    removeCount++;
                    this.getMiddle().remove(bit);
                }
            }
        } while (removeCount > 0);
    }

    private int getUsage(List<OperationalBit> bits, OperationalBit bit) {
        List<OperationalBit> result = new ArrayList<>();
        for (OperationalBit operationalBit : bits) {
            if (operationalBit.hasBit(bit)) {
                result.add(operationalBit);
            }
        }
        return result.size();
    }

    public enum LayerType {
        INPUT, MIDDLE, OUTPUT
    }
}
