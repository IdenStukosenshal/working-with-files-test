package fileParser.dataStorage;

import fileParser.dto.SessionParametres;
import fileParser.dto.StatisticsType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class StatisticsHolder {

    private final StatisticsType statisticsType;

    private Long integerCountNumbers = 0L;
    private Integer integerMinNumber = Integer.MAX_VALUE;
    private Integer integerMaxNumber = Integer.MIN_VALUE;
    private BigInteger integerSumNumbers = BigInteger.valueOf(0);

    private Long doubleCountNumbers = 0L;
    private Double doubleMinNumber = Double.MAX_VALUE;
    private Double doubleMaxNumber = Double.MIN_VALUE;
    private BigDecimal doubleSumNumbers = BigDecimal.valueOf(0);


    private Long stringCount = 0L;
    private Integer stringMinLength = Integer.MAX_VALUE;
    private Integer stringMaxLength = 0;

    public StatisticsHolder(SessionParametres sessionParametres) {
        this.statisticsType = sessionParametres.getStatisticsType();
    }

    public void increaseIntegerStatistics(Integer currentIntValue) {
        switch (statisticsType) {
            case NONE -> {
            }
            case SHORT -> integerCountNumbers++;
            case FULL -> {
                integerCountNumbers++;
                if (currentIntValue < integerMinNumber) integerMinNumber = currentIntValue;
                if (currentIntValue > integerMaxNumber) integerMaxNumber = currentIntValue;
                integerSumNumbers = integerSumNumbers.add(BigInteger.valueOf(currentIntValue));
            }
        }
    }

    public String getIntegerStatistics() {
        if (integerCountNumbers != 0) {
            switch (statisticsType) {
                case SHORT -> {
                    return "Integer Statistics: \n" +
                            "количество чисел: " + integerCountNumbers +"\n";
                }
                case FULL -> {
                    return "Integer Statistics: \n" +
                            "количество чисел: " + integerCountNumbers +
                            ", минимальное значение: " + integerMinNumber +
                            ", максимальное значение: " + integerMaxNumber +
                            ", сумма всех значений: " + integerSumNumbers.toString() +
                            ", среднее значение: " + calculateMidInteger() +"\n";
                }
            }
        }
        return "";
    }

    public void increaseDoubleStatistics(Double currentValue) {
        switch (statisticsType) {
            case NONE -> {
            }
            case SHORT -> doubleCountNumbers++;
            case FULL -> {
                doubleCountNumbers++;
                if (currentValue < doubleMinNumber) doubleMinNumber = currentValue;
                if (currentValue > doubleMaxNumber) doubleMaxNumber = currentValue;
                doubleSumNumbers = doubleSumNumbers.add(BigDecimal.valueOf(currentValue));
            }
        }
    }

    public String getDoubleStatistics() {
        if (doubleCountNumbers != 0) {
            switch (statisticsType) {
                case SHORT -> {
                    return "Float Statistics: \n" +
                            "количество чисел: " + doubleCountNumbers +"\n";
                }
                case FULL -> {
                    return "Float Statistics: \n" +
                            "количество чисел: " + doubleCountNumbers +
                            ", минимальное значение: " + doubleMinNumber +
                            ", максимальное значение: " + doubleMaxNumber +
                            ", сумма всех значений: " + doubleSumNumbers +
                            ", среднее значение: " + calculateMidDouble() +"\n";
                }
            }
        }
        return "";
    }

    public void increaseStringStatistics(String currentString) {
        switch (statisticsType) {
            case NONE -> {
            }
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
                            "количество строк: " + stringCount +"\n";
                }
                case FULL -> {
                    return "String Statistics :\n" +
                            "количество строк: " + stringCount +
                            ", минимальная длина строки: " + stringMinLength + " символов" +
                            ", максимальная длина строки: " + stringMaxLength + " символов" +"\n";
                }
            }
        }
        return "";
    }

    private BigDecimal calculateMidInteger() {
        BigDecimal result = new BigDecimal(integerSumNumbers.toString());
        return result.divide(BigDecimal.valueOf(integerCountNumbers), 3, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateMidDouble() {
        return doubleSumNumbers.divide(BigDecimal.valueOf(doubleCountNumbers), 3, RoundingMode.HALF_UP);
    }
}
