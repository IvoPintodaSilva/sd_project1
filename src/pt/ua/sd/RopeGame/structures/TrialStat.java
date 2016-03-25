package pt.ua.sd.RopeGame.structures;

import pt.ua.sd.RopeGame.enums.WonType;

/**
 * Created by tiago on 25-03-2016.
 */
public class TrialStat {
    private boolean has_next_trial;
    private int team;
    private WonType wonType;
    private  int center_rope;

    public TrialStat(boolean has_next_trial, int team, WonType wonType, int center_rope)
    {
        this.has_next_trial = has_next_trial;
        this.team = team;
        this.wonType = wonType;
        this.center_rope = center_rope;

    }


    public boolean isHas_next_trial() {
        return has_next_trial;
    }

    public int getCenter_rope() {
        return center_rope;
    }

    public int getTeam() {
        return team;
    }

    public WonType getWonType() {
        return wonType;
    }

}
