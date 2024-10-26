package server;

import java.io.DataInputStream;
import java.net.Socket;

public class ServerThread extends Thread{
    Socket s;
    Server srv;
    DataInputStream din;
    public ServerThread(Socket s, Server athis){
        this.s = s;
        this.srv = athis;
    }
    public void run(){
        try{
            din = new DataInputStream(s.getInputStream());
            while(true){
                String msg = din.readUTF();
                System.out.println("Reception de : "+msg+ "de la part de"+s);
                srv.sendToAll(msg);
            }
        }catch(Exception e){
            System.out.println("Erreur dans run du Thread");
        }finally {
            srv.removeClient(String.valueOf(s));
        }
    }
}
