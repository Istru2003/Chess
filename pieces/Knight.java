package pieces;

import java.awt.image.BufferedImage;
import main.Board;

public class Knight extends Piece{

    public Knight(Board board, int col, int row, boolean isWhite){
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.titleSize;
        this.yPos = row * board.titleSize;
        this.isWhite = isWhite;
        this.name = "Cavaler";
        this.sprite = sheet.getSubimage(3 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.titleSize, board.titleSize, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(int col, int row){
        return Math.abs(col - this.col) * Math.abs(row - this.row) == 2;
    }
}
