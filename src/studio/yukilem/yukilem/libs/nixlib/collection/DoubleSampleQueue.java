package studio.yukilem.yukilem.libs.nixlib.collection;

public final class DoubleSampleQueue {
    private final double[] buffer;
    private int head;
    private int size;

    public DoubleSampleQueue(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.buffer = new double[n];
    }

    public void add(double d) {
        int length = this.buffer.length;
        if (this.size < length) {
            this.buffer[(this.head + this.size) % length] = d;
            ++this.size;
        } else {
            this.buffer[this.head] = d;
            this.head = (this.head + 1) % length;
        }
    }

    public void clear() {
        this.head = 0;
        this.size = 0;
    }

    public int size() {
        return this.size;
    }

    public int capacity() {
        return this.buffer.length;
    }

    public boolean isFull() {
        return this.size >= this.buffer.length;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public double[] copyInto(double[] dArray) {
        int length = this.buffer.length;
        if (this.size <= length - this.head) {
            System.arraycopy(this.buffer, this.head, dArray, 0, this.size);
        } else {
            int n = length - this.head;
            System.arraycopy(this.buffer, this.head, dArray, 0, n);
            System.arraycopy(this.buffer, 0, dArray, n, this.size - n);
        }
        return dArray;
    }
}

