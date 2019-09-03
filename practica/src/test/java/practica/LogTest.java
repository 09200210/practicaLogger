package practica;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import configuracion.JobLogger;
import configuracion.LogLocation;
import configuracion.TypeMessage;
 
public class LogTest {
	JobLogger jobLogger ;
	/**
	 * Method for preparing the connection
	 * @throws Exception 
	 */
	@Before
	public void initializeConnection()  {
		Map<String,String> dbParamsMap = new HashMap<String,String>();
		dbParamsMap.put("userName", "root");
		dbParamsMap.put("password", "root");
		dbParamsMap.put("dbms", "mysql");
		dbParamsMap.put("serverName", "localhost");
		dbParamsMap.put("portNumber", "3306");
		dbParamsMap.put("database", "db_test");
		dbParamsMap.put("logFileFolder","C:\\Users\\Omar\\Documents");
		try {
			TypeMessage typeMessage = new TypeMessage(true, true, true);
			LogLocation logLocation = new LogLocation(true, true, true);
			jobLogger = new JobLogger(typeMessage, logLocation, dbParamsMap);
		}catch(Exception e) {
			fail();
		}
	}
	
//	@Test
//	public void testValidatingNullTextMessage()  {
//		try {
//			jobLogger.logMessage(null, JobLogger.LOG_MESSAGE);
//			assertTrue(true);
//		}catch(Exception e){
//			e.printStackTrace();
//			fail();
//		}
//	}
	@Test
	public void testValidatingRegisteringLogMessage()  {
		try {
			jobLogger.logMessage("this is a message log test", JobLogger.LOG_MESSAGE);
			assertTrue(true);
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}
	}
//	@Test
//	public void testValidatingRegisteringLogWarning()  {
//		try {
//			jobLogger.logMessage("this is a warning log test", JobLogger.LOG_WARNING);
//			assertTrue(true);
//		}catch(Exception e){
//			e.printStackTrace();
//			fail();
//		}
//	}
//	@Test
//	public void testValidatingRegisteringLogError()  {
//		try {
//			jobLogger.logMessage("this is a error log test", JobLogger.LOG_ERROR);
//			assertTrue(true);
//		}catch(Exception e){
//			e.printStackTrace();
//			fail();
//		}
//	}
//	@Test
//	public void testValidatingFailingWithTypeError()  {
//		try {
//			jobLogger.logMessage("this is a error log test", 12);
//			fail();
//		}catch(Exception e){
//			assertTrue(true);
//		}
//	}
}
