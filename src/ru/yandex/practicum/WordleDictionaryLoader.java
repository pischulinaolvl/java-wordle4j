package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {
    public static WordleDictionary getWordleDictionary(String fileName) {

        List<String> words = new ArrayList<>();
        try ( BufferedReader br = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            while (br.ready()) {
                String line = br.readLine();
                if (line.length() == 5) {
                    words.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка при загрузке справочника слов.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new WordleDictionary(words);
    }
}
