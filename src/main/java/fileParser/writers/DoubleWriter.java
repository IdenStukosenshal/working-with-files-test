package fileParser.writers;

import fileParser.dto.DataHolder;
import fileParser.SessionParametres;
import fileParser.dto.DoubleStatisticsHolder;
import fileParser.dto.IntegerStatisticsHolder;

import java.io.*;
import java.math.BigDecimal;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

public class DoubleWriter implements Callable<DoubleStatisticsHolder> {
    private final DataHolder dataHolder;
    private final SessionParametres sessionParametres;
    private final AtomicBoolean isFinished;

    private long countNumbers = 0;
    private double minNumber = Double.MAX_VALUE;
    private double maxNumber = Double.MIN_VALUE;
    private BigDecimal sumNumbers = new BigDecimal(0);


    public DoubleWriter(DataHolder dataHolder,
                        SessionParametres sessionParametres,
                        AtomicBoolean isFinished) {
        this.dataHolder = dataHolder;
        this.sessionParametres = sessionParametres;
        this.isFinished = isFinished;
    }


    @Override
    public DoubleStatisticsHolder call() throws Exception {

        String separator = File.separator;
        String outputPath = sessionParametres.resultsPath() +
                separator + sessionParametres.prefix() + "floats.txt";

        File fileDouble = new File(outputPath);
        fileDouble.getParentFile().mkdirs();
        try(BufferedWriter bfwriter = new BufferedWriter(new FileWriter(fileDouble, sessionParametres.append()))){
            while (true){
                Double doubleValue = dataHolder.getOneDouble();
                if(doubleValue != null){
                    bfwriter.write(String.valueOf(doubleValue));
                    bfwriter.newLine();
                    calculateStatistics(doubleValue);
                }
                else if (isFinished.get() && dataHolder.getDoublesQueue().isEmpty()){
                    break;
                }
            }
        }
        catch (IOException ee){
            System.out.println("Не удалось создать/открыть файл: " + sessionParametres.prefix() +"floats.txt");
        }
        return new DoubleStatisticsHolder(
                countNumbers,
                minNumber,
                maxNumber,
                sumNumbers,
                sumNumbers.divide(BigDecimal.valueOf(countNumbers)).doubleValue());
    }

    private void calculateStatistics(Double doubleValue){
        countNumbers++;
        if(doubleValue < minNumber) minNumber = doubleValue;
        if(doubleValue > maxNumber) maxNumber = doubleValue;

        sumNumbers = sumNumbers.add(BigDecimal.valueOf(doubleValue));
    }
}
