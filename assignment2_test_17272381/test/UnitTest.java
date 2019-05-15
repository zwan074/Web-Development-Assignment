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
        
        assertEquals ( numberOfComputerMoves, 1 );
        assertTrue ( state.contains("O")  );
       
    } 
    
    @Test
    public void test_state() throws IOException  {
        
        httpget = new HttpGet(SERVER_URL + "/ttt/state?format=txt");
        response = httpclient.execute(httpget);
        state = EntityUtils.toString(response.getEntity());
        
        assertTrue (state.contains("HTTP Status 404 ? Not Found") ) ; 
        
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
        httppost = new HttpPost(SERVER_URL + "/ttt/move/x1y1");
        response = httpclient.execute(httppost);
        state = EntityUtils.toString(response.getEntity());
        
        assertTrue (state.contains("HTTP Status 404 – Not Found") ) ;
        
        httppost = new HttpPost(SERVER_URL + "/ttt/istart");
        httpclient.execute(httppost);
        httppost = new HttpPost(SERVER_URL + "/ttt/move/x1y1");
        httpclient.execute(httppost);
        httpget = new HttpGet(SERVER_URL + "/ttt/state?format=txt");
        response = httpclient.execute(httpget);
        state = EntityUtils.toString(response.getEntity());
        
        int numberOfComputerMoves = state.length() - state.replace("O","").length();
        int numberOfPlayerMoves = state.length() - state.replace("X","").length();
        assertEquals ( numberOfComputerMoves, 1 );
        assertEquals ( numberOfPlayerMoves, 1 );
        assertTrue ( state.contains("O")  );
        assertTrue ( state.contains("X")  );
        
        
        httppost = new HttpPost(SERVER_URL + "/ttt/istart");
        httpclient.execute(httppost);
        httppost = new HttpPost(SERVER_URL + "/ttt/move/x1y4");
        response = httpclient.execute(httppost);
        state = EntityUtils.toString(response.getEntity());
        assertTrue (state.contains("HTTP Status 400 – Bad Request") ) ;
        
        httppost = new HttpPost(SERVER_URL + "/ttt/istart");
        httpclient.execute(httppost);
        httppost = new HttpPost(SERVER_URL + "/ttt/move/x1y4xxxxxxxxxxx");
        response = httpclient.execute(httppost);
        state = EntityUtils.toString(response.getEntity());
        assertTrue (state.contains("HTTP Status 400 – Bad Request") ) ;
        
        
    }
    @Test
    public void test_won() throws IOException {
        httpget = new HttpGet(SERVER_URL + "/ttt/won");
        response = httpclient.execute(httpget);
        state = EntityUtils.toString(response.getEntity());
        
        assertTrue (state.contains("HTTP Status 404 ? Not Found") ) ; 
        
        httppost = new HttpPost(SERVER_URL + "/ttt/istart");
        httpclient.execute(httppost);
        httppost = new HttpPost(SERVER_URL + "/ttt/move/x1y1");
        httpclient.execute(httppost);
        httpget = new HttpGet(SERVER_URL + "/ttt/won");
        response = httpclient.execute(httpget);
        state = EntityUtils.toString(response.getEntity());

        assertTrue ( state.contains("game is ongoing"));
        
    }
    
    @Test
    public void test_possiblemoves() throws IOException {
        
        httpget = new HttpGet(SERVER_URL + "/ttt/possiblemoves");
        response = httpclient.execute(httpget);
        state = EntityUtils.toString(response.getEntity());
        
        assertTrue (state.contains("HTTP Status 404 ? Not Found") ) ; 

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
       String state_to_possiblemoves = "";
       for ( int i = 0; i < 3 ; i++ ) {
            for ( int j = 0; j < 3 ; j++ ) {
                if ( "_".equals(state.split("\r\n")[i].substring(j, j+1))  ){
                    state_to_possiblemoves += "x" + (i+1) + "y" + (j+1) + "\n";
                } 
            }
        }

       assertEquals ( state_to_possiblemoves , possiblemoves );
       
       
    }
}
