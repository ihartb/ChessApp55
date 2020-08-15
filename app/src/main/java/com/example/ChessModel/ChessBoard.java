/**
 * Chessboard class keeps track of whos move it is and maintains the game state
 *
 *
 * @author  Bharti mehta bam270 Jeancarlo Bolanos jb1618
 */
package com.example.ChessModel;


import com.example.chessapp55.ChessGameActivity;

import java.util.*;
public class ChessBoard {
    public List<List<ChessPieces>> board = new ArrayList<List<ChessPieces>>(8);
    public boolean whosmove = true;
    public boolean running = true;
    public ChessPieces enpassant = new Blank(0, 0);
    public ArrayList<ArrayList<Integer>> checkerslol = new ArrayList<ArrayList<Integer>>();

    public ChessBoard(){
        ArrayList<ChessPieces> r1 = new ArrayList<ChessPieces>(8);
            r1.add(new Rook(false,0,0));
            r1.add(new Knight(false,0, 1));
            r1.add(new Bishop(false,0, 2));
            r1.add(new Queen(false,0, 3));
            r1.add(new King(false,0, 4));
            r1.add(new Bishop(false,0, 5));
            r1.add(new Knight(false,0, 6));
            r1.add(new Rook(false,0, 7));
            this.board.add(r1);

        ArrayList<ChessPieces> r2 = new ArrayList<ChessPieces>(8);
        ArrayList<ChessPieces> r7 = new ArrayList<ChessPieces>(8);
        for(int j = 0; j < 8; j++){
            r2.add(new Pawn(false, 1,j));
            r7.add(new Pawn(true, 6,j));
        }
        this.board.add(r2);

        for (int i= 2; i < 6; i++) {
            ArrayList<ChessPieces> r3 = new ArrayList<ChessPieces>(8);
            for(int j = 0; j < 8; j++){
                r3.add(new Blank(i,j));
            }
            this.board.add(r3);
        }

        this.board.add(r7);

        ArrayList<ChessPieces> r8 = new ArrayList<ChessPieces>(8);
        r8.add(new Rook(true,7,0));
        r8.add(new Knight(true,7, 1));
        r8.add(new Bishop(true,7, 2));
        r8.add(new Queen(true,7, 3));
        r8.add(new King(true,7, 4));
        r8.add(new Bishop(true,7, 5));
        r8.add(new Knight(true,7, 6));
        r8.add(new Rook(true,7, 7));
        this.board.add(r8);
    }

    public ChessBoard(ChessBoard j){
        this.board = new ArrayList<>();
        this.whosmove = j.whosmove;
        this.running = true;
        this.enpassant = j.enpassant;
    }

    public void copyboard(List<List<ChessPieces>> board){
        for(List<ChessPieces> currrow : board){
            ArrayList<ChessPieces> row = new ArrayList<ChessPieces>();
            for(ChessPieces currP:currrow){
                if(currP instanceof Rook){
                    row.add(new Rook(currP.getColor(),currP.getX(),currP.getY()));

                }
                if(currP instanceof Blank){
                    row.add(new Blank(currP.getX(),currP.getY()));

                }
                if(currP instanceof Pawn){
                    row.add(new Pawn(currP.getColor(),currP.getX(),currP.getY()));

                }
                if(currP instanceof Knight){
                    row.add(new Knight(currP.getColor(),currP.getX(),currP.getY()));

                }
                if(currP instanceof Bishop){
                    row.add(new Bishop(currP.getColor(),currP.getX(),currP.getY()));

                }
                if(currP instanceof Queen){
                    row.add(new Queen(currP.getColor(),currP.getX(),currP.getY()));

                }
                if(currP instanceof King){
                    row.add(new King(currP.getColor(),currP.getX(),currP.getY()));

                }

            }
            this.board.add(row);


        }

    }

    public void dispBoard(){
        System.out.println();
        for (int i = 0; i < 8; i++){
            for (int j=0; j < 8; j++){
                ChessPieces c = this.board.get(i).get(j);
                if (c instanceof Blank) {
                    if ( (i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1) ) {
                        System.out.print("   ");
                    } else {
                        System.out.print("## ");
                    }
                } else {
                    System.out.print(c+" ");
                }
            }
            System.out.println(8-i);
        }
        System.out.println(" 0  1  2  3  4  5  6  7");
    }

    public boolean hasInterference(ChessPieces current, int x2, int y2){

        if (current instanceof Knight){
            ChessPieces pieceInPath = this.board.get(x2).get(y2);
            if (pieceInPath instanceof Blank) {
                return false;
            } else if (pieceInPath.getColor() == current.getColor()) {
                return true;
            }
            return false;
        }

        ArrayList<ArrayList<Integer>> path = current.getPath(x2, y2);

        for (int i=0; i < path.get(0).size(); i++){
            int x = path.get(0).get(i);
            int y = path.get(1).get(i);
            ChessPieces pieceInPath = this.board.get(x).get(y);

            if (!(pieceInPath instanceof Blank)){
                if (x == x2 && y == y2) {
                    if (current instanceof Pawn && !((Pawn) current).captureMove(x2, y2) ) {
                        return true;
                    }
                    if (pieceInPath.getColor() == current.getColor()) {
                        return true;
                    } else { //else capture, we might not need this else here, idk yet
                        return false;
                    }
                }
                return true;
            }
        }

        return false;
    }

    public void movePiece(ChessPieces current, int x2, int y2) {
        int x1 = current.getX();
        int y1 = current.getY();

        this.board.get(x1).set(y1, new Blank(x1,y1));
        current.updatePosition(x2, y2);
        this.board.get(x2).set(y2, current);

    }

    public boolean isPromotion(ChessPieces current, int x2) {
        // not pawn return
        if ( !(current instanceof Pawn) ||  !( (x2 == 0) || (x2 == 7) ) || ! ( (x2 == 0 && current.getColor()) || (x2 == 7 && !current.getColor()) ) ) {
            return false;
        }

        return true;
    }

    public boolean isEnpassant(Pawn current, int x2, int y2) {

        if ( enpassant.getColor() == current.getColor() || current.getX() != enpassant.getX() ) {
//            System.out.println("color match or x dont match");
            return false;
        }

        if ( !(current.getY() == enpassant.getY()+1 || current.getY() == enpassant.getY()-1) ){
//            System.out.println("y incorrect ");
            return false;
        }

        if ( current.captureMove(x2, y2) && this.board.get(x2).get(y2) instanceof Blank) {
            return true;
        }

        return false;
    }

    public boolean isCastling(King current, int x2, int y2) {
        int x1 = current.getX();
        int y1 = current.getY();

        if (! (current.firstMove && x1 == x2 && Math.abs(y2-y1) == 2)) {
            return false;
        }

        int diffY = y1-y2;
        ChessPieces r1 = this.board.get(x1).get(7);
        if (diffY > 0) {
            r1 = this.board.get(x1).get(0);
        }


        if  (!( r1 instanceof Rook && ((Rook) r1).firstMove && r1.getColor() == current.getColor()) ){
            return false;
        }

        //doesnt pass thru check
        ArrayList<ArrayList<Integer>> path = current.getPath(x2, y2);
        for (int i=0; i < path.get(0).size(); i++){
            int x = path.get(0).get(i);
            int y = path.get(1).get(i);
            ChessBoard copy = new ChessBoard(this);
            copy.copyboard(this.board);
            ChessPieces pcopy = copy.board.get(current.getX()).get(current.getY());
            copy.movePiece(pcopy, x, y);
            System.out.println("Move 1: path for checks:");
            copy.dispBoard();
            if (!copy.kingCheck(x,y,current.getColor()).isEmpty() ) {
                System.out.println("is in check");
                return false;
            }
        }

        //no interference
        path = current.getPath(r1.getX(), r1.getY());
        for (int i=0; i < path.get(0).size()-1; i++){
            int x = path.get(0).get(i);
            int y = path.get(1).get(i);
            ChessPieces pieceInPath = this.board.get(x).get(y);
            if (!(pieceInPath instanceof Blank)) {
                return false;
            }
        }

        return true;

    }

    // here we check the perimeter of the king to see if the current move will put us in check
    public ArrayList<ChessPieces> kingCheck(int x, int y,boolean color){
        ArrayList<ChessPieces> mansincheck = new ArrayList<>();
        ArrayList<Integer> todd = new ArrayList<Integer>();
        //have to check for blanks

//        this.dispBoard();

        //check right
        for(int i =1; y+i<=7;i++){
            ChessPieces current = this.board.get(x).get(y+i);
            todd.add(x);
            todd.add(y+i);
            if(current.getClass()==Blank.class){
                continue;
            }
            if(current.getClass()==Rook.class||current.getClass()==Queen.class||(current.getClass() == King.class && i ==1 )){
                if(current.getColor()==color){
                    break;
                }
                else checkerslol.add(todd); mansincheck.add(current);
            }
            else{
                break;
            }
        }

        //checking left
        todd = new ArrayList<Integer>();
        for(int i =1; y-i>=0;i++){
            ChessPieces current = this.board.get(x).get(y-i);
            todd.add(x);
            todd.add(y-i);
            if(current.getClass()==Blank.class){
                continue;
            }
            if(current.getClass()==Rook.class||current.getClass()==Queen.class||(current.getClass() == King.class && i ==1 )){
                if(current.getColor()==color){
                    break;
                }
                else checkerslol.add(todd); mansincheck.add(current);
            }
            else{ break;}
        }
        todd = new ArrayList<Integer>();

        //check top
        for(int i=1;x-i>=0;i++){
            ChessPieces current = this.board.get(x-i).get(y);
            todd.add(x-i);
            todd.add(y);
            if(current.getClass()==Blank.class){
                continue;
            }
            if(current.getClass()==Rook.class||current.getClass()==Queen.class||(current.getClass() == King.class && i ==1 )){
                if(current.getColor()==color){
                    break;
                }
                else{
                    checkerslol.add(todd);
                    mansincheck.add(current);
                    break;
                }
            }
            else {break;}

        }
        todd = new ArrayList<Integer>();

        //check bottom
//        System.out.println("bottom");
        for(int i=1;x+i<=7;i++) {
            ChessPieces current = this.board.get(x + i).get(y);
//            System.out.println( "Current piece: "+(x+i)+", "+y+ " "+current);
            todd.add(x+i);
            todd.add(y);
            if(current.getClass()==Blank.class){
//                System.out.println("blank");
                continue;
            }
            if (current.getClass() == Rook.class || current.getClass() == Queen.class || (current.getClass() == King.class && i == 1)) {
                if (current.getColor() == color) {
//                    System.out.println("interferece same color right class");
                    break;
                } else{
//                    System.out.println("add");
                    checkerslol.add(todd);
                    mansincheck.add(current);
                    break;
                }

            } else  {
//                System.out.println("interferece");
                break;
            }

        }
        todd = new ArrayList<Integer>();

        //check diagonal down left
        for(int i =1; y-i>=0&&x+i<=7;i++){
            ChessPieces current = this.board.get(x+i).get(y-i);
            todd.add(x+i);
            todd.add(y-i);
            if(current.getClass()==Blank.class){
                continue;
            }
            if(current.getColor()==color){
                break;
            }
            if(current.getClass()==Pawn.class&&i==1&&!color){
                //pawn can attack kings move (black specifically
                checkerslol.add(todd);
                mansincheck.add(current);
                break;
            }
            if(current.getClass() == Queen.class || (current.getClass() == King.class&& i == 1)||current.getClass()==Bishop.class){
                checkerslol.add(todd);
                mansincheck.add(current);
                break;
            }

        }
        todd = new ArrayList<Integer>();

        // check diagonal down right
        for(int i=1; i+y<=7 &&i+x<=7;i++){
            ChessPieces current = this.board.get(x+i).get(y+i);
            todd.add(x+i);
            todd.add(y+i);
            if(current instanceof Blank){
                System.out.println("hitblank");
                continue;
            }
            if(current.getColor()==color){
                break;
            }
            if(current.getClass()==Pawn.class&&i==1&&!color){
                //pawn can attack kings move
                if(current.getColor()!=color){
                    checkerslol.add(todd);
                     mansincheck.add(current);
                     break;
                }
            }
            if(current.getClass() == Queen.class || (current.getClass() == King.class && i == 1)||current.getClass()==Bishop.class){
                checkerslol.add(todd);
                mansincheck.add(current);
                break;

            }
        }
        todd = new ArrayList<Integer>();

        //check diagonally up left
        for(int i=1; y-i>=0&&x-i>=0;i++){
            ChessPieces current = this.board.get(x-i).get(y-i);
            todd.add(x-i);
            todd.add(y-i);
            if(current.getClass()==Blank.class){
                continue;
            }
            if(current.getColor()==color){
                break;
            }
            if(current.getClass()==Pawn.class&&i==1&&color){
                //pawn can attack kings move
                checkerslol.add(todd);
                mansincheck.add(current);
                break;
            }
            if(current.getClass() == Queen.class || (current.getClass() == King.class&& i == 1)||current.getClass()==Bishop.class){
                checkerslol.add(todd);
                mansincheck.add(current);
                break;

            }
        }
        todd = new ArrayList<Integer>();

        //check diagonal up right
        for(int i =1; y+i<=7&&x-i>=0;i++){
            System.out.println("check opposite diagonals");
            ChessPieces current = this.board.get(x-i).get(y+i);
            todd.add(x-i);
            todd.add(y+i);

            if(current instanceof Blank){
                System.out.println("hitblank");
                continue;
            }
            if(current.getColor()==color){
                System.out.println("hitinterf");
                System.out.println(current.getX()+", "+current.getY());
                System.out.println(current);
                dispBoard();
                break;
            }
            if(current.getClass()==Pawn.class&&i==1&&color){
                //pawn can attack kings move
                checkerslol.add(todd);
                mansincheck.add(current);
                break;
            }
            if(current.getClass() == Queen.class || (current.getClass() == King.class&&i == 1)||current.getClass()==Bishop.class){
                System.out.println("added");
                checkerslol.add(todd);
                mansincheck.add(current);
            }

        }
        todd = new ArrayList<Integer>();
        //check for knights
        ChessPieces normal = this.board.get(x).get(y);
        int x1 = normal.getX();
        int y2 = normal.getY();
        if(x+2<=7){
            if(y+1<=7){
                ChessPieces c1 = this.board.get(x+2).get(y+1);
//                todd.add(x);
//                todd.add(y);
                if(c1.getClass()==Knight.class){
                    if(c1.getColor()!=color){
//                        checkerslol.add(todd);
                        mansincheck.add(c1);
                    }
                }
            }
            todd = new ArrayList<Integer>();
            if(y-1>=0){
                ChessPieces c1 = this.board.get(x+2).get(y-1);
//                todd.add(x);
//                todd.add(y);
                if(c1.getClass()==Knight.class){
                    if(c1.getColor()!=color){
//                        checkerslol.add(todd);
                        mansincheck.add(c1);
                    }
                }
            }
        }
        todd = new ArrayList<Integer>();
        if(x+1<=7){
            if(y+2<=7){
                ChessPieces c1 = this.board.get(x+1).get(y+2);
//                todd.add(x);
//                todd.add(y);
                if(c1.getClass()==Knight.class){
                    if(c1.getColor()!=color){
//                        checkerslol.add(todd);
                        mansincheck.add(c1);
                    }
                }
            }
            todd = new ArrayList<Integer>();
            if(y-2>=0){
                ChessPieces c1 = this.board.get(x+1).get(y-2);
//                todd.add(x);
//                todd.add(y);
                if(c1.getClass()==Knight.class){
                    if(c1.getColor()!=color){
//                        checkerslol.add(todd);
                        mansincheck.add(c1);
                    }
                }
            }
            todd = new ArrayList<Integer>();

        }
        todd = new ArrayList<Integer>();
        if(x-2>=0){
            if(y+1<=7){
                ChessPieces c1 = this.board.get(x-2).get(y+1);
//                todd.add(x);
//                todd.add(y);
                if(c1.getClass()==Knight.class){
                    if(c1.getColor()!=color){
//                        checkerslol.add(todd);
                        mansincheck.add(c1);
                    }
                }
            }
            todd = new ArrayList<Integer>();
            if(y-1>=0){
                ChessPieces c1 = this.board.get(x-2).get(y-1);
//                todd.add(x);
//                todd.add(y);
                if(c1.getClass()==Knight.class){
                    if(c1.getColor()!=color){
//                        checkerslol.add(todd);
                        mansincheck.add(c1);
                    }
                }
            }

        }
        todd = new ArrayList<Integer>();
        if(x-1>=0){
            if(y+2<=7){
                ChessPieces c1 = this.board.get(x-1).get(y+2);
//                todd.add(x);
//                todd.add(y);
                if(c1.getClass()==Knight.class){
                    if(c1.getColor()!=color){
//                        checkerslol.add(todd);
                        mansincheck.add(c1);
                    }
                }
            }
            todd = new ArrayList<Integer>();
            if(y-2>=0){
                ChessPieces c1 = this.board.get(x-1).get(y-2);
//                todd.add(x);
//                todd.add(y);
                if(c1.getClass()==Knight.class){
                    if(c1.getColor()!=color){
//                        checkerslol.add(todd);
                        mansincheck.add(c1);
                    }
                }
            }


        }
        todd = new ArrayList<Integer>();

//        System.out.println("checkerslol:");
//        for (int i = 0; i < checkerslol.size(); i++) {
//            System.out.println(checkerslol.get(i).get(0)+", "+ checkerslol.get(i).get(1) );
//        }
        System.out.println("mansincheck:");
        System.out.println(mansincheck);
        return mansincheck;

    }

    public boolean checkMate(ChessPieces king) {
        // here we run the kingCheck on each square around the king for each available space check
//        System.out.println("Checkmate checkerslol:");
//        for (int i = 0; i < checkerslol.size(); i++) {
//            System.out.println(this.board.get(checkerslol.get(i).get(0)).get(checkerslol.get(i).get(1)));
//        }

        boolean currcol = king.getColor();
        int x = king.getX();
        int y = king.getY();
        //these for loops go around in a square around the king and search to see if any spot is safe (must also check for interference)


        ArrayList<ArrayList<Integer>> moves = king.getMoves();
        for (int i = 0; i < moves.get(0).size(); i++) {
            ChessBoard copy = new ChessBoard(this);
            copy.copyboard(this.board);
//            System.out.println("OG BOARD");
//            this.dispBoard();
//            System.out.println("COPY BOARD");
//            copy.dispBoard();
            ChessPieces pcopy = copy.board.get(x).get(y);
            int x2 = moves.get(0).get(i);
            int y2 = moves.get(1).get(i);
            if (!copy.hasInterference(pcopy, x2, y2)) {
                copy.movePiece(pcopy, x2, y2);
//                System.out.println("COPY BOARD AFTER MOVE");
//                copy.dispBoard();
                if (copy.kingCheck(x2, y2,king.getColor()).isEmpty() ) {
                    checkerslol.clear();
                    return false;
                }
            }
        }


//        for (int i = 1; i >= -1; i--) {
//            if(y+i>=0&&y+i<=7){
//                for (int j = -1; j <= 1; j++) {
//                    if(x+j<=7&&x+j>=0){
//                        if (i == 0 && j == 0) {
//                            continue;
//                        }
//
//                        ChessBoard copy = new ChessBoard(this);
//                        copy.copyboard(this.board);
////                        System.out.println("OG BOARD");
////                        this.dispBoard();
////                        System.out.println("COPY BOARD");
////                        copy.dispBoard();
//                        ChessPieces pcopy = copy.board.get(x).get(y);
//                        copy.movePiece(pcopy, x+j, y+i);
////                        System.out.println("COPY BOARD AFTER MOVE");
////                        copy.dispBoard();
//                        if (!copy.hasInterference(king, x+j, y+i)){
//                            if (copy.kingCheck(x+j,y+i,king.getColor()).isEmpty() ) {
//                                checkerslol.clear();
////                                System.out.println("king can be moved to: "+ (x+j) + ", "+(y+i) );
//                                return false;
//                            }
//                        }
//
////                        if (kingCheck(x + j, y + i, king.getColor()).isEmpty()) {
////                            //now see if space is occupied
////                            if(!this.hasInterference(king,x+j,y+i)){
////                                //System.out.println("Safe place found at x"+(x+j)+"and y"+(y+i)+"original coordinates given"+x+y);
////                                checkerslol.clear();
////                                System.out.println("king can be moved to: "+ (x+j) + ", "+(y+i) );
////                                return false;
////                            }
////
////                        }
//
//                    }
//
//                }
//
//            }
//        }
            /*The function below will run throughout the entire board to see if we can block the attackers path by */
        for(List<ChessPieces> meep: this.board){
            for(ChessPieces curr: meep){
                if(curr.getClass()==Blank.class){
//                    System.out.println("blank");
                    continue;
                }
                if(curr.getColor()==currcol){
                    //since its the same color as the king we're going to check if we're able to save him
                    for(List<Integer> row: checkerslol){
                        for(int i=0; i<row.size()-1;i++){
                            x= row.get(i);
                            y= row.get(i+1);
                            System.out.println(curr);
                            System.out.println(curr.getX()+", "+curr.getY());
                            System.out.println(x+", "+y);
                            //if this is true then we can save the king
                            if(curr.legalMove(x,y) && !this.hasInterference(curr, x, y)){
                                //see if it will keep the king still in check.
                                System.out.println(curr);
                                System.out.println(curr.getX()+", "+curr.getY());
                                System.out.println(x+", "+y);
                                if(safetycheck(x,y,curr,king)){
                                    System.out.println("path can be blocked");
                                    checkerslol.clear();
                                    return false;
                                }

                            }

                        }
                    }

                }

            }
        }

        // now check to see if we can take out the piece holding our king captive.
        if(checkingenemy(kingCheck(x,y,king.getColor()), king)){
            checkerslol.clear();
            System.out.println("capture the checking piece");
            return false;
        }
        return true;
    }


    //this methods checks if we can move the piece and have it not be a check.
    public boolean safetycheck(int x, int y, ChessPieces piece, ChessPieces currking){

        ChessBoard copy = new ChessBoard(this);
        copy.copyboard(this.board);


        ChessPieces pcopy = copy.board.get(piece.getX()).get(piece.getY());
//        System.out.println("og board");
//        b1.dispBoard();
//        System.out.println("copied board");
//        copy.dispBoard();
//        System.out.println("og piece");
//        System.out.println(piece);
//        System.out.println(piece.getX()+", "+piece.getY());
//
//        System.out.println("copied piece");
//        System.out.println(pcopy);
//        System.out.println(pcopy.getX()+", "+pcopy.getY());
        int res = new ChessGameActivity(true).specialMoves(pcopy, x, y, copy, 0);
        if (res == 0) {
            return false;
        } else if (res == 2) {
            copy.movePiece(pcopy,x,y);
        } // res == 1, piece moved
        if (pcopy instanceof King) {
            currking = pcopy;
        }
//        System.out.println("copied board after move");
//        copy.dispBoard();
        if(!copy.kingCheck(currking.getX(),currking.getY(),currking.getColor()).isEmpty()){
            return false;
        }

        return true;

    }

    //function that returns true if we are able to escape enemies checks by capturing their attacking piece
    public boolean checkingenemy(ArrayList<ChessPieces> NME, ChessPieces currking){
        //looks at the pieces holding current king in check
        for (ChessPieces piece: NME) {
            //Look to see if any of our pieces are threatening that checking piece
            ArrayList<ChessPieces> nmenmes = this.kingCheck(piece.getX(),piece.getY(),piece.getColor());
            for(ChessPieces npiece: nmenmes){
                //here im checking to see if we can move an ally piece to the threatenings position and resolve the check
                ChessBoard copy = new ChessBoard(this);
                copy.copyboard(this.board);
                ChessPieces pcopy = copy.board.get(npiece.getX()).get(npiece.getY());
                copy.movePiece(pcopy, piece.getX(), piece.getY());
                if (pcopy instanceof King) {
                    currking = pcopy;
                }

                if(copy.kingCheck(currking.getX(),currking.getY(),currking.getColor()).isEmpty()){
                    //that means the king is no longer in check
                    return true;
                }


            }


        }
        return false;

    }

}
