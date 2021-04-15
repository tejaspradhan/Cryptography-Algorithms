import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            System.out.println("Client Program Started");
            Socket soc = new Socket("localhost", 8000);
        }

        catch (Exception e) {
            System.err.println(e);
        }

    }
}
