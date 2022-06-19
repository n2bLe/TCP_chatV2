import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket client;
    private BufferedReader in;
    private BufferedWriter out;
    private String clientUsername;

    public ClientHandler(Socket client) throws IOException{
        try {
            this.client = client;

            this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

            clientUsername = in.readLine();
            clientHandlers.add(this);
            broadcast("[SERVER]  "+ clientUsername + " has entered the chat !");


        }catch (IOException e){
            e.printStackTrace();
                shutdown();
        }
    }
    @Override
    public void run() {
        String msg;
        while(client.isConnected()){
            try{
                msg = in.readLine();
                broadcast(msg);
            }catch (IOException e){
                e.printStackTrace();
                shutdown();
                break;
            }
        }

    }
    public void broadcast(String msg){
        for(ClientHandler ch : clientHandlers){
            try{
                ch.out.write(msg);
                ch.out.newLine();
                ch.out.flush();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcast("[SERVER]  "+ clientUsername + " has left the chat") ;
    }
    public void shutdown(){
        removeClientHandler();
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
}