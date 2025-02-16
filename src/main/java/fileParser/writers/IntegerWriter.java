package fileParser.writers;

import fileParser.dto.DataHolder;
import fileParser.SessionParametres;
import fileParser.dto.IntegerStatisticsHolder;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

public class IntegerWriter implements Callable<IntegerStatisticsHolder> {

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
    public IntegerStatisticsHolder call() throws Exception {
        while (true){ //TODO доделать
            Integer x = dataHolder.getOneInteger();
            if(x != null){
                System.out.println("Integer thread: " + x);
            } else if(isFinished.get() && dataHolder.getIntegersQueue().isEmpty()){
                break;
            }
        }//TODO доделать
        return new IntegerStatisticsHolder(null, null, null, null, null);
    }
}
