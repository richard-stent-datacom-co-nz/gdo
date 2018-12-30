package com.stent.garagedooropener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class GdoRequestor {
	
	static Socket requestSocket;
	static ObjectOutputStream out;
	static ObjectInputStream in;
	
	static String message;
	
	static int GdoRequester() {
		return 0;
	}
		
	static int ConnectToGDO(String hostname, String port, String doorSide)
	{
		
		int portasint = Integer.parseInt(port); 
		
		try{
			requestSocket = new Socket(hostname, portasint);
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			do {
				try{
					sendMessage(doorSide);
				}
				finally{
				}
			
			} while(!message.equals("bye"));
			
		}
		catch(UnknownHostException unknownhost) {
			return 1; 
		}
		catch(IOException ioException) {
		}
		finally{
			//4 closing connection
			try{
				in.close();
				out.close();
				requestSocket.close();
			}
			catch(IOException ioException) {
				ioException.printStackTrace();
			
			} 
			
		}
		return 0;
		
		}
					
		static void sendMessage(String msg)
		{
			try{
				out.writeObject(msg);
				out.flush();
				System.out.println("client>" + msg);
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
			
	}
		
