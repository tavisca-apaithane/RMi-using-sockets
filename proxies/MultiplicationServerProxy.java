package sockets.proxies;

import sockets.Servers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiplicationServerProxy implements Servers, Serializable {
    private static ServerSocket proxyServer = null;
    private static Socket serverSideClientSocket = null;

    private  Double input1 = 0.0;
    private  Double input2 = 0.0;
    private Double result = 0.0;
    @Override
    public Double calculate(Double a, Double b) {
        Double result = 0.0;
        try {
            System.out.println("in calculate...");
            Socket client = new Socket("localhost", 6667);
            System.out.println("before ObjectStreams");
            ObjectOutputStream dataOut = new ObjectOutputStream(client.getOutputStream());
            dataOut.writeDouble(a);
            dataOut.flush();
            dataOut.writeDouble(b);
            dataOut.flush();
            ObjectInputStream dataIn = new ObjectInputStream(client.getInputStream());
            System.out.println("after object input stream...");

            result = dataIn.readDouble();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static void main(String[] args){
        try {
            proxyServer = new ServerSocket(6667);
                while(true) {
                    System.out.println("proxy listening...");
                    try {
                        serverSideClientSocket = proxyServer.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    MultiplicationServerProxy proxy = new MultiplicationServerProxy();
                    proxy.handleProxyClient(serverSideClientSocket);
                    System.out.println("client connected...");
                }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }


    }

    public  void handleProxyClient(Socket serverSideClientSocket){
        try {
            ObjectInputStream dataIn = new ObjectInputStream(serverSideClientSocket.getInputStream());
            input1 = dataIn.readDouble();
            input2 = dataIn.readDouble();
            Class cl = Class.forName("sockets.servers.MultiplicationServer");
            for(Method m : cl.getDeclaredMethods()){
                if(m.getName().equals("calculate")) {
                   this.result = (Double) m.invoke(cl.newInstance(), input1, input2);
                    break;
                }
            }
            ObjectOutputStream dataOut = new ObjectOutputStream(serverSideClientSocket.getOutputStream());
            dataOut.writeDouble(this.result);
            dataOut.flush();
            Thread.sleep(10000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



}
