/**
 * Chess pieces class keeps track of where the current location of each piece is and what faction they belong to
 * setters getters are available and legal move is to be implemented
 *
 *
 * @author  Bhart mehta bam270 Jeancarlo Bolanos jb1618
 */
package com.example.ChessModel;

import java.util.*;


public class ChessPieces implements LegalMove {
    private boolean color; //"w"=true or "b"=false
    private int x;
    private int y;
    private String img;

    public ChessPieces(){
    }


    public ChessPieces(int x, int y){
        this.x = x;
        this.y = y;
        this.img = this.toString();
    }

    public ChessPieces(boolean color, int x, int y){
        this.color = color;
        this.x = x;
        this.y = y;
        this.img = this.toString();
    }

    public String toString(){
        String res = "w";
        if (!color) {
            res = "b";
        }
        return res;
    }

    public void updatePosition(int x, int y){
        this.setX(x);
        this.setY(y);
    }


    public ArrayList<ArrayList<Integer>> getMoves(){
        ArrayList<ArrayList<Integer>> path = new  ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < 2; i++){
            ArrayList<Integer> r1 = new ArrayList<Integer>();
            path.add(r1);
        }
        return path;

    }

    public boolean legalMove(int x2, int y2){
        return false;
    }

    //dont include start position in getPath, bc that is only occupied by current piece
    public ArrayList<ArrayList<Integer>> getPath(int x2, int y2){
        ArrayList<ArrayList<Integer>> path = new  ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < 2; i++){
            ArrayList<Integer> r1 = new ArrayList<Integer>();
            path.add(r1);
        }
        return path;
    }

    public boolean getColor(){
        return this.color;
    }

    public void setColor(boolean c){
        this.color = c;
    }

    public int getX(){
        return this.x;
    }

    public void setX(int x){
        if (this instanceof Queen){
            System.out.println("set x");
        }
        this.x = x;
    }

    public int getY(){
        return this.y;
    }

    public void setY(int x){
        this.y = x;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
