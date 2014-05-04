package pl.krakow.vlo.jpks;

/**
 * The listener that listens for commands from server. Can be set to
 * {@link pl.krakow.vlo.jpks.JPKSClient} via the
 * {@link pl.krakow.vlo.jpks.JPKSClient#setCommandListener(JPKSCommandListener)} method.
 */
public interface JPKSCommandListener {
    /**
     * Called when new question should be displayed. Command:
     * <code>{@value pl.krakow.vlo.jpks.JPKSClient#COMMAND_QUESTION}</code>.
     *
     * @param question the question that should be displayed
     */
    void onQuestion(String question);

    /**
     * Called after timeout for answering the question. Contains the "template" correct answer.
     * Command: <code>{@value pl.krakow.vlo.jpks.JPKSClient#COMMAND_CORRECT_ANSWER}</code>.
     *
     * @param answer the correct answer
     */
    void onCorrectAnswer(String answer);

    /**
     * Called when new message should be displayed. The kind of the message may be various:
     * message from another player, "prepare for the next question" message,
     * category of the next question, etc. The command is
     * <code>{@value pl.krakow.vlo.jpks.JPKSClient#COMMAND_MESSAGE}</code>.
     *
     * @param message the message that should be displayed
     */
    void onMessage(String message);

    /**
     * Called when the name of the image that is going to be displayed is sent. Command:
     * <code>{@value pl.krakow.vlo.jpks.JPKSClient#COMMAND_IMAGE}</code>.
     * <p/>
     * <strong>Please note</strong> that calling this method does <em>not</em> mean that the
     * client should display the image immediately. It is called to allow for caching the image
     * earlier; the client should display the image after calling the {@link #onQuestion(String)}.
     *
     * @param imageURL filename of the image to download
     */
    void onImageSent(String imageURL);

    /**
     * Called after each tick of next-question-counter. Command:
     * <code>{@value pl.krakow.vlo.jpks.JPKSClient#COMMAND_COUNT}</code>.
     *
     * @param countVal current amount of seconds remaining to display the new question
     */
    void onCount(String countVal);

    /**
     * Called when the TOP10 ranking should be cleared. Calling this method is usually followed
     * by calling {@link #onAppendToRanking(String)} method multiple times,
     * generally. The command is
     * <code>{@value pl.krakow.vlo.jpks.JPKSClient#COMMAND_CLEAR_RANKING}</code>.
     */
    void onClearRanking();

    /**
     * Called when the new line of ranking is sent. Usually called multiple times at once.
     * Command: <code>{@value pl.krakow.vlo.jpks.JPKSClient#COMMAND_APPEND_RANKING}</code>.
     *
     * @param line the line to append
     */
    void onAppendToRanking(String line);

    /**
     * Called when some player gets the point. The command is
     * <code>{@value pl.krakow.vlo.jpks.JPKSClient#COMMAND_POINT_GOT}</code>.
     *
     * @param user the user that has got the point
     */
    void onPointGot(String user);

    /**
     * Called when the image is intended to be repainted (and made visible if it is not already).
     * The command is: <code>{@value pl.krakow.vlo.jpks.JPKSClient#COMMAND_REPAINT}</code>.
     * <p/>
     * <strong>Note: </strong> it seems like this command isn't really used.
     */
    void onRepaint();

    /**
     * Called when <code>{@value pl.krakow.vlo.jpks.JPKSClient#COMMAND_CLEAR}</code> command is
     * sent. The client is intended to clear the question field.
     * <p/>
     * <strong>Note: </strong> it seems like this command isn't really used.
     */
    void onClearQuestion();
}
