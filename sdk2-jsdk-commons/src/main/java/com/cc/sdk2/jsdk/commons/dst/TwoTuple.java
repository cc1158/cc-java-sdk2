package com.cc.sdk2.jsdk.commons.dst;

public class TwoTuple<A, B> {

    private final A first;
    private final B second;

    public TwoTuple(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "['" + first.toString() + "', '"  + second.toString() + "']";
    }
}
