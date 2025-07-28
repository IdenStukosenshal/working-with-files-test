package fileParser.parameters;

public enum OutFileNames {
    INTEGERS("integers.txt"),
    DOUBLES("floats.txt"),
    STRINGS("strings.txt");

    private final String fileName;

    OutFileNames(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
