package main; // Declară pachetul main

import java.awt.*; // Importă biblioteca AWT pentru elemente grafice
import javax.swing.*; // Importă biblioteca Swing pentru elemente UI

public class MoveLog extends JPanel{ // Declară clasa MoveLog extinzând JPanel
    private final JTextArea movesTextArea; // Declară JTextArea pentru afișarea mutărilor
    private int moveCounter = 1; // Declară un contor pentru mutări, inițializat la 1

    public MoveLog() { // Constructorul clasei MoveLog
        setLayout(new BorderLayout()); // Setează layout-ul panelului ca BorderLayout
        movesTextArea = new JTextArea(); // Inițializează JTextArea pentru mutări
        movesTextArea.setEditable(false); // Setează JTextArea ca needitabilă
        JScrollPane scrollPane = new JScrollPane(movesTextArea); // Creează un JScrollPane pentru JTextArea
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Setează bara de scroll verticală mereu vizibilă
        setPreferredSize(new Dimension(200, 400)); // Setează dimensiunea preferată a JPanel-ului
        add(scrollPane); // Adaugă JScrollPane la JPanel
    }

    public void addMove(String move) { // Metodă pentru adăugarea unei mutări
        movesTextArea.append(moveCounter + ". " + move + "\n"); // Adaugă mutarea la JTextArea
        moveCounter++; // Incrementează contorul de mutări
    }

    public void clearMoves() { // Metodă pentru ștergerea mutărilor
        movesTextArea.setText(""); // Șterge textul din JTextArea
        moveCounter = 1; // Resetează contorul de mutări la 1
    }
}
