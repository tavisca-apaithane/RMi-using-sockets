package sockets.servers;

import sockets.proxies.MultiplicationServerProxy;
import sockets.Servers;

import java.io.*;
import java.net.Socket;

public class MultiplicationServer implements Servers {

    private Socket registrationSocket = null;
    public Double result = 0.00;

    public void registerServer(){
        try {
            registrationSocket = new Socket("localhost", 6666);
            ObjectOutputStream dataOut = new ObjectOutputStream(registrationSocket.getOutputStream());
            dataOut.writeUTF("server");
            dataOut.flush();
            dataOut.writeUTF("multiply");
            dataOut.flush();
            dataOut.writeObject(new MultiplicationServerProxy());
            dataOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Double calculate(Double a, Double b) {
        return a * b;
    }


    public static void main(String[] args){
        MultiplicationServer server = new MultiplicationServer();
        server.registerServer();
    }
}
