package main; // Pachetul în care se află clasa Main

import java.awt.*; // Importă clasele AWT pentru manipularea interfeței grafice
import javax.swing.*; // Importă clasele Swing pentru crearea interfeței grafice

public class Main{ // Declarația clasei Main
    public static void main(String[] args) { // Metoda principală a programului
        JFrame frame = new JFrame(); // Crează un nou cadru (fereastră) pentru aplicație
        frame.setTitle("Chess Game"); // Setează titlul ferestrei
        frame.setLayout(new BorderLayout()); // Setează managerul de layout pentru fereastră

        // Setează dimensiunea minimă a ferestrei
        frame.setMinimumSize(new Dimension(900, 750));

        // Plasează fereastra în centrul ecranului
        frame.setLocationRelativeTo(null);

        // Crează un jurnal de mutări (clasa MoveLog)
        MoveLog moveLog = new MoveLog();

        // Adaugă jurnalul de mutări în partea dreaptă a ferestrei
        frame.add(moveLog, BorderLayout.EAST);

        // Crează tabla de șah (clasa Board) și îi transmite jurnalul de mutări
        Board board = new Board(moveLog);

        // Adaugă tabla de șah în partea stângă a ferestrei
        frame.add(board, BorderLayout.WEST);

        // Crează un panou pentru butoane
        JPanel buttonPanel = new JPanel();

        // Crează un buton pentru repornirea jocului
        JButton restartButton = new JButton("Restart");

        // Adaugă un ascultător pentru butonul de repornire
        restartButton.addActionListener(e -> {
            board.resetBoard(); // Resetează tabla de șah
            moveLog.clearMoves(); // Șterge toate mutările din jurnalul de mutări
            frame.revalidate(); // Revalidează conținutul ferestrei
            frame.repaint(); // Reafișează ferestra
        });

        // Adaugă butonul de repornire în panoul de butoane
        buttonPanel.add(restartButton);

        // Adaugă panoul de butoane în partea de jos (sud) a ferestrei
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Face fereastra vizibilă
        frame.setVisible(true);
    }
}
