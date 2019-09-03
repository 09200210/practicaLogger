package configuracion;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JobLogger {
	
	private TypeMessage typeMessage;
	private LogLocation logLocation;
	private static Map dbParams;
	private static Logger logger;
	public static final int LOG_MESSAGE=1;
	public static final int LOG_WARNING=2;
	public static final int LOG_ERROR=3;
	public JobLogger(TypeMessage typeMessage,
			LogLocation logLocation, Map dbParams) throws Exception {
		logger = Logger.getLogger("MyLog");  
		this.typeMessage = typeMessage;
		this.logLocation = logLocation;
		this.dbParams = dbParams;
		if (!logLocation.isLogToConsole() && !logLocation.isLogToFile() && !logLocation.isLogToDatabase()) {
			throw new Exception("Invalid configuration");
		}
		if (!typeMessage.isLogError() && !typeMessage.isLogMessage() && !typeMessage.isLogWarning()) {
			throw new Exception("Error or Warning or Message must be specified");
		}
	}
	/**
	 * This method validates can save in the proper logs 
	 * @param messageText
	 * @param messageType
	 * @throws Exception
	 */
	public void logMessage(String messageText, int messageType) throws Exception {
		if (messageText != null && messageText.length() != 0) {
			String formatMessage = createFormattedMessage(messageText,messageType);
			Level loggingLevel = returnEquivalentLevel(messageType);
			logger.setUseParentHandlers(false);
			if(logLocation.isLogToConsole()) {
				createLogConsole(formatMessage,loggingLevel);
			}
			if(logLocation.isLogToFile()) {
				createLogFile(formatMessage,loggingLevel);
			}
			if(logLocation.isLogToDatabase()) {
				createLogDatabase(formatMessage,messageType);
			}
		}
	}

	/**
	 * Creates a formatted message for saving in the log
	 * @param messageText
	 * @param messageType
	 * @return
	 * @throws Exception 
	 */
	private String createFormattedMessage(String messageText, int messageType) throws Exception {
		String message= "";
		if (typeMessage.isLogError() && messageType==LOG_ERROR) {
			message = message + "error ";
		}
		else if (typeMessage.isLogWarning() && messageType==LOG_WARNING) {
			message = message + "warning ";
		}
		else if (typeMessage.isLogMessage() && messageType==LOG_MESSAGE) {
			message = message + "message " ;
		}
		else {
			throw new Exception("Invalid type of message");
		}
		message= message+DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + ": "+messageText.trim();
		return message;
	}
	private Level returnEquivalentLevel(int messageType) throws Exception {
		if (typeMessage.isLogError()&&messageType==LOG_ERROR) {
			return Level.SEVERE;
		}
		else if (typeMessage.isLogWarning() && messageType==LOG_WARNING) {
			return Level.WARNING;
		}
		else if (typeMessage.isLogMessage() && messageType==LOG_MESSAGE) {
			return Level.INFO;
		}
		else {
			throw new Exception("Invalid type of message");
		}
		
	}
	private  void createLogConsole(String messageText, Level loggingLevel) {
		ConsoleHandler ch = new ConsoleHandler();
		logger.addHandler(ch);
		logger.log(loggingLevel, messageText);
	}
	/**
	 * Create a log of file 
	 * @param messageText
	 * @param loggingLevel
	 */
	private  void createLogFile(String messageText, Level loggingLevel) {
		try {
			String fileLocation = dbParams.get("logFileFolder") + "/logFile.txt";
			File logFile = new File(fileLocation);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			FileHandler fh = new FileHandler(fileLocation);
			logger.addHandler(fh);
			logger.log(loggingLevel, messageText);
			fh.close();
		}catch(IOException e) {
			logger.log(Level.SEVERE, "An error while saving log in file has occurred",e);
		}
	}
	/**
	 * Create a log in a table of the database
	 * @param messageText
	 * @param messageType
	 */
	private void createLogDatabase(String messageText, int messageType)  {
		try {
			Connection connection = null;
			Properties connectionProps = new Properties();
			connectionProps.put("user", dbParams.get("userName"));
			connectionProps.put("password", dbParams.get("password"));
	
			connection = DriverManager.getConnection("jdbc:" + dbParams.get("dbms") + "://" + dbParams.get("serverName")
					+ ":" + dbParams.get("portNumber") + "/"+dbParams.get("database"), connectionProps);
			Statement stmt = connection.createStatement();
			stmt.executeUpdate("insert into Log_Values(mensaje,tipo) values('" + messageText + "', " + messageType+ ")");
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "An error while saving log in database has occurred",e);
		}
	}





		  
}
