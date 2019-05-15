<%-- 
    Document   : assignment2_server_17272381
    Created on : 12/05/2019, 4:56:29 PM
    Author     : Vincent
--%>

<%@page contentType="text/html" import = "javax.servlet.http.HttpSession,org.json.JSONObject" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Tc Tac Toe Page</title>
    </head>
    <body>
        
        
        <h1>Tc Tac Toe</h1>

        <form method = post action= "ttt/istart" >
            <input type="submit" value="Player Start">
        </form> 
        <form method = post action="ttt/ustart">
            <input type="submit" value="Computer Start">
        </form>
        <br>
        <p>Game Board</p>
        <script>
        <%
            
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
            
        %>
        </script>
        
        
        <section id ="state"></section>
        <p>Winner</p>
        <Strong><%= won%></Strong>
        <script>
            document.getElementById("state").innerHTML = <%=txt%>
        </script>
    </body>
</html>
