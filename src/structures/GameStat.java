package structures;

import enums.WonType;

/**
 * Created by tiago on 25-03-2016.
 */
public class GameStat {
    private boolean has_next_game;
    private int team;
    private WonType wonType;

    public GameStat(boolean has_next_game, int team, WonType wonType)
    {
        this.has_next_game = has_next_game;
        this.team = team;
        this.wonType = wonType;

    }


    public boolean isHas_next_game() {
        return has_next_game;
    }

    public int getWinnerTeam() {
        return team;
    }

    public WonType getWonType() {
        return wonType;
    }
}
