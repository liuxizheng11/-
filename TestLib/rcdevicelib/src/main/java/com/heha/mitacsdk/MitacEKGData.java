package com.heha.mitacsdk;

/* for reference
public class MitacCPCEKG {
	private int id;
	private int ansAge;
	private int balance;
	private int energy;
	private int heartRate;
	private int stress;
	private boolean isLeadoff;
	private boolean isSuccess;
	private int goodCount;
	private int perfectCount;
	private int poorCount;
	private int matching;
	private int interval;
	private int score;
	private int catchUp ;
	private int[] finalRRInterval;
	private int qi;

	public static enum TrainingType {
		TRAINING_LEVEL_8(0),TRAINING_LEVEL_7(1),TRAINING_LEVEL_6(2);

		private int _value = 0;
		private TrainingType(int value) {
			this._value = value;
		}

		public int getValue() {
			return _value;
		}
	}


	private int id;
	private int ansAge;
	private int balance;
	private int energy;
	private int heartRate;
	private int stress;
	private boolean isLeadoff;
	private boolean isSuccess;
	private int rrInterval;
	private int[] finalRRInterval;
	private int qi;



 */

public class MitacEKGData
{
    public int mSuccessRatio = 0;
    public boolean mIsSuccess = false;
    public int mrrInterval = 0;
    public int mHeartRate = 0;
    public int mANSAge = 0;
    public int mEnergy = 0;
    public int mStress = 0;
    public int mBalance = 0;
    public int mScore = 0;
    public int mCatchUp = 0;
    public int mMatching = 0;
    public int mPerfectCount = 0;
    public int mGoodCount = 0;
    public int mPoorCount = 0;
    public boolean mIsLeadoff = false;
    public int mZ = 0;
    public int[] mFinalRRInterval = null;
    public int[] mFinalBeatTime = null;
    public int mQI = 0;

    public MitacEKGData(int rr_ii, int rrInterval, int instantHR, int sdnnSys, int pnnSys, int poincareSys, int symbolicSys, int meanHR, int cpcScore, int matchResult, int catchUp, int poor_count, int good_count, int perfect_count, int successRatio, boolean isSuccess, boolean isLeadoff, int Z, int[] final_RRInterval, int[] final_BeatTime, int QI)
    {
        this.mrrInterval = rrInterval;
        if (meanHR == 0) {
            this.mHeartRate = instantHR;
        } else {
            this.mHeartRate = meanHR;
        }
        this.mANSAge = sdnnSys;
        this.mEnergy = pnnSys;
        this.mBalance = poincareSys;
        this.mStress = symbolicSys;
        this.mScore = cpcScore;
        this.mCatchUp = catchUp;
        this.mMatching = matchResult;
        this.mPerfectCount = perfect_count;
        this.mGoodCount = good_count;
        this.mPoorCount = poor_count;
        this.mIsSuccess = isSuccess;
        this.mSuccessRatio = successRatio;
        this.mIsLeadoff = isLeadoff;
        this.mZ = Z;
        this.mFinalRRInterval = final_RRInterval;
        this.mFinalBeatTime = final_BeatTime;
        this.mQI = QI;
    }


}


