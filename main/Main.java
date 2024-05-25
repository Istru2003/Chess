package main;
import java.awt.*;
import javax.swing.*;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Main {
    public static String selectedTimeControl = "60"; // Default time control
    private static JButton startButton;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            createMainMenu();
        });
    }

    private static void createMainMenu() {
        JFrame dialogFrame = new JFrame();
        dialogFrame.setTitle("Selectează timpul și opțiunile");
        dialogFrame.setLayout(new GridLayout(5, 1));

        JLabel titleLabel = new JLabel("Joc de Șah", SwingConstants.CENTER);
        dialogFrame.add(titleLabel);

        startButton = new JButton("Start / Timp: " + selectedTimeControl + " mins");
        JButton timeButton = new JButton("Selectează timpul");
        JButton guideButton = new JButton("Ghid");
        JButton exitButton = new JButton("Ieșire");

        dialogFrame.add(startButton);
        dialogFrame.add(timeButton);
        dialogFrame.add(guideButton);
        dialogFrame.add(exitButton);

        dialogFrame.setSize(300, 200);
        dialogFrame.setLocationRelativeTo(null);
        dialogFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dialogFrame.setVisible(true);

        startButton.addActionListener(e -> {
            dialogFrame.dispose();
            createGameWindow();
        });

        timeButton.addActionListener(e -> {
            selectTimeControl();
        });

        exitButton.addActionListener(e -> {
            System.exit(0);
        });

        guideButton.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.chess.com/ro/sah-reguli"));
            } catch (IOException | URISyntaxException ex) {
                ex.printStackTrace();
            }
        });
    }

    private static void selectTimeControl() {
        String[] options = {"Classic (60 mins)", "Rapid (30 mins)", "Blitz (10 mins)"};
        String[] optionstext = {"60", "30", "10"};
        JComboBox<String> comboBox = new JComboBox<>(options);
        int result = JOptionPane.showConfirmDialog(null, comboBox, "Selectează timpul", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            int selectedIndex = comboBox.getSelectedIndex();
            selectedTimeControl = optionstext[selectedIndex];
            startButton.setText("Start / Timp: " + selectedTimeControl + " mins");
        }
    }


    public static void createGameWindow() {
        JFrame frame = new JFrame();
        frame.setTitle("Șah");
        frame.setLayout(new BorderLayout());

        frame.setMinimumSize(new Dimension(900, 750));
        frame.setLocationRelativeTo(null);

        MoveLog moveLog = new MoveLog();
        frame.add(moveLog, BorderLayout.EAST);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel blackTimerLabel = new JLabel("Negru: " + selectedTimeControl + ":00", SwingConstants.CENTER);
        JLabel whiteTimerLabel = new JLabel("Alb: ", SwingConstants.CENTER);

        int totalTime = getTimeControlInSeconds(selectedTimeControl);

        GameTimer gameTimer = new GameTimer(whiteTimerLabel, blackTimerLabel, totalTime, selectedTimeControl);
        gameTimer.startTimers();

        JPanel timerPanel = new JPanel(new GridLayout(3, 1));
        timerPanel.add(blackTimerLabel);
        timerPanel.add(whiteTimerLabel);

        JPanel eastPanel = new JPanel(new BorderLayout());
        eastPanel.add(moveLog, BorderLayout.CENTER);
        eastPanel.add(timerPanel, BorderLayout.SOUTH);
        frame.add(eastPanel, BorderLayout.EAST);

        Board board = new Board(moveLog, gameTimer);
        mainPanel.add(board, BorderLayout.CENTER);

        frame.add(mainPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton restartButton = new JButton("Repornire");
        JButton backButton = new JButton("Menu");
        buttonPanel.add(backButton);

        backButton.addActionListener(e -> {
            frame.dispose();
            main(null);
        });

        restartButton.addActionListener(e -> {
            board.resetBoard();
            moveLog.clearMoves();
            gameTimer.stopAndRemoveTimers(selectedTimeControl);
            int time = getTimeControlInSeconds(selectedTimeControl);
            GameTimer newGameTimer = new GameTimer(whiteTimerLabel, blackTimerLabel, time, selectedTimeControl);
            newGameTimer.startTimers();
            frame.revalidate();
            frame.repaint();
        });

        buttonPanel.add(restartButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }


    public static int getTimeControlInSeconds(String timeControl) {
        switch (timeControl) {
            case "60":
                return 60 * 60; // 60 minutes in seconds
            case "30":
                return 30 * 60; // 30 minutes in seconds
            case "10":
                return 10 * 60; // 10 minutes in seconds
            default:
                return 60 * 60; // Default to Classic if something goes wrong
        }
    }
}
