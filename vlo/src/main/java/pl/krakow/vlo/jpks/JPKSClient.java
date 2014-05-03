package pl.krakow.vlo.jpks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by m4tx3 on 5/3/14.
 */
public class JPKSClient {
    private static final String HOSTNAME = "users.v-lo.krakow.pl";
    private static final int PORT = 6666;

    private static JPKSClient instance;

    public JPKSClient() {
        try {
            Socket sock = new Socket(HOSTNAME, PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream
                    ()));
            PrintWriter writer = new PrintWriter(sock.getOutputStream(), true);
            writer.println("logandroid");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            instance = this;
        }
    }
}
