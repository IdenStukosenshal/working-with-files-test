package fileParser.dataStorage;

import fileParser.parameters.StatisticsType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class StatisticsHolder {

    private final StatisticsType statisticsType;

    private Long countBigIntegerNumbers = 0L;
    private BigInteger bigIntegerMinNumber = null;
    private BigInteger bigIntegerMaxNumber = null;
    private BigInteger bigIntegerSumNumbers = BigInteger.ZERO;

    private Long countDoubleNumbers = 0L;
    private Double doubleMinNumber = Double.MAX_VALUE;
    private Double doubleMaxNumber = Double.MIN_VALUE;
    private BigDecimal doubleSumNumbers = BigDecimal.valueOf(0);


    private Long stringCount = 0L;
    private Integer stringMinLength = Integer.MAX_VALUE;
    private Integer stringMaxLength = 0;

    public StatisticsHolder(StatisticsType statisticsType) {
        this.statisticsType = statisticsType;
    }

    public void increaseBigIntegerStatistics(BigInteger currentIntValue) {
        switch (statisticsType) {
            case SHORT -> countBigIntegerNumbers++;
            case FULL -> {
                if (bigIntegerMinNumber == null || currentIntValue.compareTo(bigIntegerMinNumber) < 0) {
                    bigIntegerMinNumber = currentIntValue;
                }
                if (bigIntegerMaxNumber == null || currentIntValue.compareTo(bigIntegerMaxNumber) > 0) {
                    bigIntegerMaxNumber = currentIntValue;
                }
                countBigIntegerNumbers++;
                bigIntegerSumNumbers = bigIntegerSumNumbers.add(currentIntValue);
            }
        }
    }

    public String getIntegerStatistics() {
        if (countBigIntegerNumbers != 0) {
            switch (statisticsType) {
                case SHORT -> {
                    return "Integer Statistics: \n" +
                            "количество чисел: " + countBigIntegerNumbers + "\n";
                }
                case FULL -> {
                    return "Integer Statistics: \n" +
                            "количество чисел: " + countBigIntegerNumbers +
                            ", минимальное значение: " + bigIntegerMinNumber +
                            ", максимальное значение: " + bigIntegerMaxNumber +
                            ", сумма всех значений: " + bigIntegerSumNumbers.toString() +
                            ", среднее значение: " + calculateMidInteger() + "\n";
                }
            }
        }
        return "";
    }

    public void increaseDoubleStatistics(Double currentValue) {
        switch (statisticsType) {
            case SHORT -> countDoubleNumbers++;
            case FULL -> {
                countDoubleNumbers++;
                if (currentValue < doubleMinNumber) doubleMinNumber = currentValue;
                if (currentValue > doubleMaxNumber) doubleMaxNumber = currentValue;
                doubleSumNumbers = doubleSumNumbers.add(BigDecimal.valueOf(currentValue));
            }
        }
    }

    public String getDoubleStatistics() {
        if (countDoubleNumbers != 0) {
            switch (statisticsType) {
                case SHORT -> {
                    return "Float Statistics: \n" +
                            "количество чисел: " + countDoubleNumbers + "\n";
                }
                case FULL -> {
                    return "Float Statistics: \n" +
                            "количество чисел: " + countDoubleNumbers +
                            ", минимальное значение: " + doubleMinNumber +
                            ", максимальное значение: " + doubleMaxNumber +
                            ", сумма всех значений: " + doubleSumNumbers +
                            ", среднее значение: " + calculateMidDouble() + "\n";
                }
            }
        }
        return "";
    }

    public void increaseStringStatistics(String currentString) {
        switch (statisticsType) {
            case SHORT -> stringCount++;
            case FULL -> {
                stringCount++;
                if (currentString.length() < stringMinLength) stringMinLength = currentString.length();
                if (currentString.length() > stringMaxLength) stringMaxLength = currentString.length();
            }
        }
    }

    public String getStringStatistics() {
        if (stringCount != 0) {
            switch (statisticsType) {
                case SHORT -> {
                    return "String Statistics :\n" +
                            "количество строк: " + stringCount + "\n";
                }
                case FULL -> {
                    return "String Statistics :\n" +
                            "количество строк: " + stringCount +
                            ", минимальная длина строки: " + stringMinLength + " символов" +
                            ", максимальная длина строки: " + stringMaxLength + " символов" + "\n";
                }
            }
        }
        return "";
    }

    private BigDecimal calculateMidInteger() {
        BigDecimal result = new BigDecimal(bigIntegerSumNumbers.toString());
        return result.divide(BigDecimal.valueOf(countBigIntegerNumbers), 3, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateMidDouble() {
        return doubleSumNumbers.divide(BigDecimal.valueOf(countDoubleNumbers), 3, RoundingMode.HALF_UP);
    }
}
