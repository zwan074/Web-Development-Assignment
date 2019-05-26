/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.massey.cs.webtech.s_17272381.server;

import java.io.IOException;
import java.io.PrintWriter;
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
public class state extends HttpServlet {

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
            
            //compute the game state in text
            if (GameState==null) {
 
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                
            }
            else {
                if ("txt".equals(request.getParameter("format"))) {
                    
                    out.println ( GameState.get( "x1y1").toString() +  GameState.get( "x1y2").toString() + GameState.get( "x1y3").toString() );
                    out.println ( GameState.get( "x2y1").toString() +  GameState.get( "x2y2").toString() + GameState.get( "x2y3").toString() );
                    out.println ( GameState.get( "x3y1").toString() +  GameState.get( "x3y2").toString() + GameState.get( "x3y3").toString() );
                } 
                
            }
            
        }
        catch (NullPointerException ex ) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } 
        catch (IOException | JSONException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
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
