package pt.ua.sd.RopeGame.interfaces;

import pt.ua.sd.RopeGame.enums.CoachState;

/**
 * Created by tiago on 21-03-2016.
 */
public interface IRepoCoach{
    void coachLog(int team_id, CoachState state);
}
