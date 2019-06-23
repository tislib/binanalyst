package net.tislib.binanalyst.lib.util;

import java.util.Map;
import java.util.function.Function;
import net.tislib.binanalyst.lib.bit.Bit;

public class MapUtil {

    public static int computeIfAbsent(Map<Bit, Integer> cache, Bit bit, Function<Bit, Integer> function) {
        if (!cache.containsKey(bit)) {
            cache.put(bit, function.apply(bit));
        }
        return cache.get(bit);
    }
}
