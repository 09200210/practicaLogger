package configuracion;

public class LogLocation {
	private boolean logToFile;
	private boolean logToConsole;
	private boolean logToDatabase;
	
	public LogLocation(boolean logToFile, boolean logToConsole, boolean logToDatabase) {
		super();
		this.logToFile = logToFile;
		this.logToConsole = logToConsole;
		this.logToDatabase = logToDatabase;
	}
	public boolean isLogToFile() {
		return logToFile;
	}
	public void setLogToFile(boolean logToFile) {
		this.logToFile = logToFile;
	}
	public boolean isLogToConsole() {
		return logToConsole;
	}
	public void setLogToConsole(boolean logToConsole) {
		this.logToConsole = logToConsole;
	}
	public boolean isLogToDatabase() {
		return logToDatabase;
	}
	public void setLogToDatabase(boolean logToDatabase) {
		this.logToDatabase = logToDatabase;
	}
	
}
