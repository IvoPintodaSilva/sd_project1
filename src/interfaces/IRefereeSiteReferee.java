package interfaces;

import structures.GameStat;
import structures.TrialStat;

/**
 * Created by ivosilva on 07/03/16.
 */
public interface IRefereeSiteReferee {
    void announceNewGame();

    GameStat declareGameWinner(int score_T1, int score_T2, int knock_out);

    int declareMatchWinner();

    int getN_games();

    int getN_games_played();
}
