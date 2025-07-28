package fileParser.utils;

import fileParser.parameters.OptionsFlag;
import fileParser.parameters.SessionParametres;
import fileParser.parameters.StatisticsType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static fileParser.parameters.ErrorMessages.*;

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
            throw new IllegalArgumentException(INVALID_FILE_PATHS.getMessage());
        return sessionParametres;
    }

    private int processPrefix(int i, String[] args, SessionParametres sessionParametres) {
        return processFlagWithArgument(
                i,
                args,
                INVALID_PREFIX.getMessage(),
                PREFIX_MISSED.getMessage(),
                this::isValidName,
                sessionParametres::setPrefix);
    }

    private int processOutPath(int i, String[] args, SessionParametres sessionParametres) {
        return processFlagWithArgument(
                i,
                args,
                INVALID_OUT_PATH.getMessage(),
                OUT_PATH_MISSED.getMessage(),
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
            else System.out.println(FILE_NOT_FOUND.getMessage().formatted(currentArg));
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
