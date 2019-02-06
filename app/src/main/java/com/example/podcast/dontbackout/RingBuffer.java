package com.example.podcast.dontbackout;

public class RingBuffer<T> {
    private T[] buffer;
    private int count = 0;
    private int indexIn = 0;
    private int indexOut = 0;

    public RingBuffer(int capacity) {
        this.buffer = (T[]) new Object[capacity];
    }

    public boolean isEmpty() {
        return this.count == 0;
    }

    public boolean isFull() {
        return this.count == this.buffer.length;
    }

    public int size() {
        return this.count;
    }

    public void clear() {
        this.count = 0;
    }

    public void push(T item) {
        if (this.count == this.buffer.length) {
            System.out.println("Ring buffer overflow");
        }
        this.buffer[this.indexIn] = item;
        this.indexIn = (this.indexIn + 1) % this.buffer.length;
        int i = this.count;
        this.count = i + 1;
        if (i == this.buffer.length) {
            this.count = this.buffer.length;
        }
    }

    public T pop() {
        if (isEmpty()) {
            System.out.println("Ring buffer pop underflow");
        }
        T item = this.buffer[this.indexOut];
        this.buffer[this.indexOut] = null;
        int i = this.count;
        this.count = i - 1;
        if (i == 0) {
            this.count = 0;
        }
        this.indexOut = (this.indexOut + 1) % this.buffer.length;
        return item;
    }

    public T next() {
        if (isEmpty()) {
            System.out.println("Ring buffer next underflow");
        }
        return this.buffer[this.indexOut];
    }
}
