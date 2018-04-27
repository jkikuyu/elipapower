/**
 * 
 */
package com.ipayafrica.elipapower.util;

/**
 * @author Jude Kikuyu
 * created on 25/04/2018
 *
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@Component
public class LogFile {
	
	@Autowired
	private Environment env;
	
	private String logPath;
	protected  final transient Log log = LogFactory.getLog(getClass());

	
	public void LogError(Exception ex) {				
		Writer output = null;
		try {
			// Create one director
			logPath =env.getProperty("errorLog.filepath")+ "/";	
			File file = new File("log");
			if(!file.exists()){
		    	new File("log").mkdir();
		    }
			Date d = new Date();
			SimpleDateFormat fomatter = new SimpleDateFormat("ddMMyyyy");
			String date = fomatter.format(d);
			String filepath =  logPath + date + "_Ebills.log";
			file = new File(filepath);
			if(ex.getMessage()!=null && !ex.getMessage().contains("IntBd.ttf")){
			boolean exists = file.exists();
			if (!exists) {
				file = new File(filepath);
				output = new BufferedWriter(new FileWriter(file));
				output.write("$$$$$$$$$$$$===========================================$$$$$$$$$" + "\r\n");
				output.append(d.toString());
				output.append(stack2string(ex));
				output.close();
			} else {

				OutputStreamWriter writer = new OutputStreamWriter(
						new FileOutputStream(filepath, true), "UTF-8");
				BufferedWriter fbw = new BufferedWriter(writer);
				fbw.write(stack2string(ex));
				fbw.newLine();
				fbw.append("$$$$$===========================================$$$$\r\n");
				fbw.append(d.toString());
				fbw.newLine();
				fbw.close();
			}
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}		
	}
	
	public void eventLog(String mystr) {
		Writer output = null;
		try {
			// Create one directory
			logPath =env.getProperty("errorLog.filepath")+ "/";	
			
		    if( !(new File("log")).exists()){
		    	new File("log").mkdir();
		    }
		    	
			Date d = new Date();
			SimpleDateFormat fomatter = new SimpleDateFormat("ddMMyyyy");
			String date = fomatter.format(d);
			String filepath =  logPath + date+ "_EbillsEventLog" + ".log";
			File file = new File(filepath);

//			SimpleDateFormat fomatter2 = new SimpleDateFormat("HH:mm:ss");
//			String date2 = fomatter2.format(d);
			
			boolean exists = file.exists();
			if (!exists) {
				file = new File(filepath);
				output = new BufferedWriter(new FileWriter(file));
				output.write("*******************| "+d.toString()+" | *****************" + "\r\n");
//				output.append(d.toString());
				output.append(mystr);
				output.close();
			} else {

				OutputStreamWriter writer = new OutputStreamWriter(
						new FileOutputStream(filepath, true), "UTF-8");
				BufferedWriter fbw = new BufferedWriter(writer);
				fbw.write("*******************| "+ d.toString() +" | *****************" + "\r\n");
				fbw.write(mystr);
				fbw.newLine();					
//				fbw.append(d.toString());
				fbw.newLine();
				fbw.close();
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}		
	}
	public void LogError3(String mystr) {				
		Writer output = null;
		try {
			// Create one directory
			logPath =env.getProperty("errorLog.filepath")+ "/";	
			
		    if( !(new File("log")).exists()){
		    	new File("log").mkdir();
		    }
		    	
			Date d = new Date();
			SimpleDateFormat fomatter = new SimpleDateFormat("ddMMyyyy");
			String date = fomatter.format(d);
			String filepath =  logPath + date+ "_EbillsServiceCheck" + ".log";
			File file = new File(filepath);

			boolean exists = file.exists();
			if (!exists) {
				file = new File(filepath);
				output = new BufferedWriter(new FileWriter(file));
				output.write("$$$$$$$$$$$$===========================================$$$$$$$$$" + "\r\n");
				output.append(d.toString());
				output.append(mystr);
				output.close();
			} else {

				OutputStreamWriter writer = new OutputStreamWriter(
						new FileOutputStream(filepath, true), "UTF-8");
				BufferedWriter fbw = new BufferedWriter(writer);
				fbw.write(mystr);
				fbw.newLine();
				fbw.append("$$$$$===========================================$$$$\r\n");
				fbw.append(d.toString());
				fbw.newLine();
				fbw.close();
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}		
	}
	
	public void LogError4(String mystr) {				
		Writer output = null;
		try {
			// Create one directory
			logPath =env.getProperty("errorLog.filepath")+ "/";	
			
		    if( !(new File("log")).exists()){
		    	new File("log").mkdir();
		    }
		    	
			Date d = new Date();
			SimpleDateFormat fomatter = new SimpleDateFormat("ddMMyyyy");
			String date = fomatter.format(d);
			String filepath =  logPath + date+ "_EbillsEmailsCheck" + ".log";
			File file = new File(filepath);

			boolean exists = file.exists();
			if (!exists) {
				file = new File(filepath);
				output = new BufferedWriter(new FileWriter(file));
				output.write("$$$$$$$$$$$$===========================================$$$$$$$$$" + "\r\n");
				output.append(d.toString());
				output.append(mystr);
				output.close();
			} else {

				OutputStreamWriter writer = new OutputStreamWriter(
						new FileOutputStream(filepath, true), "UTF-8");
				BufferedWriter fbw = new BufferedWriter(writer);
				fbw.write(mystr);
				fbw.newLine();
				fbw.append("$$$$$===========================================$$$$\r\n");
				fbw.append(d.toString());
				fbw.newLine();
				fbw.close();
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}		
	}
	
	public void LogError2(Exception ex) {
			
		LogError(ex);
		/*  */
		Writer output = null;
		try {
			logPath =env.getProperty("errorLog.filepath")+ "/";	
			
		    if( !(new File(logPath)).exists()){
		    	new File(logPath).mkdir();
		    }
			Date d = new Date();
			SimpleDateFormat fomatter = new SimpleDateFormat("ddMMyyyy");
			String date = fomatter.format(d);
			String filepath = logPath + date + "_ebills.log";
			File file = new File(filepath);
			if(!ex.getMessage().contains("IntBd.ttf")){
//				log.debug("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ HERE 3" );
			if (ex !=null)
			{
				boolean exists = file.exists();
				if (!exists) {
					file = new File(filepath);
					output = new BufferedWriter(new FileWriter(file));
//						output.write("$$$$$$$$$$$$===========================================$$$$$$$$$" + "\r\n");
					output.append(stack2string(ex));
					output.close();
				} 
				else 
				{
					OutputStreamWriter writer = new OutputStreamWriter(
							new FileOutputStream(filepath, true), "UTF-8");
					BufferedWriter fbw = new BufferedWriter(writer);
					fbw.write(stack2string(ex));
					fbw.newLine();
//						fbw.append("$$$$$===========================================$$$$\r\n");
					fbw.append(d.toString());
					fbw.newLine();
					fbw.close();
				}
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String stack2string(Exception e) {
		  try {
		    StringWriter sw = new StringWriter();
		    PrintWriter pw = new PrintWriter(sw);
		    e.printStackTrace(pw);
		    return "########*************_____________________________________________\r\n" +
		    sw.toString() + "_____________________________________________*************########\r\n";
		  }
		  catch(Exception e2) {
		    return "bad stack2string";
		  }
	}
}


