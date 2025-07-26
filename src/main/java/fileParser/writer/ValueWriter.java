package fileParser.writer;

import fileParser.dataStorage.DataHolder;
import fileParser.parametres.SessionParametres;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ValueWriter<T> implements Runnable {

    private final DataHolder<T> dataHolder;
    private final SessionParametres sessionParametres;
    private final AtomicBoolean isFinished;
    private final String fileName;

    public ValueWriter(DataHolder<T> dataHolder,
                       SessionParametres sessionParametres,
                       AtomicBoolean isFinished,
                       String fileName) {
        this.dataHolder = dataHolder;
        this.sessionParametres = sessionParametres;
        this.isFinished = isFinished;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        String outputPath = sessionParametres.getResultsPath() +
                File.separator + sessionParametres.getPrefix() + fileName;
        File file = new File(outputPath);
        BufferedWriter bfwriter = null;
        try {
            while (true) {
                var value = dataHolder.getOneValue();
                if (value != null) {
                    if (bfwriter == null) {
                        bfwriter = new BufferedWriter(new FileWriter(file, sessionParametres.getAppend()));
                    }
                    bfwriter.write(value.toString());
                    bfwriter.newLine();
                } else if (isFinished.get() && dataHolder.getQueue().isEmpty()) {
                    break;
                }
            }
        } catch (IOException ee) {
            System.out.println("Не удалось создать/открыть файл: " + sessionParametres.getPrefix() + fileName);
        } finally {
            if (bfwriter != null) {
                try {
                    bfwriter.close();
                } catch (IOException ee) {
                    System.out.println("Ошибка при попытке закрыть поток записи, часть информации может быть утеряна");
                }
            }
        }
    }
}
