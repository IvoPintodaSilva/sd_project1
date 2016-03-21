package shared_mem;

/**
 * Created by ivosilva on 22/02/16.
 */
public class MGeneralInfoRepo {
    public enum refStates{
        SOM,SOG,TSR,WTC,EOM,EOG,NONE
    };
    public enum  coachStates{
        WRC,AST,WTR,NONE
    };
    public enum contestantStates{
        SAB,SIP,DYB,NONE
    };
    private refStates referee_state;
    private coachStates[] coach_state;
    private contestantStates[] team1_state;
    private contestantStates[] team2_state;

    public MGeneralInfoRepo()
    {
        referee_state = refStates.NONE;
        coach_state = new coachStates[2];
        for (int i=0;i<coach_state.length;i++) {
            coach_state[i]= coachStates.NONE;
        }

        team1_state = new contestantStates[5];
        team2_state = new contestantStates[5];
        for (int i=0;i<team1_state.length;i++) {
            team1_state[i]= contestantStates.NONE;
            team2_state[i]= contestantStates.NONE;
        }
    }

}
