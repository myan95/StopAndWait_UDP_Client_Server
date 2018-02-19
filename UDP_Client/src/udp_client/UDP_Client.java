/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udp_client;

/**
 *
 * @author Owner
 */
import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.Random;

//-----------------------------------------------------------------------------------------------------------------------------
public class UDP_Client {

    /**
     * @param args the command line arguments
     */
    public static final int BUFFER_SIZE = 1024;
    public static final int PORT = 6789;
    public static final String HOSTNAME = "localhost";
    public static final String FILE_NAME = "udpsocket.txt";
    public static String packet, ack, str, msg;
    public static int n, i = 0, sequence = 1;

//----------------------------------------------------------------------------------------------------------------------------------    
    public static void main(String[] args) throws SocketException, UnknownHostException, FileNotFoundException, IOException {

        // Create a socket
        DatagramSocket ClientSocket = new DatagramSocket();
        ClientSocket.setSoTimeout(1000);
        byte[] sendData = new byte[BUFFER_SIZE];
        byte[] receiveData = new byte[BUFFER_SIZE];
        InetAddress IPAddress = InetAddress.getByName(HOSTNAME);

        FileInputStream fstream = new FileInputStream(FILE_NAME);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader bcr = new BufferedReader(new InputStreamReader(in));
        byte[] FNBytes = new byte[BUFFER_SIZE];
        FNBytes = FILE_NAME.getBytes();
        DatagramPacket sendPacketFN = new DatagramPacket(FNBytes, FNBytes.length, IPAddress, PORT);
        ClientSocket.send(sendPacketFN);

        String Sentences;
        int counter = 0;
        while ((Sentences = bcr.readLine()) != null) {

            boolean timedOut = true;
            sequence=(sequence==0)?1:0;
            msg = String.valueOf(sequence);
            Sentences = msg.concat(Sentences);
            System.out.println("after concatination \t"+Sentences);
                
            while (timedOut) {

                System.out.println("counetr " + counter);
                
                sendData = Sentences.getBytes();
                  try {
                    Random r = new Random();
                    float chance = r.nextFloat();
                   if(chance <= 0.90f ){
              
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT);
                    ClientSocket.send(sendPacket);
                   }else {System.out.println("***packet lost wait till time out***");} 
   
                   
                   DatagramPacket AckRecive = new DatagramPacket(receiveData, receiveData.length);
		   ClientSocket.receive( AckRecive );
		   String AckSequence = new String(AckRecive.getData(), 0, AckRecive.getLength());
            			
                   if(Integer.valueOf(AckSequence.substring(0,1))== sequence) 
                   {System.out.println("Ack  not corrupted wait to resend ");}
                   else {System.out.println("Ack corrupted wait to resend ");}
                     
                   timedOut=false ;   
                    
                } catch (SocketTimeoutException exception) {
                    //resend 
                    System.out.println("******Time out resend packet******");
                    
                }
                counter++;
            }

        }

        ClientSocket.close();

    }

}
