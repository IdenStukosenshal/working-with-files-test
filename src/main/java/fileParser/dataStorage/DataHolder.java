package fileParser.dataStorage;

import java.math.BigInteger;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataHolder {

    private final Queue<String> stringsQ = new ConcurrentLinkedQueue<>();
    private final Queue<BigInteger> bigIntegersQ = new ConcurrentLinkedQueue<>();
    private final Queue<Double> doublesQ = new ConcurrentLinkedQueue<>();

    public String getOneString() {
        return stringsQ.poll();
    }

    public void setOneString(String line){
        stringsQ.offer(line);
    }

    public BigInteger getOneBigInteger() {
        return bigIntegersQ.poll();
    }

    public void setOneBigInteger(BigInteger oneInteger){
        bigIntegersQ.offer(oneInteger);
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

    public Queue<BigInteger> getBigIntegersQueue() {
        return bigIntegersQ;
    }

    public Queue<Double> getDoublesQueue() {
        return doublesQ;
    }
}
