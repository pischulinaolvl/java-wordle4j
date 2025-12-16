package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.*;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    private static int maxSteps = 6;
    private String answer;
    private int steps;
    private WordleDictionary dictionary;

    //private HashMap<String, String> wordsTry = new HashMap<>();
    //private String[] wordsTry = new String[maxSteps];
    List<String> wordsTry = new ArrayList<>();
    private StringBuilder maskAnswer = new StringBuilder(".....");  // Если буква
    private StringBuilder lettersExists = new StringBuilder();
    private StringBuilder lettersNotExists = new StringBuilder();
    private Random random = new Random();

    public WordleGame(WordleDictionary wordDictionary){
        this.dictionary = wordDictionary;
        this.answer = chooseAnswer();
        steps = 0;
    }

    public String chooseAnswer(){
        int randomInt = random.nextInt(dictionary.getWords().size()-1); // генерирует новое число от 0 до 1000
        return dictionary.getWords().get(randomInt);
    }

    public void printWelcomeWords(){
        System.out.println( "Добро пожаловать в игру Wordle!");
        System.out.println( "Мы загадали слово из 5 букв. У вас будет 6 попыток, чтобы отгадать его.");
        System.out.println( "Начинаем!");
    }

    public boolean checkSteps(){
        return steps < maxSteps;
    }

    public boolean checkNewWordRussian(String newWord){
        return newWord.matches("^[а-яА-ЯёЁ]+$");
    }

    public String tryWord(String newWord, PrintWriter fileLog) throws WordleGameException{
        fileLog.println("Начало проверки вводного слова в методе tryWord");

        newWord = newWord.toLowerCase().replace('ё', 'е');
        if (newWord.length()!=answer.length()){
            throw new WordleGameException("Необходимо ввести слово, состоящее из 5 букв. Попробуйте еще раз");
        } else if (!checkNewWordRussian(newWord)){
            throw new WordleGameException("Необходимо ввести слово, состоящее только из букв русского алфавита. Попробуйте еще раз");
        } else if (!dictionary.getWords().contains(newWord)){
            throw new WordleGameException("Данное слово отсутствует в справочнике. Попробуйте еще раз");
        } else {
            wordsTry.add(newWord);
            steps = steps + 1;
            return checkAnswer(newWord, fileLog);
        }
    }

    public String checkAnswer(String newWord, PrintWriter fileLog){
        fileLog.println("Проверка пробного слова в методе checkAnswer");
        fileLog.println("Сравнение пробного слова с правильным ответом");
        if (newWord.equals(answer)){
            return "+++++";
        }

        StringBuilder result = new StringBuilder();
        fileLog.println("Начало побуквенного анализа пробного слова с ответом");
        for (int i = 0; i < answer.length(); i++){
            if (answer.charAt(i) == newWord.charAt(i)){
                maskAnswer.replace(i, i+1, answer.substring(i, i+1));
                result.append("+");
            }
            else if (answer.contains(newWord.substring(i, i+1))){
                result.append("^");
                if (lettersExists.indexOf(newWord.substring(i, i+1)) == -1){
                    lettersExists.append(newWord.charAt(i));
                }
            }
            else {
                result.append("-");
                if (lettersNotExists.indexOf(newWord.substring(i, i+1)) == -1){
                    lettersNotExists.append(newWord.charAt(i));
                }
            }
        }
        fileLog.println("Завершение побуквенного анализа пробного слова с ответом");
        return result.toString();
    }

    public String giveHint(PrintWriter fileLog) throws WordleGameException{
        boolean b;
        List<String> words = dictionary.getWords();
        fileLog.println("Начитали справочник слов");
        Collections.shuffle(words);
        fileLog.println("Перемешали слова в справочнике");
        int i;
        String result = "";
        for (i = 0; i < words.size(); i++){
            result = words.get(i);
            fileLog.println("Начитали " + (i+1) + " слово. Начали выполнение проверок");
            if (!result.matches(maskAnswer.toString())||wordsTry.contains(result)){
                continue;
            }
            b = true;
            for (int k = 0; k < lettersExists.length(); k++){
                if (!result.contains(lettersExists.substring(k, k+1))){
                    b = false;
                }
            }
            if (!b){
                continue;
            }
            b = true;
            for (int k = 0; k < lettersNotExists.length(); k++){
                if (result.contains(lettersNotExists.substring(k, k+1))){
                    b = false;
                }
            }
            if (!b){
                continue;
            }
            fileLog.println("Нашли подходящее слово");
            break;
        }

        if (i == words.size()){
            throw new WordleGameException("Не удалось подобрать подсказку");
        }

        return result;
    }

    public int getSteps() {
        return steps;
    }

    public String getAnswer() {
        return answer;
    }
}
