package com.oysb.utils;

import java.util.LinkedList;

/* loaded from: classes2.dex */
public class Stack<T> extends LinkedList<T> {
    private static final long serialVersionUID = -3572332953942493147L;

    @Override // java.util.LinkedList, java.util.Deque
    public void push(T t) {
        addFirst(t);
    }

    @Override // java.util.LinkedList, java.util.Deque
    public T pop() {
        if (isEmpty()) {
            return null;
        }
        T first = getFirst();
        removeFirst();
        return first;
    }

    @Override // java.util.LinkedList, java.util.Deque, java.util.Queue
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return getFirst();
    }
}
