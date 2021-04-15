import java.util.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int p, g, Xb;
        System.out.println("Enter value of p");
        p = sc.nextInt();
        System.out.println("Enter value of generator polynomial g");
        g = sc.nextInt();
        System.out.println("Enter value of a");
        Xb = sc.nextInt();
        int Yb = exp(g, Xb, p);
        String y = Integer.toString(Yb);
        System.out.println("Waiting for Connections : ");
        try {
            ServerSocket ss = new ServerSocket(8000);
            Socket soc = ss.accept();
            System.out.println("Connection Established");

            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            PrintWriter out = new PrintWriter(soc.getOutputStream(), true);

            // reading public key sent by client
            String x = in.readLine();
            int Ya = Integer.valueOf(x);
            int Kab = exp(Ya, Xb, p);
            System.out.println("Public key received from client : " + Ya);
            System.out.println("Shared secret key computed as : " + Kab);
            out.println(y);

        }

        catch (Exception e) {
            System.err.println(e);
        }
    }

    public static int exp(int a, int b, int n) {
        if (b == 0)
            return 1;

        else if (b % 2 == 0) {
            int x = exp(a, b / 2, n);
            return (x * x) % n;
        }

        else {
            int x = exp(a, b / 2, n);
            return (a * ((x * x) % n)) % n;
        }
    }
}
