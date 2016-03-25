package pt.ua.sd.RopeGame.interfaces;

import pt.ua.sd.RopeGame.enums.ContestantState;

/**
 * Created by tiago on 21-03-2016.
 */
public interface IRepoContestant {
    void contestantLog(int id, int team_id, int strength, ContestantState state);
    void updtRopeCenter(int new_val);
}
