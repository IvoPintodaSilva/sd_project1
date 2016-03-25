package pt.ua.sd.RopeGame.interfaces;

import pt.ua.sd.RopeGame.enums.RefState;
import pt.ua.sd.RopeGame.enums.WonType;

/**
 * Created by tiago on 21-03-2016.
 */
public interface IRepoReferee {
    void refereeLog(RefState state, int trial_number);
    void Addheader(boolean first);
    void setResult(int team_id, WonType wonType, int nr_trials);
    void printMatchResult(int winner, int score1, int score2);
    void updGame_nr();
    void updtRopeCenter(int center);
}
