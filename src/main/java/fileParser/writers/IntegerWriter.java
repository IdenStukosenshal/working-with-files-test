package fileParser.writers;

import fileParser.dataStorage.DataHolder;
import fileParser.dto.SessionParametres;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
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
        String outputPath = sessionParametres.getResultsPath() +
                File.separator + sessionParametres.getPrefix() + "integers.txt";
        File fileInteger = new File(outputPath);
        BufferedWriter bfwriter = null;
        try {
            while (true) {
                BigInteger bigIntegerValue = dataHolder.getOneBigInteger();
                if (bigIntegerValue != null) {
                    if (bfwriter == null) {
                        bfwriter = new BufferedWriter(new FileWriter(fileInteger, sessionParametres.getAppend()));
                    }
                    bfwriter.write(bigIntegerValue.toString());
                    bfwriter.newLine();
                    bfwriter.flush();
                } else if (isFinished.get() && dataHolder.getBigIntegersQueue().isEmpty()) {
                    break;
                }
            }
        } catch (IOException ee) {
            System.out.println("Не удалось создать/открыть файл: " + sessionParametres.getPrefix() + "integers.txt");
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
