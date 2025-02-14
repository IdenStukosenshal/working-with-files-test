package fileParser;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


class FileProcessorTest {

    private FileProcessor fileProcessor;

    private static File testFile;

    @BeforeEach
    public void setup() throws IOException {
        fileProcessor = new FileProcessor();
        testFile = File.createTempFile("tmpFileForTest", ".txt");
    }
    @AfterAll
    public static void deletingTmpFile(){
        if(testFile.exists()){
            testFile.delete();
        }
    }


    @Test
    void givenOnlyIntegersResultsListWithIntegers() throws IOException {
        write(testFile, "321\n543\n1\n231312324\n999999999999999999999999");

        fileProcessor.fileProcessing(testFile.getAbsolutePath());

        assertEquals(List.of(321, 543, 1, 231312324), fileProcessor.getIntegersLst());

    }
    @Test
    void givenOnlyDoubleResultsListWithDoubles() throws IOException {
        write(testFile, "1.528535047E-25\n34.2\n0.1\n-0.0000001");

        fileProcessor.fileProcessing(testFile.getAbsolutePath());

        assertEquals(List.of(1.528535047E-25, 34.2, 0.1, -0.0000001), fileProcessor.getDoublesLst());
    }
    @Test
    void stringtt() throws IOException {
    }
    @Test
    void mixedtt() throws IOException {
    }

    private void write(File file, String text) throws IOException{
        try(FileWriter fw = new FileWriter(file)){
            fw.write(text);
        }
    }
}