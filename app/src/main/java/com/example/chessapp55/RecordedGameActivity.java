package com.example.chessapp55;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ChessModel.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class RecordedGameActivity extends AppCompatActivity {
    Button playGameButton, recordedGamesButton, nextMoveButton;
    GridLayout board;
    TextView whosTurnText, checkDisp;
    SavedGames curr;


    ChessBoard b1 = new ChessBoard();
    boolean isKingInCheck = false;
    ChessPieces wk = b1.board.get(7).get(4);
    ChessPieces bk = b1.board.get(0).get(4);
    ArrayList<int[]> listOfMoves;
    int index = 0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recorded_game);
        loadData();
        listOfMoves = curr.tuXmi;
        recordedGamesButton = findViewById(R.id.recordedGames);
        nextMoveButton = findViewById(R.id.nextMove);
        playGameButton = findViewById(R.id.playGame);
        board = findViewById(R.id.board);
        whosTurnText = findViewById(R.id.whos_turn_text);
        checkDisp = findViewById(R.id.checkDisp);
        nextMoveButton.setOnClickListener(v -> next());
        playGameButton.setOnClickListener(v -> playGame());
        recordedGamesButton.setOnClickListener(v -> recordedGames());
        whosTurnText.setText("WHITE");
        checkDisp.setText("");
        initializeBoard();
    }
    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("currgame",null);
        Type type = new TypeToken<SavedGames>(){}.getType();
        curr = gson.fromJson(json,type);
        if(curr == null){
            curr = new SavedGames();
        }
    }

    //go to ChessGameActivity page
    public void playGame(){
        Intent menuIntent = new Intent(this, ChessGameActivity.class);
        startActivity(menuIntent);
    }

    //go to ListOfGamesActivity page
    public void recordedGames(){
        Intent menuIntent = new Intent(this, ListOfGamesActivity.class);
        startActivity(menuIntent);

    }

    public void gameEnd(String s){
        Toast.makeText(this, s, Toast.LENGTH_LONG);
        checkDisp.setText(s);
        nextMoveButton.setEnabled(false);
    }

    public void next(){
        if (index == listOfMoves.size()) {
            gameEnd("Game end.");
            return;
        }
        int i1 = listOfMoves.get(index)[0];
        int i2 = listOfMoves.get(index)[1];
        int promo = listOfMoves.get(index)[2];
        parseInputToCommand(i1, i2, promo);
        b1.whosmove = !b1.whosmove;
        index++;
        gamePreqs();
    }

    public void gamePreqs(){
        b1.dispBoard();
        checkDisp.setText(R.string.blank);
        if(b1.whosmove){
            isKingInCheck = b1.kingCheck(wk.getX(),wk.getY(),wk.getColor()).isEmpty() ? false: true;
            if(isKingInCheck){
                if(b1.checkMate(wk)){
                    Context context = getApplicationContext();
                    Toast.makeText(context, "Checkmate. Black Wins!",
                            Toast.LENGTH_SHORT).show();
                    gameEnd("Checkmate. Black Wins!");
                    return;
                }
                checkDisp.setText(R.string.check);
                System.out.println("check");
            }
            whosTurnText.setText(R.string.whiteTurn);
            System.out.println("white");
        }else{
            System.out.println(bk.getX()+", "+bk.getY());
            isKingInCheck = b1.kingCheck(bk.getX(),bk.getY(),bk.getColor()).isEmpty()?false:true;
            if(isKingInCheck) {
                if (b1.checkMate(bk)) {
                    Context context = getApplicationContext();
                    Toast.makeText(context, "Checkmate. White Wins!",
                            Toast.LENGTH_SHORT).show();
                    gameEnd("Checkmate. White Wins!");
                    return;
                }
                checkDisp.setText(R.string.check);
                System.out.println("check");
            }
            whosTurnText.setText(R.string.blackTurn);
            System.out.println("black");
        }
    }

    public boolean parseInputToCommand(int start, int end, int promo){
        ArrayList<Integer> meep = convertIndexToPoint(start, end);
        int x1 = meep.get(0);
        int y1 = meep.get(1);
        int x2 = meep.get(2);
        int y2 = meep.get(3);
        ChessPieces piece = b1.board.get(x1).get(y1);
        System.out.println(piece.getX()+", "+piece.getY()+": "+piece);
        System.out.println(x2+", "+y2);
        // make sure that the piece corresponds to the persons move it currently is
        if(piece.getColor()!=b1.whosmove){
            return false;
        }

        System.out.println(x1+", "+y1+": "+piece);
        System.out.println(x2+", "+y2);
        ChessPieces currking;
        if(b1.whosmove){
            currking = wk;
        } else{
            currking = bk;
        }
        System.out.println(x1+", "+y1+": "+piece);
        System.out.println(x2+", "+y2);

        if (piece instanceof Blank) {
            System.out.println("blank");
            return false;
        }
        System.out.println(x1+", "+y1+": "+piece);
        System.out.println(x2+", "+y2);
        if( !piece.legalMove(x2, y2) ){
            System.out.println(x1+", "+y1+": "+piece);
            System.out.println(x2+", "+y2);
            System.out.println("legal");
            return false;
        }
        if (b1.hasInterference(piece, x2, y2)){
            System.out.println("interf");
            return false;
        }

        //this will check if the king is in check in general
        if(piece instanceof King){
            if(!b1.kingCheck(x2,y2,piece.getColor()).isEmpty()){
                System.out.println("move keeps king in check1");
                return false;
            }
        }

        if (!KINGSTUFF(x1, y1, x2, y2, currking, promo)) {
            return false;
        }

        //***FIRST CHECK IF KING IN CHECK, IF SO KING SHOULD BE REMOVED FROM CHECK FIRST else return false
        if(isKingInCheck){
            if (!KINGSTUFF(x1, y1, x2, y2, currking, promo)) {
                System.out.println("move keeps king in check2");
                return false;
            }
        }
        isKingInCheck = false;
        int res = specialMoves(piece, x2, y2, b1, promo);
        if (res == 0) {
            return false;
        } else if (res == 1) {
            return true;
        }


        if (b1.enpassant instanceof Pawn) {
            b1.enpassant = new Blank(0,0);
        }

        movePieceUI(piece, x2, y2);
        b1.movePiece(piece, x2, y2);
        return true;

    }

    public boolean KINGSTUFF(int x1, int y1, int x2, int y2, ChessPieces currking, int promo) {
        ChessBoard copy = new ChessBoard(b1);
        copy.copyboard(b1.board);
        ChessPieces pcopy = copy.board.get(x1).get(y1);
        int res = specialMoves(pcopy, x2, y2, copy, promo);
        if (res == 0) {
            return false;
        } else if (res == 2) {
            copy.movePiece(pcopy,x2,y2);
        } // res == 1, piece moved
        if (pcopy instanceof King) {
            currking = pcopy;
        }
        if(!copy.kingCheck(currking.getX(),currking.getY(),currking.getColor()).isEmpty()){
//            checkDisp.setText(R.string.check);
            System.out.println("check");
            return false;
        }

        return true;
    }

    public int specialMoves(ChessPieces piece, int x2, int y2, ChessBoard b, int promo) {
        // 0 = false
        // 1 = true
        // 2 = neither

        if (piece instanceof Pawn) {
            if (b.isPromotion(piece, x2)) {
                int x1 = piece.getX();
                int y1 = piece.getY();
                ChessPieces update = new Queen(piece.getColor(), x1, y1);
                switch(promo){
                    case 0:
                        break;
                    case 1:
                        update = new Bishop(piece.getColor(), x1, y1);
                        break;
                    case 2:
                        update = new Knight(piece.getColor(), x1, y1);
                        break;
                    case 3:
                        update = new Rook(piece.getColor(), x1, y1);
                        break;
                }

                if (b.equals(b1)) {
                    movePieceUI(update, x2, y2);
                }

                b.movePiece(update, x2, y2);
                return 1;
            }
            if (b.enpassant instanceof Pawn && b.isEnpassant((Pawn) piece, x2, y2)) {
                System.out.println("Enpassant");
                if (b.equals(b1)) {
                    movePieceUI(piece, x2, y2);
                    int index = convertPointToIndex(b1.enpassant.getX(), b1.enpassant.getY());
                    LinearLayout ll = (LinearLayout) board.getChildAt(index);
                    setImageToLinearLayout(new Blank(b1.enpassant.getX(), b1.enpassant.getY()), ll);
                }
                b.movePiece(piece, x2, y2);
                b.board.get(b.enpassant.getX()).set(b.enpassant.getY(), new Blank(b.enpassant.getX(), b.enpassant.getY()));
                b.enpassant = new Blank(0,0);
                return 1;
            }

            if (((Pawn) piece).captureMove(x2, y2) && b.board.get(x2).get(y2) instanceof Blank ) {
                return 0;
            }

            if ( Math.abs( piece.getX() - x2 ) == 2 ) {
                System.out.println("enpassant set");
                if (b.equals(b1)) {
                    movePieceUI(piece, x2, y2);
                }
                b.movePiece(piece, x2, y2);
                b.enpassant = piece;
                return 1;
                //need the return here so if enpassant condition is not reached
            }
        } else if (piece instanceof King && !isKingInCheck){
            System.out.println("Check castling");
            if (b.isCastling((King) piece, x2, y2)) {
                System.out.println("castling");
                int diffY = piece.getY()-y2;
                ChessPieces r1 = b.board.get(piece.getX()).get(7);
                if (diffY > 0) {
                    r1 = b.board.get(piece.getX()).get(0);
                }

                if (b.equals(b1)) {
                    movePieceUI(piece, x2, y2);
                    if (r1.getY() == 0 ){
                        movePieceUI(r1, x2, y2+1);
                    } else {
                        movePieceUI(r1, x2, y2-1);
                    }
                }

                b.movePiece(piece, x2, y2);
                if (r1.getY() == 0 ){
                    b.movePiece(r1, x2, y2+1);
                } else {
                    b.movePiece(r1, x2, y2 - 1);
                }
                return 1;
            } else if (Math.abs(y2 - piece.getY()) == 2 ) {
                return 0;
            }
        }
        return 2;
    }

    public ArrayList<Integer> convertIndexToPoint(int i, int j){
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(i / 8);
        list.add(i % 8);
        list.add(j / 8);
        list.add(j % 8);
        return list;
    }

    public int convertPointToIndex(int x, int y) {
        return x*8+y;
    }

    public void movePieceUI(ChessPieces current, int x2, int y2) {
        System.out.println("moved");
        int x1 = current.getX();
        int y1 = current.getY();
        int i1 = convertPointToIndex(x1, y1);
        int i2 = convertPointToIndex(x2, y2);
        LinearLayout ll1 = (LinearLayout) board.getChildAt(i1);
        LinearLayout ll2 = (LinearLayout) board.getChildAt(i2);

        setImageToLinearLayout(current, ll2);
        setImageToLinearLayout(new Blank(x1, y1),ll1);
    }

    public void initializeBoard() {
        board.removeAllViews();
        System.out.println("initializeBoar");
        for (int i = 0; i < 8; i++){
            for (int j=0; j < 8; j++){
                LinearLayout ll = new LinearLayout(this);
                ll.setGravity(Gravity.CENTER);
                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                layoutParams.width = (int) getResources().getDimension(R.dimen.pieceSize);
                layoutParams.height = (int) getResources().getDimension(R.dimen.pieceSize);
                ll.setLayoutParams(layoutParams);

                ChessPieces c = b1.board.get(i).get(j);
                setImageToLinearLayout(c, ll);

                board.addView(ll);
            }
        }
    }

    public void setImageToLinearLayout(ChessPieces c, LinearLayout ll) {
        ll.removeAllViews();
        ImageView image = new ImageView(this);
        int imageResource = getResources().getIdentifier(c.getImg(), "drawable", getPackageName());
        Drawable draw = getResources().getDrawable(imageResource);
        image.setImageDrawable(draw);
        ll.addView(image);
    }


}
