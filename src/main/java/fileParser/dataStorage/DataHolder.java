package fileParser.dataStorage;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataHolder<T> {

    private final Queue<T> queue = new ConcurrentLinkedQueue<>();

    public T getOneValue() {
        return queue.poll();
    }

    public void setOneValue(T oneValue) {
        queue.offer(oneValue);
    }

    public Queue<T> getQueue() {
        return queue;
    }
}