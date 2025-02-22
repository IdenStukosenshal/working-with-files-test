package fileParser.writers;

import fileParser.dataStorage.DataHolder;
import fileParser.dto.SessionParametres;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class DoubleWriter implements Runnable {
    private final DataHolder dataHolder;
    private final SessionParametres sessionParametres;
    private final AtomicBoolean isFinished;

    public DoubleWriter(DataHolder dataHolder,
                        SessionParametres sessionParametres,
                        AtomicBoolean isFinished) {
        this.dataHolder = dataHolder;
        this.sessionParametres = sessionParametres;
        this.isFinished = isFinished;
    }

    @Override
    public void run() {
        String outputPath = sessionParametres.getResultsPath() +
                File.separator + sessionParametres.getPrefix() + "floats.txt";
        File fileDouble = new File(outputPath);
        BufferedWriter bfwriter = null;
        try {
            while (true) {
                Double doubleValue = dataHolder.getOneDouble();
                if (doubleValue != null) {
                    if (bfwriter == null) {
                        bfwriter = new BufferedWriter(new FileWriter(fileDouble, sessionParametres.getAppend()));
                    }
                    bfwriter.write(String.valueOf(doubleValue));
                    bfwriter.newLine();
                    bfwriter.flush();
                } else if (isFinished.get() && dataHolder.getDoublesQueue().isEmpty()) {
                    break;
                }
            }
        } catch (IOException ee) {
            System.out.println("Не удалось создать/открыть файл: " + sessionParametres.getPrefix() + "floats.txt");
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
