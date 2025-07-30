package fileParser.reader;

import fileParser.dataStorage.DataHolder;
import fileParser.dataStorage.StatisticsHolder;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import static fileParser.parameters.ErrorMessages.READING_ERROR;


public class FileProcessor implements Runnable {

    private final List<String> filePathsLst;
    private final DataHolder<BigInteger> bigIntegerDataHolder;
    private final DataHolder<Double> doubleDataHolder;
    private final DataHolder<String> stringDataHolder;
    private final StatisticsHolder statisticsHolder;
    private final AtomicBoolean isFinished;
    private final Pattern integerPattern = Pattern.compile("^-?\\d+$");

    public FileProcessor(List<String> filePathsLst,
                         DataHolder<BigInteger> bigIntegerDataHolder,
                         DataHolder<Double> doubleDataHolder,
                         DataHolder<String> stringDataHolder,
                         StatisticsHolder statisticsHolder,
                         AtomicBoolean isFinished) {
        this.filePathsLst = filePathsLst;
        this.bigIntegerDataHolder = bigIntegerDataHolder;
        this.doubleDataHolder = doubleDataHolder;
        this.stringDataHolder = stringDataHolder;
        this.statisticsHolder = statisticsHolder;
        this.isFinished = isFinished;
    }

    @Override
    public void run() {
        fileProcessing();
        isFinished.set(true);
    }

    public void fileProcessing() {
        String line;
        BigInteger bigIntegerValue;
        Double doubleValue;
        boolean creatable;

        for (String oneFilePath : filePathsLst) {
            try (BufferedReader reader = new BufferedReader(new FileReader(oneFilePath))) {
                while ((line = reader.readLine()) != null) {
                    if (line.isEmpty()) continue;
                    creatable = NumberUtils.isCreatable(line);
                    if (isBigInteger(line, creatable)) {
                        bigIntegerValue = new BigInteger(line);
                        bigIntegerDataHolder.setOneValue(bigIntegerValue);
                        statisticsHolder.increaseBigIntegerStatistics(bigIntegerValue);
                    } else if (creatable) {
                        doubleValue = Double.parseDouble(line);
                        doubleDataHolder.setOneValue(doubleValue);
                        statisticsHolder.increaseDoubleStatistics(doubleValue);
                    } else {
                        stringDataHolder.setOneValue(line);
                        statisticsHolder.increaseStringStatistics(line);
                    }
                }
            } catch (Exception ee) {
                System.out.println(READING_ERROR.getMessage().formatted(oneFilePath));
            }
        }
    }

    private boolean isBigInteger(String testString, boolean creatable) {
        return creatable && integerPattern.matcher(testString).matches();
    }
}
