package com.ipayafrica.elipapower.util;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
/**
 * 
 * @author jkikuyu
 * created on 18/04/2018
 */
@Component
@PropertySource("classpath:application.properties")
public class RequestToken {
	private byte[] res = null; 
	
	@Autowired
	private Environment env;
	
	@Autowired
	private LogFile logfile;

	@Autowired
	ResponseToken responseToken;
	

	public RequestToken() {
	}


	/**
	 * 
	 * @param reqB
	 * @return
	 */
	public HashMap<String,Object> makeRequest(byte[] reqB, String meterNo,boolean test){
		
		/*
		 * method only passes a static value for testing purpose
		 */
	    HashMap<String, Object> messResponse = null;
		messResponse = new HashMap<String,Object>();
		String mess = "<ipayMsgclient=\"IPAYAFRICA\"term=\"00001\"seqNum=\"32\"time=\"2018-05-1112:17:52+0200\"><elecMsgver=\"2.48\"><vendRes><ref>813113170032</ref><rescode=\"elec001\"extCode=\"0\">MaximumAmountExceededMaximumAmountExceeded=1.0Notvalid-Range10.0-9.9999999999999E13.Verifythedataprovidedandretry</res></vendRes></elecMsg></ipayMsg>";
		try {
			messResponse = responseToken.cleanXML(mess);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return messResponse;
	}

	public HashMap<String,Object> makeRequest(byte[] reqB, String meterNo){
//		String serverIP= "41.204.194.188";
		String serverIP = env.getProperty("token.server.ip");
		int port =  Integer.parseInt(env.getProperty("token.server.port"));
//		int port = 8902;
//		int timeout = 30000;
		int timeout = Integer.parseInt(env.getProperty("token.server.timeout"));
		Scanner is = null;
		DataOutputStream os = null;
		Socket socket = null;

	    String responseLine =""; // obtain response from server
	    HashMap<String, Object> messResponse = null;

		try {
			res = wrap(reqB);
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		/*	InputStream inputStream = new ByteArrayInputStream(res); //convert into input stream
		try {
			message = unWrap(inputStream);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/

		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(serverIP,port),timeout);
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			// This stops the request from dragging on after connection succeeds.
			socket.setSoTimeout(timeout);
			os = new DataOutputStream(socket.getOutputStream());
			is = new Scanner(socket.getInputStream());
			String s = new String(res);
			System.out.println(s);
			os.write(res);
            while (is.hasNext()) {
            	responseLine += is.next();
            }
            String mess = "response: " + responseLine;
            
    		logfile.eventLog(mess);
    		try {
    			messResponse = new HashMap<String,Object>();
				messResponse = responseToken.cleanXML(mess);
				
				
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
            socket.close();

		} catch (SocketException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	return messResponse;
  	}

	 private byte[] wrap(byte[] msg) throws Exception {
        int len = msg.length;
        if (len > 65535) {
            throw new IllegalArgumentException("Exceeds 65535 bytes.");
        }
        byte firstByte = (byte)(len >>> 8);
        byte secondByte = (byte)len;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(len + 2);
        baos.write(firstByte);
        baos.write(secondByte);
        baos.write(msg);
        return baos.toByteArray();

	}
    /**
     * @author ipay
     * @param inputStream
     * @return
     * @throws Exception
     */
	    public byte[] unWrap(InputStream inputStream) throws Exception {
	        int firstByte = inputStream.read();
	        if (firstByte ==-1) {
	            throw new IOException("End of Stream while trying to read vli byte 1");
	        }
	        int firstByteValue = firstByte << 8;   
	        int secondByteValue = inputStream.read();
	        if (secondByteValue ==-1) {
	            throw new IOException("End of Stream reading vli byte 2.");
	        }
	        int len = firstByteValue + secondByteValue;
	        byte[] message = new byte[len];
	        int requestLen;
	        int readLen;
	        int currentIndex = 0;
	        while(true) { 
	            requestLen = len - currentIndex;
	            readLen = inputStream.read(message, currentIndex, requestLen);
	            if (readLen == requestLen) {
	                break;  // Message is complete.
	            }
	            
	            // Either data was not yet available, or End of Stream.
	            currentIndex += readLen;
	            int nextByte = inputStream.read();
	            if (nextByte ==  1) {
	                throw new IOException("End of Stream at " + currentIndex );
	            }
	            message[currentIndex++] = (byte)nextByte;
	        }
	        return message;
	    }


}
