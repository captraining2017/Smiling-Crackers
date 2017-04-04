package smiling.crackers.fraudalerts.datamodel;

import java.util.Date;

import com.sun.xml.internal.ws.client.ClientSchemaValidationTube;

public class TransactionDTO {
	
    private static final String CSV_SEPARATOR = ",";

	private String AccNo;
	private int Amount;
	private String CrDrFlag;
	private Date TxnDate;
	private String RiskFlag;
	public String getAccNo() {
		return AccNo;
	}
	public void setAccNo(String accNo) {
		AccNo = accNo;
	}
	public int getAmount() {
		return Amount;
	}
	public void setAmount(int amount) {
		Amount = amount;
	}
	public String getCrDrFlag() {
		return CrDrFlag;
	}
	public void setCrDrFlag(String crDrFlag) {
		CrDrFlag = crDrFlag;
	}
	public Date getTxnDate() {
		return TxnDate;
	}
	public void setTxnDate(Date txnDate) {
		TxnDate = txnDate;
	}
	
	
	public String getRiskFlag() {
		return RiskFlag;
	}
	public void setRiskFlag(String riskFlag) {
		RiskFlag = riskFlag;
	}
	public String getCSV()
	{
		return TxnDate+CSV_SEPARATOR+AccNo+CSV_SEPARATOR+Amount+CSV_SEPARATOR+CrDrFlag+CSV_SEPARATOR+RiskFlag;
	}

}
