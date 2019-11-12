package ru.bolgar.kiko.rental.utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CollectionUtils {
    /**
     * Convert collection 'values' to HashMap by key
     *
     * @param values source collection
     * @param key    key function
     * @return HashMap
     */
    public static <K, V> Map<K, V> asHashMap(Iterable<V> values, Function<V, K> key) {
        Map<K, V> map = new HashMap<>();
        for (V value : values) {
            map.put(key.apply(value), value);
        }
        return map;
    }
}
