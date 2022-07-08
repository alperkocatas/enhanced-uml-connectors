package roundrobinrequester;

public class ECls {

	ACls a;
	BCls b;
	CCls c;
	
	RoundRobinConnectorCls connector;
	
	public ECls()
	{
		a  = new ACls();
		b = new BCls();
		c = new CCls();
		connector = new RoundRobinConnectorCls();
		
		a.setItsPaXIfc(connector);
		connector.setItspConnectorXIfc(b, 0);
		connector.setItspConnectorXIfc(c, 1);
	}
	
	public void execute()
	{
		a.execute();
	}
} 
