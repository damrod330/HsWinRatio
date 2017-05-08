package com.a16mb.damrod.hearthstonewinratio;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_victory)
    Button btnVictory;

    @BindView(R.id.btn_defeat)
    Button btnDefeat;

    @BindView(R.id.deck_name_text_view)
    TextView deckNameTextView;

    @BindView(R.id.games_won_text_view)
    TextView gamesWonTextView;

    @BindView(R.id.games_lost_text_view)
    TextView gamesLostTextView;

    @BindView(R.id.win_ratio_text_view)
    TextView winRatioTextView;

    @BindView(R.id.navbar_linear_layout)
    LinearLayout navbarLinearLayout;

    @BindView(R.id.body_linear_layout)
    LinearLayout bodyLinearLayout;

    @BindView(R.id.relative_layout)
    RelativeLayout relativeLayout;

    private DatabaseHelper myDb;
    private List<deck> deckList;
    private deck currentDeck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        //drop table
        //myDb = new DatabaseHelper(this);
        //myDb.dropTable();

        myDb = new DatabaseHelper(this);
        //test only
        //myDb.removeDeck("test");
        //myDb.createDeck("Hunter", 0, 0, R.drawable.hunter_4);

        initialSetup();

        deckNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete deck " + currentDeck.getDeckName())
                        .setMessage("Are you sure you want to delete this deck?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                myDb.removeDeck(currentDeck.getDeckName());
                                initialSetup();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(R.drawable.icon)
                        .show();
            }
        });

        btnVictory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.updateDeck(currentDeck.getDeckName(), currentDeck.getVictoryCount() + 1, currentDeck.getLostCount());
                currentDeck.setVictoryCount(currentDeck.getVictoryCount() + 1);
                loadDataToViews(currentDeck);
            }
        });

        btnDefeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.updateDeck(currentDeck.getDeckName(), currentDeck.getVictoryCount(), currentDeck.getLostCount() + 1);
                currentDeck.setLostCount(currentDeck.getLostCount() + 1);
                loadDataToViews(currentDeck);
            }
        });
    }

    public void createDeckButtons(List<deck> decksList) {
        clearNavLinearLayout();
        for (final deck deck : decksList) {
            ImageButton btn = new ImageButton((new ContextThemeWrapper(this, R.style.nav_button)), null, 0);
            btn.setImageDrawable(getDrawable(deck.getIcon_id()));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(convertPixelsToDp(55), convertPixelsToDp(55));
            params.setMargins(convertPixelsToDp(4), convertPixelsToDp(4), convertPixelsToDp(4), convertPixelsToDp(4));
            btn.setLayoutParams(params);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentDeck = deck;
                    loadDataToViews(currentDeck);
                }
            });
            navbarLinearLayout.addView(btn);
        }
    }

    private void clearNavLinearLayout() {
        navbarLinearLayout.removeAllViewsInLayout();
        ImageButton btn = new ImageButton((new ContextThemeWrapper(this, R.style.nav_button)), null, 0);
        btn.setImageDrawable(getDrawable(R.drawable.add_new));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(convertPixelsToDp(55), convertPixelsToDp(55));
        params.setMargins(convertPixelsToDp(4), convertPixelsToDp(4), convertPixelsToDp(4), convertPixelsToDp(4));
        btn.setLayoutParams(params);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText deckNameEt = new EditText(MainActivity.this);
                deckNameEt.setHint("Deck name");

                final Spinner selectClassSpinner = new Spinner(MainActivity.this);
                String[] arraySpinner =
                        {"Mage",
                        "Hunter",
                        "Warlock",
                        "Warrior",
                        "Druid",
                        "Priest",
                        "Paladin",
                        "Shaman",
                        "Rogue"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_spinner_item, arraySpinner);
                selectClassSpinner.setAdapter(adapter);
                //selectClassSpinner.setPadding(convertPixelsToDp(0),convertPixelsToDp(0),convertPixelsToDp(0), convertPixelsToDp(0));

                LinearLayout linearLayout = new LinearLayout(MainActivity.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(deckNameEt);
                linearLayout.addView(selectClassSpinner);
                linearLayout.setPadding(convertPixelsToDp(24),convertPixelsToDp(8),convertPixelsToDp(24), convertPixelsToDp(8));


                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Create new deck ")
                        .setView(linearLayout)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (deckNameEt.getText().length() >= 4 && deckNameEt.getText().length() <= 22) {
                                    myDb.createDeck(deckNameEt.getText().toString(), 0, 0, getImage(selectClassSpinner.getSelectedItemPosition()));
                                    initialSetup();
                                } else {
                                    Toast.makeText(MainActivity.this, "invalid deck name", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(R.drawable.icon)
                        .show();
            }
        });
        navbarLinearLayout.addView(btn);
    }

    private int getImage(int s) {

        int[] iconsIds = {
                R.drawable.mage_13,
                R.drawable.hunter_4,
                R.drawable.warlock_21,
                R.drawable.warrior_11,
                R.drawable.druid_22,
                R.drawable.priest_12,
                R.drawable.paladin_10,
                R.drawable.shaman_5,
                R.drawable.rogue_8
        };

        return iconsIds[s];
    }

    private void loadDataToViews(deck deck) {
        deckNameTextView.setText(deck.getDeckName());
        gamesWonTextView.setText(Integer.toString(deck.getVictoryCount()));
        gamesLostTextView.setText(Integer.toString(deck.getLostCount()));
        winRatioTextView.setText(calculateWinRatin(deck.getVictoryCount(), deck.getLostCount()));
    }

    private String calculateWinRatin(int winCount, int lostCount) {
        if (winCount + lostCount != 0) {
            String winRatio = Integer.toString((winCount * 100 / (winCount + lostCount)));
            return winRatio + "%";
        } else {
            return "none";
        }
    }

    private int convertPixelsToDp(int pixels) {
        return Math.round(getResources().getDisplayMetrics().density * pixels);
    }

    private void initialSetup() {
        deckList = myDb.getDeckList();
        if (!deckList.isEmpty()) {
            bodyLinearLayout.setVisibility(View.VISIBLE);
            currentDeck = deckList.get(0);
            createDeckButtons(deckList);
            loadDataToViews(currentDeck);
        } else {
            bodyLinearLayout.setVisibility(View.INVISIBLE);
            clearNavLinearLayout();
        }
    }
}
