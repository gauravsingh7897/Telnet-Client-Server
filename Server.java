
// A Java program for a Server 
import java.net.*;
import java.io.*;
import java.time.LocalTime;

public class Server {
	// initialize socket and input stream
	private Socket socket = null;
	private ServerSocket server = null;
	private DataInputStream in = null;
	private DataOutputStream out = null;
	private BufferedReader input = null;

	// constructor with port
	public Server(int port) {
		// starts server and waits for a connection
		try {
			server = new ServerSocket(port);
			System.out.println("(host)> Server started :(TIME):	" + LocalTime.now());
			System.out.println("(host)> Waiting for a client ...");
			socket = server.accept();
			System.out.println("(host)> Client accepted :(TIME):	" + LocalTime.now());
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream())); // takes input from the client
			out = new DataOutputStream(socket.getOutputStream()); // sends output to the socket
			input = new BufferedReader(new InputStreamReader(System.in));
			/*} catch (IOException i) {
				System.out.println(i);
			}*/	
			String Command = "";
			String Password = "";
			String LoginName = "";
			String line = "";
			String chat_r = "";
			String chat_s = "";
			//System.out.println("Welcome");
			try{
				while(true){
					line = in.readUTF();
	//--------------------------------------------------------------------------------------------------------				
					if(line.equals("chatbox")){
						while (true) { 
							try{ 
								chat_r = in.readUTF(); 
								System.out.println(">>>client:	"+chat_r);
								if(chat_r.equals("over"))
									break;
								System.out.print(">>>admin:	");
								chat_s = input.readLine();
								out.writeUTF(chat_s);
							}catch(IOException i) { 
								System.out.println(i); 
							} 
						}
						System.out.println("exiting: Chatbox Closed");
						
					}
	//--------------------------------------------------------------------------------------------------------				
					else if(line.equals("telnet")){
						String login = "1";
						String Permission = "Not_Allowed";
						while (login.equals("1")){
							System.out.println("(host)> client requesting to use telnet");
							LoginName = in.readUTF();
							Password = in.readUTF();
							System.out.println(LoginName +" "+Password);						
							if( LoginName.equals("a") && Password.equals("1")){
								Permission = "ALLOWED";
								System.out.println(Permission);
								out.writeUTF(Permission);
								System.out.println("(host)> logged in from remote host :"+LoginName);
								while(true){
									try{
									Process process = null;
									Command = in.readUTF();							
									if(Command.equals("quit")){
										System.out.println(LoginName+"is logged out");
										login = "0";
										break;
									}
									
									else if (Command.equals("dir")){
										process = Runtime.getRuntime().exec("cmd /c dir");
										printResults(process);																	
									}									
									
									else 
										//process = Runtime.getRuntime().exec(Command);
										out.writeUTF("Wrong-attempt use help");
									
									Command = Command + " command received"; 
									System.out.println(Command); 
									
									}catch (IOException i) {
										System.out.println(i);
									}	
								}					
							}
							else 
								out.writeUTF(Permission);						
							login = in.readUTF();					
						}
					
					
					}
	//--------------------------------------------------------------------------------------------------------				
					else if(line.equals("quit")){
						System.out.println("exiting");
						break;
					}
				}
				
			}catch (IOException i) {
				System.out.println(i);
			}
		
			//try {
			input.close();
			out.close();
			socket.close();
			in.close();
			System.out.println("connection closed at :(TIME):	" + LocalTime.now());
        } catch (IOException i) {
            System.out.println(i);
        }
	}	
//--------------------------------------------------------------------------------------------------------

	public void printResults(Process process) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = "";
		String result ="";
		while ((line = reader.readLine()) != null) {
			result = result+ "\r\n" + line;
		}
		out.writeUTF(result);
	}
//--------------------------------------------------------------------------------------------------------
	public static void main(String args[]) {
		Server server = new Server(5000);
	}
}