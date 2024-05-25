package pieces; // Declară pachetul pieces

// Importarea clasei BufferedImage pentru lucrul cu imagini.
import java.awt.image.BufferedImage;

// Importarea clasei Board din alt pachet.
import main.Board;

// Declarația clasei Bishop, care extinde clasa Piece.
public class Bishop extends Piece{

    // Constructorul clasei, care primește un obiect de tip Board, coloana și rândul piesei, și indicatorul pentru culoarea acesteia.
    public Bishop(Board board, int col, int row, boolean isWhite){
        // Apelarea constructorului clasei de bază.
        super(board);
        // Inițializarea atributelor piesei.
        this.col = col;
        this.row = row;
        this.xPos = col * board.titleSize;
        this.yPos = row * board.titleSize;

        this.isWhite = isWhite;
        this.name = "Episcop";

        // Încărcarea sprite-ului specific pentru nebun din imaginea pieselor și scalarea acestuia.
        this.sprite = sheet.getSubimage(2 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.titleSize, board.titleSize, BufferedImage.SCALE_SMOOTH);
    }

    // Metoda care verifică dacă o mutare este validă pentru nebun.
    public boolean isValidMovement(int col, int row){
        // Verificarea dacă diferența dintre coloane este egală cu diferența dintre rânduri, ceea ce indică o mișcare pe diagonală.
        return Math.abs(this.col - col) == Math.abs(this.row - row);
    }

    // Metoda care verifică dacă mutarea colidează cu o altă piesă.
    public boolean moveCollidesWithPiece(int col, int row){

        // Verificarea dacă mutarea se află în direcția "sus-stânga".
        if(this.col > col && this.row > row){
            // Iterarea prin toate pozițiile intermediare între poziția curentă și poziția finală.
            for(int i = 1; i < Math.abs(this.col - col); i++){
                // Verificarea dacă există o piesă pe această poziție.
                if(board.getPiece(this.col - i, this.row - i) != null){
                    return true;
                }
            }
        }

        // Verificarea dacă mutarea se află în direcția "sus-dreapta".
        if(this.col < col && this.row > row){
            // Iterarea prin toate pozițiile intermediare între poziția curentă și poziția finală.
            for(int i = 1; i < Math.abs(this.col - col); i++){
                // Verificarea dacă există o piesă pe această poziție.
                if(board.getPiece(this.col + i, this.row - i) != null){
                    return true;
                }
            }
        }

        // Verificarea dacă mutarea se află în direcția "jos-stânga".
        if(this.col > col && this.row < row){
            // Iterarea prin toate pozițiile intermediare între poziția curentă și poziția finală.
            for(int i = 1; i < Math.abs(this.col - col); i++){
                // Verificarea dacă există o piesă pe această poziție.
                if(board.getPiece(this.col - i, this.row + i) != null){
                    return true;
                }
            }
        }

        // Verificarea dacă mutarea se află în direcția "jos-dreapta".
        if(this.col < col && this.row < row){
            // Iterarea prin toate pozițiile intermediare între poziția curentă și poziția finală.
            for(int i = 1; i < Math.abs(this.col - col); i++){
                // Verificarea dacă există o piesă pe această poziție.
                if(board.getPiece(this.col + i, this.row + i) != null){
                    return true;
                }
            }
        }

        // Dacă nu se găsește nicio coliziune, returnează false.
        return false;
    }
}
