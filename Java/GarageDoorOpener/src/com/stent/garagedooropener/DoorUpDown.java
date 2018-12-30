package com.stent.garagedooropener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class DoorUpDown extends Activity {
	
	private static final String TAG = "ConnectThreadMessaging";
	private static Handler mMainHandler;
	private static Handler mChildHandler;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread connectThread;
        Message msg = null;
        
        setContentView(R.layout.activity_door_up_down);
		// Create the text view
        final TextView textView = new TextView(this);
        
        @SuppressWarnings("unused")
		Intent intent = getIntent();
        
        mMainHandler = new Handler() {
        	
        	public void handleMessage(Message msg) {
        		
        		Log.i(TAG, "Got an incoming message from the child thread - "  + (String)msg.obj);
        		
        		/*
        		 * Handle the message coming from the child thread.
        		 */
        		textView.setTextSize(40);
        		textView.setText(textView.getText() + (String)msg.obj + "\n");
        	}
        };
                        
        connectThread = new ConnectThread();
        connectThread.start();
   			
        Log.i(TAG, "Main handler is bound to - " + mMainHandler.getLooper().getThread().getName());
        
        /*
		 * We cannot guarantee that the mChildHandler is created 
		 * in the child thread in time so perhaps we delay
		 */
        
        try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
                
        if (mChildHandler != null) {
			
			/* 
			 * Send a message to the child thread.
			 */
        	msg = mChildHandler.obtainMessage();
        	msg.obj = mMainHandler.getLooper().getThread().getName() + " says OPEN THE DOOR!";
			mChildHandler.sendMessage(msg);
			Log.i(TAG, "Send a message to the child thread - " + (String)msg.obj);
		}
        
        try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        msg = mChildHandler.obtainMessage(); 
        textView.setText((String)msg.obj);
       
        // Set the text view as the activity layout
        setContentView(textView);	
     }
	
	
	@Override
	protected void onDestroy() {
    	
    	Log.i(TAG, "Stop looping the child thread's message queue");
    	
    	/*
    	 * Remember to stop the looper
    	 */
    	mChildHandler.getLooper().quit();
    	
		super.onDestroy();
	}
	
	static void sendMessage(String msg, ObjectOutputStream out)
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
	
	static public class ConnectThread extends Thread {
		
		private static final String INNER_TAG = "InnerChildThread";
	    @Override
	    public void run() {
	    	final String message = null;
	    	
		/*
		* You have to prepare the looper before creating the handler.
		*/
		Looper.prepare();

		/*
		 * Create the child handler on the child thread so it is bound to the
		 * child thread's message queue.
		 */
		mChildHandler = new Handler() {
	
			public void handleMessage(Message msg) {
	
				Log.i(INNER_TAG, "Got an incoming message from the main thread - " + (String)msg.obj);
		
				/*
				 * Do some expensive operation there. For example, you need
				 * to constantly send some data to the server.
				 */
				try {
					Message toMain = mMainHandler.obtainMessage();
					Socket requestSocket = null;
					ObjectOutputStream out = null;
					ObjectInputStream in = null;
			
					try	{
			    		requestSocket = new Socket("192.168.1.97", 7654);
//						requestSocket = new Socket("127.0.0.1", 7654);
			    		out = new ObjectOutputStream(requestSocket.getOutputStream());
			    		out.flush();
			    	    in = new ObjectInputStream(requestSocket.getInputStream());
			    		DoorUpDown.sendMessage(message, out);
			    	}
			    	catch(UnknownHostException unknownhost) {
			    		toMain.obj = "UNKNOWN HOST - server may be down";
			    		// "This is " + this.getLooper().getThread().getName() + ".  Did you send me \"" + (String)msg.obj + "\"?";
						mMainHandler.sendMessage(toMain);
						Log.i(INNER_TAG, "UnknownHostException : Send message back to main thread - " + (String)toMain.obj);
			    		return; 
			    	}
			    	catch(IOException ioException) {
			    		toMain.obj = ioException.getLocalizedMessage();
			    		mMainHandler.sendMessage(toMain);
						Log.i(INNER_TAG, "IOException : Send a message to the main thread - " + (String)toMain.obj);
			    		ioException.printStackTrace();	
			    	}
			    	finally{
			    		if (requestSocket != null) {
			    			toMain.obj = "SERVER SHOULD HAVE GOT MESSAGE - Door should be moving!";
							mMainHandler.sendMessage(toMain);
			    		}
			    	}
					
					//4 closing connection
			    	try{
			    		in.close();
			    		out.close();
			    		requestSocket.close();   // closing socket after closing in and out streams
			    	}
			    	catch(IOException ioException) {
			    		ioException.printStackTrace();
			    	} 
			    	finally {
			    	}
			    	    
			    }
			   finally{
			   }
		    }
		};
		Log.i(INNER_TAG, "Child handler is bound to - " + mChildHandler.getLooper().getThread().getName());
		Looper.loop();
	}
	    
}
	
}




	    	
	    	    			
	    	
	    			
	    	

	
