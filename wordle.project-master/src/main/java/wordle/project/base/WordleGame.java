package wordle.project.base;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class WordleGame implements ActionListener {

	class WordPanel extends JPanel {
                //Se crea la clase del panel de las palabras
		JLabel[] wordColumns = new JLabel[5];
                //Constructor en el cual se crea visualmente el panel
		public WordPanel() {
			this.setLayout(new GridLayout(1, 5));
			Border blackBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
			for (int i = 0; i < 5; i++) {
				wordColumns[i] = new JLabel();
				wordColumns[i].setHorizontalAlignment(JLabel.CENTER);
				wordColumns[i].setOpaque(true);
				wordColumns[i].setBorder(blackBorder);
				this.add(wordColumns[i]);
			}
		}
                //Metodo para limpiar el panel
		public void clearWordPanel() {
			for (int i = 0; i < 5; i++) {
				wordColumns[i].setText("");
			}
		}
                //Metodo para colocar el texto en el panel
		public void setPanelText(String charValue, int position, Color color) {
			this.wordColumns[position].setText(charValue);
			this.wordColumns[position].setBackground(color);
		}
	}
        //Clase de panel del usuario
	class UserPanel extends JPanel {

		private JTextField userInput;
		private JButton okButton;

		public UserPanel() {
			this.setLayout(new GridLayout(1, 2));
			userInput = new JTextField();
			this.add(userInput);
			okButton = new JButton("OK");
			this.add(okButton);
		}
                //Campo de insercion de la palabra
		public JTextField getUserInput() {
			return userInput;
		}
                //Botón  de OK
		public JButton getOkButton() {
			return okButton;
		}

	}

	private JFrame gameFrame;
	private WordPanel[] wordPanelArray = new WordPanel[6];
	private UserPanel userPanel;
	private String wordleString;
	private int count = 0;
        //Constructor de la clase del la logica del juego
	public WordleGame() {
		gameFrame = new JFrame("Wordle Game");
		gameFrame.setSize(300, 300);
		gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		gameFrame.setLayout(new GridLayout(7, 1));
		gameFrame.setVisible(true);
		gameFrame.setLocationRelativeTo(null);

		for (int i = 0; i < 6; i++) {
			wordPanelArray[i] = new WordPanel();
			gameFrame.add(wordPanelArray[i]);
		}
		userPanel = new UserPanel();
		userPanel.getOkButton().addActionListener(this);
		gameFrame.add(userPanel);
		gameFrame.revalidate();

		wordleString = getWordleString();
		System.out.println("Word for the day : " + wordleString);
	}
        
	public static void main(String[] args) {
		new WordleGame();
	}
        
	@Override
	public void actionPerformed(ActionEvent e) {
		String userWord = this.userPanel.getUserInput().getText();
                //En caso de que se acierte la palabra
		if (userWord.length() > 4) {
			if (isWordleWordEqualTo(userWord.trim().toUpperCase())) {
				clearAllPanels();
				JOptionPane.showMessageDialog(null, "You Win!!!", "Congrats", JOptionPane.INFORMATION_MESSAGE);
				gameFrame.dispose();
				return;
			}
		}//En caso de que se acaben los intentos
		if (count > 5) {
			JOptionPane.showMessageDialog(null, "You Lost.Better luck next time.", "Oops",
					JOptionPane.INFORMATION_MESSAGE);
			gameFrame.dispose();
			return;
		}
		count++;
	}
        //Limpiar los paneles del juego
	private void clearAllPanels() {
		for (int i = 0; i <= count; i++) {
			wordPanelArray[i].clearWordPanel();
		}
	}
        //Funcion en la cual se determina si la palabra es igual a la que se escoge
	private boolean isWordleWordEqualTo(String userWord) {
		List<String> wordleWordsList = Arrays.asList(wordleString.split(""));
		String[] userWordsArray = userWord.split("");
		List<Boolean> wordMatchesList = new ArrayList<Boolean>();

		for (int i = 0; i < 5; i++) {
			if (wordleWordsList.contains(userWordsArray[i])) {
				if (wordleWordsList.get(i).equals(userWordsArray[i])) {
					getActivePanel().setPanelText(userWordsArray[i], i, Color.GREEN);
					wordMatchesList.add(true);
				} else {
					getActivePanel().setPanelText(userWordsArray[i], i, Color.YELLOW);
					wordMatchesList.add(false);
				}
			} else {
				getActivePanel().setPanelText(userWordsArray[i], i, Color.GRAY);
				wordMatchesList.add(false);
			}
		}
		return !wordMatchesList.contains(false);
	}
        
	public WordPanel getActivePanel() {
		return this.wordPanelArray[count];
	}
        //Obtener palabra de la lista de palabras predeterminadas.
	public String getWordleString() {
                //Se debe colocar la ruta de donde se tenga almacenado el archivo
                Path path = Paths.get("..\\wordle.project-master\\assets\\Words.txt");
                List<String> wordList = new ArrayList<String>();
		try {
			wordList = Files.readAllLines(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Random random = new Random();
		int position = random.nextInt(wordList.size());
		return wordList.get(position).trim().toUpperCase();
	}

}
