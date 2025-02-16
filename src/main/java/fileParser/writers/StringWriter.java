package fileParser.writers;

import fileParser.dto.DataHolder;
import fileParser.SessionParametres;
import fileParser.dto.StringStatisticsHolder;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

public class StringWriter implements Callable<StringStatisticsHolder> {
    private final DataHolder dataHolder;
    private final SessionParametres sessionParametres;
    private final AtomicBoolean isFinished;

    public StringWriter(DataHolder dataHolder,
                         SessionParametres sessionParametres,
                         AtomicBoolean isFinished) {
        this.dataHolder = dataHolder;
        this.sessionParametres = sessionParametres;
        this.isFinished = isFinished;
    }


    @Override
    public StringStatisticsHolder call() throws Exception {
        while (true){//TODO доделать
            String x = dataHolder.getOneString();
            if(x != null){
                System.out.println("String thread: " + x);
            } else if(isFinished.get() && dataHolder.getStringsQueue().isEmpty()){
                break;
            }
        }
        //TODO доделать
        return new StringStatisticsHolder(null, null, null);
    }
}
