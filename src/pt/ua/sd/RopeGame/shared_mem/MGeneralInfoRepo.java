package pt.ua.sd.RopeGame.shared_mem;

import pt.ua.sd.RopeGame.enums.CoachState;
import pt.ua.sd.RopeGame.enums.ContestantState;
import pt.ua.sd.RopeGame.enums.RefState;
import pt.ua.sd.RopeGame.enums.WonType;
import pt.ua.sd.RopeGame.interfaces.IRepoCoach;
import pt.ua.sd.RopeGame.interfaces.IRepoContestant;
import pt.ua.sd.RopeGame.interfaces.IRepoReferee;

import java.io.*;
import java.nio.file.*;

/**
 * Created by ivosilva on 22/02/16.
 */
public class MGeneralInfoRepo implements IRepoCoach, IRepoContestant, IRepoReferee{

    private int referee_trial_number = 0;
    private static int PS_center = Integer.MAX_VALUE;//position of the center of rope

    public enum refStates{
        SOM,SOG,TSR,WTC,EOM,EOG,NON
    };
    public enum  coachStates{
        WRC,AST,WTR,NON
    };
    public enum contestantStates{
        SAB,SIP,DYB,NON
    };

    private static refStates referee_state;
    private static coachStates[] coach_state;
    private static contestantStates[] team1_state;
    private static contestantStates[] team2_state;
    private static int[] team1_strength;
    private static int[] team2_strength;
    private static int game_nr;
    private static int score_t1;
    private static int score_t2;
    private static File OUTPUT_FILE;
    private String TO_WRITE="";
    private static String LOG_LOCATION;
    private static Writer output=null;

    private static int[] contestants_team1 = {-1, -1, -1};
    private static int[] contestants_team2 = {-1, -1, -1};

    public MGeneralInfoRepo()
    {
        LOG_LOCATION = "RopeGame.log";
        TO_WRITE="";
        OUTPUT_FILE = new File(LOG_LOCATION);
        if(OUTPUT_FILE.exists())
        {
            deleteFile();
        }
        game_nr = 0;
        score_t1=0;
        score_t2=0;
        referee_state = refStates.NON;
        coach_state = new coachStates[2];
        for (int i=0;i<coach_state.length;i++) {
            coach_state[i]= coachStates.NON;
        }

        team1_state = new contestantStates[5];
        team2_state = new contestantStates[5];
        for (int i=0;i<team1_state.length;i++) {
            team1_state[i]= contestantStates.NON;
            team2_state[i]= contestantStates.NON;
        }

        team1_strength = new int[5];
        team2_strength = new int[5];
        for (int i=0;i<team1_strength.length;i++) {
            team1_strength[i] = 0;
            team2_strength[i] = 0;
        }

        Addheader(true);
    }

    public synchronized void Addheader(boolean first)
    {
        String temp="";//temporary string

        if(first) {
            temp="                               Game of the Rope - Description of the internal state" +
                    "\n\n" +
                    "Ref Coa 1 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5 Coa 2 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5     Trial    \n" +
                    "Sta  Stat Sta SG Sta SG Sta SG Sta SG Sta SG  Stat Sta SG Sta SG Sta SG Sta SG Sta SG 3 2 1 . 1 2 3 NB PS\n";
            //System.out.printf(temp);
            TO_WRITE += temp;
        }
        else {
           // game_nr +=1;
            temp = "Game " + game_nr +
                    " \nRef Coa 1 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5 Coa 2 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5     Trial    \n" +
                    "Sta  Stat Sta SG Sta SG Sta SG Sta SG Sta SG  Stat Sta SG Sta SG Sta SG Sta SG Sta SG 3 2 1 . 1 2 3 NB PS\n";
            //System.out.printf(temp);
            TO_WRITE += temp;
        }
    }
    @Override
    public synchronized void coachLog(int team_id, CoachState state) {
        switch (state){
            case WAIT_FOR_REFEREE_COMMAND:
                this.coach_state[team_id - 1] = coachStates.WRC;
                break;
            case ASSEMBLE_TEAM:
                this.coach_state[team_id - 1] = coachStates.AST;
                break;
            case WATCH_TRIAL:
                this.coach_state[team_id - 1] = coachStates.WTR;
                break;
        }

        printStates();

    }

    private synchronized  void gameWinnerLog(int teamId)
    {
        if(teamId == 1)
            this.score_t1 +=1;
        else if(teamId == 2)
            this.score_t2 +=1;
        else
        {
            this.score_t2 +=1;
            this.score_t1 +=1;}
    }

    public synchronized void updGame_nr()
    {
        game_nr +=1;
    }



    /**
     * imprime resultado do jogo ou da partida, ou de ambos
     * @param team_id
     * @param wonType knock out, draw or points
     * @param nr_trials
     */
    public synchronized void setResult(int team_id, WonType wonType, int nr_trials)
    {
        String temp="";

                if(wonType == WonType.KNOCKOUT)
                {
                    gameWinnerLog(team_id);
                    temp = "Game "+ game_nr+" was won by team "+team_id +" by knock out in "+ nr_trials +" trials.\n";
                }
                else if(wonType == WonType.DRAW)
                {
                    gameWinnerLog(3);
                    temp = "Game "+game_nr+" was a draw.\n";
                }
                else if(wonType == WonType.POINTS)
                {
                    gameWinnerLog(team_id);
                    temp = "Game "+game_nr+" was won by team "+team_id+" by points.\n";

                }

        TO_WRITE += temp;
        writeToFile();
    }

    public synchronized void printMatchResult(int winner,int score1, int score2)
    {
        String temp="";
        if(score1 != score2)
        {
            temp = "Match was won by team"+ winner+" ("+score1+"-"+score2+").\n";
        }
        else
        {
            temp="Match was draw.\n";
        }
        TO_WRITE += temp;
        writeToFile();
    }

    @Override
    public synchronized void refereeLog(RefState state, int trial_number) {
        // START_OF_THE_MATCH, START_OF_A_GAME, TEAMS_READY, WAIT_FOR_TRIAL_CONCLUSION, END_OF_A_GAME, END_OF_A_MATCH
        switch (state){
            case START_OF_THE_MATCH:
                this.referee_state = refStates.SOM;
                this.referee_trial_number = 0;
                break;
            case START_OF_A_GAME:
                this.referee_state = refStates.SOG;
                this.referee_trial_number = trial_number;
                break;
            case TEAMS_READY:
                this.referee_state = refStates.TSR;
                this.referee_trial_number = trial_number;
                break;
            case WAIT_FOR_TRIAL_CONCLUSION:
                this.referee_state = refStates.WTC;
                this.referee_trial_number = trial_number;
                break;
            case END_OF_A_GAME:
                this.referee_state = refStates.EOG;
                this.referee_trial_number = trial_number;
                break;
            case END_OF_A_MATCH:
                this.referee_state = refStates.EOM;
                this.referee_trial_number = trial_number;
                break;
        }

        printStates();
        writeToFile();
//        if(state== RefState.END_OF_A_GAME){
//            writeToFile(true);
//        }
    }

    @Override
    public synchronized void contestantLog(int id, int team_id, int strength, ContestantState state) {
        //    SEAT_AT_THE_BENCH, STAND_IN_POSITION, DO_YOUR_BEST, START
        switch (state){
            case SEAT_AT_THE_BENCH:
                if(team_id == 1){
                    this.team1_state[id] = contestantStates.SAB;
                    this.team1_strength[id] = strength;

                    if(this.contestants_team1[0] == id){
                        this.contestants_team1[0] = -1;
                    }else if(this.contestants_team1[1] == id){
                        this.contestants_team1[1] = -1;
                    }else if(this.contestants_team1[2] == id){
                        this.contestants_team1[2] = -1;
                    }
                }else if (team_id == 2){
                    this.team2_state[id] = contestantStates.SAB;
                    this.team2_strength[id] = strength;

                    if(this.contestants_team2[0] == id){
                        this.contestants_team2[0] = -1;
                    }else if(this.contestants_team2[1] == id){
                        this.contestants_team2[1] = -1;
                    }else if(this.contestants_team2[2] == id){
                        this.contestants_team2[2] = -1;
                    }
                }

                break;
            case STAND_IN_POSITION:
                if(team_id == 1){
                    this.team1_state[id] = contestantStates.SIP;
                    this.team1_strength[id] = strength;

                    if(this.contestants_team1[0] == -1){
                        this.contestants_team1[0] = id;
                    }else if(this.contestants_team1[1] == -1){
                        this.contestants_team1[1] = id;
                    }else if(this.contestants_team1[2] == -1){
                        this.contestants_team1[2] = id;
                    }
                }else if (team_id == 2){
                    this.team2_state[id] = contestantStates.SIP;
                    this.team2_strength[id] = strength;

                    if(this.contestants_team2[0] == -1){
                        this.contestants_team2[0] = id;
                    }else if(this.contestants_team2[1] == -1){
                        this.contestants_team2[1] = id;
                    }else if(this.contestants_team2[2] == -1){
                        this.contestants_team2[2] = id;
                    }
                }
                break;
            case DO_YOUR_BEST:
                if(team_id == 1){
                    this.team1_state[id] = contestantStates.DYB;
                    this.team1_strength[id] = strength;


                }else if (team_id == 2){
                    this.team2_state[id] = contestantStates.DYB;
                    this.team2_strength[id] = strength;


                }
                break;
        }

        printStates();
        writeToFile();
    }


    public synchronized void printStates(){
        // Ref Coa 1 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5 Coa 2 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5 Trial
        // Sta Stat Sta SG Sta SG Sta SG Sta SG Sta SG Stat Sta SG Sta SG Sta SG Sta SG Sta SG 3 2 1 . 1 2 3 NB PS

        TO_WRITE += String.format("%s   %s %s %02d %s %02d %s %02d %s %02d %s %02d   %s %s %02d %s %02d %s %02d %s %02d %s %02d %s %s %s . %s %s %s %s %s\n",
                referee_state,
                coach_state[0],
                team1_state[0],
                team1_strength[0],
                team1_state[1],
                team1_strength[1],
                team1_state[2],
                team1_strength[2],
                team1_state[3],
                team1_strength[3],
                team1_state[4],
                team1_strength[4],
                coach_state[1],
                team2_state[0],
                team2_strength[0],
                team2_state[1],
                team2_strength[1],
                team2_state[2],
                team2_strength[2],
                team2_state[3],
                team2_strength[3],
                team2_state[4],
                team2_strength[4],
                (contestants_team1[2] != -1) ? String.format("%01d", contestants_team1[2]+1) : "-",
                (contestants_team1[1] != -1) ? String.format("%01d", contestants_team1[1]+1) : "-",
                (contestants_team1[0] != -1) ? String.format("%01d", contestants_team1[0]+1) : "-",
                (contestants_team2[0] != -1) ? String.format("%01d", contestants_team2[0]+1) : "-",
                (contestants_team2[1] != -1) ? String.format("%01d", contestants_team2[1]+1) : "-",
                (contestants_team2[2] != -1) ? String.format("%01d", contestants_team2[2]+1) : "-",
                (referee_trial_number != 0) ? String.format("%02d", referee_trial_number) : "--",
                (PS_center != Integer.MAX_VALUE) ? String.format("%02d", PS_center) : "--");


        System.out.println(String.format("%s   %s %s %02d %s %02d %s %02d %s %02d %s %02d   %s %s %02d %s %02d %s %02d %s %02d %s %02d %s %s %s . %s %s %s %s %s",
                referee_state,
                coach_state[0],
                team1_state[0],
                team1_strength[0],
                team1_state[1],
                team1_strength[1],
                team1_state[2],
                team1_strength[2],
                team1_state[3],
                team1_strength[3],
                team1_state[4],
                team1_strength[4],
                coach_state[1],
                team2_state[0],
                team2_strength[0],
                team2_state[1],
                team2_strength[1],
                team2_state[2],
                team2_strength[2],
                team2_state[3],
                team2_strength[3],
                team2_state[4],
                team2_strength[4],
                (contestants_team1[2] != -1) ? String.format("%01d", contestants_team1[2]+1) : "-",
                (contestants_team1[1] != -1) ? String.format("%01d", contestants_team1[1]+1) : "-",
                (contestants_team1[0] != -1) ? String.format("%01d", contestants_team1[0]+1) : "-",
                (contestants_team2[0] != -1) ? String.format("%01d", contestants_team2[0]+1) : "-",
                (contestants_team2[1] != -1) ? String.format("%01d", contestants_team2[1]+1) : "-",
                (contestants_team2[2] != -1) ? String.format("%01d", contestants_team2[2]+1) : "-",
                (referee_trial_number != 0) ? String.format("%02d", referee_trial_number) : "--",
                (PS_center != Integer.MAX_VALUE) ? String.format("%02d", PS_center) : "--"));
    }


    public synchronized void updtRopeCenter(int new_val)
    {
//        if(PS_center == Integer.MAX_VALUE)
//        {
//            PS_center=0;
//        }
        PS_center = new_val;

    }

    public synchronized void writeToFile(){
        //use buffering

        try {
            output = new BufferedWriter(new FileWriter(OUTPUT_FILE,true));
            //FileWriter always assumes default encoding is OK!
            output.write(TO_WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) try {
                output.close();
                TO_WRITE="";
            } catch (IOException ignore) {}
        }
    }

    public void deleteFile()
    {
        try {
            Files.delete(OUTPUT_FILE.toPath());
        } catch (NoSuchFileException x) {
            System.err.format("%s: no such" + " file or directory%n", OUTPUT_FILE.toPath());
        } catch (DirectoryNotEmptyException x) {
            System.err.format("%s not empty%n", OUTPUT_FILE.toPath());
        } catch (IOException x) {
            // File permission problems are caught here.
            System.err.println(x);
        }
    }


}