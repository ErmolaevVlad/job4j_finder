Проект содержит утилиту для поиска файлов

- Программа ищет данные в заданном каталоге и подкаталогах.
- Имя файла может задаваться: целиком, по маске, по регулярному выражению.
- Программа запускается с параметрами, например:  -d=c:  -n=*.?xt -t=mask -o=log.txt
- Ключи:
   -d - директория, в которой начинать поиск.
   -n - имя файла, маска, либо регулярное выражение.
   -t - тип поиска: mask искать по маске, name по полному совпадение имени, regex по регулярному выражению.
   -o - результат записать в файл.
- Программа записывает результат в файл.