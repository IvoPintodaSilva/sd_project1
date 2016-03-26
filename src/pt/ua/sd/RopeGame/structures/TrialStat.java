package pt.ua.sd.RopeGame.structures;

import pt.ua.sd.RopeGame.enums.WonType;

/**
 * Created by tiago and ivosilva on 25-03-2016.<br>
 *     <font size=4>This class represents the status of the trial in a certain moment</font>
 *
 *
 */
public class TrialStat {
    /**
     *  The {@link Boolean } instance tells if has(true) or not(false) one next trial in the current game
     */
    private boolean has_next_trial;

    /**
     *  The {@link int} represents the team id (1 or 2)
     */
    private int team;
    /**
     *  The {@link WonType } instance represents the type of the result in the trial
     */
    private WonType wonType;
    /**
     *  The {@link int } instance tells the center of the rope in the trial
     */
    private  int center_rope;


    /**
     *  Constructor
     */
    public TrialStat(boolean has_next_trial, int team, WonType wonType, int center_rope)
    {
        this.has_next_trial = has_next_trial;
        this.team = team;
        this.wonType = wonType;
        this.center_rope = center_rope;

    }

    /**
     * @return the {@link Boolean } instance that tells if has(true) or not(false) one next trial in the current game
     */
    public boolean isHas_next_trial() {
        return has_next_trial;
    }

    /**
     * @return the center of the rope
     */
    public int getCenter_rope() {
        return center_rope;
    }

    /**
     * @return the team id
     */
    public int getTeam() {
        return team;
    }

    /**
     * @return the {@link WonType } instance that represents the type of the result in the trial
     */
    public WonType getWonType() {
        return wonType;
    }

}
