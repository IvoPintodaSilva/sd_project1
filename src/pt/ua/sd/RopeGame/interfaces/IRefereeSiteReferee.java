package pt.ua.sd.RopeGame.interfaces;

import pt.ua.sd.RopeGame.structures.GameStat;

/**
 * Created by ivosilva on 07/03/16.
 */
public interface IRefereeSiteReferee {
    void announceNewGame();

    GameStat declareGameWinner(int score_T1, int score_T2, int knock_out, int n_games);

    int getN_games_played();
}
