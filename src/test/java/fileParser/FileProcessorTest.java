package fileParser;

import fileParser.dataStorage.DataHolder;
import fileParser.dataStorage.StatisticsHolder;
import fileParser.dto.SessionParametres;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileProcessorTest {
    private final DataHolder dataHolder = new DataHolder();
    private final SessionParametres sessionParametres = new SessionParametres();
    private final StatisticsHolder statisticsHolder = new StatisticsHolder(sessionParametres);
    private AtomicBoolean isFinished = new AtomicBoolean(false);
    private final List<File> filesLst = new ArrayList<>();


    @BeforeEach
    public void setUp() throws IOException {
        filesLst.add(File.createTempFile("tmpForTest-1", ".txt"));
        filesLst.add(File.createTempFile("tmpForTest-2", ".txt"));
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
    public void correctPathsCausesCorrectOrderResults() throws Exception {
        Runnable fileProcessor = createDefaultFileProcessorObj();
        for (int i = 1; i < filesLst.size() + 1; i++) {
            try (FileWriter fileWriter = new FileWriter(filesLst.get(i - 1))) {
                fileWriter.write("row number " + (i) + "\n");
                fileWriter.write((i * 10) + "\n");
                fileWriter.write((i * 1.11) + "\n");
            }
        }
        runAndWaitThread(fileProcessor);

        assertTrue(isFinished.get());
        assertEquals("row number 1", dataHolder.getOneString());
        assertEquals("row number 2", dataHolder.getOneString());
        assertEquals(10, dataHolder.getOneInteger());
        assertEquals(20, dataHolder.getOneInteger());
        assertEquals(1.11, dataHolder.getOneDouble(), 0.005);
        assertEquals(2.22, dataHolder.getOneDouble(), 0.005);
        assertTrue(
                dataHolder.getDoublesQueue().isEmpty() &&
                        dataHolder.getIntegersQueue().isEmpty() &&
                        dataHolder.getStringsQueue().isEmpty()
        );
    }

    @Test
    public void filesContainsOnlyIntegersCausesOnlyIntegersResult() throws Exception {
        Runnable fileProcessor = createDefaultFileProcessorObj();
        for (int i = 1; i < filesLst.size() + 1; i++) {
            try (FileWriter fileWriter = new FileWriter(filesLst.get(i - 1))) {
                fileWriter.write((i * 8) + "\n"); // 8,      16
                fileWriter.write(String.valueOf(i * 21)); //21,     42
            }
        }
        runAndWaitThread(fileProcessor);

        assertTrue(isFinished.get());
        List<Integer> resultList = dataHolder.getIntegersQueue().stream().toList();
        assertEquals(List.of(8, 16, 21, 42), resultList);
        assertTrue(
                dataHolder.getDoublesQueue().isEmpty() &&
                        dataHolder.getStringsQueue().isEmpty()
        );
    }

    @Test
    public void filesContainsOnlyDoublesCausesOnlyDoublesResult() throws Exception {
        Runnable fileProcessor = createDefaultFileProcessorObj();
        for (int i = 1; i < filesLst.size() + 1; i++) {
            try (FileWriter fileWriter = new FileWriter(filesLst.get(i - 1))) {
                fileWriter.write((i * 1.2) + "\n");
                fileWriter.write(String.valueOf(i * 1.3));
            }
        }
        runAndWaitThread(fileProcessor);

        assertTrue(isFinished.get());
        List<Double> resultList = dataHolder.getDoublesQueue().stream().toList();
        assertEquals(List.of(1.2, 2.4, 1.3, 2.6), resultList);
        assertTrue(
                dataHolder.getIntegersQueue().isEmpty() &&
                        dataHolder.getStringsQueue().isEmpty()
        );

    }

    @Test
    public void filesContainsOnlyStringsCausesOnlyStringsResult() throws Exception {
        Runnable fileProcessor = createDefaultFileProcessorObj();
        for (int i = 1; i < filesLst.size() + 1; i++) {
            try (FileWriter fileWriter = new FileWriter(filesLst.get(i - 1))) {
                fileWriter.write("row " + (i * 2) + "\n");
                fileWriter.write("row " + (i * 3));
            }
        }
        runAndWaitThread(fileProcessor);

        assertTrue(isFinished.get());
        List<String> resultList = dataHolder.getStringsQueue().stream().toList();
        assertEquals(List.of("row 2", "row 4", "row 3", "row 6"), resultList);
        assertTrue(
                dataHolder.getIntegersQueue().isEmpty() &&
                        dataHolder.getDoublesQueue().isEmpty()
        );
    }

    @Test
    public void onlyIncorrectPathsCauseEmptyResults() throws Exception {
        Runnable fileProcessor = new FileProcessor(
                List.of("?#$%\"", ",.?/\\"),
                dataHolder,
                statisticsHolder,
                isFinished
        );
        runAndWaitThread(fileProcessor);

        assertTrue(isFinished.get());
        assertTrue(
                dataHolder.getDoublesQueue().isEmpty() &&
                        dataHolder.getIntegersQueue().isEmpty() &&
                        dataHolder.getStringsQueue().isEmpty()
        );
    }

    @Test
    public void correctPathsToNonExistentFilesCauseEmptyResults() throws Exception {
        Runnable fileProcessor = new FileProcessor(
                List.of("no-file1", "no-file2"),
                dataHolder,
                statisticsHolder,
                isFinished
        );
        runAndWaitThread(fileProcessor);

        assertTrue(isFinished.get());
        assertTrue(
                dataHolder.getDoublesQueue().isEmpty() &&
                        dataHolder.getIntegersQueue().isEmpty() &&
                        dataHolder.getStringsQueue().isEmpty()
        );
    }

    @Test
    public void incorrectPathsWillBeSkipped() throws Exception {
        File fileToWrite = filesLst.get(0);
        Runnable fileProcessor = new FileProcessor(
                List.of(",.?/", "?%\"", "wrong/path", fileToWrite.getAbsolutePath()),
                dataHolder,
                statisticsHolder,
                isFinished
        );
        try (FileWriter fileWriter = new FileWriter(fileToWrite)) {
            fileWriter.write("first row\n");
            fileWriter.write(100500 + "\n");
            fileWriter.write(3.14 + "\n");
        }
        runAndWaitThread(fileProcessor);

        assertTrue(isFinished.get());
        assertEquals(100500, dataHolder.getOneInteger());
        assertEquals("first row", dataHolder.getOneString());
        assertEquals(3.14, dataHolder.getOneDouble());
        assertTrue(
                dataHolder.getDoublesQueue().isEmpty() &&
                        dataHolder.getIntegersQueue().isEmpty() &&
                        dataHolder.getStringsQueue().isEmpty()
        );
    }

    private Runnable createDefaultFileProcessorObj(){
        return new FileProcessor(
                filesLst.stream().map(File::getAbsolutePath).collect(Collectors.toList()),
                dataHolder,
                statisticsHolder,
                isFinished
        );
    }

    private void runAndWaitThread(Runnable fileProcessor) throws InterruptedException {
        Thread fileProcessingThread = new Thread(fileProcessor);
        fileProcessingThread.start();
        fileProcessingThread.join();
    }
}