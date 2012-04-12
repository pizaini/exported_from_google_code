package com.pizaini.messaging;

import javax.microedition.io.Connector;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;
/**
 * 
 * @author Pizaini
 * 05 April 2012
 */

public class CrazyMessaging extends MIDlet implements CommandListener, Runnable{
    private Command send, exit;
    private TextField phone, message, loop;
    private StringItem smsResult;
    private final String sign = "Sent from Crazy Messaging";

    protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
            // TODO Auto-generated method stub

    }

    protected void pauseApp() {
            // TODO Auto-generated method stub

    }

    protected void startApp() throws MIDletStateChangeException{
    
        Form form = new Form("Crazy Messaging");
    
        phone = new TextField("Phone number", "", 20, TextField.PHONENUMBER);

        form.append(phone);

        message = new TextField("Message", "", 160, TextField.ANY);

        form.append(message);
        
        loop = new TextField("Loop", "1", 3, TextField.NUMERIC);

        form.append(loop);

        smsResult = new StringItem("Create by", "Pizaini (c) 2012 - Beta 1.0");

        form.append(smsResult);

        form.addCommand(send = new Command("Send", Command.OK, 1));
        form.addCommand(exit = new Command("Exit", Command.EXIT, 1));
        form.setCommandListener(this);

        Display.getDisplay(this).setCurrent(form);
    }
    public void sendSms(String number, String message, String loop){
        int nbLoop = Integer.parseInt(loop);
        if(nbLoop > 0){
            for(int i = 1; i <= nbLoop; i++){
                try {
                    //sets address to send message
                    String addr = "sms://"+number;
                    // opens connection
                    MessageConnection conn = (MessageConnection) Connector.open(addr);
                    // prepares text message
                    TextMessage msg = (TextMessage)conn.newMessage(MessageConnection.TEXT_MESSAGE);
                    //set text
                    msg.setPayloadText(message + " "+i+"\n\n=====\n"+sign);
                    // send message
                    conn.send(msg);
                    conn.close();
                }catch(SecurityException se) {

                } catch (Exception e) {

                }
            }
        }
    }
    
    public void commandAction(Command c, Displayable d){
        if(c == exit){
                notifyDestroyed();
        } else{
                new Thread(this).start();
        }
    }
    
    public void run(){
        if(loop.getString().length() > 0)
            sendSms(phone.getString(), message.getString(), loop.getString());
    }
}