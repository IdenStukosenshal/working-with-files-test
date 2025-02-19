package fileParser.writers;

import fileParser.dataStorage.DataHolder;
import fileParser.dto.SessionParametres;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

        String outputPath = sessionParametres.resultsPath() +
                File.separator + sessionParametres.prefix() + "strings.txt";
        File fileString = new File(outputPath);
        BufferedWriter bfwriter = null;
        try {
            while (true) {
                String stringValue = dataHolder.getOneString();
                if (stringValue != null) {
                    if (bfwriter == null) {
                        bfwriter = new BufferedWriter(new FileWriter(fileString, sessionParametres.append()));
                    }
                    bfwriter.write(stringValue);
                    bfwriter.newLine();
                } else if (isFinished.get() && dataHolder.getStringsQueue().isEmpty()) {
                    break;
                }
            }
        } catch (IOException ee) {
            System.out.println("Не удалось создать/открыть файл: " + sessionParametres.prefix() + "strings.txt");
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
