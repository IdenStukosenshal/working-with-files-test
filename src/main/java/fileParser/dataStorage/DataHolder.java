package fileParser.dataStorage;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DataHolder {

    private final Queue<String> stringsQ = new ConcurrentLinkedQueue<>();
    private final Queue<Integer> integersQ = new ConcurrentLinkedQueue<>();
    private final Queue<Double> doublesQ = new ConcurrentLinkedQueue<>();

    public String getOneString() {
        return stringsQ.poll();
    }

    public void setOneString(String line){
        stringsQ.offer(line);
    }

    public Integer getOneInteger() {
        return integersQ.poll();
    }

    public void setOneInteger(Integer oneInteger){
        integersQ.offer(oneInteger);
    }

    public Double getOneDouble() {
        return doublesQ.poll();
    }

    public void setOneDouble(Double oneDouble){
        doublesQ.offer(oneDouble);
    }

    public Queue<String> getStringsQueue() {
        return stringsQ;
    }

    public Queue<Integer> getIntegersQueue() {
        return integersQ;
    }

    public Queue<Double> getDoublesQueue() {
        return doublesQ;
    }
}
