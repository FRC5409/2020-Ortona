package org.frc.team5409.robot.training.collections;

import java.util.Map;
import java.util.Objects;

public class Entry<K, V> {
    protected final K _key;
    protected final V _value;

    public static <K, V> Entry<K, V> of(K key, V value) {
        return new Entry<>(key, value);
    }

    public static <K, V> Entry<K, V> of(Map.Entry<K,V> entry) {
        return new Entry<>(entry.getKey(), entry.getValue());
    }

    public Entry(K key, V value) {
        _key = key;
        _value = value;
    }

    public K key() {
        return _key;
    }

    public V value() {
        return _value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entry<?, ?> entry = (Entry<?, ?>) o;
        return Objects.equals(_key, entry._key) && Objects.equals(_value, entry._value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_key, _value);
    }
}
