package pieces; // Declară pachetul pieces

// Importarea clasei BufferedImage pentru lucrul cu imagini.
import java.awt.image.BufferedImage;

// Importarea clasei Board din alt pachet.
import main.Board;

// Declarația clasei Pawn, care extinde clasa Piece.
public class Pawn extends Piece{

    // Constructorul clasei, care primește un obiect de tip Board, coloana și rândul piesei, și indicatorul pentru culoarea acesteia.
    public Pawn(Board board, int col, int row, boolean isWhite){
        // Apelarea constructorului clasei de bază.
        super(board);
        // Inițializarea atributelor piesei.
        this.col = col;
        this.row = row;
        this.xPos = col * board.titleSize;
        this.yPos = row * board.titleSize;

        this.isWhite = isWhite;
        this.name = "Pion";

        // Încărcarea sprite-ului specific pentru pion din imaginea pieselor și scalarea acestuia.
        this.sprite = sheet.getSubimage(5 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.titleSize, board.titleSize, BufferedImage.SCALE_SMOOTH);
    }

    // Metoda care verifică dacă o mutare este validă pentru pion.
    public boolean isValidMovement(int col, int row){
        // Indexul de culoare pentru a determina direcția de mutare a pionului.
        int colorIndex = isWhite ? 1 : -1;

        // Mutarea simplă înainte cu o poziție.
        if(this.col == col && row == this.row - colorIndex && board.getPiece(col, row) == null){
            return true;
        }

        // Mutarea inițială a pionului, cu două poziții înainte.
        if(isFirstMove && this.col == col && row == this.row - colorIndex * 2 && board.getPiece(col, row) == null && board.getPiece(col, row + colorIndex) == null){
            return true;
        }

        // Capturarea piesei din stânga pionului.
        if(col == this.col - 1 && row == this.row - colorIndex && board.getPiece(col, row) != null){
            return true;
        }

        // Capturarea piesei din dreapta pionului.
        if(col == this.col + 1 && row == this.row - colorIndex && board.getPiece(col, row) != null){
            return true;
        }

        // Capturarea en passant pe stânga.
        if(board.getTileNum(col, row) == board.enPassantTile && col == this.col - 1 && row == this.row - colorIndex && board.getPiece(col, row + colorIndex) != null){
            return true;
        }

        // Capturarea en passant pe dreapta.
        if(board.getTileNum(col, row) == board.enPassantTile && col == this.col + 1 && row == this.row - colorIndex && board.getPiece(col, row + colorIndex) != null){
            return true;
        }

        // Dacă niciuna dintre condițiile de mai sus nu este îndeplinită, mutarea nu este validă.
        return false;
    }
}
