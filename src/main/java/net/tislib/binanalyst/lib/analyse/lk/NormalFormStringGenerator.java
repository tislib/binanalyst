package net.tislib.binanalyst.lib.analyse.lk;

import net.tislib.binanalyst.lib.analyse.AnalyserUtil;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.Operation;

import java.util.*;
import java.util.stream.Collectors;

public class NormalFormStringGenerator implements LogicKeeper {

    private final Map<String, Set<Set<String>>> formulaCache = new HashMap<>();

    @Override
    public void keep(NamedBit namedBit) {
        if (namedBit instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) namedBit;
            Set<Set<Set<String>>> formulas = Arrays.stream(operationalBit.getBits())
                    .map(this::locateFormula)
                    .collect(Collectors.toSet());

            if (operationalBit.getOperation() == Operation.AND) {
                Set<Set<String>> formula = intersectFormulas(formulas);
                formulaCache.put(operationalBit.getName(), formula);
            } else if (operationalBit.getOperation() == Operation.OR) {
                Set<Set<String>> formula = variateFormulas(formulas);
                formulaCache.put(operationalBit.getName(), formula);
            } else if (operationalBit.getOperation() == Operation.NOT) {
                formulaCache.put(operationalBit.getName(), Collections.singleton(Collections.singleton(operationalBit.showFull(false))));
            } else {
                throw new RuntimeException("Unsupported operation: " + operationalBit.getOperation());
            }
        } else {
            formulaCache.put(namedBit.getName(), Collections.singleton(Collections.singleton(namedBit.getName())));
        }
    }

    private Set<Set<String>> variateFormulas(Set<Set<Set<String>>> formulas) {
        Set<Set<String>> result = new HashSet<>();
        for (Set<Set<String>> formula : formulas) {
            for (Set<String> variant : formula) {
                addVariation(result, variant);
            }
        }
        return result;
    }

    private void addVariation(Set<Set<String>> result, Set<String> variant) {
        if (result.contains(variant)) {
            return;
        }
        result.add(variant);
    }

    private Set<Set<String>> intersectFormulas(Set<Set<Set<String>>> formulas) {
        Set<Set<String>> checkingFormula = formulas.iterator().next();
        if (formulas.size() == 1) {
            return checkingFormula;
        }

        HashSet<Set<Set<String>>> rightFormulas = new HashSet<>(formulas);
        rightFormulas.remove(checkingFormula);
        Set<Set<String>> rightIntersection = intersectFormulas(rightFormulas);

        Set<Set<String>> result = new HashSet<>();

        for (Set<String> variant : checkingFormula) {
            for (Set<String> rightVariant : rightIntersection) {
                if (!AnalyserUtil.hasConflicts(variant, rightVariant)) {
                    Set<String> fullVariant = combineTwoFormula(variant, rightVariant);
                    addVariation(result, fullVariant);
                } else {
                    result.size();
                }
            }
        }

        return result;
    }

    private Set<String> combineTwoFormula(Set<String> variant, Set<String> rightVariant) {
        Set<String> fullVariant = new TreeSet<>(variant);
        fullVariant.addAll(rightVariant);
        return fullVariant;
    }

    private Set<Set<String>> locateFormula(NamedBit namedBit) {
        if (formulaCache.containsKey(namedBit.getName())) {
            return formulaCache.get(namedBit.getName());
        }

        return resolve(namedBit);
    }

    @Override
    public void showFormula(NamedBit namedBit) {
        Set<Set<String>> res = locateFormula(namedBit);
        res.forEach(System.out::println);
    }

    public void showAllFormulas() {
        Set<Set<String>> result = formulaCache.values().stream().reduce(new HashSet<>(), (a, b) -> {
            Set<Set<String>> res = new HashSet<>();
            res.addAll(a);
            res.addAll(b);
            return res;
        });

        result.forEach(System.out::println);
    }

    public Set<Set<String>> resolve(NamedBit namedBit) {
        keep(namedBit);

        return formulaCache.get(namedBit.getName());
    }
}
