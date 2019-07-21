package net.tislib.binanalyst.lib.analyse;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;

public class AnalyserUtil {

    public static long countConflicts(Collection<String> set1, Collection<String> set2) {
        long count = 0;
        for (String bit1 : set1) {
            if (hasConflict(set2, bit1)) count++;
        }
        return count;
    }

    public static boolean hasConflicts(Collection<String> set1, Collection<String> set2) {
        for (String bit1 : set1) {
            if (hasConflict(set2, bit1)) return true;
        }
        return false;
    }

    public static Set<String> findConflicts(Collection<String> set) {
        return findConflicts(set, set);
    }

    public static Set<String> findConflicts(Collection<String> set1, Collection<String> set2) {
        Set<String> result = new HashSet<>();
        for (String bit1 : set1) {
            if (hasConflict(set2, bit1) && !bit1.startsWith("!")) {
                result.add(bit1);
            }
        }

        return result;
    }

    public static boolean hasConflict(Collection<String> set, String bitName) {
        boolean isNot = bitName.startsWith("!");
        String bitReverse = isNot ? bitName.substring(1) : "!" + bitName;
        return set.contains(bitReverse);
    }

    public static boolean variationEquals(List<String> existingVariation, List<String> variant) {
        return existingVariation.stream().sorted().collect(Collectors.joining(","))
                .equals(
                        variant.stream().sorted().collect(Collectors.joining(","))
                );
    }

    public static Set<String> findConflicts(Set<NamedBit> bits) {
        Set<String> bitNames = bits.stream().map(item -> {
            if (item instanceof OperationalBit) {
                return ((OperationalBit) item).showFull(false);
            } else {
                return item.getName();
            }
        }).collect(Collectors.toSet());

        return findConflicts(bitNames);
    }

    public static Set<String> findConflicts(OperationalBit operationalBit) {
        return findConflicts(new HashSet<NamedBit>(Arrays.asList(operationalBit.getBits())));
    }

    public static boolean isConflicting(NamedBit bit, NamedBit truth) {
        String bitName = bit.getName();
        if (bit instanceof OperationalBit) {
            bitName = ((OperationalBit) bit).showFull(false);
        }
        return truth.getName().equals(negativate(bitName));
    }

    public static String negativate(String bitName) {
        boolean isNot = bitName.startsWith("!");
        return isNot ? bitName.substring(1) : "!" + bitName;
    }
}
