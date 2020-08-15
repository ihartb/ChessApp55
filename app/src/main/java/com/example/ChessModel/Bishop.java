// Bhart mehta bam270 Jeancarlo Bolanos jb1618
package com.example.ChessModel;
import java.util.ArrayList;

public class Bishop extends ChessPieces implements LegalMove {

    public Bishop(boolean color,int x, int y){
        super(color, x, y );
    }

    public String toString(){
        return super.toString()+"b";
    }

    //x and y position based on array list index!
    public boolean legalMove(int x2, int y2){
        int diffX = x2 - super.getX();
        int diffY = y2 - super.getY();
        if (super.getX() == x2 && super.getY() == y2) {
            return false;
        }
        return Math.abs(diffX) == Math.abs(diffY);

    }

    @Override
    public ArrayList<ArrayList<Integer>> getMoves() {
        ArrayList<ArrayList<Integer>> moves =  super.getMoves();
        int x = this.getX();
        int y = this.getY();

        for (int i = -7;  i < 8; i ++) {
            if (i == 0) { continue; }
            if ( (x+i <= 7 && x+i >= 0) && (y+i<=7 && y+i >= 0)) {
                moves.get(0).add(x+i);
                moves.get(1).add(y+i);
            }

            if ( (x+i <= 7 && x+i >= 0) && (y-i<=7 && y-i >= 0)) {
                moves.get(0).add(x+i);
                moves.get(1).add(y-i);
            }

        }

        return moves;
    }

    //implement
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
            }
        } else {
            for (int i=super.getX()-1; i >= x2; i--){
                path.get(0).add(i);
            }
        }

        if (diffY > 0) {
            for (int i=super.getY()+1; i <= y2; i++){
                path.get(1).add(i);
            }
        } else {
            for (int i=super.getY()-1; i >= y2; i--){
                path.get(1).add(i);
            }
        }


        return path;
    }
}
