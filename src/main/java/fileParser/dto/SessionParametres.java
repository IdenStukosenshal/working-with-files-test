package fileParser.dto;

import java.util.ArrayList;
import java.util.List;

public class SessionParametres {
    private StatisticsType statisticsType = StatisticsType.NONE;
    private String prefix = "";
    private String resultsPath = System.getProperty("user.dir");
    private Boolean append = false;
    private List<String> filesPathsLst = new ArrayList<>();

    public String getMessage() {
        return "Данные будут прочитаны из файлов: \n" +
                filesPathsLst.toString() + "\n" +
                "Со следующими параметрами: \n" +
                "* путь файлов результата: " + resultsPath + "\n" +
                "* префикс имён файлов: " + (!prefix.isEmpty() ? prefix : "нет" ) + "\n" +
                "* добавить в конец файлов(вместо перезаписи): " + (append ? "да" : "нет") + "\n" +
                "* тип статистики: " + statisticsType.getMsg();
    }

    public StatisticsType getStatisticsType() {
        return statisticsType;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getResultsPath() {
        return resultsPath;
    }

    public Boolean getAppend() {
        return append;
    }

    public List<String> getFilesPathsLst() {
        return filesPathsLst;
    }

    public void setStatisticsType(StatisticsType statisticsType) {
        this.statisticsType = statisticsType;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setResultsPath(String resultsPath) {
        this.resultsPath = resultsPath;
    }

    public void setAppend(Boolean append) {
        this.append = append;
    }

    public void setFilesPathsLst(List<String> filesPathsLst) {
        this.filesPathsLst = filesPathsLst;
    }

    public void addToFilesPathsLst(String filesPaths) {
        this.filesPathsLst.add(filesPaths);
    }
}
