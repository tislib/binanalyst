package net.tislib.binanalyst.lib.util;

import java.util.Map;
import java.util.function.Function;
import net.tislib.binanalyst.lib.bit.Bit;

public class MapUtil {

    public static <T> T computeIfAbsent(Map<Bit, T> cache, Bit bit, Function<Bit, T> function) {
        if (!cache.containsKey(bit)) {
            cache.put(bit, function.apply(bit));
        }
        return cache.get(bit);
    }
}
