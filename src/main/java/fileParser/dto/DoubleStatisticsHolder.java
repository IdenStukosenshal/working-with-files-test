package fileParser.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

public record DoubleStatisticsHolder(Long countNumbers,
                                     Double minNumber,
                                     Double maxNumber,
                                     BigDecimal sumNumbers,
                                     Double middleNumber){
    @Override
    public String toString() {
        return "Float Statistics: \n" +
                "количество чисел: " + countNumbers +
                ", минимальное значение: " + minNumber +
                ", максимальное значение: " + maxNumber +
                ", сумма всех значений: " + sumNumbers +
                ", среднее значение: " + middleNumber ;
    }
}
