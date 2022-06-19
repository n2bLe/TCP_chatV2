import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket client;
    private BufferedReader in;
    private BufferedWriter out;
    private String username;

    public Client(Socket socket,String username) {
        try {
            this.client = socket;
            this.username = username;
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        }catch (IOException e){
            e.printStackTrace();
            shutdown();
        }
    }

    public void sendMsg(){
        try{
            out.write(username);
            out.newLine();
            out.flush();

            Scanner sc = new Scanner(System.in);

            while(client.isConnected()){

                String messageToSend = sc.nextLine();
                out.write(username + ": " + messageToSend);
                out.newLine();
                out.flush();
            }
        }catch (IOException e){
            e.printStackTrace();
            shutdown();
        }
    }
    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                String msgFromClients;
                while(client.isConnected()){
                    msgFromClients = in.readLine();
                    System.out.println(msgFromClients);
                }
            }
            catch (IOException e){
                    e.printStackTrace();
                    shutdown();
            }}
        }).start();
    }
    public void shutdown(){
        try{
            if(out !=null){
                out.close();
            }
            if(in !=null){
                in.close();
            }
            if(client !=null){
                client.close();
            }


        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[]args )throws IOException{
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a username to connect");
        String username = sc.nextLine();
        Socket client = new Socket("localhost",5000);
        Client c = new Client(client,username);
        c.listenForMessage();
        c.sendMsg();
    }

}