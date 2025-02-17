package fileParser;

import fileParser.dto.SessionParametres;
import fileParser.dto.StatisticsType;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ArgumentsParser {

    public SessionParametres parse(String[] args) throws FileNotFoundException {
        StatisticsType statisticsType = StatisticsType.NONE;
        String prefix = "";
        String resultsPath = ""; //корневой каталог
        boolean append = false;
        List<String> filesPathsLst = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-s" -> statisticsType = StatisticsType.SHORT;
                case "-f" -> statisticsType = StatisticsType.FULL;
                case "-a" -> append = true;
                case "-p" -> prefix = args[++i];
                case "-o" -> resultsPath = args[++i];
                default -> filesPathsLst.add(args[i]);
            }
        }
        if (filesPathsLst.isEmpty()) throw new FileNotFoundException();

        return new SessionParametres(
                statisticsType,
                prefix,
                resultsPath,
                append,
                filesPathsLst
        );
    }
}
