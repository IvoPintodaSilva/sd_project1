package pt.ua.sd.RopeGame;

import pt.ua.sd.RopeGame.active_entities.Coach;
import pt.ua.sd.RopeGame.active_entities.Contestant;
import pt.ua.sd.RopeGame.active_entities.Referee;
import pt.ua.sd.RopeGame.interfaces.*;
import pt.ua.sd.RopeGame.interfaces.*;
import pt.ua.sd.RopeGame.shared_mem.MContestantsBench;
import pt.ua.sd.RopeGame.shared_mem.MGeneralInfoRepo;
import pt.ua.sd.RopeGame.shared_mem.MPlayground;
import pt.ua.sd.RopeGame.shared_mem.MRefereeSite;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
/**
 * Main class of the project<br>
 *  Created by tiago and ivosilva on 25-03-2016.<br>
 * <b><center><font size=6>Main class of the project</font></center></b><br>
 *  <b><center><font size=5>This class represents the main class of the project, is in this class that the shared memories are created and the threads launched.</font></center></b><br>
 */
public class RopeGame {
    public static void main(String[] args) {

        MRefereeSite refereeSite = new MRefereeSite();
        MPlayground playground = new MPlayground();
        MContestantsBench bench = new MContestantsBench();
        MGeneralInfoRepo repo = new MGeneralInfoRepo();

        int players_team=0;
        int players_pushing=0;
        int n_trials=0;
        int n_games=0;

        try {
            Properties p = new Properties();
            System.out.println("Settings loaded from: " + System.getProperty("user.dir") + "/rg.config");
            p.load(new FileInputStream( System.getProperty("user.dir") + "/rg.config"));

            players_team = Integer.parseInt(p.getProperty("N_PLAYERS_TEAM"));
            players_pushing = Integer.parseInt(p.getProperty("N_PLAYERS_PUSHING"));
            n_trials = Integer.parseInt(p.getProperty("N_TRIALS"));
            n_games = Integer.parseInt(p.getProperty("N_GAMES"));


        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

        Coach coach_team1 = new Coach(1, 1,
                (IPlaygroundCoach) playground,
                (IRefereeSiteCoach) refereeSite,
                (IContestantsBenchCoach) bench,
                (IRepoCoach) repo,
                players_team,
                players_pushing,
                n_trials,
                n_games
        );
        Coach coach_team2 = new Coach(2, 2,
                (IPlaygroundCoach) playground,
                (IRefereeSiteCoach) refereeSite,
                (IContestantsBenchCoach) bench,
                (IRepoCoach) repo,
                players_team,
                players_pushing,
                n_trials,
                n_games
        );

        Referee ref = new Referee(
                (IPlaygroundReferee) playground,
                (IRefereeSiteReferee) refereeSite,
                (IContestantsBenchReferee) bench,
                (IRepoReferee) repo,
                players_team,
                players_pushing,
                n_trials,
                n_games);


        Random rn = new Random();
        Contestant[] contestants_team1 = new Contestant[players_team];
        for(int i = 0; i < players_team; i++){
            contestants_team1[i] = new Contestant(i, 1, rn.nextInt(20 - 10 + 1) + 10,
                    (IPlaygroundContestant) playground,
                    (IRefereeSiteContestant) refereeSite,
                    (IContestantsBenchContestant) bench,
                    (IRepoContestant) repo,
                    players_team,
                    players_pushing,
                    n_trials,
                    n_games);
            contestants_team1[i].start();
        }

        Contestant[] contestants_team2 = new Contestant[players_team];
        for(int i = 0; i < players_team; i++){
            contestants_team2[i] = new Contestant(i, 2, rn.nextInt(20 - 10 + 1) + 10,
                    (IPlaygroundContestant) playground,
                    (IRefereeSiteContestant) refereeSite,
                    (IContestantsBenchContestant) bench,
                    (IRepoContestant) repo,
                    players_team,
                    players_pushing,
                    n_trials,
                    n_games);
            contestants_team2[i].start();
        }

        coach_team1.start();
        coach_team2.start();
        ref.start();

        /*  threads join  */

        try {
            coach_team1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            coach_team2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            ref.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < 5; i++){
            try {
                contestants_team1[i].join();
                contestants_team2[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
