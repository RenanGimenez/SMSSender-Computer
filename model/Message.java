package model;

import java.text.SimpleDateFormat;
import java.util.Date;

import tools.Options;

public class Message {
    private String sender;
    private String receiver;
    private String name;
    private Long date;
    private String content;
    private String type;

    public Message(String sender, String receiver, String name, Long date, String content, String type) {
    	if(sender.startsWith("0"))
    		this.sender = Options.getPrefixOfCountry() + sender.substring(1);
    	else 
    		this.sender = sender;
    	
    	if(receiver.startsWith("0"))
    		this.receiver = Options.getPrefixOfCountry() + receiver.substring(1);
    	else 
    		this.receiver = receiver;

        this.name = name;
        this.date = date;
        this.content = content;
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getName() {
        return name;
    }

    public Long getDate() {
        return date;
    }

    public String getFormatedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        return sdf.format(new Date(date)).toString();
    }

    public String getContent() {
        return content;
    }

    public String getType() {
        return type;
    }

    public String toString(){
        return "sender: "+sender+"\treceiver: "+receiver+"\tname: "+name+"\tdate: "+getFormatedDate()+"\tmsg: "+content+"\ttype: "+type;
    }
}
