package pl.krakow.vlo.ui.screens;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;

import pl.krakow.vlo.R;
import pl.krakow.vlo.jpks.JpksClient;
import pl.krakow.vlo.jpks.JpksCommandListener;
import pl.krakow.vlo.ui.EmptyTabContentFactory;

/**
 * Created by m4tx3 on 5/3/14.
 */
public class JpksScreen extends Fragment implements Screen, JpksCommandListener, TabHost
        .OnTabChangeListener {
    private static final String TAG_GAME = "game";
    private static final String TAG_TOP10 = "top10";

    class JpksPagerAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = createGameView(container);
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return true;
        }
    }

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

        TabHost tabHost = (TabHost) view.findViewById(R.id.tabHost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec(TAG_GAME).setIndicator("Game").setContent(new
                EmptyTabContentFactory(getActivity())));
        tabHost.addTab(tabHost.newTabSpec(TAG_TOP10).setIndicator("TOP10").setContent(new
                EmptyTabContentFactory(getActivity())));
        tabHost.setOnTabChangedListener(this);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new JpksPagerAdapter());



        return view;
    }

    private View createGameView(ViewGroup container) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_jpks_game,
                container, false);

        final ImageButton sendButton = (ImageButton) view.findViewById(R.id.sendButton);
        sendButton.setEnabled(false);
        final EditText messageEditText = (EditText) view.findViewById(R.id.messageEditText);
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sendButton.setEnabled(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JpksClient.getInstance().sendAnswer(messageEditText.getText().toString());
                messageEditText.setText("");
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (JpksClient.getInstance() == null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... arg) {
                    new JpksClient("android", getActivity()).setCommandListener(JpksScreen.this);
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

    private void setQuestionText(String text) {
        TextView tv = (TextView) getView().findViewById(R.id.questionTextView);
        tv.setText(text);
    }

    private void setImage(Bitmap image) {
        ImageView view = (ImageView) getView().findViewById(R.id.imageView);
        view.setImageBitmap(image);
    }

    private void setMessages(String messages) {
        TextView tv = (TextView) getView().findViewById(R.id.messageTextView);
        tv.setText(messages);

        ScrollView tvScrollView = (ScrollView) getView().findViewById(R.id.messageScrollView);
        tvScrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }

    @Override
    public void onClearQuestion() {
        setQuestionText("");
    }

    @Override
    public void onQuestion(String question, Bitmap image) {
        setQuestionText(question);
        setImage(image);
    }

    @Override
    public void onCorrectAnswer(String answer) {
        setQuestionText(answer);
    }

    @Override
    public void onMessage(String messages) {
        setMessages(messages);
    }

    @Override
    public void onCount(int countVal) {
        ProgressBar progress = (ProgressBar) getView().findViewById(R.id.questionProgress);
        progress.setProgress((10 - countVal) * 10);
    }

    @Override
    public void onClearRanking() {

    }

    @Override
    public void onAppendToRanking(String ranking) {

    }

    @Override
    public void onPointGot(String user) {
        //appendToMessages(user + " to ciota");
    }

    @Override
    public void onRepaint() {

    }

    @Override
    public void onTabChanged(String s) {

    }
}
