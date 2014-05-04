package pl.krakow.vlo.jpks;

import android.graphics.Bitmap;

/**
 * The listener that listens for commands from server. Can be set to
 * {@link JpksClient} via the
 * {@link pl.krakow.vlo.jpks.JpksClient#setCommandListener(JpksCommandListener)} method.
 */
public interface JpksCommandListener {
    /**
     * Called when <code>{@value BaseJpksClient#COMMAND_CLEAR}</code> command is
     * sent. The client is intended to clear the question field.
     */
    void onClearQuestion();

    /**
     * Called when new question (and image) should be displayed. Command:
     * <code>{@value BaseJpksClient#COMMAND_QUESTION}</code>.
     *
     * @param question the question that should be displayed
     * @param image    the image to show
     */
    void onQuestion(String question, Bitmap image);

    /**
     * Called after timeout for answering the question. Contains the "template" correct answer.
     * Command: <code>{@value BaseJpksClient#COMMAND_CORRECT_ANSWER}</code>.
     *
     * @param answer the correct answer
     */
    void onCorrectAnswer(String answer);

    /**
     * Called when new message should be displayed. The kind of the message may be various:
     * message from another player, "prepare for the next question" message,
     * category of the next question, etc. The command is
     * <code>{@value BaseJpksClient#COMMAND_MESSAGE}</code>.
     *
     * @param messages current full message log
     */
    void onMessage(String messages);

    /**
     * Called after each tick of next-question-counter. Command:
     * <code>{@value BaseJpksClient#COMMAND_COUNT}</code>.
     *
     * @param countVal current amount of seconds remaining to display the new question
     */
    void onCount(int countVal);

    /**
     * Called when the TOP10 ranking should be cleared. Calling this method is usually followed
     * by calling {@link #onAppendToRanking(String)} method multiple times,
     * generally. The command is
     * <code>{@value BaseJpksClient#COMMAND_CLEAR_RANKING}</code>.
     */
    void onClearRanking();

    /**
     * Called when the new line of ranking is sent. Usually called multiple times at once.
     * Command: <code>{@value BaseJpksClient#COMMAND_APPEND_RANKING}</code>.
     *
     * @param ranking current full ranking
     */
    void onAppendToRanking(String ranking);

    /**
     * Called when some player gets the point. The command is
     * <code>{@value BaseJpksClient#COMMAND_POINT_GOT}</code>.
     *
     * @param user the user that has got the point
     */
    void onPointGot(String user);

    /**
     * Called when the image is intended to be repainted (and made visible if it is not already).
     * The command is: <code>{@value BaseJpksClient#COMMAND_REPAINT}</code>.
     */
    void onRepaint();
}
