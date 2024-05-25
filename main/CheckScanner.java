package main; // Declară pachetul main

// Importarea clasei Piece din alt pachet.
import pieces.Piece;

// Declarația clasei CheckScanner.
public class CheckScanner {
    // Variabila membru pentru tabla de joc.
    Board board;

    // Constructorul clasei, care primește un obiect de tip Board.
    public CheckScanner(Board board){
        // Inițializarea variabilei membru cu tabla primită ca argument.
        this.board = board;
    }

    // Metoda care verifică dacă regele este în șah în urma unei mutări.
    public boolean isKingChecked(Move move) {
        // Găsirea regelei pe tabla de joc și asigurarea că aceasta nu este nulă.
        Piece king = board.findKing(move.piece.isWhite);
        assert king != null;

        // Coordonatele coloanei și rândului regelei.
        int kingCol = king.col;
        int kingRow = king.row;

        // Actualizarea coordonatelor regelei dacă este selectat.
        if (board.selectedPiece != null && board.selectedPiece.name.equals("Rege")) {
            kingCol = move.newCol;
            kingRow = move.newRow;
        }

        // Verificarea dacă regele este amenințat de o tură în toate direcțiile, apoi de un nebun, un cal, un pion și, în cele din urmă, de un rege.
        return hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, 0, 1) || // în sus
                hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, 1, 0) || // la dreapta
                hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, 0, -1) || // în jos
                hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, -1, 0) || // la stânga
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, -1, -1) || // în sus-stânga
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, 1, -1) || // în sus-dreapta
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, 1, 1) || // în jos-dreapta
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, -1, 1) || // în jos-stânga
                hitByKnight(move.newCol, move.newRow, king, kingCol, kingRow) || // sărit de cal
                hitByPawn(move.newCol, move.newRow, king, kingCol, kingRow) || // atacat de pion
                hitByKing(king, kingCol, kingRow); // atacat de un alt rege
    }

    // Metoda privată care verifică dacă regele este atacat de o tură.
    private boolean hitByRook(int col, int row, Piece king, int kingCol, int kingRow, int colVal, int rowVal){
        for(int i = 1; i < 8; i++){
            // Verificarea dacă regele este atacat în direcția specificată.
            if(kingCol + (i * colVal) == col && kingRow + (i * rowVal) == row){
                break;
            }

            // Obținerea piesei la coordonatele actuale.
            Piece piece = board.getPiece(kingCol + (i * colVal), kingRow + (i * rowVal));
            // Verificarea dacă există o piesă și nu este echipa aceleiași echipe cu regele și este o tură sau regina.
            if(piece != null && piece != board.selectedPiece){
                if(!board.sameTeam(piece, king) && (piece.name.equals("Rook") || piece.name.equals("Regina"))){
                    return true;
                }
                break;
            }
        }
        return false;
    }

    // Metoda privată care verifică dacă regele este atacat de un nebun.
    private boolean hitByBishop(int col, int row, Piece king, int kingCol, int kingRow, int colVal, int rowVal){
        for(int i = 1; i < 8; i++){
            // Verificarea dacă regele este atacat în direcția specificată.
            if(kingCol - (i * colVal) == col && kingRow - (i * rowVal) == row){
                break;
            }

            // Obținerea piesei la coordonatele actuale.
            Piece piece = board.getPiece(kingCol - (i * colVal), kingRow - (i * rowVal));
            // Verificarea dacă există o piesă și nu este echipa aceleiași echipe cu regele și este un nebun sau regina.
            if(piece != null && piece != board.selectedPiece){
                if(!board.sameTeam(piece, king) && (piece.name.equals("Episcop") || piece.name.equals("Regina"))){
                    return true;
                }
                break;
            }
        }
        return false;
    }

    // Metoda privată care verifică dacă regele este atacat de un cal.
    private boolean hitByKnight(int col, int row, Piece king, int kingCol, int kingRow){
        // Verificarea fiecărei poziții posibile pentru un cal în raport cu poziția regeleui și dacă aceasta este o amenințare.
        return checkKnight(board.getPiece(kingCol - 1, kingRow - 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 1, kingRow - 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 2, kingRow - 1), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 2, kingRow + 1), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 1, kingRow + 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol - 1, kingRow + 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol - 2, kingRow + 1), king, col, row) ||
                checkKnight(board.getPiece(kingCol - 2, kingRow - 1), king, col, row);
    }

    // Metoda privată care verifică dacă o anumită piesă este un cal și nu este de aceeași echipă cu regele.
    private boolean checkKnight(Piece p, Piece k, int col, int row){
        return p != null && !board.sameTeam(p, k) && p.name.equals("Cavaler") && !(p.col == col && p.row == row);
    }

    // Metoda privată care verifică dacă regele este atacat de un alt rege.
    private boolean hitByKing(Piece king, int kingCol, int kingRow){
        // Verificarea fiecărei poziții posibile pentru un rege în raport cu poziția regeleui și dacă aceasta este o amenințare.
        return checkKing(board.getPiece(kingCol - 1, kingRow - 1), king) ||
                checkKing(board.getPiece(kingCol + 1, kingRow - 1), king) ||
                checkKing(board.getPiece(kingCol, kingRow - 1), king) ||
                checkKing(board.getPiece(kingCol - 1, kingRow), king) ||
                checkKing(board.getPiece(kingCol + 1, kingRow), king) ||
                checkKing(board.getPiece(kingCol - 1, kingRow + 1), king) ||
                checkKing(board.getPiece(kingCol + 1, kingRow + 1), king) ||
                checkKing(board.getPiece(kingCol, kingRow + 1), king);

    }

    // Metoda privată care verifică dacă o anumită piesă este un rege și nu este de aceeași echipă cu regele dat.
    private boolean checkKing(Piece p, Piece k){
        return p != null && !board.sameTeam(p, k) && p.name.equals("Rege");
    }

    // Metoda privată care verifică dacă regele este atacat de un pion.
    private boolean hitByPawn(int col, int row, Piece king, int kingCol, int kingRow){
        // Valoarea care indică direcția de deplasare a pionului în funcție de culoarea acestuia.
        int colorVal = king.isWhite ? -1 : 1;
        // Verificarea dacă pionul este o amenințare pentru rege.
        return checkPawn(board.getPiece(kingCol + 1, kingRow + colorVal), king, col, row) ||
                checkPawn(board.getPiece(kingCol - 1, kingRow + colorVal), king, col, row);
    }

    // Metoda privată care verifică dacă o anumită piesă este un pion și nu este de aceeași echipă cu regele dat.
    private boolean checkPawn(Piece p, Piece k, int col, int row){
        return p != null && !board.sameTeam(p, k) && p.name.equals("Pion");
    }

    // Metoda care verifică dacă jocul s-a încheiat.
    public boolean isGameOver(Piece king){
        // Iterarea prin lista de piese pentru a verifica dacă vreo piesă poate face o mutare validă.
        for(Piece piece : board.pieceList){
            // Dacă piesa este de aceeași echipă cu regele, verificăm dacă poate face o mutare validă.
            if(board.sameTeam(piece, king)){
                // Selectăm piesa pentru a efectua verificările de mutare.
                board.selectedPiece = piece == king ? king : null;
                for(int row = 0; row < board.rows; row++){
                    for(int col = 0; col < board.cols; col++){
                        // Crearea unei mutări și verificarea dacă aceasta este validă.
                        Move move = new Move(board, piece, col, row);
                        if(board.isValidMove(move)){
                            return false;
                        }
                    }
                }
            }
        }
        // Dacă nicio piesă nu poate face o mutare validă, jocul este terminat.
        return true;
    }
}
