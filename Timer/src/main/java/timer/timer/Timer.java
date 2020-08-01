/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timer.timer;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;
import javax.jms.Topic;

/**
 *
 * @author aca19
 */
@Stateless
public class Timer {   
    
    //JMS Resource--------------------------------------------------------------
   @Resource (lookup = "jms/__defaultConnectionFactory")
   public ConnectionFactory conncetionFactory;
   @Resource (lookup = "TimerClientTopic")
   public Topic sendTopic;
   //---------------------------------------------------------------------------
    private JMSProducer producer=null; 
   //Timer method
   @Schedule(second="*/5", minute = "*",  hour = "*")
   public void sendNotivication(){
       JMSContext context = conncetionFactory.createContext();
       if(producer == null)
           producer = context.createProducer();
       TextMessage msg = context.createTextMessage();
       producer.send(sendTopic, msg);
       System.out.println("Notivication sent!");
       
   }
    
}

