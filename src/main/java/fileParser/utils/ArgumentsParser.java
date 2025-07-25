package fileParser.utils;

import fileParser.parametres.OptionsFlag;
import fileParser.parametres.SessionParametres;
import fileParser.parametres.StatisticsType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class ArgumentsParser {
    private final Pattern forbiddenWinNamePattern = Pattern.compile(".*[<>:\"|?*/\\\\]+.*");

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
        return processFlagWithArgument(
                i,
                args,
                "Префикс: %s не корректен и не будет применён",
                "Префикс не указан!",
                this::isValidName,
                sessionParametres::setPrefix);
    }

    private int processOutPath(int i, String[] args, SessionParametres sessionParametres) {
        return processFlagWithArgument(
                i,
                args,
                "Путь файлов результата: %s не корректен и не будет применён",
                "Путь файлов результата не указан!",
                this::isValidOutPath,
                sessionParametres::setResultsPath);
    }

    private int processFlagWithArgument(int i,
                                        String[] args,
                                        String errorIfNotCorrect,
                                        String errorIfMissing,
                                        Predicate<String> validator,
                                        Consumer<String> setter) {
        if (i < args.length - 1) {
            String value = args[i + 1];
            if (validator.test(value)) setter.accept(value);
            else System.out.println(errorIfNotCorrect.formatted(value));
        } else throw new IllegalArgumentException(errorIfMissing);
        return i + 1;
    }

    private void processFilePath(String currentArg, SessionParametres sessionParametres) {
        if (!currentArg.isBlank()) {
            if (isFileExists(currentArg)) sessionParametres.addToFilesPathsLst(currentArg);
            else System.out.println("Файл: " + currentArg + " не найден и не будет добавлен");
        }
    }

    private boolean isValidName(String prefix) {
        return !forbiddenWinNamePattern.matcher(prefix).matches();
    }

    private boolean isValidOutPath(String pathString) {
        try {
            Path.of(pathString.trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isFileExists(String pathString) {
        try {
            return Files.exists(Path.of(pathString.trim()));
        } catch (Exception e) {
            return false;
        }
    }
}
