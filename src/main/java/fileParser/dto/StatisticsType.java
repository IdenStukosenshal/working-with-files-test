package fileParser.dto;

public enum StatisticsType {

    SHORT("краткая"), FULL("полная"), NONE("нет");
    private final String msg;

    StatisticsType(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
