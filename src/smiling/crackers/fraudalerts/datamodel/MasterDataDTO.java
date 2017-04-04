package smiling.crackers.fraudalerts.datamodel;

import java.util.ArrayList;

public class MasterDataDTO {
	
	private String CustId;
	private String AccNo;
	private String CustName;
	private int Sal;
	private int RiskFreq;
	private int Category;
	private int CrLimit;
	private int DRLimit;
	public int getCrLimit() {
		return CrLimit;
	}
	public void setCrLimit(int crLimit) {
		CrLimit = crLimit;
	}
	public int getDRLimit() {
		return DRLimit;
	}
	public void setDRLimit(int dRLimit) {
		DRLimit = dRLimit;
	}
	public int getCategory() {
		return Category;
	}
	public void setCategory(int category) {
		Category = category;
	}
	public String getCustId() {
		return CustId;
	}
	public void setCustId(String custId) {
		CustId = custId;
	}
	public String getAccNo() {
		return AccNo;
	}
	public void setAccNo(String accNo) {
		AccNo = accNo;
	}
	public String getCustName() {
		return CustName;
	}
	public void setCustName(String custName) {
		CustName = custName;
	}
	public int getSal() {
		return Sal;
	}
	public void setSal(int sal) {
		Sal = sal;
	}
	public int getRiskFreq() {
		return RiskFreq;
	}
	public void setRiskFreq(int riskFreq) {
		RiskFreq = riskFreq;
	}
	
	

}
