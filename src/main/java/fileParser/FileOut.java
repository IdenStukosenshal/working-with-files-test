package fileParser;

import java.util.List;

public class FileOut {

    private String prefix;
    private String resultsPath;
    private Boolean append;

    public FileOut(String prefix, String resultsPath, Boolean append) {
        this.prefix = prefix;
        this.resultsPath = resultsPath;
        this.append = append;
    }

    public void writeFiles(FileProcessor fileProcessor){
        fileProcessor.getIntegersLst();
        fileProcessor.getDoublesLst();
        fileProcessor.getStringsLst();
    }
}
