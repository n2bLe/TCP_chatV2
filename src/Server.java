import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }
    public void startServer(){
        try{
            while(!serverSocket.isClosed()){
                Socket client = serverSocket.accept();
                System.out.println("A new client connected");
                ClientHandler clientHandler = new ClientHandler(client);

                Thread thread = new Thread(clientHandler);
                thread.start();

            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void closeSerever(){
        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[]args) throws IOException{
        ServerSocket socket = new ServerSocket(5000);
        Server server = new Server(socket);
        server.startServer();
    }
}