/**
 * Knight class implements legal move and is a subclass of chessPieces
 *
 *
 * @author  Bharti Mehta bam270 Jeancarlo Bolanos jb1618
 */
package com.example.ChessModel;


import java.util.ArrayList;

public class Knight extends ChessPieces implements LegalMove {

    public Knight(boolean color,int x, int y){
        super(color, x, y );
    }

    public String toString(){
        return super.toString()+"n";
    }


    @Override
    public ArrayList<ArrayList<Integer>> getMoves() {
        ArrayList<ArrayList<Integer>> moves =  super.getMoves();
        int x = this.getX();
        int y = this.getY();

        if (x+2 <= 7) {
            if (y+1 <= 7) {
                moves.get(0).add(x+2);
                moves.get(1).add(y+1);
            }
            if (y-1 >= 0){
                moves.get(0).add(x+2);
                moves.get(1).add(y-1);
            }

        }

        if (x+1 <= 7) {
            if (y+2 <= 7) {
                moves.get(0).add(x + 1);
                moves.get(1).add(y + 2);
            }
            if (y-2 >= 0){
                moves.get(0).add(x + 1);
                moves.get(1).add(y - 2);
            }
        }


        if (x-2 >= 0) {
            if (y+1 <= 7) {
                moves.get(0).add(x-2);
                moves.get(1).add(y+1);
            }
            if (y-1 >= 0){
                moves.get(0).add(x-2);
                moves.get(1).add(y-1);
            }

        }

        if (x-1 >= 0) {
            if (y+2 <= 7) {
                moves.get(0).add(x-1);
                moves.get(1).add(y + 2);
            }
            if (y-2 >= 0){
                moves.get(0).add(x - 1);
                moves.get(1).add(y - 2);
            }
        }

        return moves;
    }

    //implement
    //x and y position based on array list index!
    public boolean legalMove(int x2, int y2){
        int xdiff = Math.abs(x2-super.getX());
        int ydiff = Math.abs(y2-super.getY());
        if(ydiff ==0 && xdiff==0){
            return false;
        }

        if ( (xdiff == 2 && ydiff == 1) || (ydiff == 2 && xdiff == 1) ) {
            return true;
        }
        return false;
//        return Math.abs(xdiff - ydiff) == 1;
    }

    //implement
    public ArrayList<ArrayList<Integer>>  getPath(int x2, int y2){

        ArrayList<ArrayList<Integer>> path = super.getPath(x2, y2);

        if (!this.legalMove(x2, y2)) {
            return path;
        }


        return path;
    }


}
