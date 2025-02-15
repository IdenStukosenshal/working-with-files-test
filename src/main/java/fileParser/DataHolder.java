package fileParser;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DataHolder {

    private BlockingQueue<String> stringsQ = new LinkedBlockingQueue<>();
    private BlockingQueue<Integer> integersQ = new LinkedBlockingQueue<>();
    private BlockingQueue<Double> doublesQ = new LinkedBlockingQueue<>();

    public String getOneString() {
        return stringsQ.poll();
    }

    public void setOneString(String line) throws InterruptedException{
        stringsQ.put(line);
    }

    public Integer getOneInteger() {
        return integersQ.poll();
    }

    public void setOneInteger(Integer oneInteger) throws InterruptedException{
        integersQ.put(oneInteger);
    }

    public Double getOneDouble() {
        return doublesQ.poll();
    }

    public void setOneDouble(Double oneDouble) throws InterruptedException{
        doublesQ.put(oneDouble);
    }
}
