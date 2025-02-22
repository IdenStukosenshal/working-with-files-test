package fileParser;

import fileParser.dto.SessionParametres;
import fileParser.dto.StatisticsType;

public class ArgumentsParser {

    public SessionParametres parse(String[] args) throws RuntimeException {
        SessionParametres sessionParametres = new SessionParametres();

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-s" -> sessionParametres.setStatisticsType(StatisticsType.SHORT);
                case "-f" -> sessionParametres.setStatisticsType(StatisticsType.FULL);
                case "-a" -> sessionParametres.setAppend(true);
                case "-p" -> {
                    if (i < args.length - 1) sessionParametres.setPrefix(args[++i]);
                }
                case "-o" -> {
                    if (i < args.length - 1) sessionParametres.setResultsPath(args[++i]);
                }
                default -> {
                    if (!args[i].isBlank()) sessionParametres.addToFilesPathsLst(args[i]);
                }
            }
        }
        if (sessionParametres.getFilesPathsLst().isEmpty()) throw new RuntimeException();

        return sessionParametres;
    }
}
