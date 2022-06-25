package wordle.project.base;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class WordleUI implements ActionListener {
	private JFrame jframe;
	private List<WordPanel> panelList;
	private LastPanel lastPanel;
	private int colCount = 0;
	private String wordleWord;
        //Elementos de la interfaz grafica
	public WordleUI() {
                //Se ajusta la ventana del juego
		wordleWord = fetchWord().trim().toUpperCase();
		jframe = new JFrame("Wordle game");
		jframe.setLayout(new GridLayout(6, 1));

		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(300, 300);

		panelList = new ArrayList<WordPanel>();
		for (int i = 0; i < 5; i++) {
			panelList.add(new WordPanel());
			jframe.add(panelList.get(i));
		}

		lastPanel = new LastPanel();
		lastPanel.getSubmit().addActionListener(this);
		jframe.add(lastPanel);
		jframe.setLocationRelativeTo(null);
		jframe.revalidate();

	}
        //Se ejecuta la interfaz grafica en la clase main
	public static void main(String[] args) {
		new WordleUI();

	}
        //Método que identifica si el usuario cumple con las condiciones para ganar
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();

		String userWord = lastPanel.getTextField().getText().trim();
		if (colCount > 5) {
			JOptionPane.showMessageDialog(null, "You loose!!!", "Oops", 0);
			getJframe().dispose();
			return;
		}
		if ("OK".equals(action) && userWord.length() > 4) {

			if (isWordleWordEqualsTo(userWord)) {
				cleanPanelList();
				JOptionPane.showMessageDialog(null, "You Win!!!", "Congrats", 1);
				getJframe().dispose();
				return;
			}
			colCount++;
		}

	}
        //Identifica si la letra es igual a la de la palabra seleccionada
	private boolean isWordleWordEqualsTo(String userWord) {
		String[] userWordLetterArray = userWord.toUpperCase().split("");
		List<String> wordleLetters = Arrays.asList(getWordleWord().split(""));
		List<Boolean> wordMatchList = new ArrayList<Boolean>();

		for (int i = 0; i < 5; i++) {
			if (wordleLetters.contains(userWordLetterArray[i])) {
				if (wordleLetters.get(i).equals(userWordLetterArray[i])) {
					getCurrentActivePanel().updatePanel(userWordLetterArray[i], i, Color.GREEN);
					wordMatchList.add(true);
				} else {
					getCurrentActivePanel().updatePanel(userWordLetterArray[i], i, Color.YELLOW);
					wordMatchList.add(false);
				}
			} else {
				getCurrentActivePanel().updatePanel(userWordLetterArray[i], i, Color.GRAY);
				wordMatchList.add(false);
			}
		}
		return !wordMatchList.contains(false);
	}
        //Se verifica que sean la cantidad de letras correctas
	public int checkAgain(String pattern, List<String> wordleLetters) {
		int count = 0;
		for (String letter : wordleLetters) {
			if (pattern.equals(letter)) {
				count++;
			}
		}
		return count;
	}
        //Metodos GET
	public JFrame getJframe() {
		return jframe;
	}

	public String getWordleWord() {
		return wordleWord;
	}

	public WordPanel getCurrentActivePanel() {
		return panelList.get(colCount);
	}

	public List<WordPanel> getPanelList() {
		return this.panelList;
	}
        //Limpiar el panel de juego
	private void cleanPanelList() {
		for (WordPanel panel : getPanelList()) {
			panel.cleanAllColumns();
		}
	}
        //Seleccionar una palabra nueva
	private String fetchWord() {
		Path path = Paths.get("..\\wordle.project-master\\assets\\Words.txt");
		List<String> wordsList = new ArrayList<String>();
		try {
			wordsList = Files.readAllLines(path);
		} catch (IOException ex) {

		}
		Random random = new Random();
		String wordOfTheDay = wordsList.get(random.nextInt(wordsList.size()));
		System.out.println(wordOfTheDay);
		return wordOfTheDay.trim();
	}
        //Configuracion del ultimo panel
	class LastPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private JTextField text;
		private JButton submit;

		LastPanel() {
			this.setLayout(new GridLayout(1, 2));
			text = new JTextField();
			text.setText("");
			this.add(text);

			submit = new JButton("OK");
			this.add(submit);
			this.setVisible(true);
		}

		public JTextField getTextField() {
			return text;
		}

		public JButton getSubmit() {
			return submit;
		}

	}

	class WordPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private JLabel[] charColumns = new JLabel[6];

		public WordPanel() {
			this.setLayout(new GridLayout(1, 5));
			this.setSize(300, 60);
			Border blackline = BorderFactory.createLineBorder(Color.lightGray);
			for (int i = 0; i < 5; i++) {
				charColumns[i] = new JLabel("", JLabel.CENTER);
				charColumns[i].setOpaque(true);
				charColumns[i].setBorder(blackline);
				this.add(charColumns[i]);
			}
		}

		public void updatePanel(String inputWord, int position, Color color) {
			charColumns[position].setBackground(color);
			charColumns[position].setText(inputWord);
		}

		public void cleanAllColumns() {
			for (int i = 0; i < 5; i++) {
				charColumns[i].setText("");
			}
		}
	}

}
