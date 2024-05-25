package pieces;

import java.awt.image.BufferedImage;
import main.*;

public class King extends Piece{

    public King(Board board, int col, int row, boolean isWhite){
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.titleSize;
        this.yPos = row * board.titleSize;
        this.isWhite = isWhite;
        this.name = "Rege";
        this.sprite = sheet.getSubimage(0 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.titleSize, board.titleSize, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(int col, int row){
        return Math.abs((col - this.col) * (row - this.row)) == 1 || Math.abs(col - this.col) + Math.abs(row - this.row) == 1 || canCastle(col, row);
    }

    public String getCastlingType(int newCol, int newRow) {
        if (this.row == newRow) {
            if (newCol == 6) {
                return "0-0";
            } else if (newCol == 2) {
                return "0-0-0";
            }
        }
        return "";
    }

    private boolean canCastle(int col, int row){
        if(this.row == row){
            if(col == 6){
                Piece rook = board.getPiece(7, row);
                if(rook != null && rook.isFirstMove && isFirstMove){
                    return board.getPiece(5, row) == null &&
                            board.getPiece(6, row) == null &&
                            !board.checkScanner.isKingChecked(new Move(board, this, 5, row));
                }
            }
            else if(col == 2){
                Piece rook = board.getPiece(0, row);
                if(rook != null && rook.isFirstMove && isFirstMove){
                    return board.getPiece(3, row) == null &&
                            board.getPiece(2, row) == null &&
                            board.getPiece(1, row) == null &&
                            !board.checkScanner.isKingChecked(new Move(board, this, 3, row));
                }
            }
        }
        return false;
    }
}
