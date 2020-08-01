/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import korisnikGUI.GUI;
import org.json.simple.JSONObject;

/**
 *
 * @author aca19
 */
public class ServisAplikacija extends Thread{
    
    private static ServisAplikacija referenceSA = null;
    
    private boolean running;
    //Konstruktor
    private ServisAplikacija(){
        
    }
    
    public static ServisAplikacija getReferenceSA(){
        if(referenceSA == null){
            referenceSA = new ServisAplikacija();
        }
        return referenceSA;
    }
    
    
    //Ovde ce biti razmena JMS poruka
    @Override
    public void run(){
        System.out.println("SERVIS Started");
        
        JMSContext context = Main.conncetionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(Main.queue);
        consumer.setMessageListener((Message msg) -> {
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject)((ObjectMessage)msg).getObject();
                System.out.println("JSONObject to send:");
                System.out.println(jsonObject.toString());
            } catch (JMSException ex) {
                Logger.getLogger(ServisAplikacija.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("JSONObject pre sendRequesr function");
            sendRequest(jsonObject);
        });
        
        while(!interrupted()){
            while(running){
                
            }
        }
        System.out.println("SERVIS Stopped");
    }
    
    //Sending Method------------------------------------------------------------
    private void sendRequest(JSONObject jObject){
        System.out.println("Sending the request!");
        try {
            URL url = new URL("http://collabnet.netset.rs:8081/is/persoCentar/submit");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);//Setting connection output true (input default === true)
            
            OutputStreamWriter outStream = new OutputStreamWriter(connection.getOutputStream());
            outStream.write(jObject.toString());
            outStream.flush();
            outStream.close();
            
            //ResponseCode
            int responseCode = connection.getResponseCode();
            BufferedReader inStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = inStream.readLine()) != null) {
                response.append(line);
            }
            System.out.println("ResponseCode -> " + responseCode);
            System.out.println("Response -> " + response);
            //ResponseReadDone
            
            //Update the status of sent Request
            if(responseCode == 200){
                int documentId = RegionalniCentar.getReferenceRC().getIdFromRequestId((String) jObject.get("id"));
                System.out.println("ID that is being updated -> " + documentId + " (sendRequest-ServisAplikacija)");
                RegionalniCentar.getReferenceRC().updateStatus(documentId, "U produkciji");
                GUI.getReferenceGUI().setMsg("Request sent SUCCESSFULLY!");
            }
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(ServisAplikacija.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error malformedURLException");
        } catch (IOException ex) {
            Logger.getLogger(ServisAplikacija.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error IOException");
        }
    }
    
    //Thread Control Methods----------------------------------------------------
    public void stopServisApplication(){
        running = false;
        this.interrupt();
    }
    
    public void pauseServisApplication(){
        running = false;
    }
    
    public void startServisApplication(){
        running = true;
        this.start();
    }
    
    public void continueServisApplication(){
        running = true;
    }
    //--------------------------------------------------------------------------
}
