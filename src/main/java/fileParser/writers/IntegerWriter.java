package fileParser.writers;

import fileParser.dataStorage.DataHolder;
import fileParser.dto.SessionParametres;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    public void run(){
        String outputPath = sessionParametres.resultsPath() +
                File.separator + sessionParametres.prefix() + "integers.txt";
        File fileInteger = new File(outputPath);
        BufferedWriter bfwriter = null;
        try {
            while (true) {
                Integer integerValue = dataHolder.getOneInteger();
                if (integerValue != null) {
                    if (bfwriter == null) {
                        bfwriter = new BufferedWriter(new FileWriter(fileInteger, sessionParametres.append()));
                    }
                    bfwriter.write(String.valueOf(integerValue));
                    bfwriter.newLine();
                } else if (isFinished.get() && dataHolder.getIntegersQueue().isEmpty()) {
                    break;
                }
            }
        } catch (IOException ee) {
            System.out.println("Не удалось создать/открыть файл: " + sessionParametres.prefix() + "integers.txt");
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
