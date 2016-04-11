package pt.ua.sd.RopeGame.interfaces;

/**
 * Created by ivosilva on 07/03/16.
 */
public interface IPlaygroundContestant {

    void pullTheRope(int team_id, int strenght, int contestant_id);

    void iAmDone();

    void seatDown();
}
