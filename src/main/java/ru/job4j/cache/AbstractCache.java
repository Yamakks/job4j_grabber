package ru.job4j.cache;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCache<K, V> {

    private final Map<K, SoftReference<V>> cache = new HashMap<>();

    public final void put(K key, V value) {

        if (key == null || value == null) {
            throw new IllegalArgumentException("Значения key или value не могут быть !NULL!");
        }
        cache.put(key, new SoftReference<>(value));
    }

    public final V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Значение key не может быть !NULL!");
        }
        SoftReference<V> reference = cache.get(key);
        V value = (reference != null) ? reference.get() : null;
        if (value == null) {
            value = load(key);
        if (value != null) {
            put(key, value);
        }
    }
        return value;
    }

    protected abstract V load(K key);
}
