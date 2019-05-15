/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.massey.cs.webtech.s_17272381.server;

import java.io.IOException;
import java.io.PrintWriter;
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
public class won extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/plain");
            PrintWriter out = response.getWriter(); 
            HttpSession session = request.getSession();
            JSONObject GameState = (JSONObject) session.getAttribute("GameState");    
            if (GameState==null) {

                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                
            }
            
            else {
                
                RequestDispatcher rd = request.getRequestDispatcher("/ttt/possiblemoves");
                rd.include(request, response); 
                
                String possibleMoves = (String) session.getAttribute("Possible Moves");
                

                if ( threeInRow (GameState,"X") || threeInCol (GameState,"X") || threeInDia (GameState,"X")) {
                    GameState.put("won", "user");
                }
                else if ( threeInRow (GameState,"O") || threeInCol (GameState,"O") || threeInDia (GameState,"O")) {
                    GameState.put("won", "computer");
                }
                
                else if (possibleMoves.length() > 0 ) {
                    GameState.put("won", "game is ongoing");
                    
                } 
                else {
                    GameState.put("won", "draw");
                } 
                
                out.print(GameState.get("won"));
                
            }
            
        }
        catch (IOException | JSONException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } 
        
        
    }
    protected boolean threeInRow (  JSONObject GameState , String Player   ) {
        boolean threeInRowFirstRow =  GameState.get("x1y1") == Player && GameState.get("x1y2") == Player && GameState.get("x1y3") == Player;
        boolean threeInRowSecondRow =  GameState.get("x2y1") == Player && GameState.get("x2y2") == Player && GameState.get("x2y3") == Player;
        boolean threeInRowThirdRow =  GameState.get("x3y1") == Player && GameState.get("x3y2") == Player && GameState.get("x3y3") == Player;
        return (threeInRowFirstRow || threeInRowSecondRow || threeInRowThirdRow);
    }
    protected boolean threeInCol (  JSONObject GameState , String Player   ) {
        boolean threeInColFirstCol =  GameState.get("x1y1") == Player && GameState.get("x2y1") == Player && GameState.get("x3y1") == Player;
        boolean threeInColSecondCol =  GameState.get("x1y2") == Player && GameState.get("x2y2") == Player && GameState.get("x3y2") == Player;
        boolean threeInColThirdCol =  GameState.get("x1y3") == Player && GameState.get("x2y3") == Player && GameState.get("x3y3") == Player;
        return (threeInColFirstCol || threeInColSecondCol || threeInColThirdCol);
    }
    
    protected boolean threeInDia (  JSONObject GameState , String Player   ) {
        boolean threeInDiaFirstDia =  GameState.get("x1y1") == Player && GameState.get("x2y2") == Player && GameState.get("x3y3") == Player;
        boolean threeInDiaSecondDia =  GameState.get("x3y1") == Player && GameState.get("x2y2") == Player && GameState.get("x1y3") == Player;
        return (threeInDiaFirstDia || threeInDiaSecondDia );
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

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
