import java.io.*;
import java.net.Socket;
import java.util.*;

public class Client {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int p, g, Xa;
        System.out.println("Enter value of p");
        p = sc.nextInt();
        System.out.println("Enter value of generator polynomial g");
        g = sc.nextInt();
        System.out.println("Enter value of a");
        Xa = sc.nextInt();
        int Ya = exp(g, Xa, p);
        String y = Integer.toString(Ya);
        try {
            System.out.println("Client Program Started");
            Socket soc = new Socket("localhost", 8000);
            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            PrintWriter out = new PrintWriter(soc.getOutputStream(), true);

            out.println(y);
            String x = in.readLine();
            int Yb = Integer.valueOf(x);
            int Kab = exp(Yb, Xa, p);
            System.out.println("Public Key received from server : " + Yb);
            System.out.println("Shared Secret key is computed as : " + Kab);
            soc.close();
        }

        catch (Exception e) {
            System.err.println(e);
        }
        sc.close();

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
