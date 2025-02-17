package fileParser.writers;

import fileParser.dataStorage.DataHolder;
import fileParser.dto.SessionParametres;

import java.util.concurrent.atomic.AtomicBoolean;

public class StringWriter implements Runnable {
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
    public void run() {

        while (true) {//TODO доделать
            String x = dataHolder.getOneString();
            if (x != null) {
                System.out.println("String thread: " + x);
            } else if (isFinished.get() && dataHolder.getStringsQueue().isEmpty()) {
                break;
            }
        }
    }
}
