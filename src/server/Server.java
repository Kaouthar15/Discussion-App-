package server;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;

public class Server {
    Hashtable<Socket, DataOutputStream> lesClients = new Hashtable<>();

    public Server(int port){
        ecouter(port);
    }
    private void ecouter(int port){
        try{
            ServerSocket ss = new ServerSocket(port);
            System.out.println("En écoute sur le port : "+port);
            while(true){
                Socket s = ss.accept();
                System.out.println("Connexion de : "+ s);
                DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                lesClients.put(s,dout);
                new ServerThread(s,this).start();

            }
        }
        catch(Exception er){
            System.out.println("Erreur dans la boucle d'écoute du serveur\n"+er);
        }
    }
    public void sendToAll(String msg){
        try{
            synchronized (lesClients){
                for (Enumeration<DataOutputStream> e = lesClients.elements();e.hasMoreElements();){
                    DataOutputStream dout = e.nextElement();
                    dout.writeUTF(msg);
                    System.out.println("Diffusion de : "+msg);
                }
            }
        }catch(Exception e){
            System.out.println("Erreur d'envoie dans sendToAll"+e);
        }
    }
    public void removeClient(String s){
        synchronized (lesClients){
            lesClients.remove(s);
        }
    }



    public static void main(String[] args) {
        new Thread(() -> new Server(9999)).start();

        try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> new Client("Jilali", "127.0.0.1", 9999)).start();
        new Thread(() -> new Client("Mouna", "127.0.0.1", 9999)).start();
        new Thread(() -> new Client("Wafae", "127.0.0.1", 9999)).start();
    }
}
