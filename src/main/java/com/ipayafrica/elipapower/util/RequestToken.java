package com.ipayafrica.elipapower.util;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Optional;
import java.util.Scanner;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.ipayafrica.elipapower.model.TokenResponse;
/**
 * 
 * @author jkikuyu
 * created on 18/04/2018
 */
@Component
@PropertySource("classpath:application.properties")
public class RequestToken {
    protected final transient Log log = LogFactory.getLog(getClass());

	private byte[] res = null; 
	
	@Autowired
	private Environment env;
	
	@Autowired
	private LogFile logfile;

	@Autowired
	ResponseToken responseToken;
	
	//Socket socket = null;
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
		String mess = "<ipayMsgclient=\"IPAYAFRICA\"term=\"00001\"seqNum=\"47\"time=\"2018-05-1708:53:10+0200\"><elecMsgver=\"2.48\"finAdj=\"-10000\"><vendRessupGrpRef=\"100405\"tariffIdx=\"52\"keyRevNum=\"1\"tokenTechCode=\"02\"algCode=\"05\"daysLastVend=\"19999\"resource=\"elec\"><ref>813709530047</ref><rescode=\"elec000\">OK</res><utiladdr=\"59WaterFront,Durban.5899.\"taxRef=\"3988339883\"distId=\"6004708001509\">EskomOnline</util><stdTokenunits=\"333.33334\"amt=\"6260\"tax=\"940\"tariff=\"aaaa.aakWh@065.72c/kWh:bbbb.bbkWh@075.42c/kWh:cccc.cckWh@109.50c/kWh:dddd.ddkWh@120.10c/kWh:\"desc=\"NormalSale\"unitsType=\"kWh\"rctNum=\"969476426191\">59032564186831613321</stdToken><debtamt=\"1700\"tax=\"0\"rem=\"7700\"desc=\"1122\">DebtRecovery</debt><fixedamt=\"956\"tax=\"144\">Fixed</fixed><rtlrMsg>HelloOperatorMessage.</rtlrMsg><customerMsg>Gooddaydearcustomer.Thisisatestcustomermessagefromcustomer.Wehavevendedatokenforthecustomer.Themessagecanbeupto160characterslong....</customerMsg></vendRes></elecMsg></ipayMsg>";
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

	public HashMap<String,Object> makeRequest(byte[] reqB, String meterNo,String term){
//		String serverIP= "41.204.194.188";
//		String serverIP = env.getProperty("token.server.ip");
//		int port =  Integer.parseInt(env.getProperty("token.server.port"));
//		int port = 8902;
//		int timeout = 30000;
		//Scanner is = null;
		//Socket socket = null;

	    //String responseLine =""; // obtain response from server
	    HashMap<String, Object> messResponse = null;

		try {
			res = wrap(reqB);
		    messResponse =  createSecureSocket();

		} catch (Exception e) {
			logfile.LogError(e);
			
		}

		/*	InputStream inputStream = new ByteArrayInputStream(res); //convert into input stream
		try {
			message = unWrap(inputStream);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		return messResponse;
	}
    protected HashMap<String, Object> createSecureSocket()  {
    	String pass = env.getProperty("keystore.pass");
    	char[] localKeyStorePassword = pass.toCharArray();
        String sep = System.getProperty("file.separator");                                                                 

    	String keystorepath = env.getProperty("keystore.path")+sep+ env.getProperty("keystore.file");
 		final SSLSocket sslSocket;
 		HashMap<String, Object> messResponse = null;
    	try {
	    	KeyStore localKeyStore = KeyStore.getInstance("JKS");

	    	localKeyStore.load(new FileInputStream(keystorepath), localKeyStorePassword);
	                    
	        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
	        kmf.init(localKeyStore, localKeyStorePassword);
	        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
	        tmf.init(localKeyStore);
	        SecureRandom secureRandom = new SecureRandom();
	        secureRandom.nextInt();  // Force initialisation to occur now.
	        
	        SSLContext sslContext = SSLContext.getInstance("TLS");
	        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), secureRandom);
	 		String serverIP = env.getProperty("token.server.ip");
	 		int port =  Integer.parseInt(env.getProperty("token.ssl.server.port"));
	 		SSLSocketFactory sf= sslContext.getSocketFactory();
			//SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
			int timeout = Integer.parseInt(env.getProperty("token.server.timeout"));

            Socket socket = new Socket();
			socket.connect(new InetSocketAddress(serverIP,port),timeout);
			socket =  sf.createSocket(serverIP, port);
/*		    sslSocket.addHandshakeCompletedListener(handshakeCompletedEvent -> {
                try {
                    log.debug("Connected [" + handshakeCompletedEvent.getSource() + ", " + 
                sslSocket.getSession().getPeerCertificateChain()[0].getSubjectDN() + "]");
                } catch (SSLPeerUnverifiedException e) {
                    log.warn(e.getMessage(), e);
                }
            });
			sslSocket.startHandshake();
*/			
			messResponse = doTunnelHandshake(socket, serverIP, port);

	       
    	}
        catch(Exception e) {
        	
        	logfile.LogError(e);
        }
    	return messResponse;
    }

	private HashMap<String, Object> doTunnelHandshake(Socket socket, String host, int port) throws IOException{
		HashMap<String, Object> messResponse = null;
		DataOutputStream os = null;		

		// This stops the request from dragging on after connection succeeds.
		//socket.setSoTimeout(timeout);
		os = new DataOutputStream(socket.getOutputStream());
		//is = new Scanner(socket.getInputStream());
		String s = new String(res);
		int timeout = Integer.parseInt(env.getProperty("token.server.timeout"));

		socket.setSoTimeout(timeout);
		System.out.println(s);
		os.write(res);
		os.flush();
		res = null;
		try {
			InputStream is = socket.getInputStream();

			res = unWrap(is);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logfile.LogError(e);
		}
/*	            while (is.hasNext()) {
	            	responseLine += is.next();
	            }
	            */
		String mess = "";
        if (res != null) {

		     mess = "response: " + new String(res);
    		logfile.eventLog(mess);
    		try {
    			messResponse = new HashMap<String,Object>();
				messResponse = responseToken.cleanXML(mess);
				
				
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				logfile.LogError(e);
			} 
    		
		}
        socket.close();

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
    public byte[] unWrap(InputStream is) throws Exception {

        int firstByte = is.read();
        if (firstByte ==-1) {
            throw new IOException("End of Stream while trying to read vli byte 1");
        }
        int firstByteValue = firstByte << 8;   
        int secondByteValue = is.read();
        if (secondByteValue ==-1) {
            throw new IOException("End of Stream reading vli byte 2.");
        }
        int len = firstByteValue + secondByteValue;
        byte[] message = new byte[len];
        
        int requestLen = 0;
        int currentIndex = 0;
        int readLen;

        
        while(true) { 
            requestLen = len - currentIndex;
            readLen = is.read(message, currentIndex, requestLen);
            if (readLen == requestLen) {
                break;  // Message is complete.
            }
            
            // Either data was not yet available, or End of Stream.
            currentIndex += readLen;
            int nextByte = is.read();
            if (nextByte == -1) {
                throw new IOException("End of Stream at " + currentIndex );
            }
            message[currentIndex++] = (byte)nextByte;
        }
        return message;
    }


}
