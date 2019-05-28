<%-- 
    Document   : TTT
    Created on : 16/05/2019, 6:40:03 PM
    Author     : Vincent
--%>

<%@page contentType="text/html" import = "javax.servlet.http.HttpSession,org.json.JSONObject" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Tic Tac Toe Page</title>
        <script>   
            function displayState() {
                var xhttp = new XMLHttpRequest();
                xhttp.onreadystatechange = function() {
                    if (this.readyState == 4 && this.status == 200) {
                        document.getElementById("state").innerHTML = this.responseText.toString().replace("\r\n","<br>").replace("\r\n","<br>");
                    }
                };
                xhttp.open("GET", "http://localhost:8080/assignment2_server_17272381/ttt/state;jsessionid=<%=session.getId()%>?format=txt", true);
                xhttp.send();
             }
            
            // update possiblemoves and won state before each action below
            function istart() {
                var xhttp = new XMLHttpRequest();
                xhttp.open("POST", "http://localhost:8080/assignment2_server_17272381/ttt/istart;jsessionid=<%=session.getId()%>", false );
                xhttp.send();
                xhttp = new XMLHttpRequest();
                xhttp.open("GET", "http://localhost:8080/assignment2_server_17272381/ttt/possiblemoves;jsessionid=<%=session.getId()%>", false );
                xhttp.send();
                xhttp = new XMLHttpRequest();
                xhttp.open("GET", "http://localhost:8080/assignment2_server_17272381/ttt/won;jsessionid=<%=session.getId()%>", false );
                xhttp.send();
                window.location.replace("http://localhost:8080/assignment2_server_17272381/TTT.jsp;jsessionid=<%=session.getId()%>");
              }
              
              function ustart() {
                var xhttp = new XMLHttpRequest();
                xhttp.open("POST", "http://localhost:8080/assignment2_server_17272381/ttt/ustart;jsessionid=<%=session.getId()%>", false );
                xhttp.send();
                xhttp = new XMLHttpRequest();
                xhttp.open("GET", "http://localhost:8080/assignment2_server_17272381/ttt/possiblemoves;jsessionid=<%=session.getId()%>", false );
                xhttp.send();
                xhttp = new XMLHttpRequest();
                xhttp.open("GET", "http://localhost:8080/assignment2_server_17272381/ttt/won;jsessionid=<%=session.getId()%>", false );
                xhttp.send();
                window.location.replace("http://localhost:8080/assignment2_server_17272381/TTT.jsp;jsessionid=<%=session.getId()%>");
              }
              
            function move(i,j){
                var xhttp = new XMLHttpRequest();
                xhttp.open("POST", "http://localhost:8080/assignment2_server_17272381/ttt/move/" + "x" + i + "y" + j + ";jsessionid=<%=session.getId()%>", false );
                xhttp.send();
                xhttp = new XMLHttpRequest();
                xhttp.open("GET", "http://localhost:8080/assignment2_server_17272381/ttt/possiblemoves;jsessionid=<%=session.getId()%>", false );
                xhttp.send();
                xhttp = new XMLHttpRequest();
                xhttp.open("GET", "http://localhost:8080/assignment2_server_17272381/ttt/won;jsessionid=<%=session.getId()%>", false );
                xhttp.send();
                window.location.reload();
            }  

        </script>

    </head>
    <body>
        <h1>Tc Tac Toe</h1>
        
        <input id = "istart" type="button" value="Player Start" onclick= istart() >
        <br>
        <input id = "ustart" type="button" value="Computer Start" onclick= ustart()>
        <br>
        <p>Game Board , Click img to move</p>
        <%
            
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
                            txt += "<a href  id= \"move" + "x" + i + "y" +j + "\" onclick= move(" + i + "," + j + ")>" ;
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
            
        %>
        <p>Game Board displayed by ttt/state?format=txt</p>
        <p id ="state"></p>
        <script>displayState();</script>
        <p>Winner</p>
        <Strong id = "winner"><%=won%></Strong>
        
    </body>

</html>
