package fileParser;

import fileParser.dataStorage.DataHolder;
import fileParser.dataStorage.StatisticsHolder;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;


public class FileProcessor implements Runnable {

    private final List<String> filePathsLst;
    private final DataHolder dataHolder;
    private final StatisticsHolder statisticsHolder;
    private final AtomicBoolean isFinished;
    private final Pattern integerPattern = Pattern.compile("^-?\\d+$");

    public FileProcessor(List<String> filePathsLst,
                         DataHolder dataHolder,
                         StatisticsHolder statisticsHolder,
                         AtomicBoolean isFinished) {
        this.filePathsLst = filePathsLst;
        this.dataHolder = dataHolder;
        this.statisticsHolder = statisticsHolder;
        this.isFinished = isFinished;
    }

    @Override
    public void run() {
        fileProcessing();
        isFinished.set(true);
    }

    public void fileProcessing() {
        BigInteger bigIntegerValue;
        Double doubleValue;
        boolean creatable;

        List<BufferedReader> readersLst = new ArrayList<>();
        for (String oneFilePath : filePathsLst) {
            try {
                readersLst.add(new BufferedReader(new FileReader(oneFilePath)));
            } catch (IOException ee) {
                System.out.println("Чтение файла: (" + oneFilePath + ") не удалось. Данные из этого файла не будут прочитаны");
            }
        }
        while (!readersLst.isEmpty()) {
            for (int i = 0; i < readersLst.size(); i++) {
                var reader = readersLst.get(i);
                String line = tryingReadLine(reader);
                if (line == null) {
                    closeReader(reader);
                    readersLst.remove(reader);
                    continue;
                }
                if (line.isEmpty()) continue;
                creatable = NumberUtils.isCreatable(line);
                if (isBigInteger(line, creatable)) {
                    bigIntegerValue = new BigInteger(line);
                    dataHolder.setOneBigInteger(bigIntegerValue);
                    statisticsHolder.increaseBigIntegerStatistics(bigIntegerValue);
                } else if (creatable) {
                    doubleValue = Double.parseDouble(line);
                    dataHolder.setOneDouble(doubleValue);
                    statisticsHolder.increaseDoubleStatistics(doubleValue);
                } else {
                    dataHolder.setOneString(line);
                    statisticsHolder.increaseStringStatistics(line);
                }
            }
        }
    }

    private boolean isBigInteger(String testString, boolean creatable) {
        return creatable && integerPattern.matcher(testString).matches();
    }

    private String tryingReadLine(BufferedReader reader) {
        try {
            return reader.readLine();
        } catch (IOException ee) {
            System.out.println("Ошибка во время чтения одного из файлов, данные из него не будут читаться далее");
        }
        return null;
    }

    private void closeReader(BufferedReader reader) {
        try {
            reader.close();
        } catch (IOException ee) {
            System.out.println("Ошибка при закрытии потока чтения одного из файлов, данные могут быть прочитаны не полностью");
        }
    }
}
