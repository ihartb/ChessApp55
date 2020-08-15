package com.example.chessapp55;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ChessModel.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ChessGameActivity extends AppCompatActivity {
    Button undoButton, drawButton, recordedGamesButton, resignButton, aiButton;
    GridLayout board;
    TextView whosTurnText, checkDisp;
    ImageView boardImage;
    SavedGames meep = new SavedGames();
    ArrayList<SavedGames>  allgames;
    ChessBoard b1 = new ChessBoard();
    boolean isKingInCheck = false;
    ChessPieces wk = b1.board.get(7).get(4);
    ChessPieces bk = b1.board.get(0).get(4);
    ArrayList<int[]> listOfMoves = new ArrayList<int[]>();
    //      0       1
    // 0    start   end
    // 1    start   end
    // 2    start   end
    LinearLayout firstClick = null;
    LinearLayout secondClick = null;
    PreviousState prev = null;

    //partially implement, save the game inside onClick and unload user at beginning
    public void gameEnd(String title){
        b1.running = false;


        final EditText input = new EditText(this);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(input)
                .setTitle(title)
                .setMessage("Would you like to save your game? If so please name your game.")
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        //serialize user data
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            //serialize user data
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        String gameName = input.getText().toString();
                        if (gameName.isEmpty()) {
                            System.out.println("empty");
                            Toast t =  Toast.makeText(getApplicationContext(), "Game name cannot be empty.", Toast.LENGTH_SHORT);
                            t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0,0);
                            t.show();
                        } else {
                            //serialize u data...
                            dialog.dismiss();
                            meep.nombre = gameName;
                            meep.date = Calendar.getInstance().getTime();
                            meep.tuXmi = listOfMoves;
                            allgames.add(meep);
                            saveData();
                            recreate();
                        }
                    }
                });

                Button button1 = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                button1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        recreate();
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    //implement, goes to listofgames activity
    public void recordedGames(){
        b1.running = false;

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Leave game")
                .setMessage("Would you like to leave your game?")
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        //serialize user data
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            //serialize user data
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                            Intent menuIntent = new Intent(getApplicationContext(), ListOfGamesActivity.class);
                            startActivity(menuIntent);
                    }
                });

                Button button1 = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                button1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }


    public ChessGameActivity(boolean i) {
        this.isKingInCheck = i;
    }

    public ChessGameActivity(){
    }

    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(allgames);
        editor.putString("loadgames",json);
        editor.apply();
    }
//loads up the previous saved games is used because if we didnt load previous games then we wouldnt
    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("loadgames",null);
        Type type = new TypeToken<ArrayList<SavedGames>>(){}.getType();
        allgames = gson.fromJson(json,type);
        if(allgames == null){
            allgames = new ArrayList<>();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chess_game);

        undoButton = findViewById(R.id.undo_button);
        drawButton = findViewById(R.id.draw_button);
        recordedGamesButton = findViewById(R.id.recorded_games_button);
        resignButton = findViewById(R.id.resign_button);
        aiButton = findViewById(R.id.ai_button);
        board = findViewById(R.id.board);
        whosTurnText = findViewById(R.id.whos_turn_text);
        checkDisp = findViewById(R.id.checkDisp);
        boardImage = findViewById(R.id.board_image);

        undoButton.setOnClickListener(v -> undo(v));
        drawButton.setOnClickListener(v -> draw());
        recordedGamesButton.setOnClickListener(v -> recordedGames());
        resignButton.setOnClickListener(v -> resign());
        aiButton.setOnClickListener(v -> ai());

        initializeBoard();
        loadData();
        gamePreqs();
    }

    public void undo(View v){
        if (prev == null) {
            return;
        }
        System.out.println("undo, prev board:");
        prev.b1.dispBoard();
        b1 = prev.b1;
        isKingInCheck = prev.isKingInCheck;
        wk = prev.wk;
        bk = prev.bk;
        prev = null;
        firstClick = null;
        secondClick = null;
        listOfMoves.remove(listOfMoves.size()-1);
        initializeBoard();
        gamePreqs();

    }

    public void draw(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        gameEnd("Game ended in a draw.");
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String turn = "WHITE: ";
        if (b1.whosmove) { turn = "BLACK: "; }
        builder.setMessage(turn+"Would you like to confirm your opponent's call for a draw?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void resign(){
        String s = "Black Wins!";
        if(!b1.whosmove) {
            s = "White Wins!";
        }

        Context context = getApplicationContext();
        Toast.makeText(context, s,
                Toast.LENGTH_SHORT).show();
        gameEnd(s);
    }

    public void ai(){
        prev = new PreviousState(b1, isKingInCheck, wk, bk);
        ChessPieces c;
        int i1 = 0;
        int i2 = 0;
        int count = 0;
        boolean continueLoop = true;
        do {
            do {
                ArrayList<Integer> x = convertIndexToPoint(new Random().nextInt(64), 0);
                c = b1.board.get(x.get(0)).get(x.get(1));
            } while (c instanceof Blank || b1.whosmove != c.getColor());

            ArrayList<ArrayList<Integer>> moves = c.getMoves();

            ArrayList<Integer> indices = new ArrayList<Integer>();
            for (int i=0; i < moves.get(0).size(); i++) {
                indices.add(i);
            }
            Collections.shuffle(indices);

            for (int i=0 ; i < indices.size(); i++){
                i1 = convertPointToIndex(c.getX(), c.getY());
                int j = indices.get(i);
                i2 = convertPointToIndex( moves.get(0).get(j), moves.get(1).get(j) );
                if (parseInputToCommand(i1, i2, 0)){
                    System.out.println("broken");
                    continueLoop = false;
                    break;
                }
            }
            count++;
            System.out.println(count);
        } while (continueLoop);

        b1.whosmove=!b1.whosmove;
        int[] startEndPromo = {i1, i2, 0};
        listOfMoves.add(startEndPromo);
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
    //tapping
    private final class MyClickListener implements View.OnClickListener {
        Drawable selectedBox = getResources().getDrawable(R.drawable.border);

        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                v.setBackground(selectedBox);
            }

            if (firstClick == null) {
                firstClick = (LinearLayout) v;
                return;
            }

            LinearLayout ll1 = firstClick;
            int i1 = board.indexOfChild(ll1);

            LinearLayout ll2 = (LinearLayout) v;
            int i2 = board.indexOfChild(ll2);

            if (i2 == i1) {
                firstClick = null;
                v.setBackgroundResource(0);
                return;
            }

            prev = new PreviousState(b1, isKingInCheck, wk, bk);

            ArrayList<Integer> meep = convertIndexToPoint(i1, i2);
            ChessPieces piece = b1.board.get(meep.get(0)).get(meep.get(1));
            if (b1.isPromotion(piece, meep.get(2))) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChessGameActivity.this);
                builder.setTitle("Promotion");
                String[] types = {"Queen", "Bishop", "Knight", "Rook"};
                builder.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int promo) {
                        int i = 0;
                        switch(promo){
                            case 0:
                                //queen
                                break;
                            case 1:
                                //bishop
                                i = 1;
                                break;
                            case 2:
                                //knight
                                i = 2;
                                break;
                            case 3:
                                //rook
                                i = 3;
                                break;
                        }
                        processClicks(i1, i2, i);
                    }
                });
                builder.create().setCanceledOnTouchOutside(false);
                builder.show();
            } else {
                processClicks(i1, i2, 0);
            }
        }
    }

    public void processClicks(int i1, int i2, int i) {
        boolean validmove = parseInputToCommand(i1, i2, i);
        if (!validmove) {
            Context context = getApplicationContext();
            Toast.makeText(context, "Illegal move, try again",
                    Toast.LENGTH_SHORT).show();
        } else {
            b1.whosmove = !b1.whosmove;
            int[] startEndPromo = {i1, i2, i};
            listOfMoves.add(startEndPromo);
            gamePreqs();
        }

        ( (LinearLayout) board.getChildAt(i2) ).setBackgroundResource(0);
        ( (LinearLayout) board.getChildAt(i1) ).setBackgroundResource(0);
        firstClick = null;
        secondClick = null;

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

                //dragging functionality:
//              ll.setOnDragListener(new MyDragListener());
//              image.setOnTouchListener(new MyTouchListener());

                //tapping functionality:
                ll.setOnClickListener(new MyClickListener());

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


//    //dragging
//    private final class MyTouchListener implements View.OnTouchListener {
//
//        public boolean onTouch(View view, MotionEvent event) {
//            ClipData data = ClipData.newPlainText("", "");
//            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(
//                    view);
//            // Starts the drag
//            view.startDrag(data,  // the data to be dragged
//                    myShadow,  // the drag shadow builder
//                    view,      // no need to use local data
//                    0          // flags (not currently used, set to 0)
//            );
//
//            view.setVisibility(View.INVISIBLE);
//            return true;
//        }
//    }
//    class MyDragListener implements View.OnDragListener {
//        Drawable enteringBox = getResources().getDrawable(R.drawable.border);
//
//        @Override
//        public boolean onDrag(View v, DragEvent event) {
//            switch (event.getAction()) {
//                case DragEvent.ACTION_DRAG_STARTED:
//                    // do nothing
//                    break;
//                case DragEvent.ACTION_DRAG_ENTERED:
////                     Change background of the layout where item is entering
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                        v.setBackground(enteringBox);
//                    }
//                    break;
//                case DragEvent.ACTION_DRAG_EXITED:
////                    Change background of the layout back to normal once item is moved out of it
//                    v.setBackgroundResource(0);
//                    break;
//                case DragEvent.ACTION_DROP:
//                    //start
//                    View view1 = (View) event.getLocalState();
//                    ImageView img1 = (ImageView) view1;
//                    LinearLayout ll1 = (LinearLayout) img1.getParent();
//                    int i1 = board.indexOfChild(ll1);
//
//                    //end
//                    LinearLayout ll2 = (LinearLayout) v;
//                    ImageView img2 = (ImageView) ll2.getChildAt(0);
//                    int i2 = board.indexOfChild(ll2);
//
//                    //if you can actually move it...
//                        ll1.removeAllViews();
//                        ll2.removeAllViews();
//
//                        ll1.addView(img2);
//                        if (i1 != i2) {
//                            ll2.addView(img1);
//                        }
//                    // else
////                        View view = (View) event.getLocalState();
////                        view.setVisibility(View.VISIBLE);
////                        Context context = getApplicationContext();
////                        Toast.makeText(context, "You can't move piece here",
////                                Toast.LENGTH_LONG).show();
////                        break;
//                    view1.setVisibility(View.VISIBLE);
//                    break;
//                case DragEvent.ACTION_DRAG_ENDED:
//                    View currentView = (View) event.getLocalState();
//                    currentView.setVisibility(View.VISIBLE);
//                    v.setBackgroundResource(0);
//                default:
//                    break;
//            }
//            return true;
//        }
//    }

}

