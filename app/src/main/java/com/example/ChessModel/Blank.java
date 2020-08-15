// Bhart mehta bam270 Jeancarlo Bolanos jb1618
package com.example.ChessModel;


public class Blank extends ChessPieces implements LegalMove {

    public Blank(int x, int y) {
        super(x, y);
    }

    @Override
    public String toString() {
        return "blank";
    }
}
