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
public abstract class BaseJpksClient {
    /**
     * Prefix used in addition to the filename to download image.
     */
    public static final String IMAGE_URL_PREFIX = "http://users.v-lo.krakow.pl/~dyrek/jpks/img/";

    private static final String LOGGER_TAG = "BaseJpksClient";

    private static final String HOSTNAME = "users.v-lo.krakow.pl";
    private static final int PORT = 6666;
    private static final int TIMEOUT = 1000;

    protected static final String COMMAND_LOGIN = "log";
    protected static final String COMMAND_ANSWER = "ans";
    protected static final String COMMAND_CLEAR = "cle";
    protected static final String COMMAND_QUESTION = "que";
    protected static final String COMMAND_CORRECT_ANSWER = "lib";
    protected static final String COMMAND_MESSAGE = "txt";
    protected static final String COMMAND_IMAGE = "img";
    protected static final String COMMAND_COUNT = "cnt";
    protected static final String COMMAND_CLEAR_RANKING = "rpr";
    protected static final String COMMAND_APPEND_RANKING = "rnk";
    protected static final String COMMAND_POINT_GOT = "pkt";
    protected static final String COMMAND_REPAINT = "rep";

    private BufferedReader reader;
    private PrintWriter writer;

    /**
     * Constructor that connects and logs into the JPKS server.
     *
     * @param nickname user's nickname
     */
    public BaseJpksClient(String nickname) {
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

            new CommandListenerThread().start();
        } catch (IOException e) {
            e.printStackTrace();
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
        String toSend = COMMAND_ANSWER + JpksStringCoder.encodeString(answer);
        Log.v(LOGGER_TAG, "Sending message: " + toSend);
        writer.println(toSend);
    }

    /**
     * The thread that listens for commands from the server and passes them the
     * {@link #processCommand(String, String)} method.
     */
    private class CommandListenerThread extends Thread {
        @Override
        public void run() {
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    line = JpksStringCoder.decodeString(line);
                    Log.v(LOGGER_TAG, "Processing command from server: \"" + line + "\"");

                    if (line.length() < 3) {
                        return;
                    }
                    processCommand(line.substring(0, 3), line.substring(3));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Processes the command from the server.
     *
     * @param command        the command sent by server
     * @param additionalData additional data sent, i.e. what cames after first 3 characters
     *                       meaning the command
     */
    protected abstract void processCommand(String command, String additionalData);
}
