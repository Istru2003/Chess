package pieces; // Declară pachetul pieces

// Importarea claselor necesare pentru lucrul cu imagini.
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

// Importarea claselor necesare pentru citirea imaginilor.
import javax.imageio.ImageIO;

// Importarea clasei Board din alt pachet.
import main.Board;

// Declarația clasei Piece.
public class Piece {
    // Coordonatele coloanei și rândului la care se află piesa.
    public int col, row;
    // Coordonatele x și y ale piesei pe ecran.
    public int xPos, yPos;

    // Indicator pentru culoarea piesei (true pentru alb, false pentru negru).
    public boolean isWhite;
    // Numele piesei.
    public String name;
    // Valoarea piesei.
    public int value;

    // Indicator pentru prima mutare a piesei.
    public boolean isFirstMove = true;

    // Imaginea de sprite pentru toate piesele.
    BufferedImage sheet;
    {
        // Blocul de inițializare pentru încărcarea imaginii pieselor.
        try {
            // Încărcarea imaginii pieselor din fișierul "pieces.png".
            sheet = ImageIO.read(ClassLoader.getSystemResourceAsStream("res/pieces.png"));
        } catch (IOException e) {
            // În caz de eroare la citirea imaginii, se afișează stack trace-ul.
            e.printStackTrace();
        }
    }

    // Scalarea imaginii pentru fiecare piesă.
    protected int sheetScale = sheet.getWidth()/6;

    // Imaginea specifică a piesei.
    Image sprite;

    // Referință către tabla de joc.
    Board board;

    // Constructorul clasei, care primește un obiect de tip Board.
    public Piece(Board board){
        // Inițializarea referinței la tabla de joc cu tabla primită ca argument.
        this.board = board;
    }

    // Metoda care verifică dacă o anumită mutare este validă pentru piesa curentă.
    public boolean isValidMovement(int col, int row){
        // Întotdeauna returnează true pentru a fi suprascrisă în subclase.
        return true;
    }

    // Metoda care verifică dacă mutarea colidează cu o altă piesă.
    public boolean moveCollidesWithPiece(int col, int row){
        // Întotdeauna returnează false pentru a fi suprascrisă în subclase.
        return false;
    }

    // Metoda pentru desenarea piesei pe tabla de joc.
    public void paint(Graphics2D g2d){
        // Desenarea imaginii sprite-ului piesei la coordonatele sale pe ecran.
        g2d.drawImage(sprite, xPos, yPos, null);
    }
}
