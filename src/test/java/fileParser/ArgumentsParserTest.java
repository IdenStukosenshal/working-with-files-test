package fileParser;

import fileParser.dto.SessionParametres;
import fileParser.dto.StatisticsType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArgumentsParserTest {
    private ArgumentsParser argumentsParser;
    private SessionParametres sessionParametres;


    @BeforeEach
    public void createArgumentsParser() {
        argumentsParser = new ArgumentsParser();
    }

    @Test
    public void mixedOrderFlagsAndFilePathsGeneratesCorrectResult() {
        String[] massive = new String[]{
                "path1/name1", "-a",
                "-p", "-prefix-",
                "-o", "res/path/",
                "path2/name2", "-f",
        };

        sessionParametres = argumentsParser.parse(massive);

        assertEquals(
                List.of("path1/name1", "path2/name2"),
                sessionParametres.filesPathsLst()
        );
        assertEquals(true, sessionParametres.append());
        assertEquals(StatisticsType.FULL, sessionParametres.statisticsType());
        assertEquals("-prefix-", sessionParametres.prefix());
        assertEquals("res/path/", sessionParametres.resultsPath());

    }

    @Test
    public void onlyFlagsWithoutFilePathsThrowsException(){
        String[] massive = new String[]{"-s", "-f", "-a", "-p", "prfx", "-o", "pth"};

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> argumentsParser.parse(massive)
        );
    }

    @Test
    public void onlyFilePathsGeneratesDefaultSettings() {
        String[] massive = new String[]{"path1/name1", "path2/name2"};

        sessionParametres = argumentsParser.parse(massive);

        assertEquals("", sessionParametres.prefix());
        assertEquals(System.getProperty("user.dir"), sessionParametres.resultsPath());
        assertEquals(false, sessionParametres.append());
        assertEquals(StatisticsType.NONE, sessionParametres.statisticsType());
        assertEquals(Arrays.stream(massive).collect(Collectors.toList()), sessionParametres.filesPathsLst());
    }


    @Test
    public void runWithoutArgumentsThrowsException() {
        String[] massive = new String[]{""};

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> argumentsParser.parse(massive)
        );
    }

    @Test
    public void blankFilePathsThrowsException() {
        String[] massive = new String[]{" ", "  ", "   "};

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> argumentsParser.parse(massive)
        );
    }

    @Test
    public void shortFlagGeneratesShortStatistics() {
        String[] massive = new String[]{"-s", "file"};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals(StatisticsType.SHORT, sessionParametres.statisticsType());
    }

    @Test
    public void fullFlagGeneratesFullStatistics() {
        String[] massive = new String[]{"-f", "file"};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals(StatisticsType.FULL, sessionParametres.statisticsType());
    }

    @Test
    public void multipleOppositeStatisticFlagsCausesTheLastToSet() {
        String[] massive = new String[]{"-f", "-s", "-f", "-s", "file"};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals(StatisticsType.SHORT, sessionParametres.statisticsType());
    }

    @Test
    public void appendFlagSetsAppendSettings() {
        String[] massive = new String[]{"file", "-a"};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals(true, sessionParametres.append());
    }

    @Test
    public void prefixFlagSetsPrefixSettings() {
        String[] massive = new String[]{"file", "-p", "-my-prefix"};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals("-my-prefix", sessionParametres.prefix());
    }

    @Test
    public void prefixFlagWithoutValueSetsDefaultPrefixSettings() {
        String[] massive = new String[]{"file", "-p"};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals("", sessionParametres.prefix());
    }

    @Test
    public void multiplePrefixFlagCausesTheLastToSet() {
        String[] massive = new String[]{"file", "-p", "prefix-one", "-p", "prefix-two", "-p", "prefix-three"};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals("prefix-three", sessionParametres.prefix());
    }

    @Test
    public void outputFlagSetsResultsPathSettings() {
        String[] massive = new String[]{"file", "-o", "result/path"};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals("result/path", sessionParametres.resultsPath());
    }

    @Test
    public void outputFlagWithoutValueSetsResultsPathDefaultSettings() {
        String[] massive = new String[]{"file", "-o"};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals(System.getProperty("user.dir"), sessionParametres.resultsPath());
    }

    @Test
    public void multipleOutputFlagCausesTheLastToSet() {
        String[] massive = new String[]{"file", "-o", "result1", "-o", "result2"};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals("result2", sessionParametres.resultsPath());
    }
}