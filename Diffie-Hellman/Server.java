import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        System.out.println("Waiting for Connections : ");
        try {
            ServerSocket ss = new ServerSocket(8000);
            Socket soc = ss.accept();
            System.out.println("Connection Established");
        }

        catch (Exception e) {
            System.err.println(e);
        }
    }
}
