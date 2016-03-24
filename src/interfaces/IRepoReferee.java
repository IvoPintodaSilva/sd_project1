package interfaces;

import enums.RefState;

/**
 * Created by tiago on 21-03-2016.
 */
public interface IRepoReferee {
    void refereeLog(RefState state, int trial_number);
}
