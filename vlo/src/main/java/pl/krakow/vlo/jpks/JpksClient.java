package pl.krakow.vlo.jpks;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by m4tx3 on 5/4/14.
 */
public class JpksClient extends BaseJpksClient {
    public static class RankingItem {
        private final int position;
        private final String username;
        private final int points;

        public RankingItem(int position, String username, int points) {
            this.position = position;
            this.username = username;
            this.points = points;
        }

        public int getPosition() {
            return position;
        }

        public String getUsername() {
            return username;
        }

        public int getPoints() {
            return points;
        }
    }

    private static final String LOGGER_TAG = "JpksClient";
    /**
     * The regexp that is used to get informations from Ranking lines. First group means the
     * position of the player, the second is their nickname and the last one is amount of points.
     * <p/>
     * Example string: "  1. android@213 ................ 0". Groups:<br />
     * <ol>
     * <li>1</li>
     * <li>android@213</li>
     * <li>0</li>
     * </ol>
     */
    private static final Pattern RANKING_PATTERN = Pattern.compile(
            "\\s*(\\d+)\\. (.+) \\.* (\\d+)");

    /**
     * Instance of the client that is kept even when the JPKS activity/fragment is closed.
     */
    private static JpksClient instance;

    private Activity activity;
    private JpksCommandListener commandListener;
    private ImageDownloadTask imageDownloadTask;

    private String question;
    private String correctAnswer;
    private String messages = "";
    private ArrayList<RankingItem> ranking = new ArrayList<>();
    private int counter;
    private Bitmap image;
    private Bitmap nextImage;

    /**
     * @param activity the Activity this client is created in
     */
    public JpksClient(Activity activity) {
        this.activity = activity;
        instance = this;
    }

    /**
     * Sets a {@link JpksCommandListener} that is notified about every command
     * sent from the server.
     *
     * @param commandListener {@link JpksCommandListener} to set
     */
    public void setCommandListener(JpksCommandListener commandListener) {
        this.commandListener = commandListener;
    }

    public static JpksClient getInstance() {
        return instance;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getMessages() {
        return messages;
    }

    public ArrayList<RankingItem> getRanking() {
        return ranking;
    }

    public int getCounter() {
        return counter;
    }

    public Bitmap getImage() {
        return image;
    }

    @Override
    protected void processCommand(String command, final String additionalData) {
        switch (command) {
            case COMMAND_CLEAR:
                question = "";
                correctAnswer = "";
                if (commandListener != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (commandListener != null) {
                                commandListener.onClearQuestion();
                            }
                        }
                    });
                }
                break;
            case COMMAND_QUESTION:
                question = additionalData;
                correctAnswer = "";
                if (nextImage != null) {
                    image = nextImage;
                    nextImage = null;
                }
                if (commandListener != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            commandListener.onQuestion(additionalData, image);
                        }
                    });
                }
                break;
            case COMMAND_CORRECT_ANSWER:
                correctAnswer = additionalData;
                if (commandListener != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            commandListener.onCorrectAnswer(additionalData);
                        }
                    });
                }
                break;
            case COMMAND_MESSAGE:
                messages += additionalData + '\n';
                if (commandListener != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            commandListener.onMessage(messages);
                        }
                    });
                }
                break;
            case COMMAND_IMAGE:
                if (imageDownloadTask != null && imageDownloadTask.getStatus() == AsyncTask.Status
                        .RUNNING) {
                    imageDownloadTask.cancel(false);
                }
                imageDownloadTask = new ImageDownloadTask();
                imageDownloadTask.execute(additionalData);
                break;
            case COMMAND_COUNT:
                counter = Integer.parseInt(additionalData);
                if (commandListener != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            commandListener.onCount(counter);
                        }
                    });
                }
                break;
            case COMMAND_CLEAR_RANKING:
                ranking.clear();
                if (commandListener != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            commandListener.onClearRanking();
                        }
                    });
                }
                break;
            case COMMAND_APPEND_RANKING:
                Matcher matcher = RANKING_PATTERN.matcher(additionalData);
                boolean found = matcher.find();
                if (found) {
                    int position = Integer.parseInt(matcher.group(1));
                    String username = matcher.group(2);
                    int points = Integer.parseInt(matcher.group(3));
                    final RankingItem item = new RankingItem(position, username, points);
                    ranking.add(item);
                    if (commandListener != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                commandListener.onAppendToRanking(item);
                            }
                        });
                    }
                }
                break;
            case COMMAND_POINT_GOT:
                if (commandListener != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            commandListener.onPointGot(additionalData);
                        }
                    });
                }
                break;
            case COMMAND_REPAINT:
                if (commandListener != null) {
                    if (imageDownloadTask != null && imageDownloadTask.getStatus() == AsyncTask
                            .Status.RUNNING) {
                        imageDownloadTask.setShouldFireRepaint();
                    } else {
                        if (nextImage != null) {
                            image = nextImage;
                            nextImage = null;
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                commandListener.onRepaint(image);
                            }
                        });
                    }
                }
                break;
            default:
                Log.w(LOGGER_TAG, "Unrecognized response from server: \"" + command + "\"");
        }
    }

    private class ImageDownloadTask extends AsyncTask<String, Void, Void> {
        private volatile boolean shouldFireRepaint = false;

        @Override
        protected Void doInBackground(String... imgName) {
            HttpURLConnection urlConnection = null;
            Bitmap bitmap = null;
            try {
                URL url = new URL(IMAGE_URL_PREFIX + imgName[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                bitmap = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }

            if (shouldFireRepaint && commandListener != null) {
                JpksClient.this.image = bitmap;
                JpksClient.this.nextImage = null;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        commandListener.onRepaint(JpksClient.this.image);
                    }
                });
            } else {
                JpksClient.this.nextImage = bitmap;
            }
            return null;
        }

        public void setShouldFireRepaint() {
            shouldFireRepaint = true;
        }
    }
}
