package net.tislib.binanalyst.lib.analyse.lk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;
import net.tislib.binanalyst.lib.calc.graph.decorator.AndOrCalculatorDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.BinderOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.UnusedBitOptimizerDecorator;

public class SimpleLogicKeeper implements LogicKeeper {

    public SimpleLogicKeeper() {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();
        calculator = new BinderOptimizationDecorator(calculator);
        calculator = new AndOrCalculatorDecorator(calculator, true);
        calculator = new SimpleOptimizationDecorator(calculator);
        calculator = new UnusedBitOptimizerDecorator(calculator);
    }

    private final Map<String, List<List<Bit>>> formulaCache = new HashMap<>();

    @SuppressWarnings("Duplicates")
    @Override
    public void keep(NamedBit namedBit) {
        if (namedBit instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) namedBit;
            List<List<List<Bit>>> formulas = Arrays.stream(operationalBit.getBits())
                    .map(this::locateFormula)
                    .collect(Collectors.toList());

            if (operationalBit.getOperation() == Operation.AND) {
                List<List<Bit>> formula = intersectFormulas(formulas);
                formulaCache.put(operationalBit.getName(), formula);
            } else if (operationalBit.getOperation() == Operation.OR) {
                List<List<Bit>> formula = variateFormulas(formulas);
                formulaCache.put(operationalBit.getName(), formula);
            } else if (operationalBit.getOperation() == Operation.NOT) {
                formulaCache.put(operationalBit.getName(), Collections.singletonList(Collections.singletonList(operationalBit)));
            } else {
                throw new RuntimeException("Unsupported operation: " + operationalBit.getOperation());
            }
        } else {
            formulaCache.put(namedBit.getName(), Collections.singletonList(Collections.singletonList(namedBit)));
        }
    }

    private List<List<Bit>> variateFormulas(List<List<List<Bit>>> formulas) {
        List<List<Bit>> result = new ArrayList<>();
        for (List<List<Bit>> formula : formulas) {
            for (List<Bit> variant : formula) {
                addVariation(result, variant);
            }

        }
        return result;
    }

    private void addVariation(List<List<Bit>> result, List<Bit> variant) {
//        for (List<Bit> existingVariation : result) {
//            if(AnalyserUtil.variationEquals(existingVariation, variant)) {
//                return;
//            }
//        }
        result.add(variant);
    }

    private List<List<Bit>> intersectFormulas(List<List<List<Bit>>> formulas) {
        List<List<Bit>> checkingFormula = formulas.get(0);
        if (formulas.size() == 1) {
            return checkingFormula;
        }

        List<List<Bit>> result = new ArrayList<>();

        for (List<Bit> variant : checkingFormula) {
            List<List<Bit>> rightIntersection = intersectFormulas(formulas.subList(1, formulas.size()));
            for (List<Bit> rightVariant : rightIntersection) {
//                if (!AnalyserUtil.hasConflicts(variant, rightVariant)) {
//                    List<Bit> fullVariant = combineTwoFormula(variant, rightVariant);
//                    addVariation(result, fullVariant);
//                } else {
//                    result.size();
//                }
            }
        }

        return result;
    }

    private List<Bit> combineTwoFormula(List<Bit> variant, List<Bit> rightVariant) {
        List<Bit> fullVariant = new ArrayList<>(variant);
        for (Bit elem : rightVariant) {
            if (!fullVariant.contains(elem)) {
                fullVariant.add(elem);
            }
        }
        return fullVariant;
    }

    private List<List<Bit>> locateFormula(NamedBit namedBit) {
        if (formulaCache.containsKey(namedBit.getName())) {
            return formulaCache.get(namedBit.getName());
        }
        throw new RuntimeException("formula not found: " + namedBit.getName());
    }

    @Override
    public void showFormula(NamedBit namedBit) {
        List<List<Bit>> res = locateFormula(namedBit);
        System.out.println(res);
    }
}
