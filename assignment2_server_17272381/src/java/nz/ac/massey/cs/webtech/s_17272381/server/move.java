/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.massey.cs.webtech.s_17272381.server;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Vincent
 */
public class move extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.net.MalformedURLException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        response.setContentType("text/html;charset=UTF-8");        
        HttpSession session = request.getSession();
        JSONObject GameState = (JSONObject) session.getAttribute("GameState");
        Game game = new Game(GameState);
        
        if (GameState == null){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }


        if (GameState != null) {
            try {
                //parse player move from URL
                String playermovePath = request.getRequestURL().toString();
                //compute possiblemoves by Game object
                String playermoves = game.possiblemoves();
                // check and throw bad request if not a valid move
                GameState.get(playermovePath.split("/ttt/move/")[1].substring(0, 4) + ""); 
                if (playermovePath.split("/ttt/move/").length > 1 ) {
                    if (playermoves.contains(playermovePath.split("/ttt/move/")[1].substring(0, 4))  ){
                        //put player's move in session
                        GameState.put(playermovePath.split("/ttt/move/")[1].substring(0, 4) + "" , "X");
                        //update game state by Game object
                        game.Won();
                        //compute and put a computer move in session
                        if (GameState.get("won") == "game is ongoing") {
                            String computerMoves = game.possiblemoves();
                            int computerMoveNumber = (int)(Math.random() * (computerMoves.split("\n").length-1) );
                            GameState.put(computerMoves.split("\n")[computerMoveNumber] , "O");
                            game.Won();

                        } 
                    }
                    
                }    
            }

            catch (JSONException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            } 
            

        }
        
                
    }
    
    
    
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
