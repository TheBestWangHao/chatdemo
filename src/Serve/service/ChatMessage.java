package Serve.service;

import org.omg.CORBA.Object;

import java.util.Comparator;

public class ChatMessage implements Comparable {
    public Long time;
    public String chatText;
    public Byte[] pictureBytes;

    public ChatMessage(){

    }

    @Override
    public int compareTo(java.lang.Object o) {
        if(this.time-((ChatMessage) o).time>0)
            return -1;
        else if(this.time-((ChatMessage) o).time<0)
            return 1;
        else if(time-((ChatMessage) o).time==0)
            return 0;
        return 0;
    }

}
