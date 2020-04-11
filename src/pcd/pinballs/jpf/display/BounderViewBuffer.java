package pcd.pinballs.jpf.display;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BounderViewBuffer {
    private int first, last, count;
    private long[] buffer;
    private Lock mutex;
    private Condition notFull, notEmpty;

    public BounderViewBuffer(int size) {
        first = last = count = 0;
        buffer = new long[size];
        mutex = new ReentrantLock();
        notFull = mutex.newCondition();
        notEmpty = mutex.newCondition();
    }

    public void put(long item) throws InterruptedException {
        try {
            mutex.lock();
            while (count == buffer.length) {
                notFull.await();
            }
            last = (last + 1) % buffer.length;
            count++;
            buffer[last] = item;
            notEmpty.signalAll();
        } finally {
            mutex.unlock();
        }
    }

    public long get() throws InterruptedException {
        try {
            mutex.lock();
            while (count == 0) {
                notEmpty.await();
            }
            first = (first + 1) % buffer.length;
            count--;
            notFull.signalAll();
            return buffer[first];
        } finally {
            mutex.unlock();
        }
    }

    public boolean isEmpty() {
        try {
            mutex.lock();
            return count == 0;
        } finally {
            mutex.unlock();
        }
    }

    public boolean isFull() {
        try {
            mutex.lock();
            return count == buffer.length;
        } finally {
            mutex.unlock();
        }
    }
}
