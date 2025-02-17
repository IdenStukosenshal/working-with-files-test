package fileParser;

import java.util.List;

public class ArgumentsParser {

    public SessionParametres parse(String[] args){ //TODO сделать!


        return new SessionParametres(
                StatisticsType.NONE,
                "префикс-файла-",
                "/папка1/папка2",
                true,
                List.of(
                        "src/main/java/fileParser/file1.txt",
                        "src/main/java/fileParser/file2.txt",
                        "src/main/java/fileParser/file3.txt")
        );
    }
}
