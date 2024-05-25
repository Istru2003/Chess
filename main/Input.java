package main; // Declară pachetul main

import java.awt.event.MouseAdapter; // Importă MouseAdapter pentru manipularea evenimentelor de mouse
import java.awt.event.MouseEvent; // Importă MouseEvent pentru evenimentele de mouse

import pieces.Piece; // Importă clasa Piece din pachetul pieces

public class Input extends MouseAdapter{ // Declară clasa Input extinzând MouseAdapter

    Board board; // Declară o variabilă Board

    public Input(Board board){ // Constructorul clasei Input
        this.board = board; // Inițializează variabila board cu valoarea primită
    }

    @Override
    public void mousePressed(MouseEvent e){ // Suprascrie metoda mousePressed
        int col = e.getX()/board.titleSize; // Calculează coloana pe baza coordonatei X
        int row = e.getY()/board.titleSize; // Calculează rândul pe baza coordonatei Y

        Piece pieceXY = board.getPiece(col, row); // Obține piesa de la coordonatele calculate
        if(pieceXY != null){ // Verifică dacă există o piesă la coordonatele respective
            board.selectedPiece = pieceXY; // Setează piesa selectată
        }
    }

    @Override
    public void mouseDragged(MouseEvent e){ // Suprascrie metoda mouseDragged
        if(board.selectedPiece != null){ // Verifică dacă există o piesă selectată
            board.selectedPiece.xPos = e.getX() - board.titleSize / 2; // Actualizează poziția X a piesei
            board.selectedPiece.yPos = e.getY() - board.titleSize / 2; // Actualizează poziția Y a piesei

            board.repaint(); // Reface desenul tablei
        }
    }

    @Override
    public void mouseReleased(MouseEvent e){ // Suprascrie metoda mouseReleased
        int col = e.getX()/board.titleSize; // Calculează coloana pe baza coordonatei X
        int row = e.getY()/board.titleSize; // Calculează rândul pe baza coordonatei Y

        if(board.selectedPiece != null){ // Verifică dacă există o piesă selectată
            Move move = new Move(board, board.selectedPiece, col, row); // Creează o mutare

            if(board.isValidMove(move)){ // Verifică dacă mutarea este validă
                board.makeMove(move); // Efectuează mutarea
            } else { // Dacă mutarea nu este validă
                board.selectedPiece.xPos = board.selectedPiece.col * board.titleSize; // Resetează poziția X a piesei
                board.selectedPiece.yPos = board.selectedPiece.row * board.titleSize; // Resetează poziția Y a piesei
            }
        }

        board.selectedPiece = null; // Deselectează piesa
        board.repaint(); // Reface desenul tablei
    }
}
