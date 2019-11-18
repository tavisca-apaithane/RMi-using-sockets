package sockets;

import java.util.Scanner;

public class ClientController {

    public static void main(String[] args){
        Client client = new Client();
        Scanner sc = new Scanner(System.in);
        String operation = sc.nextLine();
        Servers server = client.lookUp(operation);
    }

}
