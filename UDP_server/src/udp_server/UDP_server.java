/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udp_server;

/**
 *
 * @author Owner
 */
import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;
import java.io.BufferedWriter;

//----------------------------------------------------------------------------------------------
public class UDP_server {

    /**
     * @param args the command line arguments
     */
    private static final int BUFFER_SIZE = 1024;
    private static final int PORT = 6789;
    private static String FILE_NAME;
    private static int sequence = 0;
    private static int RecievedSequence =0;
    private static String Ack = "0";
   // private static String packet,ack,data="";

	
    public static void main(String[] args) throws SocketException, IOException {

        // Create a server socket
        DatagramSocket serverSocket = new DatagramSocket(PORT);

        // Set up byte arrays for sending/receiving data
        byte[] receiveData = new byte[BUFFER_SIZE];
        byte[] dataForSend = new byte[BUFFER_SIZE];
        
        DatagramPacket ReceivedFN = new DatagramPacket(receiveData, receiveData.length);
        serverSocket.receive(ReceivedFN);
        String FN = new String(ReceivedFN.getData(), 0, ReceivedFN.getLength());
        System.out.println("FN" + FN);
        File file = new File("Reciedfile" + FN);
        FileOutputStream fout = new FileOutputStream(file);
        System.out.println("file created ");
        int counter = 0;

        while (true) {
            System.out.println("while true entered ");
            String NL = "\n";
            
            ReceivedFN = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(ReceivedFN);
            FN = new String(ReceivedFN.getData(), 0, ReceivedFN.getLength());
            Random random = new Random( );
             
            //int AckChance = random.nextInt( 100 );
            Random r = new Random();
            float chance = r.nextFloat();
            
            if(Integer.valueOf(FN.substring(0,1))== sequence){
           // if( ((AckChance % 2) == 0) ){
            if(chance <= 0.90f ){
            
                System.out.println("FROM CLIENT: " + FN);
                  String AckValue = String.valueOf(sequence);
                  System.out.println("if entered ");
                  FN =FN.substring(1,FN.length());
		  sequence=(sequence==0)?1:0;
                  System.out.println("seuence "+sequence);
                  byte[] LineBytes = FN.getBytes();
                  fout.write(LineBytes, 0, LineBytes.length);
                  String NewLine = "\n";
                  byte[] gap = NewLine.getBytes();
                  fout.write(gap);
                  
              fout.flush();
                 
              // Get packet's IP and port
              Random K = new Random();
              float Ackcorrution = K.nextFloat();
              if(chance <= 0.10f ){
                 if(AckValue.equals("0"))
                     AckValue="1";
                 else 
                     AckValue ="0";
              
              }
              InetAddress IPAddress = ReceivedFN.getAddress();
              int port = ReceivedFN.getPort();
              byte[] Ackpacket= AckValue.getBytes();
              DatagramPacket Ack = new DatagramPacket(Ackpacket,Ackpacket.length, IPAddress, port);
              serverSocket.send(Ack);

              
             } else {
              System.out.println( "Oops, packet with sequence number    "+ sequence+ "     was dropped");
            }
            
            }else {System.out.println("Wrong packet sequence *** doubled data ");
                System.out.println("sequence "+sequence);
            }

            
        }

    }

}
