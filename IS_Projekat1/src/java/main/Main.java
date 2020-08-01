/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import entities.Documentrequest;
import javax.annotation.Resource;
import javax.jms.*;
import korisnikGUI.GUI;

/**
 *
 * @author aca19
 */
public class Main {
    
   //JMS Resource--------------------------------------------------------------
   @Resource (lookup = "jms/__defaultConnectionFactory")
   public static ConnectionFactory conncetionFactory;
   @Resource(lookup = "ClientServerQueue")
   public static Queue queue;
   @Resource (lookup = "ClientTimerQueue")
   public static Queue sendQueue;
   @Resource (lookup = "TimerClientTopic")
   public static Topic recievTopic;
   //---------------------------------------------------------------------------
            
    public static void main(String[] args) {
        
        System.out.println("Poceto");
        RegionalniCentar.getReferenceRC(); //Da bi napravio samo
        GUI g = GUI.getReferenceGUI();
        ServisAplikacija.getReferenceSA().startServisApplication();
        System.out.println("Zavrseno");
    }
    
}
