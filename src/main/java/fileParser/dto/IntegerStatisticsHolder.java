package fileParser.dto;

import java.math.BigInteger;

public record IntegerStatisticsHolder(Long countNumbers,
                                      Integer minNumber,
                                      Integer maxNumber,
                                      BigInteger sumNumbers,
                                      Double middleNumber) {

    @Override
    public String toString() {
        return "Integer Statistics: \n" +
                "количество чисел: " + countNumbers +
                ", минимальное значение: " + minNumber +
                ", максимальное значение: " + maxNumber +
                ", сумма всех значений: " + sumNumbers +
                ", среднее значение: " + middleNumber ;
    }
}

