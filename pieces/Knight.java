package pieces; // Declară pachetul pieces

// Importarea clasei BufferedImage pentru lucrul cu imagini.
import java.awt.image.BufferedImage;

// Importarea clasei Board din alt pachet.
import main.Board;

// Declarația clasei Knight, care extinde clasa Piece.
public class Knight extends Piece{

    // Constructorul clasei, care primește un obiect de tip Board, coloana și rândul piesei, și indicatorul pentru culoarea acesteia.
    public Knight(Board board, int col, int row, boolean isWhite){
        // Apelarea constructorului clasei de bază.
        super(board);
        // Inițializarea atributelor piesei.
        this.col = col;
        this.row = row;
        this.xPos = col * board.titleSize;
        this.yPos = row * board.titleSize;

        this.isWhite = isWhite;
        this.name = "Cavaler";

        // Încărcarea sprite-ului specific pentru cal din imaginea pieselor și scalarea acestuia.
        this.sprite = sheet.getSubimage(3 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.titleSize, board.titleSize, BufferedImage.SCALE_SMOOTH);
    }

    // Metoda care verifică dacă o mutare este validă pentru cal.
    public boolean isValidMovement(int col, int row){
        // Verificarea dacă produsul dintre diferența de coloane și diferența de rânduri este 2, ceea ce indică mișcarea în forma de L a calului.
        return Math.abs(col - this.col) * Math.abs(row - this.row) == 2;
    }
}
