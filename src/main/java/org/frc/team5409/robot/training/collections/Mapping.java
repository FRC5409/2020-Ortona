package org.frc.team5409.robot.training.collections;

import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Mapping<K, V> {
    private final Map<K, V> _forward;
    private final Map<V, K> _reverse;

    public static <K, V> Mapping<K, V> of() {
        return new Mapping<>(Collections.emptySet());
    }

    public static <K, V> Mapping<K, V> of(K k1, V v1) {
        return new Mapping<>(Collections.singleton(Entry.of(k1, v1)));
    }

    public static <K, V> Mapping<K, V> of(K k1, V v1, K k2, V v2) {
        return from(k1, v1, k2, v2);
    }

    public static <K, V> Mapping<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return from(k1, v1, k2, v2, k3, v3);
    }

    public static <K, V> Mapping<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return from(k1, v1, k2, v2, k3, v3, k4, v4);
    }

    public static <K, V> Mapping<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return from(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
    }

    public static <K, V> Mapping<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        return from(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6);
    }

    public static <K, V> Mapping<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5,
                               K k6, V v6, K k7, V v7) {
        return from(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7);
    }

    public static <K, V> Mapping<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5,
                               K k6, V v6, K k7, V v7, K k8, V v8) {
        return from(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8);
    }

    public static <K, V> Mapping<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5,
                               K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        return from(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9);
    }

    public static <K, V> Mapping<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5,
                               K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        return from(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10);
    }

    @SuppressWarnings("unchecked")
    private static <K, V> Mapping<K, V> from(Object... args) {
        assert(args.length % 2 == 0);

        Set<Entry<K, V>> entries = new HashSet<>();
        for (int i = 0; i < args.length; i+=2) {
            entries.add(Entry.of((K) args[i], (V) args[i+1]));
        }
        
        return new Mapping<>(entries);
    }

    public Mapping(Set<Entry<K, V>> entries) {
        Map<K, V> forward = new HashMap<>();
        Map<V, K> reverse = new HashMap<>();

        for (Entry<K, V> entry : entries) {
            if (forward.containsKey(entry.key())) {
                throw new IllegalArgumentException("Key collision at " + entry.key());
            } else if (reverse.containsKey(entry.value())) {
                throw new IllegalArgumentException("Value collision at " + entry.value());
            }

            forward.put(entry.key(), entry.value());
            reverse.put(entry.value(), entry.key());
        }

        _forward = Collections.unmodifiableMap(forward);
        _reverse = Collections.unmodifiableMap(reverse);
    }

    @Nullable
    public V forward(K value) {
        return _forward.get(value);
    }

    @Nullable
    public K reverse(V value) {
        return _reverse.get(value);
    }

    public boolean contains(Object value) {
        return _forward.containsKey(value) || _forward.containsValue(value);
    }

    public boolean containsForward(K value) {
        return _forward.containsKey(value);
    }

    public boolean containsReverse(V value) {
        return _reverse.containsKey(value);
    }
}
