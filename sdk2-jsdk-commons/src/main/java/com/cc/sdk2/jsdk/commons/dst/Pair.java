package com.cc.sdk2.jsdk.commons.dst;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Objects;

/**
 * 一对数据结构
 * @author sen.hu
 * @date
 * @param <K>
 * @param <V>
 */
public class Pair<K, V> implements Externalizable, Cloneable {

    private K key;
    private V value;

    public Pair() {
    }

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(key);
        out.writeObject(value);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.key= (K) in.readObject();
        this.value = (V) in.readObject();
    }

    @Override
    protected Pair<K, V> clone() throws CloneNotSupportedException {
        try {
            return (Pair<K, V>) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ;
        Pair<?, ?> pair = (Pair<?, ?>) obj;
        return Objects.equals(key, pair.key) &&
                Objects.equals(value, pair.value);

    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return "<" + key + "," + value + ">";
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
