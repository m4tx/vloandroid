package pl.krakow.vlo.ui.screens;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
        .OnTabChangeListener, ViewPager.OnPageChangeListener {
    private static final String TAG_GAME = "game";
    private static final String TAG_RANKING = "ranking";
    private static final int GAME_POS = 0;
    private static final int RANKING_POS = 1;
    private static final String KEY_CURRENT_MESSAGE = "currentMessage";
    private static final String KEY_MESSAGE_EDIT_TEXT_STATE = "messageEditTextState";
    private static final String KEY_MESSAGE_VIEW_SCROLL = "messageViewScroll";
    private static final String KEY_RANKING_VIEW_SCROLL = "rankingViewScroll";

    private JpksClient jpksClient;
    private View gameView;
    private View rankingView;

    class JpksPagerAdapter extends PagerAdapter {
        private final Bundle savedInstanceState;

        JpksPagerAdapter(Bundle savedInstanceState) {
            this.savedInstanceState = savedInstanceState;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = null;
            switch (position) {
                case GAME_POS:
                    v = (gameView = createGameView(container, savedInstanceState));
                    break;
                case RANKING_POS:
                    v = (rankingView = createRankingView(container, savedInstanceState));
                    break;
            }
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
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
        tabHost.addTab(tabHost.newTabSpec(TAG_RANKING).setIndicator("TOP10").setContent(new
                EmptyTabContentFactory(getActivity())));
        tabHost.setOnTabChangedListener(this);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new JpksPagerAdapter(savedInstanceState));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(this);

        jpksClient.setCommandListener(JpksScreen.this);

        return view;
    }

    private View createGameView(ViewGroup container, Bundle savedInstanceState) {
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
        messageEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendMessage();
                    return true;
                }
                return false;
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        if (jpksClient.getImage() != null) {
            imageView.setImageBitmap(jpksClient.getImage());
        }
        TextView messageView = (TextView) view.findViewById(R.id.messageTextView);
        messageView.setText(jpksClient.getMessages());
        TextView questionView = (TextView) view.findViewById(R.id.questionTextView);
        questionView.setText(jpksClient.getQuestion());
        ProgressBar questionProgress = (ProgressBar) view.findViewById(R.id.questionProgress);
        questionProgress.setProgress((10 - jpksClient.getCounter()) * 10);

        if (savedInstanceState != null) {
            messageEditText.setText(savedInstanceState.getString(KEY_CURRENT_MESSAGE));
            messageEditText.onRestoreInstanceState(savedInstanceState.getParcelable
                    (KEY_MESSAGE_EDIT_TEXT_STATE));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                ScrollView messageScrollView = (ScrollView) view.findViewById(R.id
                        .messageScrollView);
                messageScrollView.setScrollY(savedInstanceState.getInt(KEY_MESSAGE_VIEW_SCROLL));
            }
        }

        return view;
    }

    private void sendMessage() {
        EditText messageEditText = (EditText) gameView.findViewById(R.id.messageEditText);
        if (messageEditText.getText().toString().length() > 0) {
            jpksClient.sendAnswer(messageEditText.getText().toString());
            messageEditText.setText("");
        }
    }

    private View createRankingView(ViewGroup container, Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_jpks_ranking,
                container, false);

        ListView lv = (ListView) view.findViewById(R.id.listView);
        lv.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return jpksClient.getRanking().size();
            }

            @Override
            public Object getItem(int i) {
                return jpksClient.getRanking().get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public boolean isEnabled(int position) {
                return false;
            }

            @Override
            public View getView(int position, View view, ViewGroup parent) {
                if (view == null) {
                    view = getActivity().getLayoutInflater().inflate(R.layout
                            .fragment_jpks_ranking_list_item, parent, false);
                }
                JpksClient.RankingItem item = (JpksClient.RankingItem) getItem(position);
                TextView positionView = (TextView) view.findViewById(R.id.userPosition);
                positionView.setText(item.getPosition() + ".");
                TextView usernameView = (TextView) view.findViewById(R.id.username);
                usernameView.setText(item.getUsername());
                TextView pointsView = (TextView) view.findViewById(R.id.pointsCount);
                pointsView.setText(Integer.toString(item.getPoints()));
                return view;
            }
        });
        if (savedInstanceState != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                lv.setScrollY(savedInstanceState.getInt(KEY_RANKING_VIEW_SCROLL));
            }
        }

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (JpksClient.getInstance() == null) {
            jpksClient = new JpksClient(getActivity());
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... arg) {
                    jpksClient.connect("android");
                    return null;
                }
            }.execute();
        } else {
            jpksClient = JpksClient.getInstance();
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        EditText messageEditText = (EditText) gameView.findViewById(R.id.messageEditText);
        outState.putString(KEY_CURRENT_MESSAGE, messageEditText.getText().toString());
        outState.putParcelable(KEY_MESSAGE_EDIT_TEXT_STATE, messageEditText.onSaveInstanceState());
        ScrollView messageScrollView = (ScrollView) gameView.findViewById(R.id.messageScrollView);
        outState.putInt(KEY_MESSAGE_VIEW_SCROLL, messageScrollView.getScrollY());
        ListView rankingListView = (ListView) rankingView.findViewById(R.id.listView);
        outState.putInt(KEY_RANKING_VIEW_SCROLL, rankingListView.getScrollY());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        jpksClient.setCommandListener(null);
        super.onDestroyView();
    }

    private void setQuestionText(String text) {
        TextView tv = (TextView) gameView.findViewById(R.id.questionTextView);
        tv.setText(text);
    }

    private void setImage(Bitmap image) {
        ImageView view = (ImageView) gameView.findViewById(R.id.imageView);
        view.setImageBitmap(image);
    }

    private void setMessages(String messages) {
        TextView tv = (TextView) gameView.findViewById(R.id.messageTextView);
        tv.setText(messages);

        ScrollView tvScrollView = (ScrollView) gameView.findViewById(R.id.messageScrollView);
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
        ProgressBar progress = (ProgressBar) gameView.findViewById(R.id.questionProgress);
        progress.setProgress((10 - countVal) * 10);
    }

    @Override
    public void onClearRanking() {

    }

    @Override
    public void onAppendToRanking(JpksClient.RankingItem item) {
        ListView lv = (ListView) rankingView.findViewById(R.id.listView);
        ((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onPointGot(String user) {
        //appendToMessages(user + " to ciota");
    }

    @Override
    public void onRepaint(Bitmap image) {
        setImage(image);
    }

    @Override
    public void onTabChanged(String s) {
        int pos = 0;
        switch (s) {
            case TAG_GAME:
                pos = 0;
                break;
            case TAG_RANKING:
                pos = 1;
                break;
        }
        ViewPager viewPager = (ViewPager) getView().findViewById(R.id.viewPager);
        viewPager.setCurrentItem(pos);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        TabHost tabHost = (TabHost) getView().findViewById(R.id.tabHost);
        tabHost.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
