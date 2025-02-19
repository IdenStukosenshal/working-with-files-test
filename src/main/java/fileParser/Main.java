package fileParser;

import fileParser.dataStorage.DataHolder;
import fileParser.dataStorage.StatisticsHolder;
import fileParser.dto.*;
import fileParser.writers.DoubleWriter;
import fileParser.writers.IntegerWriter;
import fileParser.writers.StringWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    public static void main(String[] args) {

        String[] argsTEST = new String[9];
        argsTEST[0] = "";//-o
        argsTEST[1] = "";//"/папка1/папка2";
        argsTEST[2] = "-p";
        argsTEST[3] = "-prefix-new";
        argsTEST[4] = "-f";
        argsTEST[5] = ""; //-a
        argsTEST[6] = "src/main/java/fileParser/file1.txt";
        argsTEST[7] = "src/main/java/fileParser/file2.txt";
        argsTEST[8] = "src/main/java/fileParser/file3.txt";

        ArgumentsParser argumentsParser = new ArgumentsParser();
        DataHolder dataHolder = new DataHolder();
        AtomicBoolean isFinished = new AtomicBoolean(false);
        SessionParametres sessionParametres;

        try {//TODO не забыть заменить + проверки
            sessionParametres = argumentsParser.parse(argsTEST);
            //sessionParametres = argumentsParser.parse(args);
        } catch (RuntimeException exc) {
            System.out.println("Пути файлов не были указаны");
            return;
        }
        createDirectories(sessionParametres.resultsPath());
        StatisticsHolder statisticsHolder = new StatisticsHolder(sessionParametres);

        System.out.println(sessionParametres.getMessage());

        FileProcessor fileProcessor = new FileProcessor(
                sessionParametres.filesPathsLst(),
                dataHolder,
                statisticsHolder,
                isFinished);

        Runnable integerWriterRunnable = new IntegerWriter(
                dataHolder,
                sessionParametres,
                isFinished);
        Runnable  doubleWriterRunnable = new DoubleWriter(
                dataHolder,
                sessionParametres,
                isFinished);
        Runnable  stringWriterRunnable = new StringWriter(
                dataHolder,
                sessionParametres,
                isFinished);

        Thread readThread = new Thread(fileProcessor);
        Thread writeIntegerThread = new Thread(integerWriterRunnable);
        Thread writeDoubleThread = new Thread(doubleWriterRunnable);
        Thread writeStringThread = new Thread(stringWriterRunnable);

        readThread.start();
        writeIntegerThread.start();
        writeDoubleThread.start();
        writeStringThread.start();

        try {
            readThread.join();
            writeIntegerThread.join();
            writeDoubleThread.join();
            writeStringThread.join();
        } catch (InterruptedException eee) {
            System.out.println("Прерывание работы программы по неизвестным причинам");
        }

        printStatistics(sessionParametres, statisticsHolder);

        System.out.println("Работа выполнена успешно ");
    }


    private static void createDirectories(String path){
        File structure = new File(path);
        structure.mkdirs();
    }
    private static void printStatistics(SessionParametres sessionParametres, StatisticsHolder statisticsHolder){
        if(sessionParametres.statisticsType() != StatisticsType.NONE){
            System.out.println(statisticsHolder.getIntegerStatistics() +
                    statisticsHolder.getDoubleStatistics() +
                    statisticsHolder.getStringStatistics());
        }
    }
}