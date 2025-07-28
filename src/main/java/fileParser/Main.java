package fileParser;

import fileParser.dataStorage.DataHolder;
import fileParser.dataStorage.StatisticsHolder;
import fileParser.parameters.SessionParametres;
import fileParser.parameters.StatisticsType;
import fileParser.reader.FileProcessor;
import fileParser.utils.ArgumentsParser;
import fileParser.writer.ValueWriter;

import java.io.File;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicBoolean;

import static fileParser.parameters.ErrorMessages.*;
import static fileParser.parameters.OutFileNames.*;


public class Main {

    public static void main(String[] args) {
        try {
            runApp(args);
        } catch (Exception e) {
            System.out.println(INTERNAL_ERROR.getMessage());
        }
    }


    private static void runApp(String[] args) throws Exception {
        ArgumentsParser argumentsParser = new ArgumentsParser();

        DataHolder<BigInteger> bigIntegerDataHolder = new DataHolder<>();
        DataHolder<Double> doubleDataHolder = new DataHolder<>();
        DataHolder<String> stringDataHolder = new DataHolder<>();

        AtomicBoolean isFinished = new AtomicBoolean(false);
        SessionParametres sessionParametres;

        try {
            sessionParametres = argumentsParser.parse(args);
        } catch (RuntimeException exc) {
            System.out.println(exc.getMessage());
            return;
        }
        try {
            createDirectories(sessionParametres.getResultsPath());
        } catch (SecurityException ee) {
            System.out.println(CREATING_DIR_ERROR.getMessage());
            return;
        }
        StatisticsHolder statisticsHolder = new StatisticsHolder(sessionParametres);

        System.out.println(sessionParametres.getMessage());

        Runnable fileProcessor = new FileProcessor(
                sessionParametres.getFilesPathsLst(),
                bigIntegerDataHolder,
                doubleDataHolder,
                stringDataHolder,
                statisticsHolder,
                isFinished);

        Runnable integerWriterRunnable = new ValueWriter<>(
                bigIntegerDataHolder,
                sessionParametres,
                isFinished,
                INTEGERS.getFileName());
        Runnable doubleWriterRunnable = new ValueWriter<>(
                doubleDataHolder,
                sessionParametres,
                isFinished,
                DOUBLES.getFileName());
        Runnable stringWriterRunnable = new ValueWriter<>(
                stringDataHolder,
                sessionParametres,
                isFinished,
                STRINGS.getFileName());

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
        } catch (InterruptedException e) {
            System.out.println(INTERRUPT_ERROR.getMessage() + e.getMessage());
            return;
        }

        printStatistics(sessionParametres, statisticsHolder);

        System.out.println("Работа выполнена");
    }

    private static void createDirectories(String path) {
        File structure = new File(path);
        structure.mkdirs();
    }

    private static void printStatistics(SessionParametres sessionParametres, StatisticsHolder statisticsHolder) {
        if (sessionParametres.getStatisticsType() != StatisticsType.NONE) {
            System.out.println(statisticsHolder.getIntegerStatistics() +
                    statisticsHolder.getDoubleStatistics() +
                    statisticsHolder.getStringStatistics());
        }
    }
}