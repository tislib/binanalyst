package net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.pattern;

import net.tislib.binanalyst.lib.bit.*;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;
import net.tislib.binanalyst.lib.calc.graph.decorator.OptimizerGraphCalculatorDecorator;

import java.util.*;

public class PatternOptimizationDecorator extends OptimizerGraphCalculatorDecorator {

    private static final Pattern[] DEFAULT_PATTERNS = {
            Pattern.compile("!(!a)=>a"), // remove double negative
            Pattern.compile("a|(!a)=>1"),
            Pattern.compile("(!a)|a=>1"),
            Pattern.compile("a&(!a)=>0"),
            Pattern.compile("(!a)&a=>0"),
            Pattern.compile("(a^(!a))e=>1"),
            Pattern.compile("(a^a)e=>0"),

            Pattern.compile("(a&)e=>a"),
            Pattern.compile("(a|)e=>a"),
            Pattern.compile("(a^)e=>a"),

            Pattern.compile("(a|a)e=>a"),
            Pattern.compile("(a&a)e=>a"),

            Pattern.compile("(a&0)=>0"),
            Pattern.compile("(a|0)e=>a"),
            Pattern.compile("(a^0)e=>a"),

            Pattern.compile("(0&a)=>0"),
            Pattern.compile("(0|a)e=>a"),
            Pattern.compile("(0^a)e=>a"),

            Pattern.compile("(a&1)=>a"),
            Pattern.compile("(a|1)e=>1"),
            Pattern.compile("(a^1)e=>!a"),

            Pattern.compile("(1&a)=>a"),
            Pattern.compile("(1|a)e=>1"),
            Pattern.compile("(1^a)e=>!a"),


            // new
            Pattern.compile("(a&b)&(!a)=>0"),
            Pattern.compile("(a&b)&(!b)=>0"),
            Pattern.compile("a&(a&b)=>a"),
    };
    private final Pattern[] patterns;

    public PatternOptimizationDecorator(BitOpsGraphCalculator calculator, Pattern... patterns) {
        super(calculator);
        this.patterns = patterns;
    }

    public PatternOptimizationDecorator(BitOpsGraphCalculator calculator) {
        this(calculator, DEFAULT_PATTERNS);
    }

    @Override
    protected Bit optimize(Operation operation, Bit... bits) {
        Bit oBit = delegate(operation, bits);
        return optimize(oBit);
    }

    private Bit optimize(Bit oBit) {
        if (oBit instanceof OperationalBit) {
            for (Pattern pattern : patterns) {
                Map<String, NamedBit> data = new HashMap<>();
                data.put("0", (NamedBit) ConstantBit.ZERO);
                data.put("1", (NamedBit) original.not(ConstantBit.ZERO));
                String name = ((OperationalBit) oBit).getName();
                boolean result = internal(pattern.structure, (OperationalBit) oBit, data);
                if (!result) {
                    continue;
                }
                return optimize(transform(pattern.transform, data));
            }
        }

        return oBit;
    }

    private Bit transform(Pattern.PatternStructure transform, Map<String, NamedBit> data) {
        if (transform.operation == null) { // varBit
            return data.get(transform.name);
        }
        return null;
    }

    private boolean internal(Pattern.PatternStructure structure, NamedBit namedBit, Map<String, NamedBit> data) {
        if (!(namedBit instanceof OperationalBit)) {
            return false;
        }
        OperationalBit operationalBit = (OperationalBit) namedBit;
        if (structure.operation != operationalBit.getOperation()) {
            return false;
        }

        Set<Integer> processedItems = new HashSet<>();

        if (structure.flag == 'e') {
            if (structure.items.size() != operationalBit.getBits().length) {
                return false;
            }
        }

        for (int i = 0; i < structure.items.size(); i++) {
            boolean processed = false;
            for (int j = 0; j < operationalBit.getBits().length; j++) {
                NamedBit oBit = operationalBit.getBits()[j];
                if (processedItems.contains(j)) {
                    continue;
                }

                if (data.containsKey(structure.items.get(i).name)) {
                    if (oBit.equals(data.get(structure.items.get(i).name))) {
                        processedItems.add(j);
                        processed = true;
                        break;
                    }
                }

                boolean isStructureVarBit = structure.items.get(i).operation == null;
                if (isStructureVarBit) {
                    if (data.containsKey(structure.items.get(i).name)) {
                        if (!data.get(structure.items.get(i).name).equals(oBit)) {
                            continue;
                        }
                    }
                    data.put(structure.items.get(i).name, oBit);
                } else if (!internal(structure.items.get(i), oBit, data)) {
                    continue;
                }
                processedItems.add(j);
                processed = true;
                break;
            }

            if (!processed) {
                return false;
            }
        }

        return true;
    }
}
