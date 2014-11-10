package com.hp.score.content.mail.entities;

/**
 * Created by giloan on 11/5/2014.
 */
public class StringOutputStream extends java.io.OutputStream{
    String written;

    public StringOutputStream(){
        written = "";
    }

    public void write(int val){
        written +=(char)val;
    }

    public void write(byte [] buff){
        written += new String(buff);
    }

    public void write(byte [] buff, int offset, int len){
        written += new String(buff, offset, len);
    }

    public String toString(){
        return written;
    }

}
