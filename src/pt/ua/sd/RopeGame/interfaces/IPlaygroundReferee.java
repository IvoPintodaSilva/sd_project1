package pt.ua.sd.RopeGame.interfaces;

import pt.ua.sd.RopeGame.structures.TrialStat;

/**
 * Created by ivosilva on 07/03/16.
 */
public interface IPlaygroundReferee {

    TrialStat assertTrialDecision(int n_players_pushing, int knockDif);

}
