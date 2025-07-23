package fileParser.parametres;

public enum OptionsFlag {
    SHORT_STAT("-s"),
    FULL_STAT("-f"),
    APPEND("-a"),
    PREFIX("-p"),
    OUT_PATH("-o");

    private final String flag;

    OptionsFlag(String flag) {
        this.flag = flag;
    }

    public String getFlag() {
        return flag;
    }

    public static OptionsFlag fromString(String strk) {
        for (var e : OptionsFlag.values()) {
            if (e.flag.equals(strk)) return e;
        }
        return null;
    }
}
