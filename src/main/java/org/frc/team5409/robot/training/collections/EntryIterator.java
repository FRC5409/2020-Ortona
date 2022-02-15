package org.frc.team5409.robot.training.collections;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public class EntryIterator<K, V> implements Iterator<Entry<K,V>> {
    private final Iterator<Map.Entry<K, V>> _base;

    public EntryIterator() {
        this(Collections.emptyMap());
    }

    public EntryIterator(Map<K, V> target) {
        _base = target.entrySet().iterator();
    }

    @Override
    public boolean hasNext() {
        return _base.hasNext();
    }

    @Override
    public Entry<K, V> next() {
        return Entry.of(_base.next());
    }
}
