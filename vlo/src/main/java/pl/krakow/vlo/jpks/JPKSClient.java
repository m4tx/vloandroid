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
    private static final String COMMAND_ANSWER = "ans";
    private static final String COMMAND_QUESTION = "que";
    private static final String COMMAND_CORRECT_ANSWER = "lib";
    private static final String COMMAND_MESSAGE = "txt";
    private static final String COMMAND_IMAGE = "img";
    private static final String COMMAND_COUNT = "cnt";
    private static final String COMMAND_CLEAR_RANKING = "rpr";
    private static final String COMMAND_APPEND_RANKING = "rnk";
    private static final String COMMAND_POINT_GOT = "pkt";
    private static final String COMMAND_REPAINT = "rep";
    private static final String COMMAND_CLEAR = "cle";

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
            switch (command.substring(0, 3)) {
                case COMMAND_QUESTION:
                    break;
                case COMMAND_CORRECT_ANSWER:
                    break;
                case COMMAND_MESSAGE:
                    break;
                case COMMAND_IMAGE:
                    break;
                case COMMAND_COUNT:
                    break;
                case COMMAND_CLEAR_RANKING:
                    break;
                case COMMAND_APPEND_RANKING:
                    break;
                case COMMAND_POINT_GOT:
                    break;
                case COMMAND_REPAINT:
                    break;
                case COMMAND_CLEAR:
                    break;
            }
        }
    }
}
