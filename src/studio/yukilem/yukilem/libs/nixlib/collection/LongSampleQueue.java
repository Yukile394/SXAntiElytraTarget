package studio.yukilem.yukilem.libs.nixlib.collection;

public final class LongSampleQueue {
    private final long[] buffer;
    private int head;
    private int size;

    public LongSampleQueue(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.buffer = new long[n];
    }

    public void add(long l) {
        int length = this.buffer.length;
        if (this.size < length) {
            this.buffer[(this.head + this.size) % length] = l;
            ++this.size;
        } else {
            this.buffer[this.head] = l;
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

    public double[] copyAsDoublesInto(double[] dArray) {
        int length = this.buffer.length;
        if (this.size <= length - this.head) {
            for (int n = 0; n < this.size; ++n) {
                dArray[n] = this.buffer[this.head + n];
            }
        } else {
            int n;
            int n2 = length - this.head;
            for (n = 0; n < n2; ++n) {
                dArray[n] = this.buffer[this.head + n];
            }
            for (n = 0; n < this.size - n2; ++n) {
                dArray[n2 + n] = this.buffer[n];
            }
        }
        return dArray;
    }
}

