package fileParser;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        StatisticsPrinter statisticsPrinter;
        ArgumentsParser argumentsParser = new ArgumentsParser();

        //Здесь должны быть проверки
        SessionParametres sessionParametres = argumentsParser.parse(args);

        FileProcessor fileProcessor = new FileProcessor();
        FileOut fileOut = new FileOut(
                sessionParametres.prefix(),
                sessionParametres.resultsPath(),
                sessionParametres.append());

        for (String path: sessionParametres.filesPathsLst()) {
            try {
                fileProcessor.fileProcessing(path);
            } catch (IOException exc){
                System.out.println("Чтение файла: " + path + " не удалось");
            }
        }

        fileOut.writeFiles(fileProcessor);

        if(!sessionParametres.statisticsType().equals(StatisticsType.NONE)){
            statisticsPrinter = new StatisticsPrinter();
            statisticsPrinter.printStatistics(
                    sessionParametres.statisticsType(),
                    fileProcessor);
        }
    }
}