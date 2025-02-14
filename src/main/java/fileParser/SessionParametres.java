package fileParser;

import java.util.List;

public record SessionParametres(StatisticsType statisticsType,
                                String prefix,
                                String resultsPath,
                                Boolean append,
                                List<String> filesPathsLst) {
}
