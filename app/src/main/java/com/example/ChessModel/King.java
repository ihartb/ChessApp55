/**
 * King class implements legal move and is a subclass of chessPieces
 *
 *
 * @author  Bharti mehta bam270 Jeancarlo Bolanos jb1618
 */
package com.example.ChessModel;
import java.lang.Math;
import java.util.ArrayList;

public class King extends ChessPieces implements LegalMove {
    boolean firstMove = true;

    public King(boolean color,int x, int y){
        super(color, x, y );
    }

    public King(ChessPieces k){
        super(k.getColor(), k.getX(), k.getY());
        if (k instanceof King){
            this.firstMove = ((King) k).firstMove;
        }
    }

    public String toString(){
        return super.toString()+"k";
    }

    @Override
    public ArrayList<ArrayList<Integer>> getMoves() {
        ArrayList<ArrayList<Integer>> moves =  super.getMoves();
        int x = this.getX();
        int y = this.getY();

        if (x!= 0) {
            moves.get(0).add(x-1);
            moves.get(1).add(y);
            if (y!=0) {
                moves.get(0).add(x-1);
                moves.get(1).add(y-1);
            }
            if (y!=7) {

                moves.get(0).add(x-1);
                moves.get(1).add(y+1);
            }
        }

        if (x!=7) {
            moves.get(0).add(x+1);
            moves.get(1).add(y);
            if (y!=0) {
                moves.get(0).add(x+1);
                moves.get(1).add(y-1);
            }
            if (y!=7) {
                moves.get(0).add(x+1);
                moves.get(1).add(y+1);
            }
        }

        if (y!=0) {
            moves.get(0).add(x);
            moves.get(1).add(y-1);
        }
        if (y!=7) {
            moves.get(0).add(x);
            moves.get(1).add(y+1);
        }
        return moves;
    }


    //x and y position based on array list index!
    public boolean legalMove(int x2, int y2){
        int xdiff = Math.abs(x2 - super.getX());
        int ydiff = Math.abs(y2-super.getY());
        if(ydiff ==0 && xdiff==0){
            return false;
        }

        //valid diagonal move
        if((xdiff == 0 && ydiff ==1)||(xdiff==1 && ydiff==0) || (ydiff == 2 && xdiff == 0 && this.firstMove) ){
            // valid horizontal or vertical move
            return true;
        } else return xdiff == 1 && ydiff == 1;

    }

    //implement
    public ArrayList<ArrayList<Integer>>  getPath(int x2, int y2){

        ArrayList<ArrayList<Integer>> path = super.getPath(x2, y2);

        if (!this.legalMove(x2, y2)) {
            return path;
        }

        if (Math.abs(y2-super.getY()) == 2) {
            path.get(0).add(x2);
            if (Math.max(y2, super.getY()) == y2) {
                path.get(1).add(super.getY()+1);
            } else {
                path.get(1).add(super.getY()-1);
            }
        }

        path.get(0).add(x2);
        path.get(1).add(y2);

        return path;
    }

    public void updatePosition(int x2, int y2) {
        if (this.firstMove) {
            this.firstMove = false;
        }
        super.updatePosition(x2, y2);
    }

}
