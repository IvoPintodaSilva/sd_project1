package pt.ua.sd.RopeGame.structures;

import pt.ua.sd.RopeGame.enums.WonType;

/**
 * Created by tiago and ivosilva on 25-03-2016.<br>
 *     <font size=4>This class represents the status of the game in a certain moment</font>
 *
 *
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

    /**
     *
     * @return the {@link Boolean } instance that tells if has(true) or not(false) one next game in the current match
     */
    public boolean isHas_next_game() {
        return has_next_game;
    }

    /**
     *
     * @return the {@link Integer} team id
     */
    public int getWinnerTeam() {
        return team;
    }

    /**
     *
     * @return the {@link WonType} type of victory in the game
     */
    public WonType getWonType() {
        return wonType;
    }
}
