package pl.krakow.vlo.jpks;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * The JPKS client. JPKS is Andrzej Dyrek's quiz system written in Java that runs on
 * users.v-lo.krakow.pl server.
 *
 * @see <a href="http://users.v-lo.krakow.pl/~dyrek/jpks/">JPKS Homepage</a>
 */
public class JPKSClient {
    private static final String LOGGER_TAG = "JPKSClient";

    private static final String HOSTNAME = "users.v-lo.krakow.pl";
    private static final int PORT = 6666;
    private static final int TIMEOUT = 1000;

    private static final String COMMAND_LOGIN = "log";
    private static final String COMMAND_ANSWER = "ans";
    private static final String COMMAND_CLEAR = "cle";
    private static final String COMMAND_QUESTION = "que";
    private static final String COMMAND_CORRECT_ANSWER = "lib";
    private static final String COMMAND_MESSAGE = "txt";
    private static final String COMMAND_IMAGE = "img";
    private static final String COMMAND_COUNT = "cnt";
    private static final String COMMAND_CLEAR_RANKING = "rpr";
    private static final String COMMAND_APPEND_RANKING = "rnk";
    private static final String COMMAND_POINT_GOT = "pkt";
    private static final String COMMAND_REPAINT = "rep";

    private static JPKSClient instance;

    private BufferedReader reader;
    private PrintWriter writer;
    private JPKSCommandListener commandListener;

    /**
     * Constructor that connects and logs in to the JPKS server.
     *
     * @param nickname user's nickname
     */
    public JPKSClient(String nickname) {
        try {
            Log.v(LOGGER_TAG, "Trying to connect to JPKS: " + HOSTNAME + ":" + PORT);
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress(HOSTNAME, PORT), TIMEOUT);
            reader = new BufferedReader(new InputStreamReader(sock.getInputStream
                    ()));
            writer = new PrintWriter(sock.getOutputStream(), true);
            Log.v(LOGGER_TAG, "Successfully connected! Sending request to log in: " +
                    COMMAND_LOGIN + nickname);
            writer.println(COMMAND_LOGIN + nickname);

            new Session().start();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            instance = this;
        }
    }

    /**
     * Sends a response from user to the server.
     * <p/>
     * <strong>Please note that this method does not check if the length of the answer is
     * ok.</strong> It means that <em>you</em> have to check if it is shorter than 60 characters.
     *
     * @param answer the answer to send
     */
    public void sendAnswer(String answer) {
        assert answer.length() >= 0 && answer.length() <= 60;
        String toSend = COMMAND_ANSWER + JPKSStringCoder.encodeString(answer);
        Log.v(LOGGER_TAG, "Sending message: " + toSend);
        writer.println(toSend);
    }

    /**
     * Sets a {@link pl.krakow.vlo.jpks.JPKSCommandListener} that is notified about every command
     * sent from the server.
     *
     * @param commandListener {@link pl.krakow.vlo.jpks.JPKSCommandListener} to set
     */
    public void setCommandListener(JPKSCommandListener commandListener) {
        this.commandListener = commandListener;
    }

    private class Session extends Thread {
        @Override
        public void run() {
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    processCommand(JPKSStringCoder.decodeString(line));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void processCommand(String command) {
            Log.v(LOGGER_TAG, "Processing command from server: \"" + command + "\"");
            if (commandListener == null) {
                return;
            }

            switch (command.substring(0, 3)) {
                case COMMAND_CLEAR:
                    commandListener.onClearQuestion();
                    break;
                case COMMAND_QUESTION:
                    commandListener.onQuestion(command.substring(3));
                    break;
                case COMMAND_CORRECT_ANSWER:
                    commandListener.onCorrectAnswer(command.substring(3));
                    break;
                case COMMAND_MESSAGE:
                    commandListener.onMessage(command.substring(3));
                    break;
                case COMMAND_IMAGE:
                    commandListener.onImageSent(command.substring(3));
                    break;
                case COMMAND_COUNT:
                    commandListener.onCount(command.substring(3));
                    break;
                case COMMAND_CLEAR_RANKING:
                    commandListener.onClearRanking();
                    break;
                case COMMAND_APPEND_RANKING:
                    commandListener.onAppendToRanking(command.substring(3));
                    break;
                case COMMAND_POINT_GOT:
                    commandListener.onPointGot(command.substring(3));
                    break;
                case COMMAND_REPAINT:
                    commandListener.onRepaint();
                    break;
                default:
                    Log.w(LOGGER_TAG, "Unrecognized response from server: \"" + command + "\"");
            }
        }
    }
}
