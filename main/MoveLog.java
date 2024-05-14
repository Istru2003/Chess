package main;

import java.awt.*;
import javax.swing.*;

public class MoveLog extends JPanel{
    private final JTextArea movesTextArea;
    private int moveCounter = 1;

    public MoveLog() {
        setLayout(new BorderLayout());
        movesTextArea = new JTextArea();
        movesTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(movesTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setPreferredSize(new Dimension(200, 400));
        add(scrollPane);
    }
    public void addMove(String move) {
        movesTextArea.append(moveCounter + ". " + move + "\n");
        moveCounter++;
    }

    public void clearMoves() {
        movesTextArea.setText("");
        moveCounter = 1;
    }


}
