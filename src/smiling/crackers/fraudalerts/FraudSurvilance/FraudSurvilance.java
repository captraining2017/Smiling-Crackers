package smiling.crackers.fraudalerts.FraudSurvilance;

public class FraudSurvilance {

	public static void main(String[] args)
	{
		FraudSurvilanceThread fraudthread=new FraudSurvilanceThread("FraudSurvilence thread");
		fraudthread.start();
	}
}
