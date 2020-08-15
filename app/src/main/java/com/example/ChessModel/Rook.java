/**
 * Rook class implements legal move and is a subclass of chessPieces
 *
 *
 * @author  Bharti mehta bam270 Jeancarlo Bolanos jb1618
 */

package com.example.ChessModel;


import java.util.ArrayList;

public class Rook extends ChessPieces implements LegalMove {

    boolean firstMove = true;

    public Rook(boolean color,int x, int y){
        super(color, x, y );
    }

    public String toString(){
        return super.toString()+"r";
    }

    @Override
    public ArrayList<ArrayList<Integer>> getMoves() {
        ArrayList<ArrayList<Integer>> moves =  super.getMoves();
        int x = this.getX();
        int y = this.getY();

        for (int i = -7;  i < 8; i ++) {
            if (i == 0) { continue; }
            if (x+i <= 7 && x+i >= 0) {
                moves.get(0).add(x+i);
                moves.get(1).add(y);
            }

            if (y+i<=7 && y+i >= 0) {
                moves.get(0).add(x);
                moves.get(1).add(y+i);
            }
        }

        return moves;
    }

    //x and y position based on array list index!
    public boolean legalMove(int x2, int y2){
        int diffX = x2 - super.getX();
        int diffY = y2 - super.getY();

        if (diffX == 0 ^ diffY == 0) {
            return true;
        }

        return false;
    }

    public ArrayList<ArrayList<Integer>>  getPath(int x2, int y2){

        ArrayList<ArrayList<Integer>> path = super.getPath(x2, y2);

        if (!this.legalMove(x2, y2)) {
            return path;
        }

        int diffX = x2 - super.getX();
        int diffY = y2 - super.getY();

        if (diffX > 0) {
            for (int i=super.getX()+1; i <= x2; i++){
                path.get(0).add(i);
                path.get(1).add(super.getY());
            }
        } else if (diffX < 0){
            for (int i=super.getX()-1; i >= x2; i--){
                path.get(0).add(i);
                path.get(1).add(super.getY());
            }
        } else if (diffY > 0) {
            for (int i=super.getY()+1; i <= y2; i++){
                path.get(0).add(super.getX());
                path.get(1).add(i);
            }
        } else if (diffY < 0){
            for (int i=super.getY()-1; i >= y2; i--){
                path.get(0).add(super.getX());
                path.get(1).add(i);
            }
        }

        return path;
    }


    public void updatePosition(int x2, int y2) {
        if (this.firstMove) {
            this.firstMove = false;
        }

        super.updatePosition(x2, y2);
    }
}
