package roundrobinrequester;

public class RoundRobinConnectorCls implements XIfc{

	public final int PRV_CNT = 2; 
	
	XIfc itspConnectorXIfc[] = new XIfc[PRV_CNT];
	
	public void setItspConnectorXIfc(XIfc arg, int i) {
	
		if (i < PRV_CNT)
			this.itspConnectorXIfc[i] = arg;
	}
	
	int order = 0;
	
	@Override
	public int xOp(int arg) {
		
		int result = this.itspConnectorXIfc[order].xOp(arg);
		
		order = (order + 1) % PRV_CNT;
		
		return result;
	}
	
}
