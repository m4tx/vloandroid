package pl.krakow.vlo.jpks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by m4tx3 on 5/3/14.
 */
public class JPKSClient {
    private static final String HOSTNAME = "users.v-lo.krakow.pl";
    private static final int PORT = 6666;
    private static final int TIMEOUT = 1000;

    private static final String COMMAND_LOGIN = "log";

    private static JPKSClient instance;

    private BufferedReader reader;
    private PrintWriter writer;
    private JPKSCommandListener commandListener;

    public JPKSClient() {
        try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress(HOSTNAME, PORT), TIMEOUT);
            reader = new BufferedReader(new InputStreamReader(sock.getInputStream
                    ()));
            writer = new PrintWriter(sock.getOutputStream(), true);
            writer.println(COMMAND_LOGIN + "android");

            new Session().start();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            instance = this;
        }
    }

    public void sendAnswer(String answer) {
        // the answer should be max 60 characters long
        throw new UnsupportedOperationException(); // not yet implemented
    }

    public void setCommandListener(JPKSCommandListener commandListener) {
        this.commandListener = commandListener;
    }

    private class Session extends Thread {
        @Override
        public void run() {
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    processCommand(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void processCommand(String command) {
            switch(command.substring(0, 3)) {
                // not yet implemented
            }
        }
    }
}
