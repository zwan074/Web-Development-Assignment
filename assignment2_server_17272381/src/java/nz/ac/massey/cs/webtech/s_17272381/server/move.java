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
        RequestDispatcher rd;
        
        HttpSession session = request.getSession();
        JSONObject GameState = (JSONObject) session.getAttribute("GameState");

        if (GameState == null){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }


        if (GameState != null) {
            try {
                String playermovePath = request.getRequestURL().toString();
                
                rd = request.getRequestDispatcher("/ttt/possiblemoves");
                rd.include(request, response); 
                
                String playermoves = (String) session.getAttribute("Possible Moves");
                
                GameState.get(playermovePath.split("/ttt/move/")[1].substring(0, 4) + ""); // throw bad request if not a valid move
                if (playermovePath.split("/ttt/move/").length > 1 ) {
                    if (playermoves.contains(playermovePath.split("/ttt/move/")[1].substring(0, 4))  ){
                        
                        GameState.put(playermovePath.split("/ttt/move/")[1].substring(0, 4) + "" , "X");
                        
                        rd = request.getRequestDispatcher("/ttt/won");
                        rd.include(request, response); 
                       
                        if (GameState.get("won") == "game is ongoing") {
                           
                            rd = request.getRequestDispatcher("/ttt/possiblemoves");
                            rd.include(request , response); 

                            String computerMoves = (String) session.getAttribute("Possible Moves");
                            int computerMoveNumber = (int)(Math.random() * (computerMoves.split("\n").length-1) );
                            GameState.put(computerMoves.split("\n")[computerMoveNumber] , "O");
                            
                            rd = request.getRequestDispatcher("/ttt/won");
                            rd.include(request, response); 

                        } 
                    }
                    
                    response.sendRedirect(response.encodeRedirectURL("http://localhost:8080/assignment2_server_17272381/TTT.jsp"));
                }    
            }
            catch (IOException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            } 
            catch (JSONException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            } 
            

        }
        
                
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
        //processRequest(request, response);
        
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
