# Тестовое задание для ЦФТ

Консольное приложение переписывает строки из файлов в порядке, указанном в командной строке  
(первая строка из первого файла, первая строка из второго файла и тд), в файлы по типам integer, float, string.  
Если данных какого-то типа не будет - файл не создастся.  
Приложение использует многопоточность для одновременного чтения и записи,  
не загружая при этом все файлы полностью в оперативную память.

## Зависимости:
* Java 21
* Maven 3.9.2
* Apache Commons Lang 3.17.0
* Junit 5.12 

## Инструкция по запуску:
1. Командой maven package собрать приложение
2. Открыть командную строку в директории с файлом .jar
3. Запустить командой:
```
java -jar file-parser-test-1.0.jar <параметры и пути файлов>
```

## Параметры:
* ```-o``` : задать путь для результатов.
Если путь начинается с "/path/.../" - папка "path" будет создана в корне диска,
если "path/.../" - в текущей директории. Пример: ```-o path``` 
* ```-p``` : задает префикс имен выходных файлов, пример: ```-p prefix```
* ```-a``` : добавление в конец файла вместо перезаписи
* ```-s``` : краткая статистика, количество элементов по типам
* ```-f``` : Полная статистика, выводит количество элементов, минимум, максимум, среднее значение.  
Для строк среднее значение не выводится.  

## Параметры по умолчанию:
* Путь для результатов: текущая директория
* Префикс: нет
* Перезапись файлов
* Статистика: нет

Параметры и имена файлов могут быть в любом порядке и количестве, но если они противоречат друг другу  
будет выбран последний.  
Также если указать например ```-p -a```, "-a" будет воспринят как префикс

Пример команды запуска:
```
java -jar file-parser-test-1.0.jar -a -s -p -prefix- -o path file1.txt file2.txt file3.txt
```

