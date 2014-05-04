package pl.krakow.vlo.ui.screens;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import pl.krakow.vlo.R;
import pl.krakow.vlo.jpks.JpksClient;
import pl.krakow.vlo.jpks.JpksCommandListener;

/**
 * Created by m4tx3 on 5/3/14.
 */
public class JpksScreen extends Fragment implements Screen, JpksCommandListener {
    @Override
    public int getNameResId() {
        return R.string.screen_jpks_title;
    }

    @Override
    public View getListItemView(LayoutInflater inflater, ViewGroup viewGroup) {
        return Screens.create2LineListItem(inflater, viewGroup, R.string.screen_jpks_title,
                R.string.screen_jpks_disconnected, R.drawable.ic_jpks);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jpks, container, false);
        TextView tv = (TextView) view.findViewById(R.id.messageTextView);
        tv.setMovementMethod(new ScrollingMovementMethod());
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (JpksClient.getInstance() == null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... arg) {
                    new JpksClient("android").setCommandListener(JpksScreen.this);
                    return null;
                }
            }.execute();
        } else {
            JpksClient.getInstance().setCommandListener(this);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        JpksClient client = JpksClient.getInstance();
        if (client != null) {
            client.setCommandListener(null);
        }
        super.onDestroy();
    }

    private void setQuestionText(final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView tv = (TextView) getView().findViewById(R.id.questionTextView);
                tv.setText(text);
            }
        });
    }

    private void appendToMessages(final String line) {
        if (line.length() == 0) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView tv = (TextView) getView().findViewById(R.id.messageTextView);
                tv.setText(tv.getText() + line + (line.charAt(line.length() - 1) == '\n' ? "" :
                        '\n'));
            }
        });
    }

    @Override
    public void onClearQuestion() {
        setQuestionText("");
    }

    @Override
    public void onQuestion(String question) {
        setQuestionText(question);
    }

    @Override
    public void onCorrectAnswer(String answer) {
        setQuestionText(answer);
    }

    @Override
    public void onMessage(String message) {
        appendToMessages(message);
    }

    @Override
    public void onImageSent(String imageURL) {
        imageURL = JpksClient.IMAGE_URL_PREFIX + imageURL;
        AsyncTask<String, Void, Bitmap> imageDownloadTask = new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... imgurl) {
                HttpURLConnection urlConnection = null;
                Bitmap bitmap = null;
                try {
                    URL url = new URL(imgurl[0]);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    bitmap = BitmapFactory.decodeStream(in);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }

                return bitmap;
            }

            @Override
            protected void onPostExecute(final Bitmap bitmap) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ImageView view = (ImageView) getView().findViewById(R.id.imageView);
                        view.setImageBitmap(bitmap);
                    }
                });
            }
        };
        imageDownloadTask.execute(imageURL);
    }

    @Override
    public void onCount(String countVal) {
        final int val = Integer.parseInt(countVal);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProgressBar progress = (ProgressBar) getView().findViewById(R.id.questionProgress);
                progress.setProgress((10 - val) * 10);
            }
        });
    }

    @Override
    public void onClearRanking() {

    }

    @Override
    public void onAppendToRanking(String line) {

    }

    @Override
    public void onPointGot(String user) {
        appendToMessages(user + " to ciota");
    }

    @Override
    public void onRepaint() {

    }
}
