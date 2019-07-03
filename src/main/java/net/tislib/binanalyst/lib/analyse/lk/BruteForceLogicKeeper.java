package net.tislib.binanalyst.lib.analyse.lk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.tislib.binanalyst.lib.analyse.AnalyserUtil;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.Operation;

public class BruteForceLogicKeeper implements LogicKeeper {

    private final Map<String, List<List<String>>> formulaCache = new HashMap<>();

    @Override
    public void keep(NamedBit namedBit) {
        if (namedBit instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) namedBit;
            List<List<List<String>>> formulas = Arrays.stream(operationalBit.getBits())
                    .map(this::locateFormula)
                    .collect(Collectors.toList());

            if (operationalBit.getOperation() == Operation.AND) {
                List<List<String>> formula = intersectFormulas(formulas);
                formulaCache.put(operationalBit.getName(), formula);
            } else if (operationalBit.getOperation() == Operation.OR) {
                List<List<String>> formula = variateFormulas(formulas);
                formulaCache.put(operationalBit.getName(), formula);
            } else if (operationalBit.getOperation() == Operation.NOT) {
                formulaCache.put(operationalBit.getName(), Collections.singletonList(Collections.singletonList(operationalBit.showFull(false))));
            } else {
                throw new RuntimeException("Unsupported operation: " + operationalBit.getOperation());
            }
        } else {
            formulaCache.put(namedBit.getName(), Collections.singletonList(Collections.singletonList(namedBit.getName())));
        }
    }

    private List<List<String>> variateFormulas(List<List<List<String>>> formulas) {
        List<List<String>> result = new ArrayList<>();
        for (List<List<String>> formula : formulas) {
            for (List<String> variant : formula) {
                addVariation(result, variant);
            }
        }
        return result;
    }

    private void addVariation(List<List<String>> result, List<String> variant) {
        for (List<String> existingVariation : result) {
            if(AnalyserUtil.variationEquals(existingVariation, variant)) {
                return;
            }
        }
        result.add(variant);
    }

    private List<List<String>> intersectFormulas(List<List<List<String>>> formulas) {
        List<List<String>> checkingFormula = formulas.get(0);
        if (formulas.size() == 1) {
            return checkingFormula;
        }

        List<List<String>> result = new ArrayList<>();

        for (List<String> variant : checkingFormula) {
            List<List<String>> rightIntersection = intersectFormulas(formulas.subList(1, formulas.size()));
            for (List<String> rightVariant : rightIntersection) {
                if (!AnalyserUtil.hasConflicts(variant, rightVariant)) {
                    List<String> fullVariant = combineTwoFormula(variant, rightVariant);
                    addVariation(result, fullVariant);
                } else {
                    result.size();
                }
            }
        }

        return result;
    }

    private List<String> combineTwoFormula(List<String> variant, List<String> rightVariant) {
        List<String> fullVariant = new ArrayList<>(variant);
        for (String elem : rightVariant) {
            if (!fullVariant.contains(elem)) {
                fullVariant.add(elem);
            }
        }
        return fullVariant;
    }

    private List<List<String>> locateFormula(NamedBit namedBit) {
        if (formulaCache.containsKey(namedBit.getName())) {
            return formulaCache.get(namedBit.getName());
        }
        throw new RuntimeException("formula not found: " + namedBit.getName());
    }

    @Override
    public void showFormula(NamedBit namedBit) {
        List<List<String>> res = locateFormula(namedBit);
        System.out.println(res);
    }
}
