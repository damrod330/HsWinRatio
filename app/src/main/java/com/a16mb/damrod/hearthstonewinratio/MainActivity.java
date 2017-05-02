package com.a16mb.damrod.hearthstonewinratio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_add_new)
    ImageButton btnAddNew;

    @BindView(R.id.deck_name_text_view)
    TextView deckNameTextView;

    @BindView(R.id.games_won_text_view)
    TextView gamesWonTextView;

    @BindView(R.id.games_lost_text_view)
    TextView gamesLostTextView;

    @BindView(R.id.win_ratio_text_view)
    TextView winRatioTextView;

    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this line is needet in oreder for injection to work
        ButterKnife.bind(this);

        myDb = new DatabaseHelper(this);
        myDb.insertData("freeze mage", 3,2,0);
    }

    void loadDataToViews(deck deck){
        deckNameTextView.setText(deck.getDeckName());
        gamesWonTextView.setText(Integer.toString(deck.getVictoryCount()));
        gamesLostTextView.setText(Integer.toString(deck.getLostCount()));

    }
}
