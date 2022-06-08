import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.rmi.registry.LocateRegistry;
import java.io.*;


class Variables2{ 
	public static int cond = 0;
	public static ArrayList<String> auxiliar = new ArrayList<String>();
	public static String answer = null;
	public static int desbloqueado = 0;
	public static String nome = "dei";
	public static int num_eleitores = 0; 
	public static int crashed = 0;
}

public class MulticastServer extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 4321;
    private long SLEEP_TIME = 5000;

    public static void main(String[] args) {
        MulticastServer server = new MulticastServer();
        server.start();
        MulticastUserServer user = new MulticastUserServer();
        user.start();
	
	
    }

    public MulticastServer() {
        super("Mesa de voto " + Variables2.nome);
    }

    public void run() { 
        MulticastSocket socket = null;
        System.out.println(this.getName() + " aberta...");
   
        try {

            socket = new MulticastSocket();  // create socket without binding it (only for sending)
            while (true) {
				
				if(Variables2.crashed == 1){
					byte[] buffer = Variables2.answer.getBytes();
					InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
					socket.send(packet);
		        	Variables2.crashed = 0;
				}
            	else if(Variables2.desbloqueado == 1){
		        	byte[] buffer = Variables2.answer.getBytes();
					InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
					socket.send(packet);
		        	Variables2.desbloqueado = 0;
		        
		        }
		        else{
		    		if(Variables2.cond == 0){
						String message = this.getName() + " aberta ";
						byte[] buffer = message.getBytes();

						InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
						DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
						socket.send(packet);
					}
					else if(Variables.cond == 1){		
						String message = "Terminal de Voto Desbloqueado";
						byte[] buffer = message.getBytes();

						InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
						DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
						socket.send(packet);
						Variables2.cond = 0;
					}
			}
		        
		    try { sleep((long) (Math.random() * SLEEP_TIME)); } catch (InterruptedException e) { }
            }
		
        } catch (Exception e) {
			System.out.println("Net foi abaixo");
        } finally {
            socket.close();
        }
    }
}

class MulticastUserServer extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 4320;

	public void reboot(RMI rmi, String RMInome, MulticastSocket socket, int tempo){
		String[] name = null;
		int cond2 = 0;
		String[] name2 = null;
		String[] name3 = null;
		try{
			 
			rmi.set_nova_ligacao("Mesa de voto ", Variables2.nome);
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            
            while (true) {
            	cond2 = 0;
				byte[] buffer = new byte[256];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				String aux = new String(packet.getData(), 0, packet.getLength());
				name = aux.split(" ");
				for(int i = 0;i<Variables2.auxiliar.size();i++){
					if (name[1].equals(Variables2.auxiliar.get(i))){
						cond2 = 1;
					}
		    	
		    	}
		    
				name2 = aux.split(";");
				name3 = name2[0].split("-");

				if(cond2 == 0 && name[0].equals("User")){
					System.out.println(aux);
					Variables2.auxiliar.add(name[1]);
					Variables2.cond = 1;
					rmi.set_nova_ligacao("Terminal de Voto ", name[1]);
				}
		    
				else if (name3.length > 1){
					if(name3[1].equals("type | login")){
						Variables2.desbloqueado = 1;
						Variables2.answer = rmi.login(aux);
				
					}
					else if(name3[1].equals("type | Listar eleicoes")){
						Variables2.desbloqueado = 1;	
						Variables2.answer = rmi.listar_eleicoes(name3[0]);
					}
					else if(name3[1].equals("type | Selecionar eleicao")){
						String[] idaux = name2[1].split(" \\| "); 
						Variables2.desbloqueado = 1;	
						Variables2.answer = rmi.selecionar_eleicao(idaux[1],name3[0],Variables2.nome);
					}
					else if(name3[1].equals("type | Votar")){
						String[] idusername = name2[1].split(" \\| "); 
						String[] ideleicao = name2[2].split(" \\| ");
						String[] idcandidato = name2[3].split(" \\| ");
						Variables2.desbloqueado = 1;	
						Variables2.answer = rmi.Votar(idusername[1],ideleicao[1],idcandidato[1],name3[0],Variables2.nome);
						if(Variables2.answer.equals(name3[0] + "-" + "Voto efetuado com sucesso!")){
							Variables2.num_eleitores = Variables2.num_eleitores + 1;
							rmi.set_num_eleitores(Variables2.nome, Variables2.num_eleitores); 
						}
					}
		    	
		    	
		    	}
		    

		    
            }

         } catch (Exception e) {
			try{
				System.out.println("Primary Console crashed.\nAdmin Console Secundary Ready!");
				Variables2.crashed = 1;
				Variables2.answer = "Try again, please!"; 
				if(RMInome.equals("RMIServer")){
					RMI rmi2 = (RMI) LocateRegistry.getRegistry(6999).lookup("RMIServer2");
					reboot(rmi2, "RMIServer2", socket, tempo);
				}
				else{
					RMI rmi2 = (RMI) LocateRegistry.getRegistry(7000).lookup("RMIServer");
					reboot(rmi2, "RMIServer", socket, tempo);
				}
			}catch(Exception re2){
						while(tempo != 30){
							try{
								sleep((long) (1000));
								tempo = tempo + 1;
								System.out.println(tempo);
								RMI rmi2 = (RMI) LocateRegistry.getRegistry(7000).lookup("RMIServer");
								reboot(rmi2, "RMIServer", socket, tempo);
							}catch(InterruptedException e1){
							}catch(Exception re){}
						}
						Variables2.answer = "Ambos servidores RMI crasharam durante mais de 30 segundos";
						Variables2.crashed = 1; 
			}
		 }
	}
    public void run() {
		int tempo = 0;
        MulticastSocket socket = null;
		try {
				socket = new MulticastSocket(PORT);  // create socket and bind it
				/*InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
				socket.joinGroup(group);*/
				InetAddress mc = InetAddress.getByName(MULTICAST_ADDRESS);
				InetSocketAddress group = new InetSocketAddress(mc, PORT);
				NetworkInterface netIf = null; //NetworkInterface.getByName("bge0");
				socket.joinGroup(group, netIf);
				try{

					RMI rmi = (RMI) LocateRegistry.getRegistry(7000).lookup("RMIServer");
					reboot(rmi, "RMIServer", socket, tempo);

				} catch (Exception e2) {
					System.out.println(e2);
					try{
						RMI rmi = (RMI) LocateRegistry.getRegistry("192.168.1.76",7000).lookup("RMIServer2");
						reboot(rmi, "RMIServer2", socket, tempo);
					}catch(Exception re2){
						while(tempo != 30){
							try{
								sleep((long) (1000));
								tempo = tempo + 1;
								RMI rmi = (RMI) LocateRegistry.getRegistry(7000).lookup("RMIServer");
								reboot(rmi, "RMIServer", socket, tempo);
							}catch(InterruptedException e){
							}catch(Exception re){}
						}
						Variables2.answer = "Ambos servidores RMI crasharam durante mais de 30 segundos";
						Variables2.crashed = 1;
					}
				
				} 
            
        } catch (IOException e) {
            System.out.println("Erro1");
        } finally {
			System.out.println("hello");
            socket.close();
        }
    }
}


