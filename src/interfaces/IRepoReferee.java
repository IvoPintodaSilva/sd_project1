package interfaces;

import enums.RefState;
import enums.WonType;

/**
 * Created by tiago on 21-03-2016.
 */
public interface IRepoReferee {
    void refereeLog(RefState state, int trial_number);
    void Addheader(boolean first);
    void setResult(int team_id, WonType wonType, int nr_trials);
    void printMatchResult();
    void updGame_nr();
    void updtRopeCenter(int center);
}
