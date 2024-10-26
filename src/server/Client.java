package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Client extends JFrame implements Runnable {
    JTextField tf;
    JTextArea ta;
    String nom;
    Socket s;
    DataOutputStream dout;
    DataInputStream din;

    public Client(String nom,String host,int port){
        super(nom);
        tf = new JTextField();
        ta = new JTextArea();

        tf.setLayout(new BorderLayout());
        ta.setLayout(new FlowLayout());

        this.add(tf, BorderLayout.NORTH);
        this.add(ta, BorderLayout.CENTER);

        try{
            s = new Socket(host,port);
            din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());
        }catch(Exception e){
            System.out.println("Erreur cnx "+e);
        }


        tf.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    dout.writeUTF( nom + " : " + tf.getText());
                    tf.setText("");
                }catch(Exception ex){
                    System.out.println("Erreur dans ActionListeners"+ex);
                }
            }
        });



        ta.setEditable(false);
        tf.setFocusable(true);
        setLocationRelativeTo(null);
        setVisible(true);
        setSize(300,400);

        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                String msg = din.readUTF();

                SwingUtilities.invokeLater(() -> ta.append(msg + "\n"));
            }
        } catch (Exception e) {
            System.out.println("Erreur dans le thread client: " + e);
        }
    }
    }
