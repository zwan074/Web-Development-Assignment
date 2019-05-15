/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2_test_17272381;

import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Vincent
 */
public class test  {

    private static final String SERVER_URL = "http://localhost:8080/assignment2_server_17272381";
    private static CloseableHttpClient httpclient = HttpClients.createDefault();
    private static HttpGet httpget;
    private static HttpPost httppost;
    private static HttpResponse response;
    private static String responseString;
    
    
    public static void main(String[] args) throws IOException {
        httppost = new HttpPost(SERVER_URL + "/ttt/istart");
        httpclient.execute(httppost);
        httppost = new HttpPost(SERVER_URL + "/ttt/move/x1y2");
       // httpclient.execute(httppost);
        //httpget = new HttpGet(SERVER_URL + "/ttt/state?format=txt");
        response = httpclient.execute(httppost);
        String state = EntityUtils.toString(response.getEntity());

        System.out.println(state);

        
    }
    
}
