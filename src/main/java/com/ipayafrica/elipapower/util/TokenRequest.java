package com.ipayafrica.elipapower.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Scanner;

import org.springframework.stereotype.Component;

@Component
public class TokenRequest {
	private byte[] res = null, message=null ;

	public TokenRequest() {
	}
	/**
	 * @author iPay
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	
	public String makeRequest(String req){
		String serverIP= "41.204.194.188";
		int port = 8902;
		int timeout = 30000;
		Scanner is = null;
		DataOutputStream os = null;
		Socket socket = null;

	    String responseLine =""; // obtain response from server

		try {
			byte[] reqB = req.getBytes(Charset.forName("UTF-8"));

			res = wrap(reqB);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		InputStream inputStream = new ByteArrayInputStream(res); //convert into input stream
		try {
			message = unWrap(inputStream);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

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
            System.out.println("response from socket " + responseLine);
            socket.close();

		} catch (SocketException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	return responseLine;
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
