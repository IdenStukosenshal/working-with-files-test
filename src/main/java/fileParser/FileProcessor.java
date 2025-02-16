package fileParser;

import fileParser.dto.DataHolder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileProcessor implements Runnable{

    private final DataHolder dataHolder;
    private final List<String> filePathsLst;
    private final AtomicBoolean isFinished;

    public FileProcessor(List<String> filePathsLst,
                         DataHolder dataHolder,
                         AtomicBoolean isFinished) {
        this.dataHolder = dataHolder;
        this.filePathsLst = filePathsLst;
        this.isFinished = isFinished;
    }

    @Override
    public void run() {
        try{
            fileProcessing();
        }catch (IOException eee){
            System.out.println("Ошибка при чтении файла");
        }finally { //TODO Посмотреть внимательнее где именно разместить это и какие исключения ловить
            isFinished.set(true);
        }

    }

    public void fileProcessing() throws IOException {
        List<BufferedReader> readersLst = new ArrayList<>();
        for (String oneFilePath : filePathsLst) {
            try {
                readersLst.add(new BufferedReader(new FileReader(oneFilePath)));
            } catch (IOException ee) {
                System.out.println("Чтение файла: " + oneFilePath + " не удалось. Данные из этого файла не будут прочитаны");
            }
        }
        while (!readersLst.isEmpty()) {
            for (int i = 0; i < readersLst.size(); i++){
                var reader = readersLst.get(i);
                String line = reader.readLine();
                if (line == null) {
                    reader.close();
                    readersLst.remove(reader);
                    continue;
                }
                if (line.isEmpty()) continue;
                try {
                    dataHolder.setOneInteger(Integer.parseInt(line));
                } catch (Exception e) { //TODO разобраться с исключениями
                    try {
                        dataHolder.setOneDouble(Double.parseDouble(line));
                    } catch (Exception ex) {
                        try {
                            dataHolder.setOneString(line);
                        } catch (Exception exc) {
                            throw new RuntimeException();
                        }
                    }
                }
            }
        }
        isFinished.set(true);
    }
}
