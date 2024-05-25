package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

class GameTimer {
    private boolean isWhiteTurn = true;
    private Timer timer;
    private JLabel whiteTimerLabel;
    private JLabel blackTimerLabel;
    private int whiteTime;
    private int blackTime;
    private String selectedTimeControl;
    private Board board;

    public GameTimer(JLabel whiteTimerLabel, JLabel blackTimerLabel, int val, String selectedTimeControl) {
        this.whiteTimerLabel = whiteTimerLabel;
        this.blackTimerLabel = blackTimerLabel;
        this.whiteTime = val;
        this.blackTime = val;
        this.selectedTimeControl = selectedTimeControl;
        this.board = board;
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTimer();
            }
        });
        timer.start();
        startTimers(); // Initially, set the timer based on the default time control
    }

    public void switchTurn() {
        isWhiteTurn = !isWhiteTurn;
    }

    public void stopBlackTimer() {
        if (!isWhiteTurn) { // Check if black's turn is ongoing
            timer.stop(); // Stop the timer if black's turn
        }
    }

    private void updateTimer() {
        if (isWhiteTurn) {
            whiteTime--;
            whiteTimerLabel.setText("Alb: " + getWhiteTimeString());
            if (whiteTime <= 0) {
                // White time exceeded
                System.out.println("Alb a depășit timpul!");
                timer.stop();
            }
        } else {
            blackTime--;
            blackTimerLabel.setText("Negru: " + getBlackTimeString());
            if (blackTime <= 0) {
                // Black time exceeded
                System.out.println("Negru a depășit timpul!");
                timer.stop();
            }
        }
    }

    public String getWhiteTimeString() {
        int minutes = whiteTime / 60;
        int seconds = whiteTime % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public String getBlackTimeString() {
        int minutes = blackTime / 60;
        int seconds = blackTime % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public void startTimers() {
        timer.start();
    }

    public void stopAndRemoveTimers(String selectedTimeControl) {
        timer.stop(); // Остановить текущий таймер
        whiteTimerLabel.setText("Alb: "); // Оставить только "Alb: "
        blackTimerLabel.setText("Negru: " + selectedTimeControl); // Оставить только "Negru: "
    }

}
