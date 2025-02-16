package fileParser.writers;

import fileParser.DataHolder;
import fileParser.SessionParametres;
import fileParser.StatisticsType;

import java.util.concurrent.atomic.AtomicBoolean;

public class IntegerWriter implements Runnable {

    private final DataHolder dataHolder;
    private final SessionParametres sessionParametres;
    private final AtomicBoolean isFinished;

    public IntegerWriter(DataHolder dataHolder,
                         SessionParametres sessionParametres,
                         AtomicBoolean isFinished) {
        this.dataHolder = dataHolder;
        this.sessionParametres = sessionParametres;
        this.isFinished = isFinished;
    }


    @Override
    public void run() {
        while (true){
            Integer x = dataHolder.getOneInteger();
            if(x != null){
                System.out.println("Integer thread: " + x);
            } else if(isFinished.get() && dataHolder.getIntegersQueue().isEmpty()){
                break;
            }
        }

    }
}
