/**
 * Pawn class implements legal move and is a subclass of chessPieces
 *
 *
 * @author  Bharti mehta bam270 Jeancarlo Bolanos jb1618
 */
package com.example.ChessModel;

import java.util.ArrayList;

public class Pawn extends ChessPieces implements LegalMove {

    boolean firstMove = true;

    public Pawn(boolean color,int x, int y){
        super(color, x, y );
    }

    public String toString(){
        return super.toString()+"p";
    }

    @Override
    public ArrayList<ArrayList<Integer>> getMoves() {
        ArrayList<ArrayList<Integer>> moves =  super.getMoves();
        int x = this.getX();
        int y = this.getY();
        if (this.getColor()) {
            if (x != 0) {
                moves.get(0).add(x-1);
                moves.get(1).add(y);
                if (firstMove) {
                    moves.get(0).add(x-2);
                    moves.get(1).add(y);
                }
                if (y != 7){
                    moves.get(0).add(x-1);
                    moves.get(1).add(y+1);
                }
                if (y != 0){
                    moves.get(0).add(x-1);
                    moves.get(1).add(y-1);
                }
            }
        } else {
            if (x != 7) {
                moves.get(0).add(x+1);
                moves.get(1).add(y);
                if (firstMove) {
                    moves.get(0).add(x+2);
                    moves.get(1).add(y);
                }
                if (y != 7){
                    moves.get(0).add(x+1);
                    moves.get(1).add(y+1);
                }
                if (y != 0){
                    moves.get(0).add(x+1);
                    moves.get(1).add(y-1);
                }
            }
        }

        return moves;
    }

    //x and y position based on array list index!
    public boolean legalMove(int x2, int y2){
        int diffX = x2 - super.getX();
        int diffY = y2 - super.getY();

        if (diffY == 0) {
            if ( (Math.abs(diffX) == 2 && firstMove) || (Math.abs(diffX) == 1)) {
                if (diffX < 0){
                    return super.getColor();
                }
                return !super.getColor();
            }
        }

        return this.captureMove(x2, y2);

    }

    public boolean captureMove(int x2, int y2) {
        int diffX = x2 - super.getX();
        int diffY = y2 - super.getY();

        if (Math.abs(diffX) == 1 && Math.abs(diffY) == 1) {
            if (diffX < 0){
                return super.getColor();
            }
            return !super.getColor();
        }

        return false;
    }

    public ArrayList<ArrayList<Integer>>  getPath(int x2, int y2){

        ArrayList<ArrayList<Integer>> path = super.getPath(x2, y2);

        if (!this.legalMove(x2, y2)) {
            return path;
        }


        if(this.captureMove(x2,y2)){
            path.get(0).add(x2);
            path.get(1).add(y2);
        } else {
            if (super.getColor()){
                for (int i=super.getX()-1; i >= x2; i--){
                    path.get(0).add(i);
                    path.get(1).add(super.getY());
                }
            } else {
                for (int i=super.getX()+1; i <= x2; i++){
                    path.get(0).add(i);
                    path.get(1).add(super.getY());
                }
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
