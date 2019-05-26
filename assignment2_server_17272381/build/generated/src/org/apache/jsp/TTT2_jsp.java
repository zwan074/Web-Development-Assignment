package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

public final class TTT2_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      out.write("        <title>Tic Tac Toe Page</title>\n");
      out.write("        <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.4.0/jquery.min.js\"></script>\n");
      out.write("        <script>   \n");
      out.write("            $(document).ready(function(){\n");
      out.write("                $(\"#istart\").click(function(){\n");
      out.write("                    $.post(\"http://localhost:8080/assignment2_server_17272381/ttt/istart;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                    $.get(\"http://localhost:8080/assignment2_server_17272381/ttt/possiblemoves;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                    $.get(\"http://localhost:8080/assignment2_server_17272381/ttt/won;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                        window.location.reload(); }); \n");
      out.write("                        });\n");
      out.write("                    });\n");
      out.write("                });\n");
      out.write("                \n");
      out.write("                $(\"#ustart\").click(function(){\n");
      out.write("                    $.post(\"http://localhost:8080/assignment2_server_17272381/ttt/ustart;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                    $.get(\"http://localhost:8080/assignment2_server_17272381/ttt/possiblemoves;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                    $.get(\"http://localhost:8080/assignment2_server_17272381/ttt/won;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                        window.location.reload(); }); \n");
      out.write("                        });\n");
      out.write("                    });\n");
      out.write("                });\n");
      out.write("                \n");
      out.write("                $(\"#movex1y1\" ).click(function(){\n");
      out.write("                    $.post(\"http://localhost:8080/assignment2_server_17272381/ttt/move/x1y1;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                    $.get(\"http://localhost:8080/assignment2_server_17272381/ttt/possiblemoves;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                    $.get(\"http://localhost:8080/assignment2_server_17272381/ttt/won;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                        window.location.reload(); }); \n");
      out.write("                        });\n");
      out.write("                    });\n");
      out.write("                });\n");
      out.write("                $(\"#movex1y2\" ).click(function(){\n");
      out.write("                   $.post(\"http://localhost:8080/assignment2_server_17272381/ttt/move/x1y2;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                    $.get(\"http://localhost:8080/assignment2_server_17272381/ttt/possiblemoves;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                    $.get(\"http://localhost:8080/assignment2_server_17272381/ttt/won;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                        window.location.reload(); }); \n");
      out.write("                        });\n");
      out.write("                    });\n");
      out.write("                });\n");
      out.write("                $(\"#movex1y3\" ).click(function(){\n");
      out.write("                   $.post(\"http://localhost:8080/assignment2_server_17272381/ttt/move/x1y3;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                    $.get(\"http://localhost:8080/assignment2_server_17272381/ttt/possiblemoves;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                    $.get(\"http://localhost:8080/assignment2_server_17272381/ttt/won;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                        window.location.reload(); }); \n");
      out.write("                        });\n");
      out.write("                    });\n");
      out.write("                });\n");
      out.write("                $(\"#movex2y1\" ).click(function(){\n");
      out.write("                   $.post(\"http://localhost:8080/assignment2_server_17272381/ttt/move/x2y1;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                    $.get(\"http://localhost:8080/assignment2_server_17272381/ttt/possiblemoves;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                    $.get(\"http://localhost:8080/assignment2_server_17272381/ttt/won;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                        window.location.reload(); }); \n");
      out.write("                        });\n");
      out.write("                    });\n");
      out.write("                });\n");
      out.write("                $(\"#movex2y2\" ).click(function(){\n");
      out.write("                   $.post(\"http://localhost:8080/assignment2_server_17272381/ttt/move/x2y2;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                    $.get(\"http://localhost:8080/assignment2_server_17272381/ttt/possiblemoves;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                    $.get(\"http://localhost:8080/assignment2_server_17272381/ttt/won;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                        window.location.reload(); }); \n");
      out.write("                        });\n");
      out.write("                    });\n");
      out.write("                });\n");
      out.write("                $(\"#movex2y3\" ).click(function(){\n");
      out.write("                   $.post(\"http://localhost:8080/assignment2_server_17272381/ttt/move/x2y3;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                    $.get(\"http://localhost:8080/assignment2_server_17272381/ttt/possiblemoves;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                    $.get(\"http://localhost:8080/assignment2_server_17272381/ttt/won;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                        window.location.reload(); }); \n");
      out.write("                        });\n");
      out.write("                    });\n");
      out.write("                });\n");
      out.write("                $(\"#movex3y1\" ).click(function(){\n");
      out.write("                   $.post(\"http://localhost:8080/assignment2_server_17272381/ttt/move/x3y1;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                    $.get(\"http://localhost:8080/assignment2_server_17272381/ttt/possiblemoves;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                    $.get(\"http://localhost:8080/assignment2_server_17272381/ttt/won;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                        window.location.reload(); }); \n");
      out.write("                        });\n");
      out.write("                    });\n");
      out.write("                });\n");
      out.write("                $(\"#movex3y2\" ).click(function(){\n");
      out.write("                   $.post(\"http://localhost:8080/assignment2_server_17272381/ttt/move/x3y2;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                    $.get(\"http://localhost:8080/assignment2_server_17272381/ttt/possiblemoves;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                    $.get(\"http://localhost:8080/assignment2_server_17272381/ttt/won;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                        window.location.reload(); }); \n");
      out.write("                        });\n");
      out.write("                    });\n");
      out.write("                });\n");
      out.write("                $(\"#movex3y3\" ).click(function(){\n");
      out.write("                   $.post(\"http://localhost:8080/assignment2_server_17272381/ttt/move/x3y3;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                    $.get(\"http://localhost:8080/assignment2_server_17272381/ttt/possiblemoves;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                    $.get(\"http://localhost:8080/assignment2_server_17272381/ttt/won;jsessionid=");
      out.print(session.getId());
      out.write("\",function(){\n");
      out.write("                        window.location.reload(); }); \n");
      out.write("                        });\n");
      out.write("                    });\n");
      out.write("                });\n");
      out.write("        });\n");
      out.write("            \n");
      out.write("        </script>\n");
      out.write("\n");
      out.write("    </head>\n");
      out.write("    <body>\n");
      out.write("        <h1>Tc Tac Toe</h1>\n");
      out.write("        \n");
      out.write("        <input id = \"istart\" type=\"button\" value=\"Player Start\">\n");
      out.write("        <br>\n");
      out.write("        <input id = \"ustart\" type=\"button\" value=\"Computer Start\">\n");
      out.write("        <br>\n");
      out.write("        <p>Game Board</p>\n");
      out.write("        ");

            
            JSONObject GameState = (JSONObject) session.getAttribute("GameState");
            String txt = "";
            String won = "";
            String possiblemoves = "" ;
            txt += "<table>";
            if (GameState != null) {
                possiblemoves = (String) GameState.get("Possible Moves");
                won =  (String) GameState.get("won");
                for ( int i = 1; i < 4 ; i++ ) {
                    txt += "<tr>";
                    for ( int j = 1; j < 4 ; j++ ) {
                        txt += "<td>";
                        if ( GameState.get("won") == "game is ongoing" && possiblemoves.contains("x" + i + "y" +j)) {
                            txt += "<a href  id= \"move" + "x" + i + "y" +j + "\">" ;
                        }
                        if ( GameState.get("x" + i + "y" +j) == "_"  ){
                            txt += "<img src=\"bar.png\" width=\"50\" height=\"50\"  />";
                        } 
                        else if ( GameState.get("x" + i + "y" +j) == "X"  ){
                            txt += "<img src=\"cross.png\" width=\"50\" height=\"50\"  />";
                        }
                        else if ( GameState.get("x" + i + "y" +j) == "O"  ){
                            txt += "<img src=\"nought.png\" width=\"50\" height=\"50\"  />";
                        }
                        if ( GameState.get("won") == "game is ongoing" ) {
                            txt += "</a>";
                        }
                        txt += "</td>";
                    }
                    txt += "</tr>";
                }    
            }
            
            txt += "</table>";
            out.println(txt);
        
      out.write("\n");
      out.write("\n");
      out.write("        <p>Winner</p>\n");
      out.write("        <Strong id = \"winner\">");
      out.print(won);
      out.write("</Strong>\n");
      out.write("        \n");
      out.write("    </body>\n");
      out.write("\n");
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
