import java.rmi.registry.LocateRegistry;
import java.io.*;
import java.io.IOException;


public class AdminConsole{

	public static void reboot(RMI rmi, String nome){
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("AdminConsole Ready!");
			String texto = null;
			String answer = null;
			String texto2 = null;
			String texto3 = null;
			while(true){
				try{
					texto = reader.readLine();
                    if(texto.equals("Registo")){
                    	answer=rmi.sendCommand(texto);
                    	System.out.println(answer);
                    	texto = reader.readLine();
                    	answer=rmi.write_file(texto);
                    	if(answer.equals("Este Numero ja foi registado!")){
							System.out.println(answer);
						}
                    	else{
		            		for(int i=0;i<7;i++){
		            			texto = reader.readLine();
		            			answer = rmi.write_file(texto);
		            		}
		            		System.out.println("Registado com sucesso");
		            	}

                    }else if(texto.equals("Criar eleicao")){
                    	answer=rmi.sendCommand(texto);
                    	System.out.println(answer);
                    	texto = reader.readLine();
                    	answer=rmi.add_reuniao(texto,1);
                    	if(answer.equals("Ja existe uma eleicao com este ID!")){
							System.out.println(answer);
						}
                    	else{
		            		for(int i=0;i<7;i++){
		            			texto = reader.readLine();
		            			answer = rmi.add_reuniao(texto,0);
		            		}
		            		texto = reader.readLine();
		            		answer = rmi.add_reuniao(texto,2);
		            		System.out.println("Eleicao Registada com sucesso");
		            	}
                    }

                    else if(texto.equals("Candidatar a eleicao")){
                    	answer=rmi.sendCommand(texto);
                    	System.out.println(answer);
                    	texto = reader.readLine();
                    	texto2 = reader.readLine();
                    	answer=rmi.add_candidato(texto,texto2 + " 0");
                    	System.out.println(answer);
                    }

					else if(texto.equals("Mesa de voto")){
						answer = rmi.sendCommand(texto);
						System.out.println(answer);
                    				texto = reader.readLine();
						texto2 = reader.readLine();
						texto3 = reader.readLine();
						answer = rmi.mesa_voto(texto, texto2, texto3);
						System.out.println(answer);
					}
					
					else if(texto.equals("Editar eleicao")){
						answer = rmi.sendCommand(texto);
						System.out.println(answer);
						texto = reader.readLine();
						texto2 = reader.readLine();
						answer = rmi.editar_eleicao(texto,texto2);
						System.out.println(answer);
					}
					else if(texto.equals("Verificar onde votou")){
						answer = rmi.sendCommand(texto);
						System.out.println(answer);
						texto = reader.readLine();
						texto2 = reader.readLine();
						answer = rmi.localvotou(texto, texto2); //id de pessoa e id de eleicao
						System.out.println(answer);
					}
					else if(texto.equals("Consultar eleicao")){
						answer = rmi.sendCommand(texto);
						System.out.println(answer);
						texto = reader.readLine();
						answer = rmi.resultados_eleicao(texto); //id de eleicao
						System.out.println(answer);
					}
					else if(texto.equals("Mostra ligacoes")){
						System.out.println(rmi.get_nova_ligacao());
						
					}
					else if(texto.equals("Mostra numero de eleitores")){ 
						texto = reader.readLine();
						System.out.println(rmi.get_num_eleitores(texto));
					}
					
                    			

                }catch(Exception re){
					System.out.println("Primary Console crashed.\nAdmin Console Secundary Ready!");
					try{
						if(nome.equals("RMIServer")){
							RMI rmi2 = (RMI) LocateRegistry.getRegistry(6999).lookup("RMIServer2");
							reboot(rmi2, "RMIServer2");
						}
						else{
							RMI rmi2 = (RMI) LocateRegistry.getRegistry(7000).lookup("RMIServer");
							reboot(rmi2, "RMIServer");
						}
					}catch(Exception re2){
						System.out.println("lulz");
					}
					
				}
			}
	}

	// =========================================================
	public static void main(String args[]) {
		

		
		try {	
			RMI rmi = (RMI) LocateRegistry.getRegistry(7000).lookup("RMIServer");
			
			reboot(rmi, "RMIServer");
			

		} catch (IOException e) {
			try{
				RMI rmi = (RMI) LocateRegistry.getRegistry(6999).lookup("RMIServer2");
				reboot(rmi, "RMIServer2");
			}catch(Exception re2){
				System.out.println("lulz");
			}

		} catch (Exception e) {
			System.out.println("Erro");
		} 

	}
}
