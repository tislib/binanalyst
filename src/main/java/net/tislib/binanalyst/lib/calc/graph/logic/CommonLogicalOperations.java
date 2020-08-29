package net.tislib.binanalyst.lib.calc.graph.logic;

import net.tislib.binanalyst.lib.bit.*;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.DoubleNotRemovalOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.UnusedBitOptimizerDecorator;

import java.util.*;
import java.util.stream.Collectors;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

public class CommonLogicalOperations {

    public static Bit mutate(NamedBit namedBit, BitOpsGraphCalculator calculator, NamedBit mutator, boolean truth) {
        if (namedBit instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) namedBit;
            NamedBit[] bits = new NamedBit[operationalBit.getBits().length];
            for (int i = 0; i < bits.length; i++) {
                bits[i] = (NamedBit) mutate(operationalBit.getBits()[i], calculator, mutator, truth);
            }
            return calculator.operation(operationalBit.getOperation(), bits);
        } else if (namedBit instanceof VarBit) {
            if (mutator == namedBit) {
                return truth ? ConstantBit.ONE : ConstantBit.ZERO;
            } else {
                return namedBit;
            }
        }

        return namedBit;
    }

    public static Bit toCnf(NamedBit namedBit, BitOpsGraphCalculator calculator) {
        if (namedBit instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) namedBit;
            List<NamedBit> newBits = convertToCnfParts(operationalBit, calculator);
            if (newBits.size() == 1) {
                return newBits.get(0);
            }

            // check for collapsing
            newBits = newBits.stream().map(item -> collapseCnf(calculator, item)).collect(Collectors.toList());

            Bit res = calculator.and(newBits.toArray(new NamedBit[0]));
            return res;
        } else {
            return namedBit;
        }
    }

    private static NamedBit collapseCnf(BitOpsGraphCalculator calculator, NamedBit item) {
        List<NamedBit> newBits = new ArrayList<>();
        if (item instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) item;
            for (NamedBit namedBit : operationalBit.getBits()) {
                if (newBits.contains(namedBit)) {
                    continue;
                }

                if (newBits.contains(calculator.not(namedBit))) {
                    return ONE;
                } else {
                    newBits.add(namedBit);
                }
            }
        } else {
            return item;
        }
        return item;
//        return (NamedBit) calculator.or(newBits.toArray(new NamedBit[0]));
    }

    private static List<NamedBit> convertToCnfParts(NamedBit namedBit, BitOpsGraphCalculator calculator) {
        if (!(namedBit instanceof OperationalBit)) {
            return Collections.singletonList(namedBit);
        }
        OperationalBit operationalBit = (OperationalBit) namedBit;
        List<NamedBit> newBits = new ArrayList<>();
        switch (operationalBit.getOperation()) {
            case AND:
                for (NamedBit bit : operationalBit.getBits()) {
                    Bit cnf = toCnf(bit, calculator);
                    if (cnf instanceof OperationalBit && ((OperationalBit) cnf).getOperation() == Operation.AND) {
                        newBits.addAll(Arrays.asList(((OperationalBit) cnf).getBits()));
                    } else {
                        newBits.add((NamedBit) cnf);
                    }
                }
                break;
            case OR:
                for (NamedBit bit : operationalBit.getBits()) {
                    Bit cnf = toCnf(bit, calculator);
                    List<NamedBit> right;
                    if (cnf instanceof OperationalBit && ((OperationalBit) cnf).getOperation() == Operation.AND) {
                        right = Arrays.asList(((OperationalBit) cnf).getBits());
                    } else {
                        right = Collections.singletonList((NamedBit) cnf);
                    }

                    if (newBits.size() == 0) {
                        newBits.addAll(right);
                    } else {
                        List<NamedBit> left = new ArrayList<>(newBits);
                        newBits.clear();

                        for (NamedBit l : left) {
                            for (NamedBit r : right) {
                                newBits.add((NamedBit) calculator.or(l, r));
                            }
                        }
                    }
                }
                break;
            case NOT:
                Bit cnf = toCnf(operationalBit.getBits()[0], calculator);
                if (cnf instanceof OperationalBit) {
                    OperationalBit oCnf = (OperationalBit) cnf;

                    NamedBit[] negatives = new NamedBit[oCnf.getBits().length];
                    for (int i = 0; i < oCnf.getBits().length; i++) {
                        negatives[i] = (NamedBit) calculator.not(oCnf.getBits()[i]);
                    }

                    switch (oCnf.getOperation()) {
                        case NOT:
                            return Collections.singletonList(oCnf.getBits()[0]);
                        case AND:
                            if (oCnf.getBits().length == 1) {
                                return Collections.singletonList((NamedBit) calculator.not(cnf));
                            }
                            return convertToCnfParts((OperationalBit) calculator.or(negatives), calculator);
                        case OR:
                            if (oCnf.getBits().length == 1) {
                                return Collections.singletonList((NamedBit) calculator.not(cnf));
                            }
                            return convertToCnfParts((OperationalBit) calculator.and(negatives), calculator);
                        default:
                            cnf = toCnf(operationalBit.getBits()[0], calculator);
                            return Collections.singletonList((NamedBit) calculator.not(cnf));
                    }
                } else {
                    return Collections.singletonList(operationalBit);
                }
            case XOR:
                NamedBit[] bits = operationalBit.getBits();
                if (bits.length == 0) {
                    return Collections.singletonList(ZERO);
                } else if (bits.length == 1) {
                    return Collections.singletonList(bits[0]);
                }
                Bit[] newBits2 = new Bit[bits.length - 1];
                System.arraycopy(bits, 0, newBits2, 0, bits.length - 1);

                NamedBit l = (NamedBit) toCnf((NamedBit) calculator.xor(newBits2), calculator);
                NamedBit r = bits[bits.length - 1];

                return convertToCnfParts((NamedBit) calculator.or(
                        calculator.and(l, calculator.not(r)),
                        calculator.and(r, calculator.not(l))
                ), calculator);
        }
        return newBits;
    }

    public static BitOpsGraphCalculator transformToLinearXorSat(BitOpsGraphCalculator calc) {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();

        calculator = new UnusedBitOptimizerDecorator(calculator);
        calculator = new DoubleNotRemovalOptimizationDecorator(calculator);

        // collect inputs
        Map<String, VarBit> inputMap = new HashMap<>();

        List<Bit> main = new ArrayList<>();

        for (OperationalBit operationalBit : calc.getMiddle()) {
            inputMap.put(operationalBit.getName(), fromBit(operationalBit));
            for (NamedBit namedBit : operationalBit.getBits()) {
                inputMap.put(namedBit.getName(), fromBit(namedBit));
            }
        }

        calculator.setInputBits(inputMap.values().toArray(new VarBit[0]));

        for (OperationalBit operationalBit : calc.getMiddle()) {
            NamedBit[] bits = new NamedBit[operationalBit.getBits().length];
            for (int i = 0; i < operationalBit.getBits().length; i++) {
                bits[i] = inputMap.get(operationalBit.getBits()[i].getName());
            }
            Bit newOp = calculator.operation(operationalBit.getOperation(), bits);

            main.add(calculator.xor(inputMap.get(operationalBit.getName()), calculator.not(newOp)));
        }
        main.add(inputMap.get(calc.getOutput().getBitL(0).getName()));

        calculator.setOutputBits(new Bit[]{calculator.and(main.toArray(new Bit[0]))});
        return calculator;
    }

    private static VarBit fromBit(NamedBit namedBit) {
        VarBit varBit = new VarBit();
        varBit.setName(namedBit.getName() + "R");
        return varBit;
    }

    public static boolean hasBitDeep(NamedBit namedBit, Bit mutation) {
        if (namedBit == mutation) {
            return true;
        }
        if (namedBit instanceof OperationalBit) {
            boolean found = false;
            for (NamedBit innerBit : ((OperationalBit) namedBit).getBits()) {
                found |= hasBitDeep(innerBit, mutation);
            }
            return found;
        } else {
            return false;
        }
    }

}
