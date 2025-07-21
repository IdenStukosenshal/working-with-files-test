package fileParser.utils;

import fileParser.dto.OptionsFlag;
import fileParser.dto.SessionParametres;
import fileParser.dto.StatisticsType;

import java.nio.file.Path;
import java.util.regex.Pattern;

public class ArgumentsParser {
    private final Pattern forbiddenPrefixPattern = Pattern.compile(".*[<>:\"|?*/\\\\]+.*");

    public SessionParametres parse(String[] args) throws RuntimeException {
        SessionParametres sessionParametres = new SessionParametres();
        for (int i = 0; i < args.length; i++) {
            String currentArg = args[i];
            OptionsFlag flag = OptionsFlag.fromString(currentArg);
            if (flag != null) {
                switch (flag) {
                    case SHORT_STAT -> sessionParametres.setStatisticsType(StatisticsType.SHORT);
                    case FULL_STAT -> sessionParametres.setStatisticsType(StatisticsType.FULL);
                    case APPEND -> sessionParametres.setAppend(true);
                    case PREFIX -> i = processPrefix(i, args, sessionParametres);
                    case OUT_PATH -> i = processOutPath(i, args, sessionParametres);
                }
            } else {
                processFilePath(currentArg, sessionParametres);
            }
        }
        if (sessionParametres.getFilesPathsLst().isEmpty())
            throw new RuntimeException("Корректные пути файлов не были указаны");
        return sessionParametres;
    }

    private int processPrefix(int i, String[] args, SessionParametres sessionParametres) {
        if (i < args.length - 1) {
            String prefix = args[i + 1];
            if (validatePrefix(prefix)) sessionParametres.setPrefix(prefix);
            else System.out.println("Префикс: " + prefix + " не корректен и не будет применён");
        } else throw new IllegalArgumentException("Префикс не указан!");
        return i + 1;
    }

    private int processOutPath(int i, String[] args, SessionParametres sessionParametres) {
        if (i < args.length - 1) {
            String path = args[i + 1];
            if (validatePath(path)) sessionParametres.setResultsPath(path);
            else System.out.println("Путь файлов результата: " + path + "не корректен и не будет применён");
        } else throw new IllegalArgumentException("Путь файлов результата не указан!");
        return i + 1;
    }

    private void processFilePath(String currentArg, SessionParametres sessionParametres) {
        if (!currentArg.isBlank()) {
            if (validatePath(currentArg)) sessionParametres.addToFilesPathsLst(currentArg);
            else System.out.println("Путь: " + currentArg + " не корректен и не будет добавлен");
        }
    }

    private boolean validatePrefix(String prefix) {
        return !forbiddenPrefixPattern.matcher(prefix).matches();
    }

    private boolean validatePath(String pathString) {
        try {
            Path.of(pathString.trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
