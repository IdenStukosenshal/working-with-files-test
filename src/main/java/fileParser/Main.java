package fileParser;

import fileParser.dto.DataHolder;
import fileParser.dto.DoubleStatisticsHolder;
import fileParser.dto.IntegerStatisticsHolder;
import fileParser.dto.StringStatisticsHolder;
import fileParser.writers.DoubleWriter;
import fileParser.writers.IntegerWriter;
import fileParser.writers.StringWriter;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    public static void main(String[] args) {
        ArgumentsParser argumentsParser = new ArgumentsParser();
        DataHolder dataHolder = new DataHolder();
        AtomicBoolean isFinished = new AtomicBoolean(false);

        //Здесь должны быть проверки
        SessionParametres sessionParametres = argumentsParser.parse(args);
        //

        //https://www.geeksforgeeks.org/callable-future-java/
        FileProcessor fileProcessor = new FileProcessor(
                sessionParametres.filesPathsLst(),
                dataHolder,
                isFinished);

        Callable<IntegerStatisticsHolder> integerWriterCallable = new IntegerWriter(
                dataHolder,
                sessionParametres,
                isFinished);
        Callable<DoubleStatisticsHolder> doubleWriterCallable = new DoubleWriter(
                dataHolder,
                sessionParametres,
                isFinished);
        Callable<StringStatisticsHolder> stringWriterCallable = new StringWriter(
                dataHolder,
                sessionParametres,
                isFinished);

        Thread readThread = new Thread(fileProcessor);

        FutureTask<IntegerStatisticsHolder> futureInteger = new FutureTask<>(integerWriterCallable);
        Thread writeIntegerThread = new Thread(futureInteger);
        FutureTask<DoubleStatisticsHolder> futureDouble = new FutureTask<>(doubleWriterCallable);
        Thread writeDoubleThread = new Thread(futureDouble);
        FutureTask<StringStatisticsHolder> futureString = new FutureTask<>(stringWriterCallable);
        Thread writeStringThread = new Thread(futureString);

        readThread.start();

        writeIntegerThread.start();
        writeDoubleThread.start();
        writeStringThread.start();

        try{
            readThread.join();

            writeIntegerThread.join();
            System.out.println(futureInteger.get());

            writeDoubleThread.join();
            System.out.println(futureDouble.get());

            writeStringThread.join();
            System.out.println(futureString.get());

        }catch (InterruptedException | ExecutionException eee){ //TODO Временно????
            System.out.println("Прерывание работы программы по неизвестным причинам");
        }



    }
}