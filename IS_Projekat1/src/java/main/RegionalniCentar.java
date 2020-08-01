/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import entities.Documentrequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.json.simple.parser.JSONParser;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.*;
import korisnikGUI.GUI;
import static main.Main.recievTopic;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

/**
 *
 * @author aca19
 */
public class RegionalniCentar {
    
    private int lastIdNum; //Bice (0) ako je baza prazna, a u suprotnom neki broj od 1 pa navise
    private static RegionalniCentar referenceRC = null;
    
    public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");
    private EntityManager em;
    
    private JMSContext contextGlobal;
    private JMSConsumer consumerGlobal;
    
    private RegionalniCentar(){
        em = emf.createEntityManager();
        lastIdNum = getMaxRequestID();
        makeMessageListenerForTimer();
    }
    
    //Base method for reaching RegionalCenter reference
    public static RegionalniCentar getReferenceRC(){
        if(referenceRC == null){
            referenceRC = new RegionalniCentar();
        }
        return referenceRC;
    }
    
    
    //DataBase Methods----------------------------------------------------------
    public void insert(Documentrequest documentRequest){
        em.getTransaction().begin();
        em.persist(documentRequest); //MORAJU SVA POLJA DA SE POPUNE
        em.flush();
        em.getTransaction().commit();
    }
    
    public void deleteAll(){
        Query qr = em.createQuery("Delete From Documentrequest");
        
        em.getTransaction().begin();
        int deletedNumber = qr.executeUpdate();
        em.getTransaction().commit();
        
        lastIdNum = 0;
        
        System.out.println("Deleted elements = " + deletedNumber);
    }
    
    public void updateStatus(int drId, String newStatus){
        Query qr = em.createQuery("Update Documentrequest d Set d.status = :newStatus Where d.iDdocumentrequest = :drId");
        
        em.getTransaction().begin();
        qr.setParameter("newStatus", newStatus).setParameter("drId", drId).executeUpdate();
        em.getTransaction().commit();
    }
    //--------------------------------------------------------------------------
    
    
    //Method for makeing new Request
    public void makeNewRequest(ArrayList<String> textFieldData, ArrayList<String> radioButtonData){
        if(checkAppointmentAvailability()){
            int newRequestID = lastIdNum + 1;
            lastIdNum++;
            Documentrequest dr = new Documentrequest(newRequestID, textFieldData, radioButtonData);
            insert(dr);
            String HTTPRequestID = changeToRequestIdString(newRequestID);
            JSONObject jObject = makeJSONObject(HTTPRequestID, newRequestID, textFieldData, radioButtonData);
            //Sad slanje JSONArray-a serveru preko JMS-a
            sendRequest(jObject);
            //Ovde ide updatestatus
        }
        else{
            //Pokazemo poruku da termin nije dostupno
            System.out.println("Termin nije dostupan!");
            GUI.getReferenceGUI().setMsg("ERROR :: Appointment is not available!");
        }
    }
    
    //Method for getting id from RequestID
    public int getIdFromRequestId(String HTTPRequestID){
        int retValue = (int)(Long.parseLong(HTTPRequestID) % 10000000);
        return retValue;
    }
    
    
    //Private Methods-----------------------------------------------------------
    private void makeMessageListenerForTimer(){
        contextGlobal= Main.conncetionFactory.createContext();
        consumerGlobal = contextGlobal.createConsumer(recievTopic);
        consumerGlobal.setMessageListener((Message msg) -> {
            
            System.out.println("Notification recieved! (RagionalniCentar - makeMessageListener)");
            
            String data = GUI.getReferenceGUI().getHTTPRequestID();
            System.out.println(data + "ID");
            if(data.length() == 12){
                em.clear();
                em.getTransaction().begin();
                Documentrequest result = em.find(Documentrequest.class, getIdFromRequestId(data));
                em.getTransaction().commit();
                
                if(result != null){
                    
                    System.out.println("Status = " + result.getStatus() + " (RagionalniCentar - makeMessageListener)");
                    
                    /*if(result.getStatus().equals("Kreiran")){
                        GUI.getReferenceGUI().setStatusLabelText("U produkciji");
                        checkStatusInPerso(data);
                    }*/
                    if(result.getStatus().equals("U produkciji")){
                        GUI.getReferenceGUI().setStatusLabelText("U produkciji");
                        checkStatusInPerso(data);
                    }
                    if(result.getStatus().equals("Ceka na urucenje")){
                        GUI.getReferenceGUI().setStatusLabelText("Ceka na urucenje");
                        GUI.getReferenceGUI().getDeliverButton().setEnabled(true);
                    }
                    if(result.getStatus().equals("Urucen")){
                        GUI.getReferenceGUI().getDeliverButton().setEnabled(false); //Mozda ovo ne mroa ovde al ajde
                        GUI.getReferenceGUI().setStatusLabelText("Urucen");
                    }
                }
            }else{
                //Nema desavanja
            }
            
        });
    }
    
    private Integer getMaxRequestID(){
        TypedQuery<Documentrequest> qr = em.createQuery("Select a From Documentrequest a Where a.iDdocumentrequest = (Select Max(b.iDdocumentrequest) From Documentrequest b)", Documentrequest.class);
        Integer maxID;
        
        em.getTransaction().begin();
        try{
            maxID = qr.getSingleResult().getIDdocumentrequest();
        }catch(NoResultException e){
            em.getTransaction().commit();
            System.out.println("Database is empty!");
            return 0;
        }
        em.getTransaction().commit();
        //System.out.println("Max ID is - " + maxID);
        return maxID;
    }
    
    private boolean checkAppointmentAvailability(){
        String idRegCenter = "17559";
        JSONParser jParser = new JSONParser();
        JSONObject jObject;
        
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        //String termin = sdfDay.format(new Date()) + "T" + sdfTime.format(new Date());
        
        String termin = "2020-02-10T10:00:00";
        
        //HTTP
        URL url;
        try{
            url = new URL("http://collabnet.netset.rs:8081/is/terminCentar/checkTimeslotAvailability?regionalniCentarId="+ idRegCenter + "&termin=" + termin);
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            jObject = (JSONObject) jParser.parse(bufferReader);
            bufferReader.close();
            
            boolean dostupnost = (boolean) jObject.get("dostupnost");
            if(dostupnost){
                System.out.println("Dostupno");
                return true;
            }
            else{
                System.out.println("Nije dostupno");
                return false;
            }
        }
        catch(MalformedURLException e){} 
        catch (IOException | ParseException ex) {
            Logger.getLogger(RegionalniCentar.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Default return
        return false;
    }
    
    private JSONObject makeJSONObject(String HTTPRequestID, int newRequestID, ArrayList<String> textFieldData, ArrayList<String> radioButtonData){
        JSONObject jObject = new JSONObject();
        
        jObject.put("id", HTTPRequestID);
        jObject.put("JMBG", textFieldData.get(0));
        jObject.put("ime", textFieldData.get(1));
        jObject.put("prezime", textFieldData.get(2));
        jObject.put("imeMajke", textFieldData.get(11));
        jObject.put("imeOca", textFieldData.get(9));
        jObject.put("prezimeMajke", textFieldData.get(12));
        jObject.put("prezimeOca", textFieldData.get(10));
        jObject.put("pol", radioButtonData.get(0));
        jObject.put("datumRodjenja", textFieldData.get(3));
        jObject.put("nacionalnost", textFieldData.get(4));
        jObject.put("profesija", textFieldData.get(5));
        jObject.put("bracnoStanje", radioButtonData.get(1));
        jObject.put("opstinaPrebivalista", textFieldData.get(6));
        jObject.put("ulicaPrebivalista", textFieldData.get(7));
        jObject.put("brojPrebivalista", textFieldData.get(8));
        
        return jObject;
    }
    
    private void sendRequest(JSONObject jObject){
        JMSContext context = Main.conncetionFactory.createContext();
        JMSProducer producer = context.createProducer();
        ObjectMessage jsonMessage = context.createObjectMessage(jObject);
        producer.send(Main.queue, jsonMessage);
    }
    
    private void checkStatusInPerso(String HTTPRequestId){
        
        JSONParser jParser = new JSONParser();
        
        try {
            URL url = new URL("http://collabnet.netset.rs:8081/is/persoCentar/"+HTTPRequestId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            JSONObject jObject = (JSONObject) jParser.parse(bufferReader);
            
            String status = (String) jObject.get("status");
            System.out.println(HTTPRequestId + " -> " + status + " (RegionalniCentar - checkStatusInPerso)");
            
            if("proizveden".equals(status)){
                updateStatus(getIdFromRequestId(HTTPRequestId), "Ceka na urucenje");
                GUI.getReferenceGUI().getDeliverButton().setEnabled(true);
                GUI.getReferenceGUI().setStatusLabelText("Ceka na urucejne");
            }
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(RegionalniCentar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RegionalniCentar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(RegionalniCentar.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    private String changeToRequestIdString(int number){
        String prefix = "17559";
        Integer num = (Integer)number;
        String retValue = "";
       
        int numLenght = num.toString().length();
        int toGo = 7 - numLenght;
        
        for(int i = 0; i < toGo; i++){
            retValue = retValue + "0";
        }
        retValue = prefix + retValue + num.toString();
       
        return retValue;
    }
}
