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
                sessionParametres.getFilesPathsLst()
        );
        assertEquals(true, sessionParametres.getAppend());
        assertEquals(StatisticsType.FULL, sessionParametres.getStatisticsType());
        assertEquals("-prefix-", sessionParametres.getPrefix());
        assertEquals("res/path/", sessionParametres.getResultsPath());

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

        assertEquals("", sessionParametres.getPrefix());
        assertEquals(System.getProperty("user.dir"), sessionParametres.getResultsPath());
        assertEquals(false, sessionParametres.getAppend());
        assertEquals(StatisticsType.NONE, sessionParametres.getStatisticsType());
        assertEquals(Arrays.stream(massive).collect(Collectors.toList()), sessionParametres.getFilesPathsLst());
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
        assertEquals(StatisticsType.SHORT, sessionParametres.getStatisticsType());
    }

    @Test
    public void fullFlagGeneratesFullStatistics() {
        String[] massive = new String[]{"-f", "file"};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals(StatisticsType.FULL, sessionParametres.getStatisticsType());
    }

    @Test
    public void multipleOppositeStatisticFlagsCausesTheLastToSet() {
        String[] massive = new String[]{"-f", "-s", "-f", "-s", "file"};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals(StatisticsType.SHORT, sessionParametres.getStatisticsType());
    }

    @Test
    public void appendFlagSetsAppendSettings() {
        String[] massive = new String[]{"file", "-a"};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals(true, sessionParametres.getAppend());
    }

    @Test
    public void prefixFlagSetsPrefixSettings() {
        String[] massive = new String[]{"file", "-p", "-my-prefix"};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals("-my-prefix", sessionParametres.getPrefix());
    }

    @Test
    public void prefixFlagWithoutValueSetsDefaultPrefixSettings() {
        String[] massive = new String[]{"file", "-p"};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals("", sessionParametres.getPrefix());
    }

    @Test
    public void multiplePrefixFlagCausesTheLastToSet() {
        String[] massive = new String[]{"file", "-p", "prefix-one", "-p", "prefix-two", "-p", "prefix-three"};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals("prefix-three", sessionParametres.getPrefix());
    }

    @Test
    public void outputFlagSetsResultsPathSettings() {
        String[] massive = new String[]{"file", "-o", "result/path"};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals("result/path", sessionParametres.getResultsPath());
    }

    @Test
    public void outputFlagWithoutValueSetsResultsPathDefaultSettings() {
        String[] massive = new String[]{"file", "-o"};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals(System.getProperty("user.dir"), sessionParametres.getResultsPath());
    }

    @Test
    public void multipleOutputFlagCausesTheLastToSet() {
        String[] massive = new String[]{"file", "-o", "result1", "-o", "result2"};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals("result2", sessionParametres.getResultsPath());
    }
}