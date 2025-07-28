package fileParser.parameters;

public enum ErrorMessages {
    READING_ERROR("Во время чтения файла: %s произошла ошибка. Данные из этого файла не будут читаться далее"),
    CLOSING_ERROR("Ошибка при попытке закрыть поток записи, часть информации может быть утеряна"),
    CREATING_DIR_ERROR("Не удалось создать директории"),
    WRITING_FILE_ERROR("Ошибка во время записи в файл: "),
    INTERRUPT_ERROR("Прерывание работы программы по неизвестной причине: "),

    INVALID_FILE_PATHS("Корректные пути файлов не были указаны"),

    INVALID_PREFIX("Префикс: %s не корректен и не будет применён"),
    PREFIX_MISSED("Префикс не указан!"),

    INVALID_OUT_PATH("Путь файлов результата: %s не корректен и не будет применён"),
    OUT_PATH_MISSED("Путь файлов результата не указан!"),

    FILE_NOT_FOUND("Файл: %s не найден и не будет добавлен"),

    INTERNAL_ERROR("Неизвестная ошибка!");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
