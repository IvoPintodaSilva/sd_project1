package pt.ua.sd.RopeGame.interfaces;

/**
 * Created by ivosilva on 07/03/16.
 */
public interface IContestantsBenchCoach {
    boolean callContestants(int team_id,int[] selected_contestants);

    void informReferee();
}
