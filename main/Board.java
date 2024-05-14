package main; // Pachetul în care se află clasa Main

import java.awt.Color; // Importăm clasa Color din biblioteca AWT
import java.awt.Dimension; // Importăm clasa Dimension din biblioteca AWT
import java.awt.Graphics; // Importăm clasa Graphics din biblioteca AWT
import java.awt.Graphics2D; // Importăm clasa Graphics2D din biblioteca AWT
import java.util.ArrayList; // Importăm clasa ArrayList din biblioteca util
import java.util.stream.Collectors; // Importăm metoda Collectors din biblioteca stream

import javax.swing.JOptionPane; // Importăm clasa JOptionPane din biblioteca Swing
import javax.swing.JPanel; // Importăm clasa JPanel din biblioteca Swing

import pieces.Bishop; // Importăm clasa Bishop din pachetul pieces
import pieces.King; // Importăm clasa King din pachetul pieces
import pieces.Knight; // Importăm clasa Knight din pachetul pieces
import pieces.Pawn; // Importăm clasa Pawn din pachetul pieces
import pieces.Piece; // Importăm clasa Piece din pachetul pieces
import pieces.Queen; // Importăm clasa Queen din pachetul pieces
import pieces.Rook; // Importăm clasa Rook din pachetul pieces
public class Board extends JPanel{
    public int titleSize = 85; // Dimensiunea titlului este 85

    int cols = 8; // Numărul de coloane este 8
    int rows = 8; // Numărul de rânduri este 8

    ArrayList<Piece> pieceList = new ArrayList<>(); // Listă de piese

    public Piece selectedPiece; // Piesa selectată

    private MoveLog moveLog; // Jurnal de mutări

    Input input = new Input(this); // Obiect de intrare

    public CheckScanner checkScanner = new CheckScanner(this); // Obiect de verificare a șahului

    public int enPassantTile = -1; // Tile pentru en passant

    private boolean isWhiteToMove = true; // Alb la mutare
    private boolean isGameOver = false; // Jocul este terminat

    public Board(MoveLog moveLog) {
        this.setPreferredSize(new Dimension(cols * titleSize, rows * titleSize)); // Setăm dimensiunea preferată
        this.addMouseListener(input); // Adăugăm ascultătorul de mouse
        this.addMouseMotionListener(input); // Adăugăm ascultătorul de mișcare a mouse-ului
        this.moveLog = moveLog; // Inițializăm jurnalul de mutări
        addPieces(); // Adăugăm piesele pe tablă
    }

    public Piece getPiece(int col, int row) {
        // Returnăm piesa de pe o anumită coloană și rând
        for (Piece piece : pieceList) {
            if (piece.col == col && piece.row == row) {
                return piece;
            }
        }
        return null;
    }

    public void makeMove(Move move) {
        // Facem o mutare
        if (move.piece.name.equals("Pawn")) {
            movePawn(move); // Mutăm pionul
        } else if (move.piece.name.equals("King")) {
            moveKing(move); // Mutăm regele
        }
        move.piece.col = move.newCol; // Actualizăm coloana piesei
        move.piece.row = move.newRow; // Actualizăm rândul piesei
        move.piece.xPos = move.newCol * titleSize; // Actualizăm poziția X
        move.piece.yPos = move.newRow * titleSize; // Actualizăm poziția Y

        move.piece.isFirstMove = false; // Piesa nu mai este la prima mutare

        capture(move.capture); // Capturăm piesa

        String moveString = (move.piece.isWhite ? "White" : "Black") + " " + move.piece.name + ": " + getAlgebraicNotation(move.oldCol, move.oldRow) + " -> " + getAlgebraicNotation(move.newCol, move.newRow); // String de mutare
        if (move.piece instanceof King) {
            String castlingType = ((King) move.piece).getCastlingType(move.newCol, move.newRow); // Tipul rocadei
            if (!castlingType.isEmpty()) {
                moveString = (move.piece.isWhite ? "White" : "Black") + " Castling: " + castlingType; // Adăugăm rocada la mutare
            } else if (move.capture != null) {
                moveString += "\n   captures " + move.capture.name; // Capturarea unei piese
            }
        } else if (move.capture != null) {
            moveString += "\n    captures " + move.capture.name; // Capturarea unei piese
        }

        moveLog.addMove(moveString); // Adăugăm mutarea în jurnal

        isWhiteToMove = !isWhiteToMove; // Schimbăm jucătorul la mutare

        updateGameState(); // Actualizăm starea jocului
    }

    public void resetBoard() {
        // Resetăm tabla de șah
        pieceList.clear(); // Golim lista de piese
        addPieces(); // Adăugăm piesele pe tablă
        isWhiteToMove = true; // Setăm jucătorul alb la mutare
        repaint(); // Reîmprospătăm tabla
    }


    public String getAlgebraicNotation(int col, int row) {
        // Obținem notația algebrică pentru o coloană și un rând
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
        return colString + (8 - row); // Returnăm notația algebrică
    }

    private void moveKing(Move move) {
        // Mutăm regele
        if (Math.abs(move.piece.col - move.newCol) == 2) {
            Piece rook;
            if (move.piece.col < move.newCol) {
                rook = getPiece(7, move.piece.row); // Obținem turnul pentru rocada mare
                rook.col = 5; // Mutăm turnul pe coloană
            } else {
                rook = getPiece(0, move.piece.row); // Obținem turnul pentru rocada mică
                rook.col = 3; // Mutăm turnul pe coloană
            }
            rook.xPos = rook.col * titleSize; // Actualizăm poziția X a turnului
        }
    }

    private void movePawn(Move move) {
        // Mutăm pionul
        int colorIndex = move.piece.isWhite ? 1 : -1;

        if (getTileNum(move.newCol, move.newRow) == enPassantTile) {
            move.capture = getPiece(move.newCol, move.newRow + colorIndex); // Capturăm piesa en passant
        }
        if (Math.abs(move.piece.row - move.newRow) == 2) {
            enPassantTile = getTileNum(move.newCol, move.newRow + colorIndex); // Setăm tile-ul pentru en passant
        } else {
            enPassantTile = -1; // Resetăm tile-ul pentru en passant
        }

        colorIndex = move.piece.isWhite ? 0 : 7;
        if (move.newRow == colorIndex) {
            promotePawn(move); // Promovăm pionul
        }
    }

    private void promotePawn(Move move) {
        // Promovăm pionul
        pieceList.add(new Queen(this, move.newCol, move.newRow, move.piece.isWhite)); // Adăugăm o regină în locul pionului
        capture(move.piece); // Capturăm pionul
    }

    public void capture(Piece piece) {
        // Capturăm piesa
        pieceList.remove(piece); // Înlăturăm piesa din lista de piese
    }

    public boolean isValidMove(Move move){
        // Verifică dacă jocul s-a terminat
        if(isGameOver){
            return false;
        }
        // Verifică dacă mutarea este făcută de jucătorul care are rândul său
        if(move.piece.isWhite != isWhiteToMove){
            return false;
        }
        // Verifică dacă piesele sunt din aceeași echipă
        if(sameTeam(move.piece, move.capture)){
            return false;
        }
        // Verifică dacă mișcarea piesei este validă conform regulilor de mișcare ale piesei respective
        if(!move.piece.isValidMovement(move.newCol, move.newRow)){
            return false;
        }
        // Verifică dacă mișcarea piesei este blocată de alte piese
        if(move.piece.moveCollidesWithPiece(move.newCol, move.newRow)){
            return false;
        }
        // Verifică dacă regele este pus în șah prin această mutare
        if(checkScanner.isKingChecked(move)){
            return false;
        }
        // Dacă toate verificările sunt trecute, mutarea este validă
        return true;
    }

    public boolean sameTeam(Piece p1, Piece p2){
        // Verifică dacă oricare dintre piese este null
        if(p1 == null || p2 == null){
            return false;
        }
        // Verifică dacă piesele sunt din aceeași echipă
        return p1.isWhite == p2.isWhite;
    }

    public int getTileNum(int col, int row){
        // Returnează numărul corespunzător unei poziții pe tablă
        return row * rows + col;
    }

    Piece findKing(boolean isWhite){
        // Găsește și returnează regele din echipa specificată
        for(Piece piece : pieceList){
            if(isWhite == piece.isWhite && piece.name.equals("King")){
                return piece;
            }
        }
        // Returnează null dacă regele nu este găsit
        return null;
    }

    public void addPieces(){
        // Adaugă piesele negre pe tablă
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

        // Adaugă piesele albe pe tablă
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
        // Găsește regele echipei care are rândul să mute
        Piece king = findKing(isWhiteToMove);
        // Verifică dacă jocul s-a terminat (șah-mat sau pat)
        if (checkScanner.isGameOver(king)) {
            if (checkScanner.isKingChecked(new Move(this, king, king.col, king.row))) {
                showGameOverDialog(isWhiteToMove ? "Black Wins!" : "White Wins!");
            } else {
                showGameOverDialog("Stalemate!");
            }
            // Verifică dacă jocul s-a terminat din cauza materialului insuficient
        } else if (insufficientMaterial(true) && insufficientMaterial(false)) {
            showGameOverDialog("Insufficient Material!");
            isGameOver = true;
        }
    }

    private void showGameOverDialog(String message) {
        // Afișează un dialog de terminare a jocului cu opțiunile de restart sau anulare
        int option = JOptionPane.showOptionDialog(
                this,
                message,
                "Game Over",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[] { "Restart", "Cancel" },
                "Restart"
        );

        // Resetează tabla de joc dacă utilizatorul a ales să restarteze jocul
        if (option == JOptionPane.OK_OPTION) {
            resetBoard();
        }
    }

    private boolean insufficientMaterial(boolean isWhite){
        // Colectează numele pieselor pentru echipa specificată
        ArrayList<String> names = pieceList.stream().filter(p -> p.isWhite == isWhite).map(p -> p.name).collect(Collectors.toCollection(ArrayList::new));
        // Verifică dacă există piese suficient de puternice pentru a continua jocul
        if(names.contains("Queen") || names.contains("Rook") || names.contains("Pawn")){
            return false;
        }
        // Verifică dacă există mai puțin de trei piese pe tablă
        return names.size() < 3;
    }

    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        // pictează tabla de șah
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                g2d.setColor((c + r) % 2 == 0 ? new Color(227,198,101) : new Color(157,105,53));
                g2d.fillRect(c * titleSize, r * titleSize, titleSize, titleSize);
            }
        }

        // pictează piesele selectate
        if(selectedPiece != null){
            for(int r = 0; r < rows; r++){
                for(int c = 0; c < cols; c++){
                    Move move = new Move(this, selectedPiece, c, r);
                    if(isValidMove(move)){
                        g2d.setColor(new Color(68, 180, 57, 190));
                        g2d.fillRect(c * titleSize, r * titleSize, titleSize, titleSize);
                        if(move.capture != null && move.capture.isWhite != selectedPiece.isWhite) {
                            g2d.setColor(Color.RED);
                            g2d.fillRect(c * titleSize, r * titleSize, titleSize, titleSize);
                        }
                    }
                }
            }
        }

        // pictează piesele
        for(Piece piece : pieceList){
            piece.paint(g2d);
        }
    }
}
