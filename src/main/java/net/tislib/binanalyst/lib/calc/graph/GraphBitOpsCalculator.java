package net.tislib.binanalyst.lib.calc.graph;

import static net.tislib.binanalyst.lib.BinValueHelper.printValues;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.ConstantBit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.optimizer.ConstantCleaner;
import net.tislib.binanalyst.lib.calc.graph.optimizer.Optimizer;
import net.tislib.binanalyst.lib.calc.graph.optimizer.Transformer;
import net.tislib.binanalyst.lib.calc.graph.tools.GraphCalculatorTools;

/**
 * Created by Taleh Ibrahimli on 2/5/18.
 * Email: me@talehibrahimli.com
 */
public class GraphBitOpsCalculator implements BitOpsGraphCalculator {

    private final Layer<VarBit> input = new Layer<>("Input");
    private final Layer<OperationalBit> middle = new Layer<>("Middle");
    private final Layer<NamedBit> output = new Layer<>("Output");

    private final Map<String, OperationalBit> middleBitCache = new HashMap<>();
    private long reusedBitsCount;
    private int operationCount = 0;

    @Override
    public Layer<VarBit> getInput() {
        return input;
    }

    @Override
    public Layer<OperationalBit> getMiddle() {
        return middle;
    }

    @Override
    public Layer<NamedBit> getOutput() {
        return output;
    }

    @Override
    public void setInputBits(VarBit[]... bits) {
        input.setBits(bits);
    }

    @Override
    public void setInputBits(VarBit... bits) {
        input.setBits(new VarBit[][]{bits});
    }

    @Override
    public void setOutputBits(Bit[]... bitsArray) {
        NamedBit[][] newBitsArray = new NamedBit[bitsArray.length][];
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
                result[i] = bits[i].getValue().isTrue() ? (NamedBit) not(ZERO) : (NamedBit) ZERO;
            } else if (bits[i] instanceof NamedBit) {
                result[i] = (NamedBit) bits[i];
            } else {
                throw new UnsupportedOperationException("unsupported bit type");
            }
        }
        return result;
    }

    @Override
    public Bit operation(Operation operation, NamedBit... bits) {
        NamedBit result = null;
        for (int i = 0; i < 10; i++) {
            if (result instanceof OperationalBit) {
                operation = ((OperationalBit) result).getOperation();
                bits = ((OperationalBit) result).getBits();
            }
        }
        if (result == null) {
            OperationalBit newBit = new OperationalBit(operation, bits);
            if (middleBitCache.containsKey(newBit.toString())) {
                reusedBitsCount++;
                return middleBitCache.get(newBit.toString());
            }
            middleBitCache.put(newBit.toString(), newBit);
            return middle.register(newBit);
        }
        return result;
    }

    public void optimize() {
        // unused method
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
        return num.longValue() == 0 ? ZERO : ONE;
    }

    @Override
    public int getOperationCount() {
        return operationCount;
    }

    @Override
    public void show() {
        show(false);
    }

    @Override
    public void show(boolean showValues) {
        input.show(showValues);
        middle.show(showValues);
        output.show(showValues);

//        System.out.println("MIDDLE SIZE: " + this.getMiddle().getBits().size());
//        System.out.println("DEPTH: " + GraphCalculatorTools.getMaxDepth(this));
    }

    public void showResult() {
        System.out.println("RESULT");
        printValues(output.getBits().toArray(new NamedBit[]{}));
    }

    @Override
    public void calculate() {
        for (OperationalBit bit : middle) {
            bit.calculate();
            operationCount++;
        }
    }

    private void reIndex() {
        //reindex
        for (int i = 0; i < this.getMiddle().getBits().size(); i++) {
            this.getMiddle().getBits().get(i).setName("M[" + i + "]");
        }
        this.getMiddle().rename();
    }

    public void transform(Transformer transformer) {
        transformer.transform(input, LayerType.INPUT);
        transformer.transform(middle, LayerType.MIDDLE);
        transformer.transform(output, LayerType.OUTPUT);
    }

    public List<Optimizer> getOptimizers() {
        throw new UnsupportedOperationException();
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

    public boolean isFictiveBit(OperationalBit item) {
        return this.getMiddle().getBits().stream().noneMatch(oBit -> oBit != item &&
                Arrays.asList(oBit.getBits()).contains(item))
                &&
                !this.getOutput().getBits().contains(item);
    }

    public void showOutputFull() {
        System.out.println("FULL OUTPUT");
        this.getOutput().forEach(item -> {
            if (item instanceof OperationalBit) {
                System.out.println(((OperationalBit) item).showFull());
            } else {
                System.out.println(item);
            }
        });
    }

    @Override
    public long getReusedBitsCount() {
        return reusedBitsCount;
    }

    @Override
    public void remake() {

    }

    @Override
    public void replaceBit(NamedBit namedBit, NamedBit varBit) {
        for (OperationalBit operationalBit : getMiddle()) {
            if (operationalBit.hasBit(namedBit)) {
                operationalBit.replaceBit(namedBit, varBit);
            }
        }
    }

    public enum LayerType {
        INPUT, MIDDLE, OUTPUT
    }
}
