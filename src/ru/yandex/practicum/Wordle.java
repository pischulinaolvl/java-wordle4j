package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {

    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try (PrintWriter fileLog = new PrintWriter (new FileWriter("C:\\Users\\pischulinaov\\IdeaProjects\\java-wordle4j\\_Logs\\log.txt", StandardCharsets.UTF_8))) {

            WordleDictionary wordleDictionary = WordleDictionaryLoader.getWordleDictionary("C:\\Users\\pischulinaov\\IdeaProjects\\java-wordle4j\\words_ru.txt");
            fileLog.println("Получен справочник слов для игры");

            WordleGame wordleGame = new WordleGame(wordleDictionary);
            fileLog.println("Создан экземпляр игры");
            wordleGame.printWelcomeWords();
            String resultCheckWord = "";

            fileLog.println("Запущен цикл по обработке попыток игрока");
            while (wordleGame.checkSteps()) {
                System.out.println("Попытка " + (wordleGame.getSteps() + 1) + ". Введите слово");
                String newWord = scanner.nextLine();
                try {
                    if (newWord.isEmpty()) {
                        newWord = wordleGame.giveHint(fileLog);
                        System.out.println("Подсказка: " + newWord);
                    }
                    resultCheckWord = wordleGame.tryWord(newWord, fileLog);
                    System.out.println("Результат проверки слова: " + resultCheckWord);
                    if (resultCheckWord.equals("+++++")) {
                        break;
                    }
                    System.out.println();
                } catch (WordleGameException e) {
                    System.out.println(e.getMessage());
                }
            }

            if (resultCheckWord.equals("+++++")) {
                System.out.println("Поздравляем с победой! Вы отгадали слово.");
            } else {
                System.out.println("Игра завершена. Было загадано слово " + wordleGame.getAnswer() + ".");
            }
        }
        catch (IOException e) {
            System.out.println("Произошла ошибка во время записи файла.");
        }
    }
}