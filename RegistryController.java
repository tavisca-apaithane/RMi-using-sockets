package sockets;

public class RegistryController {
    public static void main(String[] args){
        Registry registry = new Registry();
        Thread thread = new Thread(()->{
            while(true) {
                registry.goOnline();
                System.out.println("request handled...");
            }
        });
        System.out.println("Thread listening...");
        thread.start();
    }
}
