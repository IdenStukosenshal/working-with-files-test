package fileParser.dto;

public record StringStatisticsHolder(Long countStrings,
                                     Integer minLength,
                                     Integer maxLength) {
    @Override
    public String toString() {
        return "String Statistics :\n" +
                "количество строк: " + countStrings +
                ", минимальная длина строки: " + minLength +
                ", максимальная длина строки: " + maxLength;
    }
}
