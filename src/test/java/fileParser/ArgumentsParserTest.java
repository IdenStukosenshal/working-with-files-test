package fileParser;

import fileParser.parametres.SessionParametres;
import fileParser.parametres.StatisticsType;
import fileParser.utils.ArgumentsParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArgumentsParserTest {

    private ArgumentsParser argumentsParser;
    private SessionParametres sessionParametres;
    private final List<String> filePathsLst = new ArrayList<>();
    private final List<File> filesLst = new ArrayList<>();


    @BeforeEach
    public void setUp() throws IOException {
        this.argumentsParser = new ArgumentsParser();
        filesLst.add(File.createTempFile("tmpForTest_1", ".txt"));
        filesLst.add(File.createTempFile("tmpForTest_2", ".txt"));
        filePathsLst.add(filesLst.get(0).getAbsolutePath());
        filePathsLst.add(filesLst.get(1).getAbsolutePath());
    }

    @AfterEach
    public void deleteFiles() {
        for (File file : filesLst) {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    @Test
    public void mixedOrderFlagsAndFilePathsGeneratesCorrectResult() {
        String filePath1 = filePathsLst.get(0);
        String filePath2 = filePathsLst.get(1);
        String[] massive = new String[]{
                filePath1, "-a",
                "-p", "-prefix-",
                "-o", "res/path/",
                filePath2, "-f",
        };

        sessionParametres = argumentsParser.parse(massive);

        assertEquals(
                List.of(filePath1, filePath2),
                sessionParametres.getFilesPathsLst()
        );
        assertEquals(true, sessionParametres.getAppend());
        assertEquals(StatisticsType.FULL, sessionParametres.getStatisticsType());
        assertEquals("-prefix-", sessionParametres.getPrefix());
        assertEquals("res/path/", sessionParametres.getResultsPath());

    }

    @Test
    public void onlyFlagsWithoutFilePathsThrowsException() {
        String[] massive = new String[]{"-s", "-f", "-a", "-p", "prfx", "-o", "pth"};

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> argumentsParser.parse(massive)
        );
    }

    @Test
    public void onlyFilePathsGeneratesDefaultSettings() {
        String[] massive = new String[]{filePathsLst.get(0), filePathsLst.get(1)};

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
        String[] massive = new String[]{"-s", filePathsLst.getFirst()};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals(StatisticsType.SHORT, sessionParametres.getStatisticsType());
    }

    @Test
    public void fullFlagGeneratesFullStatistics() {
        String[] massive = new String[]{"-f", filePathsLst.getFirst()};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals(StatisticsType.FULL, sessionParametres.getStatisticsType());
    }

    @Test
    public void multipleOppositeFlagsCausesTheLastToSet() {
        String[] massive = new String[]{"-f", "-s", "-f", "-s", filePathsLst.getFirst()};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals(StatisticsType.SHORT, sessionParametres.getStatisticsType());
    }

    @Test
    public void appendFlagSetsAppendSettings() {
        String[] massive = new String[]{filePathsLst.getFirst(), "-a"};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals(true, sessionParametres.getAppend());
    }

    @Test
    public void prefixFlagSetsPrefixSettings() {
        String[] massive = new String[]{filePathsLst.getFirst(), "-p", "-my-prefix"};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals("-my-prefix", sessionParametres.getPrefix());
    }

    @Test
    public void prefixFlagWithoutValueThrowsException() {
        String[] massive = new String[]{filePathsLst.getFirst(), "-p"};
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> argumentsParser.parse(massive)
        );
    }

    @Test
    public void outputFlagSetsResultsPathSettings() {
        String[] massive = new String[]{filePathsLst.getFirst(), "-o", "result/path"};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals("result/path", sessionParametres.getResultsPath());
    }

    @Test
    public void outputFlagWithoutValueThrowsException() {
        String[] massive = new String[]{filePathsLst.getFirst(), "-o"};
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> argumentsParser.parse(massive)
        );
    }

    @Test
    public void invalidPrefixSetsDefaultPrefixSettings() {
        String[] massive = new String[]{filePathsLst.getFirst(), "-p", "\\/ ?inc|o*rr>e<c:t"};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals("", sessionParametres.getPrefix());
    }

    @Test
    public void invalidFilePathsWillBeSkipped() {
        String[] massive = new String[]{filePathsLst.getFirst(), "path\"/*|?pa\\th"};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals(List.of(filePathsLst.getFirst()), sessionParametres.getFilesPathsLst());
    }

    @Test
    public void invalidResultPathsSetsDefault() {
        String[] massive = new String[]{filePathsLst.getFirst(), "-o", "pat>h\"/*\\|?pa<th"};
        sessionParametres = argumentsParser.parse(massive);
        assertEquals(System.getProperty("user.dir"), sessionParametres.getResultsPath());
    }

    @Test
    public void onlyInvalidFilePathsThrowsException() {
        String[] massive = new String[]{" ", "?pa<t", "pat>h\"/*"};

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> argumentsParser.parse(massive)
        );
    }
}