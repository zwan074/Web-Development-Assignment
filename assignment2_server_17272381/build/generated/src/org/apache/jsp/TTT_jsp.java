package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

public final class TTT_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html>\n");
      out.write("    <head>\n");
      out.write("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
      out.write("        <title>Tc Tac Toe Page</title>\n");
      out.write("    </head>\n");
      out.write("    <body>\n");
      out.write("        \n");
      out.write("        \n");
      out.write("        <h1>Tc Tac Toe</h1>\n");
      out.write("\n");
      out.write("        <form method = post action= \"ttt/istart\" >\n");
      out.write("            <input type=\"submit\" value=\"Player Start\">\n");
      out.write("        </form> \n");
      out.write("        <form method = post action=\"ttt/ustart\">\n");
      out.write("            <input type=\"submit\" value=\"Computer Start\">\n");
      out.write("        </form>\n");
      out.write("        <br>\n");
      out.write("        <p>Game Board</p>\n");
      out.write("        ");

            
            JSONObject GameState = (JSONObject) session.getAttribute("GameState");
            String txt = "";
            String won = "";
            txt += "\"<table>";
            if (GameState != null) {
                won =  (String) GameState.get("won");
                for ( int i = 1; i < 4 ; i++ ) {
                    txt += "<tr>";
                    for ( int j = 1; j < 4 ; j++ ) {
                        txt += "<td>";
                        if ( GameState.get("won") == "game is ongoing" ) {
                            txt += "<form id= \\\"move" + "x" + i + "y" +j + "\\\"action=\\\"ttt/move/" + "x" + i + "y" +j + ";jsessionid=" + session.getId() + "\\\" method= post>" ;
                            txt += "<a href=\\\"javascript:;\\\" onclick=\\\"document.getElementById('move" + "x" + i + "y" + j +  "').submit();\\\">" ;
                        }
                        if ( GameState.get("x" + i + "y" +j) == "_"  ){
                            txt += "<img src=\\\"bar.png\\\" width=\\\"50\\\" height=\\\"50\\\"  />";
                        } 
                        else if ( GameState.get("x" + i + "y" +j) == "X"  ){
                            txt += "<img src=\\\"cross.png\\\" width=\\\"50\\\" height=\\\"50\\\"  />";
                        }
                        else if ( GameState.get("x" + i + "y" +j) == "O"  ){
                            txt += "<img src=\\\"nought.png\\\" width=\\\"50\\\" height=\\\"50\\\"  />";
                        }
                        txt += "</a>";
                        txt += "</form>";
                        txt += "</td>";
                    }
                    txt += "</tr>";
                }    
            }
            
            txt += "</table>\"";
            
        
      out.write("\n");
      out.write("        \n");
      out.write("        \n");
      out.write("        \n");
      out.write("        <section id =\"state\"></section>\n");
      out.write("        <p>Winner</p>\n");
      out.write("        <Strong>");
      out.print( won);
      out.write("</Strong>\n");
      out.write("        <script>\n");
      out.write("            document.getElementById(\"state\").innerHTML = ");
      out.print(txt);
      out.write("\n");
      out.write("        </script>\n");
      out.write("    </body>\n");
      out.write("</html>\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
