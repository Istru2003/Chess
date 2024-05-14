package main;

import java.awt.*;
import javax.swing.*;

public class Main{
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("Chess Game");
        frame.setLayout(new BorderLayout());

        frame.setMinimumSize(new Dimension(900, 750));
        frame.setLocationRelativeTo(null);

        MoveLog moveLog = new MoveLog();
        frame.add(moveLog, BorderLayout.EAST);

        Board board = new Board(moveLog);
        frame.add(board, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel();

        JButton restartButton = new JButton("Restart");

        restartButton.addActionListener(e -> {
            board.resetBoard();
            moveLog.clearMoves();
            frame.revalidate();
            frame.repaint();
        });

        buttonPanel.add(restartButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}
