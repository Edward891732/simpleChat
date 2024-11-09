// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 2024.11.08

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  String logInId;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String ID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.logInId = ID;
    openConnection();
    
    //this.sendToServer("#login " + ID);
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
    
    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
    	if(message.startsWith("#")){
    		handleMessageFromClientConsole(message);
    	} else {
    		sendToServer(message);
    	}
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  public void handleMessageFromClientConsole(String input) {
	  if(input.startsWith("#")) {
		  String[] inputs = input.split(" ");
		  String function = inputs[0];
		  switch (function) {
		  
		  case "#quit":
			  quit();
			  break;
		  case "#logoff":
			  try {
				  this.closeConnection();
			  } catch (IOException e) {
				  System.out.println("Error for logging off");
			  }
			  break;
		  case "#sethost":
			  if(this.isConnected()) {
				  System.out.println("Error: the client should be logged off");
			  } else {
				  this.setHost(inputs[1]);
			  }
			  break;
		  case "#setport":
			  if(this.isConnected()) {
				  System.out.println("Error: the client should be logged off");
			  } else {
				  this.setPort(Integer.parseInt(inputs[1]));
			  }
			  break;
		  case "#login":
			  if (this.isConnected()) {
				  System.out.println("Error: the client should be logged off");
			  } else {
				  try {
					  this.openConnection();
				  } catch (IOException e) {
					  System.out.println("Error for setting a port");
				  }
			  }
			  break;
		  case "#gethost":
			  System.out.println("Host: " + this.getHost());
			  break;
		  case "#getport":
			  System.out.println("Port: " + this.getPort());
			  break;
		  
		  }
	  }
  }
  //newly added
  
 	protected void connectionClosed() {
 		clientUI.display("The connection closed.");
 	}
   
   
   	@Override
 	protected void connectionException(Exception exception) {
   		
   		clientUI.display("The connection shutted down");
   		System.out.println(exception);
   		quit();
 	} 
   	
 	protected void connectionEstablished(){
 		System.out.println("A new client has connected to the server. "); 
 		try {
 			sendToServer("#login " + logInId);
 		} catch (IOException e) {
 			System.out.println("Login error.");
 			quit();
 		}
 		System.out.println(logInId + " has logged in.");
 	}
  
  
  
  
}


//End of ChatClient class
