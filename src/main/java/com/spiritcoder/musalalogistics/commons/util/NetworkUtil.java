package com.spiritcoder.musalalogistics.commons.util;

import java.net.InetSocketAddress;
import java.net.Socket;


public class NetworkUtil {

    public static boolean ping(String ip, Integer port){
        try{
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip,port), 2000);
            socket.close();
            return false;
        }catch(Exception exception){
            return true;
        }
    }
}
