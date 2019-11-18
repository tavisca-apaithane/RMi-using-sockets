package sockets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private Socket clientSocket = null;
    private ObjectOutputStream dataOut = null;
    private ObjectInputStream dataIn = null;
    Servers server = null;
    public Client(){

        try {
            clientSocket = new Socket("localhost", 6666);
            System.out.println("connected to registry");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Servers lookUp(String operation){
        try {
            dataOut = new ObjectOutputStream(clientSocket.getOutputStream());
            dataOut.writeUTF("client");
            dataOut.writeUTF(operation);
            dataOut.flush();
            System.out.println("great1");
            dataIn = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println("great2");
            server = (Servers) dataIn.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return server;
    }
    public static void main(String[] args){
         Client client = new Client();
        System.out.println("done");
         Servers server = client.lookUp("multiply");
        System.out.println("interface Collected from registry...");
        Double result = server.calculate(new Double(2.0), new Double(2.0));
        System.out.println("From server : " + result);
    }

}
