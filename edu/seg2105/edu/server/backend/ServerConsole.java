package edu.seg2105.edu.server.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import edu.seg2105.client.common.ChatIF;
import edu.seg2105.client.ui.ClientConsole;

public class ServerConsole implements ChatIF{

	EchoServer echServer;
	final public static int DEFAULT_PORT = 5555;
	Scanner fromCon;
	
	
	public ServerConsole(int port) {
		echServer = new EchoServer (port, this);
		fromCon = new Scanner(System.in); 
	}
	
	public void accept() {
		try {
			//BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			String message;
			
			while(true) {
				message = fromCon.nextLine();
				echServer.handleMessageFromServerConsole(message);
				//display(message);
				
			}
		} catch (Exception e) {
			System.out.println("Can't read a line");
		}
		
	}
	
	public void display(String message) 
	  {
	    System.out.println("FROM SERVER> " + message);
	    echServer.sendToAllClients("SERVER MESSAGE > " + message);
	  }
	
	
	public static void main(String[] args) 
	  {

	    int port = 0; //Port to listen on

	    try
	    {
	    	port = Integer.parseInt(args[0]);
	    }
	    catch(Throwable t)
	    {
	    	port = DEFAULT_PORT;
	    }
	   
	    ServerConsole server = new ServerConsole(port);
	    
	    try {
	    	server.echServer.listen();
	    } catch (Exception ex) {
	    	System.out.println("Can't listen for clients");
	    } 
	    
	    server.accept();
	  }
	}

