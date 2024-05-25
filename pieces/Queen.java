package pieces; // Declară pachetul pieces

// Importarea clasei BufferedImage pentru lucrul cu imagini.
import java.awt.image.BufferedImage;

// Importarea clasei Board din alt pachet.
import main.Board;

// Declarația clasei Queen, care extinde clasa Piece.
public class Queen extends Piece{

    // Constructorul clasei, care primește un obiect de tip Board, coloana și rândul piesei, și indicatorul pentru culoarea acesteia.
    public Queen(Board board, int col, int row, boolean isWhite){
        // Apelarea constructorului clasei de bază.
        super(board);
        // Inițializarea atributelor piesei.
        this.col = col;
        this.row = row;
        this.xPos = col * board.titleSize;
        this.yPos = row * board.titleSize;

        this.isWhite = isWhite;
        this.name = "Regina";

        // Încărcarea sprite-ului specific pentru regină din imaginea pieselor și scalarea acestuia.
        this.sprite = sheet.getSubimage(1 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.titleSize, board.titleSize, BufferedImage.SCALE_SMOOTH);
    }

    // Metoda care verifică dacă o mutare este validă pentru regină.
    public boolean isValidMovement(int col, int row){
        // Verificarea dacă regina se mișcă pe aceeași coloană, rând sau pe diagonale.
        return this.col == col || this.row == row || Math.abs(this.col - col) == Math.abs(this.row - row);
    }

    // Metoda care verifică dacă o mutare a reginei se intersectează cu o altă piesă.
    public boolean moveCollidesWithPiece(int col, int row){

        // Verificarea dacă regina se mișcă pe aceeași coloană sau pe același rând.
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
        } else {
            // Verificarea dacă se intersectează cu o piesă pe traseul deplasării pe diagonale.

            // Verificarea pentru deplasarea în diagonală în stânga sus.
            if(this.col > col && this.row > row){
                for(int i = 1; i < Math.abs(this.col - col); i++){
                    if(board.getPiece(this.col - i, this.row - i) != null){
                        return true;
                    }
                }
            }

            // Verificarea pentru deplasarea în diagonală în dreapta sus.
            if(this.col < col && this.row > row){
                for(int i = 1; i < Math.abs(this.col - col); i++){
                    if(board.getPiece(this.col + i, this.row - i) != null){
                        return true;
                    }
                }
            }

            // Verificarea pentru deplasarea în diagonală în stânga jos.
            if(this.col > col && this.row < row){
                for(int i = 1; i < Math.abs(this.col - col); i++){
                    if(board.getPiece(this.col - i, this.row + i) != null){
                        return true;
                    }
                }
            }

            // Verificarea pentru deplasarea în diagonală în dreapta jos.
            if(this.col < col && this.row < row){
                for(int i = 1; i < Math.abs(this.col - col); i++){
                    if(board.getPiece(this.col + i, this.row + i) != null){
                        return true;
                    }
                }
            }
        }

        // Dacă nu se intersectează cu nicio piesă pe traseul de deplasare, se returnează false.
        return false;
    }
}
