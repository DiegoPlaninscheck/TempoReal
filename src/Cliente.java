import java.net.*;
import java.io.*;

public class Cliente extends Thread {

    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

    DatagramSocket clientSocket;
    String servidor = "localhost";
    int porta = 9090;

    InetAddress IPAddress;

    byte[] sendData = new byte[1024];
    byte[] receiveData = new byte[1024];

    public Cliente() {
        try {
            clientSocket = new DatagramSocket();

            IPAddress = InetAddress.getByName(servidor);

            while (true) {
                System.out.println("Digite o texto a ser enviado ao servidor: ");
                String sentence = inFromUser.readLine();
                sendData = sentence.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, porta);

                clientSocket.send(sendPacket);

                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                clientSocket.receive(receivePacket);
                String modifiedSentence = new String(receivePacket.getData());
                System.out.println(modifiedSentence);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String args[]) throws Exception {
        new Cliente();
//        clientSocket.close();
    }
}
