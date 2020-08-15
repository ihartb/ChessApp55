/**
 * Legal move interface used to check if the current piece we are working with can move to the destination
 *
 *
 * @author  Bharti mehta bam270 Jeancarlo Bolanos jb1618
 */
package com.example.ChessModel;



public interface LegalMove {

    //only check if the current position of piece to next position is possible,
    //doesn't look for interfering pieces or captures etc
    //x and y coordinates depend on array list indexing, not how chessboard displayed
    public boolean legalMove(int x2, int y2);
}
