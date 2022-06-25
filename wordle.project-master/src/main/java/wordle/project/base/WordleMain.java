package wordle.project.base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class WordleMain {
        //Se inicializan los metodos necesarios
	public static void main(String[] args) {
		String todaysWord = fetchWord();
		String userWord =getUserInput();
		List<String> wordAsList = Arrays.asList(todaysWord.split(""));
		List<String> userInputAsList = Arrays.asList(userWord.split(""));
		
		compareWordWithUserInput(wordAsList,userInputAsList);
	}

	private static void compareWordWithUserInput(List<String> wordAsList, List<String> userInputAsList) {
		
		
	}
        //Se obtiene la palabra insertada por el usuario
	private static String getUserInput() {
		Scanner keyboard = new Scanner(System.in);
		System.out.print("Enter a word:");
		if(keyboard.hasNext()) {
			String userInput = keyboard.next();
			System.out.println("The user word is : "+userInput);
			return userInput;
		}
		return "";
	}
        //Se obtiene las palabras de la lista
	private static String fetchWord() {
		File wordFile = new File("..\\wordle.project-master\\assets\\Words.txt");
		List<String> wordsList = new ArrayList<String>();
		try {
			wordsList = Files.readAllLines(wordFile.toPath());
		}catch(IOException ex) {
			
		}
		Random random = new Random();
		String wordOfTheDay = wordsList.get(random.nextInt(wordsList.size()));
		System.out.println(wordOfTheDay);
		return wordOfTheDay.trim();
	}

}
