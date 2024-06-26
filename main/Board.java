package main;

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.swing.*;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

import static main.Main.getTimeControlInSeconds;
import static main.Main.selectedTimeControl;

public class Board extends JPanel{
    public int titleSize = 85;
    private GameTimer gameTimer;
    int cols = 8;
    int rows = 8;

    ArrayList<Piece> pieceList = new ArrayList<>();

    public Piece selectedPiece;

    private MoveLog moveLog;

    Input input = new Input(this);

    public CheckScanner checkScanner = new CheckScanner(this);

    public int enPassantTile = -1;
    private boolean isWhiteToMove = true;
    private boolean isGameOver = false;

    public Board(MoveLog moveLog, GameTimer gameTimer) {
        this.gameTimer = gameTimer;
        this.setPreferredSize(new Dimension(cols * titleSize, rows * titleSize));
        this.addMouseListener(input);
        this.addMouseMotionListener(input);
        this.moveLog = moveLog;
        addPieces();
    }

    public Piece getPiece(int col, int row){
        for(Piece piece : pieceList){
            if(piece.col == col && piece.row == row){
                return piece;
            }
        }
        return null;
    }

    public void makeMove(Move move) {
        if (move.piece.name.equals("Pion")) {
            movePawn(move);
        } else if (move.piece.name.equals("Rege")) {
            moveKing(move);
        }
        move.piece.col = move.newCol;
        move.piece.row = move.newRow;
        move.piece.xPos = move.newCol * titleSize;
        move.piece.yPos = move.newRow * titleSize;

        move.piece.isFirstMove = false;

        capture(move.capture);
        String moveString = (move.piece.isWhite ? "Alb" : "Negru") + " " + move.piece.name + ": " + getAlgebraicNotation(move.oldCol, move.oldRow) + " -> " + getAlgebraicNotation(move.newCol, move.newRow);
        if (move.piece instanceof King) {
            String castlingType = ((King) move.piece).getCastlingType(move.newCol, move.newRow);
            if (!castlingType.isEmpty()) {
                moveString = (move.piece.isWhite ? "Alb" : "Negru") + " Rocare: " + castlingType;
            } else if (move.capture != null) {
                moveString += "\n   capturi " + move.capture.name;
            }
        } else if (move.capture != null) {
            moveString += "\n    capturi " + move.capture.name;
        }

        moveLog.addMove(moveString);

        isWhiteToMove = !isWhiteToMove;
        gameTimer.switchTurn();

        updateGameState();

    }

    public void resetBoard() {
        pieceList.clear();
        addPieces();
        isWhiteToMove = true;
        repaint();
    }


    public String getAlgebraicNotation(int col, int row) {
        String colString = "";
        switch (col) {
            case 0:
                colString = "a";
                break;
            case 1:
                colString = "b";
                break;
            case 2:
                colString = "c";
                break;
            case 3:
                colString = "d";
                break;
            case 4:
                colString = "e";
                break;
            case 5:
                colString = "f";
                break;
            case 6:
                colString = "g";
                break;
            case 7:
                colString = "h";
                break;
        }
        return colString + (8 - row);
    }

    private void moveKing(Move move){
        if(Math.abs(move.piece.col - move.newCol) == 2){
            Piece rook;
            if(move.piece.col < move.newCol){
                rook = getPiece(7, move.piece.row);
                rook.col = 5;
            } else{
                rook = getPiece(0, move.piece.row);
                rook.col = 3;
            }
            rook.xPos = rook.col * titleSize;
        }
    }

    private void movePawn(Move move){
        int colorIndex = move.piece.isWhite ? 1 : -1;

        if(getTileNum(move.newCol, move.newRow) == enPassantTile){
            move.capture = getPiece(move.newCol, move.newRow + colorIndex);
        }
        if(Math.abs(move.piece.row - move.newRow) == 2){
            enPassantTile = getTileNum(move.newCol, move.newRow + colorIndex);
        } else {
            enPassantTile = -1;
        }

        colorIndex = move.piece.isWhite ? 0 : 7;
        if(move.newRow == colorIndex){
            promotePawn(move);
        }
    }

    private void promotePawn(Move move){
        pieceList.add(new Queen(this, move.newCol, move.newRow, move.piece.isWhite));
        capture(move.piece);
    }

    public void capture(Piece piece){
        pieceList.remove(piece);
    }

    public boolean isValidMove(Move move){

        if(isGameOver){
            return false;
        }
        if(move.piece.isWhite != isWhiteToMove){
            return false;
        }
        if(sameTeam(move.piece, move.capture)){
            return false;
        }
        if(!move.piece.isValidMovement(move.newCol, move.newRow)){
            return false;
        }
        if(move.piece.moveCollidesWithPiece(move.newCol, move.newRow)){
            return false;
        }
        if(checkScanner.isKingChecked(move)){
            return false;
        }
        return true;
    }

    public boolean sameTeam(Piece p1, Piece p2){
        if(p1 == null || p2 == null){
            return false;
        }
        return p1.isWhite == p2.isWhite;
    }

    public int getTileNum(int col, int row){
        return row * rows + col;
    }

    Piece findKing(boolean isWhite){
        for(Piece piece : pieceList){
            if(isWhite == piece.isWhite && piece.name.equals("Rege")){
                return piece;
            }
        }
        return null;
    }

    public void addPieces(){
        pieceList.add(new Knight(this, 1, 0, false));
        pieceList.add(new Knight(this, 6, 0, false));
        pieceList.add(new Rook(this, 0, 0, false));
        pieceList.add(new Rook(this, 7, 0, false));
        pieceList.add(new Bishop(this, 2, 0, false));
        pieceList.add(new Bishop(this, 5, 0, false));
        pieceList.add(new Queen(this, 3, 0, false));
        pieceList.add(new King(this, 4, 0, false));

        pieceList.add(new Pawn(this, 1, 1, false));
        pieceList.add(new Pawn(this, 6, 1, false));
        pieceList.add(new Pawn(this, 0, 1, false));
        pieceList.add(new Pawn(this, 7, 1, false));
        pieceList.add(new Pawn(this, 2, 1, false));
        pieceList.add(new Pawn(this, 5, 1, false));
        pieceList.add(new Pawn(this, 3, 1, false));
        pieceList.add(new Pawn(this, 4, 1, false));

        pieceList.add(new Knight(this, 1, 7, true));
        pieceList.add(new Knight(this, 6, 7, true));
        pieceList.add(new Rook(this, 0, 7, true));
        pieceList.add(new Rook(this, 7, 7, true));
        pieceList.add(new Bishop(this, 2, 7, true));
        pieceList.add(new Bishop(this, 5, 7, true));
        pieceList.add(new Queen(this, 3, 7, true));
        pieceList.add(new King(this, 4, 7, true));

        pieceList.add(new Pawn(this, 1, 6, true));
        pieceList.add(new Pawn(this, 6, 6, true));
        pieceList.add(new Pawn(this, 0, 6, true));
        pieceList.add(new Pawn(this, 7, 6, true));
        pieceList.add(new Pawn(this, 2, 6, true));
        pieceList.add(new Pawn(this, 5, 6, true));
        pieceList.add(new Pawn(this, 3, 6, true));
        pieceList.add(new Pawn(this, 4, 6, true));
    }

    public void updateGameState() {
        Piece king = findKing(isWhiteToMove);
        if (checkScanner.isGameOver(king)) {
            if (checkScanner.isKingChecked(new Move(this, king, king.col, king.row))) {
                showGameOverDialog(isWhiteToMove ? "Negrul Castiga!" : "Albul Castiga!");
            } else {
                showGameOverDialog("Impas!"); // Afișează dialogul de pat
            }
        } else if (insufficientMaterial(true) && insufficientMaterial(false)) {
            showGameOverDialog("Material insuficient!");
            isGameOver = true;
        }
    }

    private void showGameOverDialog(String message) {
        int option = JOptionPane.showOptionDialog(
                this,
                message,
                "Joc încheiat",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[] { "Repornire", "Menu", "Închide" },
                "Repornire"
        );

        if (option == JOptionPane.YES_OPTION) {
            resetBoard();
            moveLog.clearMoves();
            gameTimer.stopAndRemoveTimers(selectedTimeControl);
            int time = getTimeControlInSeconds(selectedTimeControl);
            JLabel blackTimerLabel = new JLabel("Negru: " + selectedTimeControl + ":00", SwingConstants.CENTER);
            JLabel whiteTimerLabel = new JLabel("Alb: ", SwingConstants.CENTER);
            GameTimer newGameTimer = new GameTimer(whiteTimerLabel, blackTimerLabel, time, selectedTimeControl);
            newGameTimer.startTimers();
            JFrame frame = new JFrame();
            frame.revalidate();
            frame.repaint();
        } else if (option == JOptionPane.NO_OPTION) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.dispose();
            Main.main(null);
        } else if (option == JOptionPane.CANCEL_OPTION) {
            System.exit(0);
        }
    }

    private boolean insufficientMaterial(boolean isWhite){
        ArrayList<String> names = pieceList.stream().filter(p -> p.isWhite == isWhite).map(p -> p.name).collect(Collectors.toCollection(ArrayList::new));
        if(names.contains("Regină") || names.contains("Rook") || names.contains("Pion")){
            return false;
        }
        return names.size() < 3;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                g2d.setColor((c + r) % 2 == 0 ? new Color(227, 198, 101) : new Color(157, 105, 53));
                g2d.fillRect(c * titleSize, r * titleSize, titleSize, titleSize);
            }
        }

        ArrayList<Piece> otherPieces = pieceList.stream().filter(p -> !sameTeam(p, selectedPiece)).collect(Collectors.toCollection(ArrayList::new));
        for (Piece piece : otherPieces) {
            piece.paint(g2d);
        }

        if (selectedPiece != null) {
            ArrayList<Piece> capturedPieces = new ArrayList<>();
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    Move move = new Move(this, selectedPiece, c, r);
                    if (isValidMove(move)) {
                        g2d.setColor(new Color(68, 180, 57, 190));
                        g2d.fillRect(c * titleSize, r * titleSize, titleSize, titleSize);
                        if (move.capture != null && move.capture.isWhite != selectedPiece.isWhite) {
                            g2d.setColor(Color.RED);
                            g2d.fillRect(c * titleSize, r * titleSize, titleSize, titleSize);
                            capturedPieces.add(move.capture);
                        }
                    }
                }
            }
            for (Piece capturedPiece : capturedPieces) {
                capturedPiece.paint(g2d);
            }
        }

        ArrayList<Piece> currentPieces = pieceList.stream().filter(p -> sameTeam(p, selectedPiece)).collect(Collectors.toCollection(ArrayList::new));
        for (Piece piece : currentPieces) {
            piece.paint(g2d);
        }
    }
}