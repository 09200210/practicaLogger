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
	private static boolean logToFile;
	private static boolean logToConsole;
	private static boolean logMessage;
	private static boolean logWarning;
	private static boolean logError;
	private static boolean logToDatabase;
	private static Map dbParams;
	private static Logger logger;
	public static final int LOG_MESSAGE=1;
	public static final int LOG_WARNING=2;
	public static final int LOG_ERROR=3;
	public JobLogger(boolean logToFileParam, boolean logToConsoleParam, boolean logToDatabaseParam,
			boolean logMessageParam, boolean logWarningParam, boolean logErrorParam, Map dbParamsMap) throws Exception {
		logger = Logger.getLogger("MyLog");  
		logError = logErrorParam;
		logMessage = logMessageParam;
		logWarning = logWarningParam;
		logToDatabase = logToDatabaseParam;
		logToFile = logToFileParam;
		logToConsole = logToConsoleParam;
		dbParams = dbParamsMap;
		if (!logToConsole && !logToFile && !logToDatabase) {
			throw new Exception("Invalid configuration");
		}
		if (!logError && !logMessage && !logWarning) {
			throw new Exception("Error or Warning or Message must be specified");
		}
	}
	/**
	 * This method validates can save in the adequated logs 
	 * @param messageText
	 * @param messageType
	 * @throws Exception
	 */
	public static void logMessage(String messageText, int messageType) throws Exception {
		if (messageText != null && messageText.length() != 0) {
			String formatMessage = createFormattedMessage(messageText,messageType);
			Level loggingLevel = returnEquivalentLevel(messageType);
			if(logToConsole) {
				createLogConsole(formatMessage,loggingLevel);
			}
			if(logToFile) {
				createLogFile(formatMessage,loggingLevel);
			}
			if(logToDatabase) {
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
	private static String createFormattedMessage(String messageText, int messageType) throws Exception {
		String message= "";
		if (logError && messageType==LOG_ERROR) {
			message = message + "error ";
		}
		else if (logWarning && messageType==LOG_WARNING) {
			message = message + "warning ";
		}
		else if (logMessage && messageType==LOG_MESSAGE) {
			message = message + "message " ;
		}
		else {
			throw new Exception("Invalid type of message");
		}
		message= message+DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + ": "+messageText;
		return message;
	}
	private static Level returnEquivalentLevel(int messageType) throws Exception {
		if (messageType==LOG_ERROR) {
			return Level.SEVERE;
		}
		else if (logWarning && messageType==LOG_WARNING) {
			return Level.SEVERE;
		}
		else if (logMessage && messageType==LOG_MESSAGE) {
			return Level.SEVERE;
		}
		else {
			throw new Exception("Invalid type of message");
		}
		
	}
	private static void createLogConsole(String messageText, Level loggingLevel) {
		ConsoleHandler ch = new ConsoleHandler();
		logger.addHandler(ch);
		logger.log(loggingLevel, messageText);
	}
	/**
	 * Create a log of file 
	 * @param messageText
	 * @param loggingLevel
	 */
	private static void createLogFile(String messageText, Level loggingLevel) {
		try {
			String fileLocation = dbParams.get("logFileFolder") + "/logFile.txt";
			File logFile = new File(fileLocation);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			FileHandler fh = new FileHandler(fileLocation);
			logger.addHandler(fh);
			logger.log(loggingLevel, messageText);
		}catch(IOException e) {
			logger.log(Level.SEVERE, "An error while saving log in file has occurred",e);
		}
	}
	/**
	 * Create a log in a table of the database
	 * @param messageText
	 * @param messageType
	 */
	private static void createLogDatabase(String messageText, int messageType)  {
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
