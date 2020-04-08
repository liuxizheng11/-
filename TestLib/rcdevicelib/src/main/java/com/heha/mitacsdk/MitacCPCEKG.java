package com.heha.mitacsdk;

import java.io.Serializable;

public class MitacCPCEKG implements  Cloneable,Serializable {
	private int id;
	private int ansAge;
	private int balance;
	private int energy;
	private int heartRate;
	private int stress;
	private boolean leadoff;
	private boolean success;
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
	
//    int EKG_TRAINING_LEVEL_1 = 0;   // 8 times/min
//    int EKG_TRAINING_LEVEL_2 = 1;   // 7 times/min
//    int EKG_TRAINING_LEVEL_3 = 2;   // 6 times/min
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getAnsAge() {
		return ansAge;
	}

	public void setAnsAge(int ansAge) {
		this.ansAge = ansAge;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public int getHeartRate() {
		return heartRate;
	}

	public void setHeartRate(int heartRate) {
		this.heartRate = heartRate;
	}

	public int getStress() {
		return stress;
	}

	public void setStress(int stress) {
		this.stress = stress;
	}

	public boolean isLeadoff() {
		return leadoff;
	}

	public void setLeadoff(boolean isLeadoff) {
		this.leadoff = isLeadoff;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean isSuccess) {
		this.success = isSuccess;
	}

	public int getGoodCount() {
		return goodCount;
	}

	public void setGoodCount(int goodCount) {
		this.goodCount = goodCount;
	}

	public int getPerfectCount() {
		return perfectCount;
	}

	public void setPerfectCount(int perfectCount) {
		this.perfectCount = perfectCount;
	}

	public int getPoorCount() {
		return poorCount;
	}

	public void setPoorCount(int poorCount) {
		this.poorCount = poorCount;
	}

	public int getMatching() {
		return matching;
	}

	public void setMatching(int matching) {
		this.matching = matching;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getCatchUp() {
		return catchUp;
	}

	public void setCatchUp(int catchUp) {
		this.catchUp = catchUp;
	}
	
	public int[] getFinalRRInterval() {
		return finalRRInterval;
	}

	public void setFinalRRInterval(int[] finalRRInterval) {
		this.finalRRInterval = finalRRInterval;
	}

	public int getQi() {
		return qi;
	}

	public void setQi(int qi) {
		this.qi = qi;
	}


	public String toString(){
		String ret = "";

		ret +="id:"+this.id+"\n";
		ret +="ansAge:"+this.ansAge+"\n";
		ret +="balance:"+this.balance+"\n";
		ret +="energy:"+this.energy+"\n";
		ret +="heartRate:"+this.heartRate+"\n";
		ret +="stress:"+this.stress+"\n";
		ret +="leadoff:"+this.leadoff +"\n";
		ret +="success:"+this.success +"\n";
		ret +="goodCount:"+this.goodCount+"\n";
		ret +="perfectCount:"+this.perfectCount+"\n";
		ret +="poorCount:"+this.poorCount+"\n";
		ret +="matching:"+this.matching+"\n";
		ret +="interval:"+this.interval+"\n";
		ret +="score:"+this.score+"\n";
		ret +="catchUp:"+this.catchUp+"\n";
		ret +="finalRRInterval:"+this.finalRRInterval+"\n";
		ret +="qi:"+this.qi+"\n";

		return ret;
	}
}
