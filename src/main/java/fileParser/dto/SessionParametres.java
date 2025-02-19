package fileParser.dto;

import java.util.List;

public record SessionParametres(StatisticsType statisticsType,
                                String prefix,
                                String resultsPath,
                                Boolean append,
                                List<String> filesPathsLst) {

    public String getMessage() {
        return "Данные будут прочитаны из файлов: \n" +
                filesPathsLst.toString() + "\n" +
                "Со следующими параметрами: \n" +
                "* путь файлов результата: " + resultsPath + "\n" +
                "* префикс имён файлов: " + prefix + "\n" +
                "* добавить в конец файлов(вместо перезаписи): " + (append ? "да" : "нет") + "\n" +
                "* тип статистики: " + statisticsType.getMsg();
    }
}
