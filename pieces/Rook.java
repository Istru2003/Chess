package pieces;

// Importarea clasei BufferedImage pentru lucrul cu imagini.
import java.awt.image.BufferedImage;

// Importarea clasei Board din alt pachet.
import main.Board;

// Declarația clasei Rook, care extinde clasa Piece.
public class Rook extends Piece{

    // Constructorul clasei, care primește un obiect de tip Board, coloana și rândul piesei, și indicatorul pentru culoarea acesteia.
    public Rook(Board board, int col, int row, boolean isWhite){
        // Apelarea constructorului clasei de bază.
        super(board);
        // Inițializarea atributelor piesei.
        this.col = col;
        this.row = row;
        this.xPos = col * board.titleSize;
        this.yPos = row * board.titleSize;

        this.isWhite = isWhite;
        this.name = "Rook";

        // Încărcarea sprite-ului specific pentru turn din imaginea pieselor și scalarea acestuia.
        this.sprite = sheet.getSubimage(4 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.titleSize, board.titleSize, BufferedImage.SCALE_SMOOTH);
    }

    // Metoda care verifică dacă o mutare este validă pentru turn.
    public boolean isValidMovement(int col, int row){
        // Verificarea dacă turnul se mișcă pe aceeași coloană sau pe același rând.
        return this.col == col || this.row == row;
    }

    // Metoda care verifică dacă o mutare a turnului se intersectează cu o altă piesă.
    public boolean moveCollidesWithPiece(int col, int row){

        // Verificarea dacă turnul se mișcă pe aceeași coloană sau pe același rând.
        if(this.col == col || this.row == row){
            // Verificarea dacă se intersectează cu o piesă pe traseul deplasării în stânga.
            if(this.col > col){
                for(int c = this.col - 1; c > col; c--){
                    if(board.getPiece(c, this.row) != null){
                        return true;
                    }
                }
            }

            // Verificarea dacă se intersectează cu o piesă pe traseul deplasării în dreapta.
            if(this.col < col){
                for(int c = this.col + 1; c < col; c++){
                    if(board.getPiece(c, this.row) != null){
                        return true;
                    }
                }
            }

            // Verificarea dacă se intersectează cu o piesă pe traseul deplasării în sus.
            if(this.row > row){
                for(int r = this.row - 1; r > row; r--){
                    if(board.getPiece(this.col, r) != null){
                        return true;
                    }
                }
            }

            // Verificarea dacă se intersectează cu o piesă pe traseul deplasării în jos.
            if(this.row < row){
                for(int r = this.row + 1; r < row; r++){
                    if(board.getPiece(this.col, r) != null){
                        return true;
                    }
                }
            }
        }

        // Dacă nu se intersectează cu nicio piesă pe traseul de deplasare, se returnează false.
        return false;
    }

}
