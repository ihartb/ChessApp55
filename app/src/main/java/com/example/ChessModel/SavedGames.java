package com.example.ChessModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

/* just a class that holds an array of the made moves in a chess game plus a name for the file and date */
public class SavedGames {
    public ArrayList<int[]> tuXmi;
    public String nombre;
    public Date date;

    // comparators in order to be able to sort our arraylist of saved games later
    public static Comparator<SavedGames> NameComparator = new Comparator<SavedGames>() {

        public int compare(SavedGames s1, SavedGames s2) {
            String StudentName1 = s1.nombre.toUpperCase();
            String StudentName2 = s2.nombre.toUpperCase();

            //ascending order
            return StudentName1.compareTo(StudentName2);
        }
    };

    public static Comparator<SavedGames> dateComparator = new Comparator<SavedGames>() {

        public int compare(SavedGames s1, SavedGames s2) {
            Date fecha1 = s1.date;
            Date fecha2 = s2.date;

            //ascending order
            return fecha1.compareTo(fecha2);
        }
    };


}
