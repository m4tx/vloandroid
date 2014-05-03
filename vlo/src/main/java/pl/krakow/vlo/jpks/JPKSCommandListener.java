package pl.krakow.vlo.jpks;

/**
 * The listener that listens for commands from server. Can be set to
 * {@link pl.krakow.vlo.jpks.JPKSClient} via the
 * {@link pl.krakow.vlo.jpks.JPKSClient#setCommandListener(JPKSCommandListener)} method.
 */
public interface JPKSCommandListener {
    /**
     * Called when <code>"cle"</code> or <code>"rep"</code> command is sent. The client is
     * intended to
     * clear the question field.
     * <p/>
     * <strong>Note: </strong> it seems like this command isn't really used.
     */
    void onClearQuestion();

    /**
     * Called when new question should be displayed. Command: <code>"que"</code>.
     *
     * @param question the question that should be displayed
     */
    void onQuestion(String question);

    /**
     * Called when new message should be displayed. The kind of the message may be various:
     * message from another player, "prepare for the next question" message,
     * category of the next question, etc. The command is <code>"txt"</code>.
     *
     * @param message the message that should be displayed
     */
    void onMessage(String message);

    /**
     * Called after timeout for answering the question. Contains the "template" correct answer.
     * Command: <code>"lib"</code>;
     *
     * @param answer the correct answer
     */
    void onCorrectAnswer(String answer);

    /**
     * Called after each tick of next-question-counter. Command: "cnt".
     *
     * @param countVal current amount of seconds remaining to display the new question
     */
    void onCount(String countVal);

    /**
     * Called when the name of the image that is going to be displayed is sent. Command:
     * <code>"img"</code>.
     * <p/>
     * <strong>Please note</strong> that calling this method does <em>not</em> mean that the
     * client should display the image immediately. It is called to allow for caching the image
     * earlier; the client should display the image after calling the {@link #onQuestion(String)}.
     *
     * @param imageURL filename of the image to download
     */
    void onImageSent(String imageURL);

    /**
     * Called when the TOP10 ranking should be cleared. Calling this method is usually followed
     * by calling {@link #onAppendToRanking(String)} method multiple times,
     * generally. The command is <code>"rpr"</code>.
     */
    void onClearRanking();

    /**
     * Called when the new line of ranking is sent. Usually called multiple times at once.
     * Command: <code>"rnk"</code>.
     *
     * @param line the line to append
     */
    void onAppendToRanking(String line);

    /**
     * Called when some player gets the point. The command is <code>"pkt"</code>.
     *
     * @param user the user that has got the point
     */
    void onPointGot(String user);

    /**
     * Called when the image is intended to be repainted (and made visible if it is not already).
     * The command is: <code>"rep"</code>.
     * <p/>
     * <strong>Note: </strong> it seems like this command isn't really used.
     */
    void onRepaint();
}