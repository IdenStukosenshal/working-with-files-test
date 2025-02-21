package fileParser;

import fileParser.dto.SessionParametres;
import fileParser.dto.StatisticsType;

import java.util.ArrayList;
import java.util.List;

public class ArgumentsParser {

    public SessionParametres parse(String[] args) throws RuntimeException {
        StatisticsType statisticsType = StatisticsType.NONE;
        String prefix = "";
        String resultsPath = System.getProperty("user.dir");
        boolean append = false;
        List<String> filesPathsLst = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-s" -> statisticsType = StatisticsType.SHORT;
                case "-f" -> statisticsType = StatisticsType.FULL;
                case "-a" -> append = true;
                case "-p" -> {
                    if (i < args.length - 1) prefix = args[++i];
                }
                case "-o" -> {
                    if (i < args.length - 1) resultsPath = args[++i];
                }
                default -> {
                    if (!args[i].isBlank()) filesPathsLst.add(args[i]);
                }
            }
        }
        if (filesPathsLst.isEmpty()) throw new RuntimeException();

        return new SessionParametres(
                statisticsType,
                prefix,
                resultsPath,
                append,
                filesPathsLst
        );
    }
}
