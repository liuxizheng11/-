package com.heha.mitacsdk;

import java.io.Serializable;

public class MitacHRVEKG implements  Cloneable,Serializable {
	private int id;
	private int ansAge;
	private int balance;
	private int energy;
	private int heartRate;
	private int stress;
	private boolean leadoff;
	private boolean success;
	private int rrInterval;
	private int[] finalRRInterval;
	private int qi;
	
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

	public int getRrInterval() {
		return rrInterval;
	}

	public void setRrInterval(int rrInterval) {
		this.rrInterval = rrInterval;
	}		

	public String toString(){

		String ret = "";


		ret += "id:"+this.id+"\n";
		ret += "ansAge:"+this.ansAge+"\n";
		ret += "balance:"+this.balance+"\n";
		ret += "energy:"+this.energy+"\n";
		ret += "heartRate:"+this.heartRate+"\n";
		ret += "stress:"+this.stress+"\n";
		ret += "leadoff:"+this.leadoff +"\n";
		ret += "success:"+this.success +"\n";
		ret += "rrInterval:"+this.rrInterval+"\n";
		ret += "finalRRInterval:"+this.finalRRInterval+"\n";
		ret += "qi:"+this.qi+"\n";

		return ret;

	}

}
