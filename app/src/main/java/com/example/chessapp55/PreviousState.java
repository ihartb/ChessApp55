package com.example.chessapp55;

import com.example.ChessModel.ChessBoard;
import com.example.ChessModel.ChessPieces;
import com.example.ChessModel.King;

public class PreviousState {
    ChessBoard b1;
    boolean isKingInCheck;
    ChessPieces wk;
    ChessPieces bk;

    public PreviousState(ChessBoard b, boolean i, ChessPieces wk, ChessPieces bk) {
        this.b1 = new ChessBoard(b);
        b1.copyboard(b.board);
        this.isKingInCheck = i;
        this.wk = new King(wk);
        this.bk = new King(bk);
    }

}
