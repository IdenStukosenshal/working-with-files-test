package fileParser;

import fileParser.dataStorage.DataHolder;
import fileParser.dataStorage.StatisticsHolder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileProcessor implements Runnable {

    private final DataHolder dataHolder;
    private final StatisticsHolder statisticsHolder;
    private final List<String> filePathsLst;
    private final AtomicBoolean isFinished;

    public FileProcessor(List<String> filePathsLst,
                         DataHolder dataHolder,
                         StatisticsHolder statisticsHolder,
                         AtomicBoolean isFinished) {
        this.dataHolder = dataHolder;
        this.filePathsLst = filePathsLst;
        this.isFinished = isFinished;
        this.statisticsHolder = statisticsHolder;
    }

    @Override
    public void run() {
        try {
            fileProcessing();
        } catch (IOException eee) {
            System.out.println("Ошибка во время чтения файлов"); //??? TODO Нужно ли это теперь?
        } finally {
            isFinished.set(true);
        }
    }

    public void fileProcessing() throws IOException {
        Integer integerValue;
        Double doubleValue;

        List<BufferedReader> readersLst = new ArrayList<>();
        for (String oneFilePath : filePathsLst) {
            if(!oneFilePath.isBlank()){
                try {
                    readersLst.add(new BufferedReader(new FileReader(oneFilePath)));
                } catch (IOException ee) {
                    System.out.println("Чтение файла: (" + oneFilePath + ") не удалось. Данные из этого файла не будут прочитаны");
                }
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
                try {
                    integerValue = Integer.parseInt(line);
                    dataHolder.setOneInteger(integerValue);
                    statisticsHolder.setIntegerStatistics(integerValue);
                } catch (NumberFormatException e) {
                    try {
                        doubleValue = Double.parseDouble(line);
                        dataHolder.setOneDouble(doubleValue);
                        statisticsHolder.setDoubleStatistics(doubleValue);
                    } catch (NumberFormatException ex) {
                        dataHolder.setOneString(line);
                        statisticsHolder.setStringStatistics(line);
                    }
                }
            }
        }
    }
    private String tryingReadLine(BufferedReader reader){
        try{
            return reader.readLine();
        }catch (IOException ee){
            System.out.println("Ошибка во время чтения одного из файлов, данные из него не будут читаться далее");
        }
        return null;
    }
    private void closeReader(BufferedReader reader){
        try{
            reader.close();
        }catch (IOException ee){
            System.out.println("Ошибка при закрытии потока чтения одного из файлов, данные могут быть прочитаны не полностью");
        }
    }
}
