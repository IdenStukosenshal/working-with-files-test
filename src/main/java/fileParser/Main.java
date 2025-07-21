package fileParser;

import fileParser.dataStorage.DataHolder;
import fileParser.dataStorage.StatisticsHolder;
import fileParser.dto.*;
import fileParser.utils.ArgumentsParser;
import fileParser.writers.DoubleWriter;
import fileParser.writers.IntegerWriter;
import fileParser.writers.StringWriter;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    public static void main(String[] args) {

        ArgumentsParser argumentsParser = new ArgumentsParser();
        DataHolder dataHolder = new DataHolder();
        AtomicBoolean isFinished = new AtomicBoolean(false);
        SessionParametres sessionParametres;

        try {
            sessionParametres = argumentsParser.parse(args);
        } catch (RuntimeException exc) {
            System.out.println(exc.getMessage());
            return;
        }
        createDirectories(sessionParametres.getResultsPath());
        StatisticsHolder statisticsHolder = new StatisticsHolder(sessionParametres);

        System.out.println(sessionParametres.getMessage());

        Runnable fileProcessor = new FileProcessor(
                sessionParametres.getFilesPathsLst(),
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

        System.out.println("Работа выполнена");
    }


    private static void createDirectories(String path){
        File structure = new File(path);
        structure.mkdirs();
    }
    private static void printStatistics(SessionParametres sessionParametres, StatisticsHolder statisticsHolder){
        if(sessionParametres.getStatisticsType() != StatisticsType.NONE){
            System.out.println(statisticsHolder.getIntegerStatistics() +
                    statisticsHolder.getDoubleStatistics() +
                    statisticsHolder.getStringStatistics());
        }
    }
}