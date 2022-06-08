import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.util.Scanner;
import java.net.*;
import java.util.*;
import java.io.*;


class Variables{
	public static int cond = 0;
	public static int cond2 = 0;
	public static ArrayList<String> messages = new ArrayList<String>();
	public static int nada = 0;
	public static String ID = "";
	public static int contador = 0;
	public static String departamento = null;
	public static int logado = 0;
	public static int Selecionado = 0;
	public static String eleicao = "";
}
 
 
public class MulticastClient extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 4321;
    

    public static void main(String[] args) {
        MulticastClient client = new MulticastClient();
        client.start();
        MulticastUser user = new MulticastUser();
        user.start();
        
        
        
    }

    public void run() {
        MulticastSocket socket = null;
        String[] name = null;
        String texto = null;
        
        try {
            socket = new MulticastSocket(PORT);  // create socket and bind it
            /*InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);*/
            InetAddress mc = InetAddress.getByName(MULTICAST_ADDRESS);
			InetSocketAddress group = new InetSocketAddress(mc, PORT);
			NetworkInterface netIf = null; //NetworkInterface.getByName("bge0");
			socket.joinGroup(group, netIf);
            
            
            while (true) {
				byte[] buffer = new byte[256];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				String aux = new String(packet.getData(), 0, packet.getLength());
				name = aux.split(" ");
				System.out.println(aux);
				Variables.cond = 1;
				Variables.messages.add(aux);
				Variables.departamento = name[1];
		 	   break;
            }
            while (true){
            	byte[] buffer = new byte[256];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				String aux = new String(packet.getData(), 0, packet.getLength());
				name = aux.split(" ");
				System.out.println(aux);
				Variables.cond2 = 1;
				Variables.messages.add(aux);
				break;
            }
            while(true){
            	byte[] buffer = new byte[256];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				String aux = new String(packet.getData(), 0, packet.getLength());
            	Variables.nada = 0;
            	for(int i = 0;i<Variables.messages.size();i++){
            		if(Variables.messages.get(i).equals(aux)){
            			Variables.nada = 1;
            		}
            	}
            	
            	if(Variables.nada == 0){
            		name = aux.split("-");
            		if(name[0].equals(Variables.ID)){
						aux = aux.replace(Variables.ID+"-","");
							
						name = aux.split(";");
						name = name[0].split(" \\| ");
						if(name.length > 1){
							if(name[1].equals("status")){
								name = aux.split(";");
								name = name[1].split(" \\| ");
								if(name[1].equals("on")){
									if(Variables.logado == 0){
										Variables.logado = 1;
										System.out.println(aux);
									}
								}
								else{
									if(Variables.logado == 1){
										Variables.logado = 1;
										System.out.println("You are already logged in");
									}
									else{
										System.out.println(aux);
									}
								}
							}

						}
						else{
							name = aux.split("-");
							if(name[0].equals("Selecionar")){
								Variables.eleicao = name[1];
								aux = aux.replace("Selecionar" + "-" + Variables.eleicao+"-","");
								Variables.Selecionado = 1;
								
							}
							if(Variables.logado == 1 && Variables.Selecionado == 1 && name[0].equals("Votar")){
								if(Variables.eleicao.equals(name[1])){
									aux = aux.replace("Votar" + "-" + Variables.eleicao+"-","");
									System.out.println(aux);
									}
								else{
									System.out.println("Nao Selecionou esta eleicao");
								}
								
							}
							else if(Variables.Selecionado == 0 && Variables.logado == 1 && name[0].equals("Votar")){
								System.out.println("Tem que selecionar uma eleicao primeiro");
							}
							else if(Variables.logado == 1){
								System.out.println(aux);
							}
							else{
								System.out.println("You need to login first!");
							}
							
						}

            		}else if(aux.equals("Try again, please!") || aux.equals("Ambos servidores RMI crasharam durante mais de 30 segundos")){
						
						System.out.println(aux);
					}
            		
            	}
            
	}
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}

class MulticastUser extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 4320;
    String[] name = null;
    String[] name2 = null;
    String texto = null;
    public MulticastUser() {
        super("User " + (long) (Math.random() * 1000));
    }

    public void run() {
        MulticastSocket socket = null;
        System.out.println(this.getName() + " ready...");
        texto = this.getName();
        name = texto.split(" ");
        Variables.ID = name[1];
        try {
            socket = new MulticastSocket();  // create socket without binding it (only for sending)
            while (true) {
            	System.out.print("");
		if(Variables.cond == 1){
			String message = this.getName() + " ready...";
		        byte[] buffer = message.getBytes();

		        InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
		        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
		        socket.send(packet);
		 }
		 if(Variables.cond2 == 1) break;
            }
            
            while(true){
				try{
					BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
					socket.setSoTimeout(120000); //nao pode estar inativo apos 120 secs
					texto = reader.readLine();
					name = texto.split(";");
					name2 = name[0].split(" \\| ");
					if(name2[1].equals("Votar")){
						name2 = name[2].split(" \\| ");
						if(Variables.Selecionado == 1 && Variables.eleicao.equals(name2[1])){
							texto = Variables.ID + "-" + texto;
							byte[] buffer = texto.getBytes();
							InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
							DatagramPacket packet = new DatagramPacket(buffer,buffer.length,group,PORT);
							socket.send(packet);
						}
						else{
							System.out.println("Houve um erro a realizar o voto");
						}
					
					}
					else{
						texto = Variables.ID + "-" + texto;
						byte[] buffer = texto.getBytes();
						InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
						DatagramPacket packet = new DatagramPacket(buffer,buffer.length,group,PORT);
						socket.send(packet);
					}
				}catch(SocketTimeoutException ex){
					System.out.println("Fechou pois passaram 120 segundos de inatividade");
				}
            }
            
            
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}
