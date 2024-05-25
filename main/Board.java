package main; // Declară pachetul main

import java.awt.*;
import java.util.ArrayList; // Importă clasa ArrayList din biblioteca util
import java.util.stream.Collectors; // Importă metoda Collectors din biblioteca stream

import javax.swing.*;

import pieces.Bishop; // Importă clasa Bishop din pachetul pieces
import pieces.King; // Importă clasa King din pachetul pieces
import pieces.Knight; // Importă clasa Knight din pachetul pieces
import pieces.Pawn; // Importă clasa Pawn din pachetul pieces
import pieces.Piece; // Importă clasa Piece din pachetul pieces
import pieces.Queen; // Importă clasa Queen din pachetul pieces
import pieces.Rook; // Importă clasa Rook din pachetul pieces

import static main.Main.getTimeControlInSeconds;
import static main.Main.selectedTimeControl;

public class Board extends JPanel{ // Declară clasa Board care extinde JPanel
    public int titleSize = 85; // Declară și inițializează dimensiunea titlului
    private GameTimer gameTimer;
    int cols = 8; // Declară și inițializează numărul de coloane
    int rows = 8; // Declară și inițializează numărul de rânduri

    ArrayList<Piece> pieceList = new ArrayList<>(); // Creează o listă de piese

    public Piece selectedPiece; // Declară o piesă selectată

    private MoveLog moveLog; // Declară un jurnal de mutări

    Input input = new Input(this); // Creează un obiect de input

    public CheckScanner checkScanner = new CheckScanner(this); // Creează un scanner de șah

    public int enPassantTile = -1; // Declară și inițializează tile-ul en passant
    private boolean isWhiteToMove = true; // Declară și inițializează mutarea albă
    private boolean isGameOver = false; // Declară și inițializează starea jocului

    public Board(MoveLog moveLog, GameTimer gameTimer) {
        this.gameTimer = gameTimer;
        this.setPreferredSize(new Dimension(cols * titleSize, rows * titleSize));
        this.addMouseListener(input);
        this.addMouseMotionListener(input);
        this.moveLog = moveLog;
        addPieces();
    }

    public Piece getPiece(int col, int row){ // Metodă pentru obținerea unei piese de pe tablă
        for(Piece piece : pieceList){ // Iterează prin lista de piese
            if(piece.col == col && piece.row == row){ // Verifică dacă piesa se află pe poziția specificată
                return piece; // Returnează piesa
            }
        }
        return null; // Returnează null dacă nu există nicio piesă pe poziția specificată
    }

    public void makeMove(Move move) { // Metodă pentru efectuarea unei mutări
        if (move.piece.name.equals("Pion")) { // Verifică dacă piesa este un pion
            movePawn(move); // Mută pionul
        } else if (move.piece.name.equals("Rege")) { // Verifică dacă piesa este un rege
            moveKing(move); // Mută regele
        }
        move.piece.col = move.newCol; // Actualizează coloana piesei
        move.piece.row = move.newRow; // Actualizează rândul piesei
        move.piece.xPos = move.newCol * titleSize; // Actualizează poziția X a piesei
        move.piece.yPos = move.newRow * titleSize; // Actualizează poziția Y a piesei

        move.piece.isFirstMove = false; // Marchează că piesa nu mai este la prima mutare

        capture(move.capture); // Capturează piesa

        String moveString = (move.piece.isWhite ? "Alb" : "Negru") + " " + move.piece.name + ": " + getAlgebraicNotation(move.oldCol, move.oldRow) + " -> " + getAlgebraicNotation(move.newCol, move.newRow); // Formatează șirul de mutare
        if (move.piece instanceof King) { // Verifică dacă piesa este un rege
            String castlingType = ((King) move.piece).getCastlingType(move.newCol, move.newRow); // Obține tipul de rocadă
            if (!castlingType.isEmpty()) { // Verifică dacă tipul de rocadă nu este gol
                moveString = (move.piece.isWhite ? "Alb" : "Negru") + " Rocare: " + castlingType; // Formatează șirul de rocadă
            } else if (move.capture != null) { // Verifică dacă a fost capturată o piesă
                moveString += "\n   capturi " + move.capture.name; // Adaugă piesa capturată la șirul de mutare
            }
        } else if (move.capture != null) { // Verifică dacă a fost capturată o piesă
            moveString += "\n    capturi " + move.capture.name; // Adaugă piesa capturată la șirul de mutare
        }

        moveLog.addMove(moveString); // Adaugă mutarea în jurnal

        isWhiteToMove = !isWhiteToMove; // Schimbă rândul de mutare
        gameTimer.switchTurn();

        updateGameState(); // Actualizează starea jocului

    }

    public void resetBoard() {
        pieceList.clear();
        addPieces();
        isWhiteToMove = true;
        repaint();
    }


    public String getAlgebraicNotation(int col, int row) { // Metodă pentru obținerea notației algebrice
        String colString = ""; // Declară șirul pentru coloane
        switch (col) { // Verifică valoarea coloanei
            case 0:
                colString = "a"; // Setează valoarea pentru coloana 0
                break;
            case 1:
                colString = "b"; // Setează valoarea pentru coloana 1
                break;
            case 2:
                colString = "c"; // Setează valoarea pentru coloana 2
                break;
            case 3:
                colString = "d"; // Setează valoarea pentru coloana 3
                break;
            case 4:
                colString = "e"; // Setează valoarea pentru coloana 4
                break;
            case 5:
                colString = "f"; // Setează valoarea pentru coloana 5
                break;
            case 6:
                colString = "g"; // Setează valoarea pentru coloana 6
                break;
            case 7:
                colString = "h"; // Setează valoarea pentru coloana 7
                break;
        }
        return colString + (8 - row); // Returnează notația algebrică
    }

    private void moveKing(Move move){ // Metodă pentru mutarea regelui
        if(Math.abs(move.piece.col - move.newCol) == 2){ // Verifică dacă mutarea este o rocada
            Piece rook; // Declară o piesă turn
            if(move.piece.col < move.newCol){ // Verifică dacă rocada este pe partea regelui
                rook = getPiece(7, move.piece.row); // Obține turnul din colțul dreapta
                rook.col = 5; // Setează coloana turnului
            } else{ // Dacă rocada este pe partea damei
                rook = getPiece(0, move.piece.row); // Obține turnul din colțul stânga
                rook.col = 3; // Setează coloana turnului
            }
            rook.xPos = rook.col * titleSize; // Actualizează poziția X a turnului
        }
    }

    private void movePawn(Move move){ // Metodă pentru mutarea pionului
        int colorIndex = move.piece.isWhite ? 1 : -1; // Setează indexul culorii

        if(getTileNum(move.newCol, move.newRow) == enPassantTile){ // Verifică dacă este mutare en passant
            move.capture = getPiece(move.newCol, move.newRow + colorIndex); // Capturează piesa en passant
        }
        if(Math.abs(move.piece.row - move.newRow) == 2){ // Verifică dacă pionul a mutat două rânduri
            enPassantTile = getTileNum(move.newCol, move.newRow + colorIndex); // Setează tile-ul en passant
        } else {
            enPassantTile = -1; // Resetează tile-ul en passant
        }

        colorIndex = move.piece.isWhite ? 0 : 7; // Setează indexul culorii pentru promovare
        if(move.newRow == colorIndex){ // Verifică dacă pionul a ajuns la capătul tablei
            promotePawn(move); // Promovează pionul
        }
    }

    private void promotePawn(Move move){ // Metodă pentru promovarea pionului
        pieceList.add(new Queen(this, move.newCol, move.newRow, move.piece.isWhite)); // Adaugă o regină în locul pionului
        capture(move.piece); // Capturează pionul promovat
    }

    public void capture(Piece piece){ // Metodă pentru capturarea unei piese
        pieceList.remove(piece); // Elimină piesa din listă
    }

    public boolean isValidMove(Move move){ // Metodă pentru verificarea validității unei mutări

        if(isGameOver){ // Verifică dacă jocul s-a terminat
            return false; // Returnează false dacă jocul s-a terminat
        }
        if(move.piece.isWhite != isWhiteToMove){ // Verifică dacă este rândul piesei să mute
            return false; // Returnează false dacă nu este rândul piesei să mute
        }
        if(sameTeam(move.piece, move.capture)){ // Verifică dacă piesele sunt din aceeași echipă
            return false; // Returnează false dacă piesele sunt din aceeași echipă
        }
        if(!move.piece.isValidMovement(move.newCol, move.newRow)){ // Verifică dacă mutarea este validă pentru piesă
            return false; // Returnează false dacă mutarea nu este validă
        }
        if(move.piece.moveCollidesWithPiece(move.newCol, move.newRow)){ // Verifică dacă mutarea colizionează cu o altă piesă
            return false; // Returnează false dacă mutarea colizionează
        }
        if(checkScanner.isKingChecked(move)){ // Verifică dacă regele este în șah
            return false; // Returnează false dacă regele este în șah
        }
        return true; // Returnează true dacă mutarea este validă
    }

    public boolean sameTeam(Piece p1, Piece p2){ // Metodă pentru verificarea dacă piesele sunt din aceeași echipă
        if(p1 == null || p2 == null){ // Verifică dacă piesele sunt null
            return false; // Returnează false dacă piesele sunt null
        }
        return p1.isWhite == p2.isWhite; // Returnează true dacă piesele sunt din aceeași echipă
    }

    public int getTileNum(int col, int row){ // Metodă pentru obținerea numărului de tile
        return row * rows + col; // Calculează și returnează numărul de tile
    }

    Piece findKing(boolean isWhite){ // Metodă pentru găsirea regelui
        for(Piece piece : pieceList){ // Iterează prin lista de piese
            if(isWhite == piece.isWhite && piece.name.equals("Rege")){ // Verifică dacă piesa este regele căutat
                return piece; // Returnează regele găsit
            }
        }
        return null; // Returnează null dacă nu a găsit regele
    }

    public void addPieces(){ // Metodă pentru adăugarea pieselor pe tablă
        pieceList.add(new Knight(this, 1, 0, false)); // Adaugă un cal
        pieceList.add(new Knight(this, 6, 0, false)); // Adaugă un cal
        pieceList.add(new Rook(this, 0, 0, false)); // Adaugă un turn
        pieceList.add(new Rook(this, 7, 0, false)); // Adaugă un turn
        pieceList.add(new Bishop(this, 2, 0, false)); // Adaugă un nebun
        pieceList.add(new Bishop(this, 5, 0, false)); // Adaugă un nebun
        pieceList.add(new Queen(this, 3, 0, false)); // Adaugă o regină
        pieceList.add(new King(this, 4, 0, false)); // Adaugă un rege

        pieceList.add(new Pawn(this, 1, 1, false)); // Adaugă un pion
        pieceList.add(new Pawn(this, 6, 1, false)); // Adaugă un pion
        pieceList.add(new Pawn(this, 0, 1, false)); // Adaugă un pion
        pieceList.add(new Pawn(this, 7, 1, false)); // Adaugă un pion
        pieceList.add(new Pawn(this, 2, 1, false)); // Adaugă un pion
        pieceList.add(new Pawn(this, 5, 1, false)); // Adaugă un pion
        pieceList.add(new Pawn(this, 3, 1, false)); // Adaugă un pion
        pieceList.add(new Pawn(this, 4, 1, false)); // Adaugă un pion

        pieceList.add(new Knight(this, 1, 7, true)); // Adaugă un cal
        pieceList.add(new Knight(this, 6, 7, true)); // Adaugă un cal
        pieceList.add(new Rook(this, 0, 7, true)); // Adaugă un turn
        pieceList.add(new Rook(this, 7, 7, true)); // Adaugă un turn
        pieceList.add(new Bishop(this, 2, 7, true)); // Adaugă un nebun
        pieceList.add(new Bishop(this, 5, 7, true)); // Adaugă un nebun
        pieceList.add(new Queen(this, 3, 7, true)); // Adaugă o regină
        pieceList.add(new King(this, 4, 7, true)); // Adaugă un rege

        pieceList.add(new Pawn(this, 1, 6, true)); // Adaugă un pion
        pieceList.add(new Pawn(this, 6, 6, true)); // Adaugă un pion
        pieceList.add(new Pawn(this, 0, 6, true)); // Adaugă un pion
        pieceList.add(new Pawn(this, 7, 6, true)); // Adaugă un pion
        pieceList.add(new Pawn(this, 2, 6, true)); // Adaugă un pion
        pieceList.add(new Pawn(this, 5, 6, true)); // Adaugă un pion
        pieceList.add(new Pawn(this, 3, 6, true)); // Adaugă un pion
        pieceList.add(new Pawn(this, 4, 6, true)); // Adaugă un pion
    }

    public void updateGameState() { // Metodă pentru actualizarea stării jocului
        Piece king = findKing(isWhiteToMove); // Găsește regele
        if (checkScanner.isGameOver(king)) { // Verifică dacă jocul s-a terminat
            if (checkScanner.isKingChecked(new Move(this, king, king.col, king.row))) { // Verifică dacă regele este în șah
                showGameOverDialog(isWhiteToMove ? "Negrul Castiga!" : "Albul Castiga!"); // Afișează dialogul de sfârșit de joc
            } else {
                showGameOverDialog("Impas!"); // Afișează dialogul de pat
            }
        } else if (insufficientMaterial(true) && insufficientMaterial(false)) { // Verifică materialul insuficient
            showGameOverDialog("Material insuficient!"); // Afișează dialogul de material insuficient
            isGameOver = true; // Marchează jocul ca fiind terminat
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

    private boolean insufficientMaterial(boolean isWhite){ // Metodă pentru verificarea materialului insuficient
        ArrayList<String> names = pieceList.stream().filter(p -> p.isWhite == isWhite).map(p -> p.name).collect(Collectors.toCollection(ArrayList::new)); // Creează o listă de nume de piese
        if(names.contains("Regină") || names.contains("Rook") || names.contains("Pion")){ // Verifică dacă lista conține regină, turn sau pion
            return false; // Returnează false dacă lista conține una dintre aceste piese
        }
        return names.size() < 3; // Returnează true dacă lista conține mai puțin de 3 piese
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
                        g2d.setColor(new Color(68, 180, 57, 190)); // Color for possible moves
                        g2d.fillRect(c * titleSize, r * titleSize, titleSize, titleSize);
                        if (move.capture != null && move.capture.isWhite != selectedPiece.isWhite) {
                            g2d.setColor(Color.RED); // Color for possible captures
                            g2d.fillRect(c * titleSize, r * titleSize, titleSize, titleSize);
                            capturedPieces.add(move.capture); // Add captured piece to the list
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


    public void switchTurn() {
        isWhiteToMove = !isWhiteToMove;
        gameTimer.switchTurn();
        updateGameState();
    }


}