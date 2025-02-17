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

        String separator = File.separator;
        String outputPath = sessionParametres.resultsPath() +
                separator + sessionParametres.prefix() + "floats.txt";

        File fileDouble = new File(outputPath);
        fileDouble.getParentFile().mkdirs();
        try (BufferedWriter bfwriter = new BufferedWriter(new FileWriter(fileDouble, sessionParametres.append()))) {
            while (true) {
                Double doubleValue = dataHolder.getOneDouble();
                if (doubleValue != null) {
                    bfwriter.write(String.valueOf(doubleValue));
                    bfwriter.newLine();
                } else if (isFinished.get() && dataHolder.getDoublesQueue().isEmpty()) {
                    break;
                }
            }
        } catch (IOException ee) {
            System.out.println("Не удалось создать/открыть файл: " + sessionParametres.prefix() + "floats.txt");
        }
    }
}
