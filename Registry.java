package sockets;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Registry {
    private ServerSocket registryServer = null;
    private Socket serverSideClientSocket = null;
    private ObjectInputStream dataIn = null;
    private ObjectOutputStream dataOut = null;
    private HashMap<String, Object> serverMap = new HashMap<>();
    public Registry(){
        try {
            registryServer = new ServerSocket(6666);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void goOnline(){
        try {
            System.out.println("registry online...");
            serverSideClientSocket = registryServer.accept();
            handleRequests(serverSideClientSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRequests(Socket serverSideClientSocket) {
        try {
            dataIn = new ObjectInputStream(serverSideClientSocket.getInputStream());
            dataOut = new ObjectOutputStream(serverSideClientSocket.getOutputStream());
            String peer = dataIn.readUTF();
            if(peer.equals("client")){
                String key = dataIn.readUTF();
                Object obj = serverMap.get(key);
                dataOut.writeObject(obj);
                dataOut.flush();
            }else if(peer.equals("server")){
                String serverIdentifier = dataIn.readUTF();
                Object serverProxy = dataIn.readObject();
                serverMap.put(serverIdentifier, serverProxy);
                System.out.println("server registration successful...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
