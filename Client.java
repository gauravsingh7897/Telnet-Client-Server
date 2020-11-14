// A Java program for a telnet Client 
import java.net.*;
import java.io.*;
import java.util.*;
import java.time.LocalTime;

public class Client {
    // initialize socket and input output streams
    private Socket socket = null;
    // private DataInputStream input = null;
    private DataOutputStream out = null;
    private DataInputStream in = null;
    private DataInputStream result = null;
    private BufferedReader input = null;

    public Client(String address, int port) {
        // establish a connection
        try {
            System.out.println("requesting to connect to server:(TIME):	" + LocalTime.now());
            socket = new Socket(address, port);
            System.out.println("Connected to server:(TIME):	" + LocalTime.now());
            // input = new DataInputStream(System.in); // takes input from terminal
            input = new BufferedReader(new InputStreamReader(System.in));
            out = new DataOutputStream(socket.getOutputStream()); // sends output to the socket
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream())); // takes input from server
        } catch (UnknownHostException u) {
            System.out.println("host not found");
        } catch (IOException i) {
            System.out.println(i);
        }
        String Command = "";
		String Password = "";
		String LoginName = "";
		String line = "";
		String chat_r = "";
		String chat_s = "";
		System.out.println("Welcome");
		
		while(true){
			System.out.println("use command \'help\' to show possible commands");
			try{
			System.out.print(">>>");	
			line = input.readLine();
			out.writeUTF(line);
			}catch(IOException i){ 
						System.out.println(i); 
			}
//----------------------------------------------------------------------------------------------			
			if (line.equals("help")){
				System.out.println("Showing help");
				System.out.println("chatbox	-	Chatbox");
				System.out.println("telnet	-	Telnet");
				System.out.println("quit	- 	close the connection ");
			}
//----------------------------------------------------------------------------------------------			
			else if(line.equals("chatbox")){
				System.out.println("Welcome to chat box");
				//out.writeUTF("chatbox");
				while (true) { 
					try{
						System.out.print(">>>client:	");
						chat_s = input.readLine(); 
						out.writeUTF(chat_s);
						if(chat_s.equals("over"))
							break;
						chat_r = in.readUTF(); 
						System.out.println(">>>admin:	"+chat_r);						
					} 
					catch(IOException i){ 
						System.out.println(i); 
					} 
				}
				System.out.println("Chatbox Closed");
				
			}
//----------------------------------------------------------------------------------------------			
			else if(line.equals("telnet")){
				try {
					String login = "" ;
					do{	
						Console console = System.console();
						System.out.println("Welcome to Telnet Client");
						System.out.println("Your Credential Please...");
						//out.writeUTF("login_request");
						System.out.print(">>>Login Name :	");
						LoginName=input.readLine();
						System.out.print(">>>Password :	");
						char[] passwordArray = console.readPassword(); 
						Password = new String(passwordArray);
						//Password=input.readLine();
						out.writeUTF(LoginName);
						out.writeUTF(Password);
						String Permission =in.readUTF();
						System.out.println(Permission);
						if (Permission.equals("ALLOWED")){
							System.out.println("<< Telnet Prompt >");
							while(true){								
								System.out.print("(telnet)>>> ");
								Command=input.readLine();            
								out.writeUTF(Command);
								if(Command.equals("quit")){
									System.out.println("you are logged out");
									login = "0";
									break;
								}
								out.flush();
								result = new DataInputStream(new BufferedInputStream(socket.getInputStream())); // takes input from server
								System.out.println(result.readUTF());  
							}
							
						}
						else{
							System.out.println("Login failed!!");												
						}
						System.out.println("Re-login press-1");
						System.out.println("exit press -other key");
						login  = input.readLine();
						out.writeUTF(login);
						out.flush();
					}while(login.equals("1"));
				
				} catch (IOException i) {
					System.out.println();
				}
			}
//----------------------------------------------------------------------------------------------		
			
			else if (line.equals("quit")){
				System.out.println("exiting");
				break ;
			}
//----------------------------------------------------------------------------------------------
			else{
				System.out.println("Wrong Attempt");
			}
		}
        // close the connection
        try {
            input.close();
            out.close();
            socket.close();
            in.close();
            System.out.println("connection closed at :(TIME):	" + LocalTime.now());
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String args[]) {
        Client client = new Client("127.0.0.1", 5000);
    }
}