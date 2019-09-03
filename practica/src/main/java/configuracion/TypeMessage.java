package configuracion;

public class TypeMessage {
	private boolean logMessage;
	private boolean logWarning;
	private boolean logError;
	public TypeMessage(boolean logMessage, boolean logWarning, boolean logError) {
		this.logMessage = logMessage;
		this.logWarning = logWarning;
		this.logError = logError;
	}
	
	public boolean isLogMessage() {
		return logMessage;
	}
	public void setLogMessage(boolean logMessage) {
		this.logMessage = logMessage;
	}
	public boolean isLogWarning() {
		return logWarning;
	}
	public void setLogWarning(boolean logWarning) {
		this.logWarning = logWarning;
	}
	public boolean isLogError() {
		return logError;
	}
	public void setLogError(boolean logError) {
		this.logError = logError;
	}
	
}
