package main; // Declară pachetul main

import pieces.Piece; // Importă clasa Piece din pachetul pieces

public class Move{ // Declară clasa Move
    int oldCol; // Declară variabila pentru coloana inițială
    int oldRow; // Declară variabila pentru rândul inițial

    int newCol; // Declară variabila pentru noua coloană
    int newRow; // Declară variabila pentru noul rând

    Piece piece; // Declară variabila pentru piesa care se mută
    Piece capture; // Declară variabila pentru piesa capturată

    public Move(Board board, Piece piece, int newCol, int newRow){ // Constructorul clasei Move
        this.oldCol = piece.col; // Inițializează coloana inițială cu coloana piesei
        this.oldRow = piece.row; // Inițializează rândul inițial cu rândul piesei
        this.newCol = newCol; // Inițializează noua coloană cu valoarea primită
        this.newRow = newRow; // Inițializează noul rând cu valoarea primită

        this.piece = piece; // Inițializează piesa care se mută cu valoarea primită
        this.capture = board.getPiece(newCol, newRow); // Inițializează piesa capturată (dacă există) cu piesa de pe noua poziție
    }
}
