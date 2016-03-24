package interfaces;

import enums.RefState;

/**
 * Created by tiago on 21-03-2016.
 */
public interface IRepoReferee {
    void refereeLog(RefState state, int trial_number);
    void Addheader(boolean first);
    void printResult(int team_id, String wonType, int nr_trials);
    void printMatchResult();
}
