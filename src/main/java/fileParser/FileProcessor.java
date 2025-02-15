package fileParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileProcessor {

    private DataHolder dataHolder;

    public FileProcessor(DataHolder dataHolder) {
        this.dataHolder = dataHolder;
    }

    public void fileProcessingNEW(List<String> filePathsLst) throws IOException {
        List<BufferedReader> readersLst = new ArrayList<>();
        for (String oneFilePath : filePathsLst) {
            try {
                readersLst.add(new BufferedReader(new FileReader(oneFilePath)));
            } catch (IOException ee) {
                System.out.println("Чтение файла: " + oneFilePath + " не удалось");
            }
        }
        int notCompleted = readersLst.size();
        while (notCompleted != 0) {
            notCompleted = readersLst.size();
            for (BufferedReader reader : readersLst) {
                String line = reader.readLine();
                if (line == null) {
                    notCompleted--;
                    if (notCompleted == 0) break; //если все файлы подошли к концу
                    continue;
                }
                if (line.isEmpty()) continue;

                try {
                    dataHolder.setOneInteger(Integer.parseInt(line));
                } catch (Exception e) {
                    try {
                        dataHolder.setOneDouble(Double.parseDouble(line));
                    } catch (Exception ex) {
                        try {
                            dataHolder.setOneString(line);
                        } catch (Exception exc) {
                            throw new IOException();
                        }
                    }
                }
            }
        }
    }
}
