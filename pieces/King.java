package pieces; // Declară pachetul pieces

// Importarea clasei BufferedImage pentru lucrul cu imagini.
import java.awt.image.BufferedImage;

// Importarea clasei Board din alt pachet.
import main.*;

// Declarația clasei King, care extinde clasa Piece.
public class King extends Piece{

    // Constructorul clasei, care primește un obiect de tip Board, coloana și rândul piesei, și indicatorul pentru culoarea acesteia.
    public King(Board board, int col, int row, boolean isWhite){
        // Apelarea constructorului clasei de bază.
        super(board);
        // Inițializarea atributelor piesei.
        this.col = col;
        this.row = row;
        this.xPos = col * board.titleSize;
        this.yPos = row * board.titleSize;

        this.isWhite = isWhite;
        this.name = "Rege";

        // Încărcarea sprite-ului specific pentru rege din imaginea pieselor și scalarea acestuia.
        this.sprite = sheet.getSubimage(0 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.titleSize, board.titleSize, BufferedImage.SCALE_SMOOTH);
    }

    // Metoda care verifică dacă o mutare este validă pentru rege.
    public boolean isValidMovement(int col, int row){
        // Verificarea dacă distanța între coloane și rânduri este 1 sau dacă este posibilă rocadă.
        return Math.abs((col - this.col) * (row - this.row)) == 1 || Math.abs(col - this.col) + Math.abs(row - this.row) == 1 || canCastle(col, row);
    }

    // Metoda care determină tipul de rocadă în funcție de noua poziție a regeului.
    public String getCastlingType(int newCol, int newRow) {
        // Verificarea dacă mutarea este o rocadă scurtă sau lungă, sau dacă nu este o rocadă.
        if (this.row == newRow) {
            if (newCol == 6) {
                return "0-0"; // Roca scurtă
            } else if (newCol == 2) {
                return "0-0-0"; // Roca lungă
            }
        }
        return ""; // Fără rocadă
    }

    // Metoda care verifică dacă este posibilă o rocadă.
    private boolean canCastle(int col, int row){
        // Verificarea dacă rocul este pe aceeași linie cu regele.
        if(this.row == row){
            // Verificarea dacă este posibilă rocadă scurtă.
            if(col == 6){
                // Obținerea piesei turei de pe poziția corespunzătoare pentru rocadă scurtă.
                Piece rook = board.getPiece(7, row);
                // Verificarea dacă tura există, nu a fost mutată și regele nu a fost mutat.
                if(rook != null && rook.isFirstMove && isFirstMove){
                    // Verificarea dacă cele două poziții intermediare sunt libere și regele nu este în șah.
                    return board.getPiece(5, row) == null &&
                            board.getPiece(6, row) == null &&
                            !board.checkScanner.isKingChecked(new Move(board, this, 5, row));
                }
            }
            // Verificarea dacă este posibilă rocadă lungă.
            else if(col == 2){
                // Obținerea piesei turei de pe poziția corespunzătoare pentru rocadă lungă.
                Piece rook = board.getPiece(0, row);
                // Verificarea dacă tura există, nu a fost mutată și regele nu a fost mutat.
                if(rook != null && rook.isFirstMove && isFirstMove){
                    // Verificarea dacă cele trei poziții intermediare sunt libere și regele nu este în șah.
                    return board.getPiece(3, row) == null &&
                            board.getPiece(2, row) == null &&
                            board.getPiece(1, row) == null &&
                            !board.checkScanner.isKingChecked(new Move(board, this, 3, row));
                }
            }
        }
        // Dacă nu se poate face rocadă, returnează false.
        return false;
    }
}
