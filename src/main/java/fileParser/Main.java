package fileParser;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        StatisticsPrinter statisticsPrinter;
        ArgumentsParser argumentsParser = new ArgumentsParser();
        DataHolder dataHolder = new DataHolder();

        //Здесь должны быть проверки
        SessionParametres sessionParametres = argumentsParser.parse(args);

        FileProcessor fileProcessor = new FileProcessor(dataHolder);
        FileOut fileOut = new FileOut(
                sessionParametres.prefix(),
                sessionParametres.resultsPath(),
                sessionParametres.append());


        try {
            fileProcessor.fileProcessingNEW(sessionParametres.filesPathsLst());
        } catch (IOException exc){
            System.out.println("Во время чтения одного из файлов произошла ошибка");
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