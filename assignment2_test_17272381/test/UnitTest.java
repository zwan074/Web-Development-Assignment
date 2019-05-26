/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Vincent
 */
public class UnitTest {
    //initialise variables
    private static final String SERVER_URL = "http://localhost:8080/assignment2_server_17272381";
    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpGet httpget;
    HttpPost httppost;
    HttpResponse response;
    String state;
    
    public UnitTest() {
        
        
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        httpclient = HttpClients.createDefault();
    }
    
    @After
    public void tearDown() {
        httpclient = null;
    }
    
    @Test
    public void test_istart() throws IOException {
        
        httppost = new HttpPost(SERVER_URL + "/ttt/istart");
        httpclient.execute(httppost);
        httpget = new HttpGet(SERVER_URL + "/ttt/state?format=txt");
        response = httpclient.execute(httpget);
        state = EntityUtils.toString(response.getEntity());
       
        assertEquals ( "___\r\n___\r\n___\r\n" , state  );
       
    }  
    
    @Test
    public void test_ustart() throws IOException {
        
        httppost = new HttpPost(SERVER_URL + "/ttt/ustart");
        httpclient.execute(httppost);
        httpget = new HttpGet(SERVER_URL + "/ttt/state?format=txt");
        response = httpclient.execute(httpget);
        state = EntityUtils.toString(response.getEntity());
        
        int numberOfComputerMoves = state.length() - state.replace("O","").length();
        
        //there is 1 x "O" for computer move in game board.
        assertEquals ( numberOfComputerMoves, 1 );
        assertTrue ( state.contains("O")  );
       
    } 
    
    @Test
    public void test_state() throws IOException  {
        
        //test 404 status without start a new game
        httpget = new HttpGet(SERVER_URL + "/ttt/state?format=txt");
        response = httpclient.execute(httpget);
        state = EntityUtils.toString(response.getEntity());
        assertTrue (state.contains("HTTP Status 404 ? Not Found") ) ; 
        
        //test player start and content type.
        httppost = new HttpPost(SERVER_URL + "/ttt/istart");
        httpclient.execute(httppost);
        httpget = new HttpGet(SERVER_URL + "/ttt/state?format=txt");
        response = httpclient.execute(httpget);
        state = EntityUtils.toString(response.getEntity());
        Header[] contentType = response.getHeaders("Content-Type");
        assertEquals ( "___\r\n___\r\n___\r\n" , state  );
        assertEquals ( "Content-Type: text/plain;charset=ISO-8859-1" , contentType[0] + ""  );
        
    }
    
    
    @Test
    public void test_move() throws IOException {
        
        //test HTTP 404 status if not start a new game.
        httppost = new HttpPost(SERVER_URL + "/ttt/move/x1y1");
        response = httpclient.execute(httppost);
        state = EntityUtils.toString(response.getEntity());
        assertTrue (state.contains("HTTP Status 404 – Not Found") ) ;
        
        //test a player move at x1, y1.
        httppost = new HttpPost(SERVER_URL + "/ttt/istart");
        httpclient.execute(httppost);
        httppost = new HttpPost(SERVER_URL + "/ttt/move/x1y1");
        httpclient.execute(httppost);
        httpget = new HttpGet(SERVER_URL + "/ttt/state?format=txt");
        response = httpclient.execute(httpget);
        state = EntityUtils.toString(response.getEntity());
        
        // number of "X" and "O" should be equal.
        int numberOfComputerMoves = state.length() - state.replace("O","").length();
        int numberOfPlayerMoves = state.length() - state.replace("X","").length();
        assertEquals ( numberOfComputerMoves, 1 );
        assertEquals ( numberOfPlayerMoves, 1 );
        assertTrue ( state.contains("O")  );
        assertTrue ( state.contains("X")  );
        
        //test HTTP 400 status for invalid move
        httppost = new HttpPost(SERVER_URL + "/ttt/istart");
        httpclient.execute(httppost);
        httppost = new HttpPost(SERVER_URL + "/ttt/move/x1y4");
        response = httpclient.execute(httppost);
        state = EntityUtils.toString(response.getEntity());
        assertTrue (state.contains("HTTP Status 400 – Bad Request") ) ;
        
        //test HTTP 400 status for malformed URL
        httppost = new HttpPost(SERVER_URL + "/ttt/istart");
        httpclient.execute(httppost);
        httppost = new HttpPost(SERVER_URL + "/ttt/move/x1y4xxxxxxxxxxx");
        response = httpclient.execute(httppost);
        state = EntityUtils.toString(response.getEntity());
        assertTrue (state.contains("HTTP Status 400 – Bad Request") ) ;
        
        
    }
    @Test
    public void test_won() throws IOException {
        
        //test HTTP 404 status if not start a new game.
        httpget = new HttpGet(SERVER_URL + "/ttt/won");
        response = httpclient.execute(httpget);
        state = EntityUtils.toString(response.getEntity());
        assertTrue (state.contains("HTTP Status 404 ? Not Found") ) ; 
        
        //test game state if player move first
        httppost = new HttpPost(SERVER_URL + "/ttt/istart");
        httpclient.execute(httppost);
        httppost = new HttpPost(SERVER_URL + "/ttt/move/x1y1");
        httpclient.execute(httppost);
        httpget = new HttpGet(SERVER_URL + "/ttt/won");
        response = httpclient.execute(httpget);
        state = EntityUtils.toString(response.getEntity());
        assertTrue ( state.contains("game is ongoing"));
        
        //test a number of moves when player start 
        httppost = new HttpPost(SERVER_URL + "/ttt/istart");
        httpclient.execute(httppost);
        for (int i = 0 ; i < 5 ; i++) { //noramlly maximum 5 steps for this game
            httpget = new HttpGet(SERVER_URL + "/ttt/state?format=txt");
            response = httpclient.execute(httpget);
            state = EntityUtils.toString(response.getEntity());
            String state_to_possiblemoves = state_to_possiblemoves(state);
            httppost = new HttpPost(SERVER_URL + "/ttt/move/" + state_to_possiblemoves.split("\n")[0] );
            httpclient.execute(httppost);
            httpget = new HttpGet(SERVER_URL + "/ttt/won");
            response = httpclient.execute(httpget);
            state = EntityUtils.toString(response.getEntity());
            assertTrue ( state.contains("game is ongoing") || state.contains("user")|| state.contains("computer")|| state.contains("draw"));
        }
        
        //test a number of moves when computer start 
        httppost = new HttpPost(SERVER_URL + "/ttt/ustart");
        httpclient.execute(httppost);
        for (int i = 0 ; i < 5 ; i++) { //noramlly maximum 5 steps for this game
            httpget = new HttpGet(SERVER_URL + "/ttt/state?format=txt");
            response = httpclient.execute(httpget);
            state = EntityUtils.toString(response.getEntity());
            String state_to_possiblemoves = state_to_possiblemoves(state);
            httppost = new HttpPost(SERVER_URL + "/ttt/move/" + state_to_possiblemoves.split("\n")[0] );
            httpclient.execute(httppost);
            httpget = new HttpGet(SERVER_URL + "/ttt/won");
            response = httpclient.execute(httpget);
            state = EntityUtils.toString(response.getEntity());
            assertTrue ( state.contains("game is ongoing") || state.contains("user")|| state.contains("computer")|| state.contains("draw"));
        }
        
        
        
    }
    
    @Test
    public void test_possiblemoves() throws IOException {
        
        //test HTTP 404 status if not start a new game.
        httpget = new HttpGet(SERVER_URL + "/ttt/possiblemoves");
        response = httpclient.execute(httpget);
        state = EntityUtils.toString(response.getEntity());
        assertTrue (state.contains("HTTP Status 404 ? Not Found") ) ; 

        //test player's move ny one step 
        httppost = new HttpPost(SERVER_URL + "/ttt/istart");
        httpclient.execute(httppost);
        httppost = new HttpPost(SERVER_URL + "/ttt/move/x1y1");
        httpclient.execute(httppost);
        httpget = new HttpGet(SERVER_URL + "/ttt/state?format=txt");
        response = httpclient.execute(httpget);
        state = EntityUtils.toString(response.getEntity());
        httpget = new HttpGet(SERVER_URL + "/ttt/possiblemoves");
        response = httpclient.execute(httpget);
        String possiblemoves = EntityUtils.toString(response.getEntity());
        String state_to_possiblemoves = state_to_possiblemoves(state);
        assertEquals ( state_to_possiblemoves , possiblemoves );
       
       //test a number of states started by player
        httppost = new HttpPost(SERVER_URL + "/ttt/istart");
        httpclient.execute(httppost);
        for (int i = 0 ; i < 5 ; i++) {
            httpget = new HttpGet(SERVER_URL + "/ttt/state?format=txt");
            response = httpclient.execute(httpget);
            state = EntityUtils.toString(response.getEntity());
            state_to_possiblemoves = state_to_possiblemoves(state);
            httppost = new HttpPost(SERVER_URL + "/ttt/move/" + state_to_possiblemoves.split("\n")[0] );
            httpclient.execute(httppost);

            httpget = new HttpGet(SERVER_URL + "/ttt/possiblemoves");
            response = httpclient.execute(httpget);
            possiblemoves = EntityUtils.toString(response.getEntity());
            
            httpget = new HttpGet(SERVER_URL + "/ttt/state?format=txt");
            response = httpclient.execute(httpget);
            state = EntityUtils.toString(response.getEntity());
            state_to_possiblemoves = state_to_possiblemoves(state);
            
            assertEquals ( state_to_possiblemoves , possiblemoves );
        }
        
        //test a number of states started by computer
        httppost = new HttpPost(SERVER_URL + "/ttt/ustart");
        httpclient.execute(httppost);
        for (int i = 0 ; i < 5 ; i++) {
            httpget = new HttpGet(SERVER_URL + "/ttt/state?format=txt");
            response = httpclient.execute(httpget);
            state = EntityUtils.toString(response.getEntity());
            state_to_possiblemoves = state_to_possiblemoves(state);
            httppost = new HttpPost(SERVER_URL + "/ttt/move/" + state_to_possiblemoves.split("\n")[0] );
            httpclient.execute(httppost);
            
            httpget = new HttpGet(SERVER_URL + "/ttt/possiblemoves");
            response = httpclient.execute(httpget);
            possiblemoves = EntityUtils.toString(response.getEntity());
            
            httpget = new HttpGet(SERVER_URL + "/ttt/state?format=txt");
            response = httpclient.execute(httpget);
            state = EntityUtils.toString(response.getEntity());
            state_to_possiblemoves = state_to_possiblemoves(state);
            
            assertEquals ( state_to_possiblemoves , possiblemoves );
        }
        
    }
    
    //transfer state to possiblemoves servlet format
    private String state_to_possiblemoves (String state) {
       String state_to_possiblemoves = "";
       for ( int i = 0; i < 3 ; i++ ) {
            for ( int j = 0; j < 3 ; j++ ) {
                if ( "_".equals(state.split("\r\n")[i].substring(j, j+1))  ){
                    state_to_possiblemoves += "x" + (i+1) + "y" + (j+1) + "\n";
                } 
            }
        }
       return  state_to_possiblemoves;
    }
}
