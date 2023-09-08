package com.example.lib;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

public class WordCounterGUI extends JFrame implements ActionListener {

    private final JTextArea textArea;
    private final JButton countButton;
    private final JLabel resultLabel;
    private final JCheckBox ignoreCommonWordsCheckBox;
    private final JTextArea statisticsTextArea;

    private Map<String, Integer> wordFrequencyMap;

    public WordCounterGUI() {
        setTitle("Word Counter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(new GridLayout(4, 1));

        textArea = new JTextArea("Enter text or provide a file path here.", 10, 40);
        countButton = new JButton("Count Words");
        resultLabel = new JLabel("");
        ignoreCommonWordsCheckBox = new JCheckBox("Ignore Common Words");
        statisticsTextArea = new JTextArea(10, 40);

        countButton.addActionListener(this);

        add(textArea);
        add(ignoreCommonWordsCheckBox);
        add(countButton);
        add(resultLabel);

        wordFrequencyMap = new HashMap<>();

        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == countButton) {
            String inputText = textArea.getText();
            boolean ignoreCommonWords = ignoreCommonWordsCheckBox.isSelected();

            if (inputText.trim().isEmpty()) {
                resultLabel.setText("Please enter text or a valid file path.");
                return;
            }

            wordFrequencyMap.clear();
            countWords(inputText, ignoreCommonWords);
            displayWordStatistics();
        }
    }

    private void countWords(String inputText, boolean ignoreCommonWords) {
        String[] words = inputText.split("[\\s\\p{Punct}]+");
        Set<String> commonWords = getCommonWords();

        for (String word : words) {
            word = word.toLowerCase();
            if (ignoreCommonWords && commonWords.contains(word)) {
                continue;
            }

            wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, 0) + 1);
        }
    }

    private Set<String> getCommonWords() {
        Set<String> commonWords = new HashSet<>();
        commonWords.add("the");
        commonWords.add("and");
        commonWords.add("is");
        commonWords.add("of");// Add more common words as needed
        return commonWords;
    }

    private void displayWordStatistics() {
        int totalCount;
        int sum = 0;
        for (Integer integer : wordFrequencyMap.values()) {
            int i = integer.intValue();
            sum += i;
        }
        totalCount = sum;
        resultLabel.setText("Total Words: " + totalCount);

        StringBuilder statistics = new StringBuilder("Word Frequency Statistics:\n");

        List<Entry<String, Integer>> toSort = new ArrayList<>();
        for (Entry<String, Integer> entry : wordFrequencyMap.entrySet()) {
            toSort.add(entry);
        }
        toSort.sort(Entry.<String, Integer>comparingByValue().reversed());
        for (Entry<String, Integer> entry : toSort) {
            statistics.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        statisticsTextArea.setText(statistics.toString());

        // Create a new window to display statistics
        JFrame statisticsFrame = new JFrame("Word Frequency Statistics");
        statisticsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        statisticsFrame.setSize(400, 400);
        statisticsFrame.add(statisticsTextArea);
        statisticsFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WordCounterGUI();
            }
        });
    }
}
