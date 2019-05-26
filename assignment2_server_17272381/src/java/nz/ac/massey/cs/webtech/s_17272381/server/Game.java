/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.massey.cs.webtech.s_17272381.server;

import org.json.JSONObject;

/**
 *
 * @author Vincent
 */
public class Game {
    JSONObject GameState;
    
    public Game (JSONObject GameState) {
        this.GameState = GameState;
        
    }
    
    //compute all possible moves in string
    public String possiblemoves () {
        String possibleMoves = "";
        for ( int i = 1; i < 4 ; i++ ) {
            for ( int j = 1; j < 4 ; j++ ) {
                if ( GameState.get("x" + i + "y" +j) == "_"   ){
                    possibleMoves += "x" + i + "y" + j + "\n";
                } 
            }
        }
        return possibleMoves;
        
    }
    //compute the game status 
    public void Won () {
        
        if ( threeInRow ("X") || threeInCol ("X") || threeInDia ("X")) {
            GameState.put("won", "user");
        }
        else if ( threeInRow ("O") || threeInCol ("O") || threeInDia ("O")) {
            GameState.put("won", "computer");
        }

        else if (possiblemoves().length() > 0 ) {
            GameState.put("won", "game is ongoing");

        } 
        else {
            GameState.put("won", "draw");
        }
        
        
    }
    
    //emmurate all possible winning state 
    private boolean threeInRow (  String Player   ) {
        boolean threeInRowFirstRow =  GameState.get("x1y1") == Player && GameState.get("x1y2") == Player && GameState.get("x1y3") == Player;
        boolean threeInRowSecondRow =  GameState.get("x2y1") == Player && GameState.get("x2y2") == Player && GameState.get("x2y3") == Player;
        boolean threeInRowThirdRow =  GameState.get("x3y1") == Player && GameState.get("x3y2") == Player && GameState.get("x3y3") == Player;
        return (threeInRowFirstRow || threeInRowSecondRow || threeInRowThirdRow);
    }
    private boolean threeInCol (  String Player   ) {
        boolean threeInColFirstCol =  GameState.get("x1y1") == Player && GameState.get("x2y1") == Player && GameState.get("x3y1") == Player;
        boolean threeInColSecondCol =  GameState.get("x1y2") == Player && GameState.get("x2y2") == Player && GameState.get("x3y2") == Player;
        boolean threeInColThirdCol =  GameState.get("x1y3") == Player && GameState.get("x2y3") == Player && GameState.get("x3y3") == Player;
        return (threeInColFirstCol || threeInColSecondCol || threeInColThirdCol);
    }
    
    private boolean threeInDia (  String Player   ) {
        boolean threeInDiaFirstDia =  GameState.get("x1y1") == Player && GameState.get("x2y2") == Player && GameState.get("x3y3") == Player;
        boolean threeInDiaSecondDia =  GameState.get("x3y1") == Player && GameState.get("x2y2") == Player && GameState.get("x1y3") == Player;
        return (threeInDiaFirstDia || threeInDiaSecondDia );
    }
}
