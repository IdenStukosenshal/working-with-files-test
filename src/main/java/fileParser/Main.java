package fileParser;

import fileParser.writers.DoubleWriter;
import fileParser.writers.IntegerWriter;
import fileParser.writers.StringWriter;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    public static void main(String[] args) {
        StatisticsPrinter statisticsPrinter;
        ArgumentsParser argumentsParser = new ArgumentsParser();
        DataHolder dataHolder = new DataHolder();
        AtomicBoolean isFinished = new AtomicBoolean(false);

        //Здесь должны быть проверки
        SessionParametres sessionParametres = argumentsParser.parse(args);
        //

        FileProcessor fileProcessor = new FileProcessor(
                sessionParametres.filesPathsLst(),
                dataHolder,
                isFinished);
        IntegerWriter integerWriter = new IntegerWriter(
                dataHolder,
                sessionParametres,
                isFinished);
        DoubleWriter doubleWriter = new DoubleWriter(
                dataHolder,
                sessionParametres,
                isFinished);
        StringWriter stringWriter = new StringWriter(
                dataHolder,
                sessionParametres,
                isFinished);

        Thread readThread = new Thread(fileProcessor);
        Thread writeIntegerThread = new Thread(integerWriter);
        Thread writeDoubleThread = new Thread(doubleWriter);
        Thread writeStringThread = new Thread(stringWriter);

        readThread.start();
        writeIntegerThread.start();
        writeDoubleThread.start();
        writeStringThread.start();

        try{
            readThread.join();
            writeIntegerThread.join();
            writeDoubleThread.join();
            writeStringThread.join();
        }catch (InterruptedException eee){
            System.out.println("Прерывание работы программы по неизвестным причинам");
        }

    }
}