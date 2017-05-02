package com.a16mb.damrod.hearthstonewinratio;

/**
 * Created by Damrod on 03.05.2017.
 */

public class deck {
    private String deckName;
    private int victoryCount;
    private int lostCount;

    public void setIcon_id(int icon_id) {
        this.icon_id = icon_id;
    }

    private int icon_id;

    public deck(String deckName, int victoryCount, int lostCount, int icon_id) {
        this.deckName = deckName;
        this.victoryCount = victoryCount;
        this.lostCount = lostCount;
        this.icon_id = icon_id;
    }

    public String getDeckName() {
        return deckName;
    }

    public int getVictoryCount() {
        return victoryCount;
    }

    public int getLostCount() {
        return lostCount;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public void setVictoryCount(int victoryCount) {
        this.victoryCount = victoryCount;
    }

    public void setLostCount(int lostCount) {
        this.lostCount = lostCount;
    }

    public int getIcon_id() {
        return icon_id;
    }
}
