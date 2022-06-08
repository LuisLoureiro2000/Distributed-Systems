import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.text.SimpleDateFormat;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class RMIServer extends UnicastRemoteObject implements RMI {	

	public String sendCommand(String texto){
		if(texto.equals("Registo")){
			System.out.println(texto);
			texto = "Por favor, insira os dados de acordo com a ordem:\n>Numero: \n>Funcao: \n>Nome: \n>Password: \n>Departamento: \n>Contacto Telemovel: \n>Morada: \n>Validade do CC: \n";
		}
		else if(texto.equals("Criar eleicao")){
			System.out.println(texto);
			texto = "Insira os dados de acordo com a ordem: \n>ID da eleicao: \n>Titulo da Reuniao: \n>Data de Inicio: \n>Hora de Inicio: \n>Data do Fim: \n>Hora do Fim: \n>Descricao da reuniao: \n>Restricao do Departamento: \n>Restricao das Funcoes: \n";
		
		}
		else if(texto.equals("Candidatar a eleicao")){
			System.out.println(texto);
			texto = "Insira o ID da eleicao: \nInsira o seu Numero: \n";
		
		}
		else if(texto.equals("Mesa de voto")){
			System.out.println(texto);
			texto = "Insira o ID da eleicao: \nInsira o departamento: \nQuer adicionar ou remover? \n";
		}
		else if(texto.equals("Editar eleicao")){
			System.out.println(texto);
			texto = "Insira o ID da eleicao: \nInsira os dados a alterar e um novo valor";
		}
		else if(texto.equals("Verificar onde votou")){
			System.out.println(texto);
			texto = "Insira o ID da pessoa: \n";
		} 
		return texto;
		
	}

	public String get_num_eleitores(String eleicao){
		ArrayList<String> num_eleitores = new ArrayList<String>();
		try{
			String[] myList = null;
			String line = "";
			BufferedReader readerfile = new BufferedReader(new FileReader("eleitores.txt"));
			
			line = readerfile.readLine();
			while(line != null){
				myList = line.split("-");
				for(int i = 0; i < myList.length; i++){
					num_eleitores.add(myList[i]);
				}
				line = readerfile.readLine();
			}
			readerfile.close();

		}catch(FileNotFoundException e){
			System.out.println("File Not Found");
		}catch(IOException e){
			System.out.println("IOException");
		}
		
		
		String output = "";
		try{
			BufferedReader readerfile = new BufferedReader(new FileReader("mesas.txt"));
			String line = "";
			String[] myList;
			String[] myList2;

			line = readerfile.readLine();
			while(line != null){
				myList = line.split("-");
				if(eleicao.equals(myList[0])){
					for(int i = 1; i < myList.length ; i++){
						for(int j = 0; j < num_eleitores.size(); j++){
							myList2 = num_eleitores.get(j).split(" ");
							if(myList2[0].equals(myList[i])){
								output = output + "Mesa de voto " + myList2[0] + " tem " + myList2[1] + " votos\n";
							}
						}
					}
					break;
				}
				line = readerfile.readLine();
			}

		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
	  	} catch (Exception re) {}

		return output;
	}
	
	public int set_num_eleitores(String id, int eleitores){
		ArrayList<String> num_eleitores = new ArrayList<String>();
		try{
			int existe = 0;
			String[] myList = null;
			String[] myList2 = null;
			String line = "";
			BufferedReader readerfile = new BufferedReader(new FileReader("eleitores.txt"));

			line = readerfile.readLine();
			while(line != null){
				myList = line.split("-");
				for(int i = 0; i < num_eleitores.size(); i++){
					num_eleitores.add(myList[i]);
				}
				line = readerfile.readLine();
			}
			readerfile.close();

			for(int i = 0; i < num_eleitores.size(); i++){
				myList2 = num_eleitores.get(i).split(" ");
				if(myList2[0].equals(id)){
					existe = 1;
					num_eleitores.set(i, id + " " + String.valueOf(eleitores) + "-");
				}
			}
			if(existe == 0){
				num_eleitores.add(id + " " + String.valueOf(eleitores) + "-");	
			}

			BufferedWriter out = null;
			PrintWriter clean2 = new PrintWriter("eleitores.txt");
			clean2.print("");
			clean2.close();
			try {
				FileWriter fstreameleicoes = new FileWriter("eleitores.txt", true);
				out = new BufferedWriter(fstreameleicoes);
				for(int l = 0;l<num_eleitores.size();l++){
					out.write(num_eleitores.get(l));
				}   
			}

			catch (IOException e) {
					System.err.println("Error: " + e.getMessage());
			}

			finally {
				if(out != null) {
					out.close();
				}
			}
		}catch(FileNotFoundException e){
			System.out.println("File Not Found");
		}catch(IOException e){
			System.out.println("IOException");
		}
		return 0;
	}


	public String get_nova_ligacao(){
		ArrayList<String> ligacoes = new ArrayList<String>();
		try{
			String[] myList = null;
			String line = "";
			BufferedReader readerfile = new BufferedReader(new FileReader("ligacoes.txt"));

			line = readerfile.readLine();
			while(line != null){
				myList = line.split("-");
				for(int i = 0; i < myList.length; i++){
					ligacoes.add(myList[i]);
				}
				line = readerfile.readLine();
			}
			readerfile.close();

		}catch(FileNotFoundException e){
			System.out.println("File Not Found");
		}catch(IOException e){
			System.out.println("IOException");
		}

		String output = "";
		for(int i = 0; i < ligacoes.size(); i++){
			output = output + ligacoes.get(i) + "\n";
		}

		try{
			BufferedReader readerfile = new BufferedReader(new FileReader("mesas.txt"));
			ArrayList<String> mesas = new ArrayList<String>();
			String line = "";
			String[] myList;
			String[] myList2;
			int existe = 0;

			line = readerfile.readLine();
			while(line != null){
				myList = line.split("-");
				for(int i = 1; i < myList.length; i++){
					mesas.add(myList[i]);
				}
				line = readerfile.readLine();
			}
			readerfile.close();

			
			for(int i = 0; i < mesas.size(); i++){
				for(int j = 0; j < ligacoes.size(); j++){
					myList2 = ligacoes.get(j).split(" ");
					if(mesas.get(i).equals(myList2[3])){
						existe = 1;
						break;
					}
				}
				if(existe == 0){
					output = output + "Mesa de voto " + mesas.get(i) + " off\n";
				}
				existe = 0;
			}

		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
	  	} catch (Exception re) {}

		return output;
	} 
	
	public int set_nova_ligacao(String tipo, String id){
		ArrayList<String> ligacoes = new ArrayList<String>();
		ligacoes.add(tipo + id + " on" + "-");
		try{
			String line = "";
			BufferedReader readerfile = new BufferedReader(new FileReader("ligacoes.txt"));

			line = readerfile.readLine();
			while(line != null){
				ligacoes.add(line);
				line = readerfile.readLine();
			}
			readerfile.close();

			BufferedWriter out = null;
			PrintWriter clean2 = new PrintWriter("ligacoes.txt");
			clean2.print("");
			clean2.close();
			try {
				FileWriter fstreameleicoes = new FileWriter("ligacoes.txt", true);
				out = new BufferedWriter(fstreameleicoes);
				for(int l = 0;l<ligacoes.size();l++){
					out.write(ligacoes.get(l));
				}   
			}

			catch (IOException e) {
					System.err.println("Error: " + e.getMessage());
			}

			finally {
				if(out != null) {
					out.close();
				}
			}
		}catch(FileNotFoundException e){
			System.out.println("File Not Found");
		}catch(IOException e){
			System.out.println("IOException");
		}
		return 0;
	}

	public String resultados_eleicao(String eleicao){
		//1.ver se eleicao ja terminou
		String output = "";
		try{
			if(checkdate(eleicao) == 3){
				//2.contar votos
				BufferedReader readerfile = new BufferedReader(new FileReader("candidatos.txt"));
				ArrayList<String> eleicoes = new ArrayList<String>();
				String line = "";
				String[] myList = null;
				String[] myListVotos = null;
				int total_votos = 0;
				int percentagem; 

				line = readerfile.readLine();
				while(line != null){
					eleicoes.add(line);
					line = readerfile.readLine();
				}
				readerfile.close();

				for(int i = 0; i < eleicoes.size(); i++){
					myList = eleicoes.get(i).split("-");
					if(eleicao.equals(myList[0])){
						for(int j = 1; j < myList.length; j++){
							myListVotos = myList[j].split(" ");
							total_votos = total_votos + Integer.parseInt(myListVotos[1]);
						}
						break;
					}
				}
				
				
				for(int i = 1; i < myList.length; i++){
					myListVotos = myList[i].split(" ");
					percentagem = (Integer.parseInt(myListVotos[1]) / total_votos) * 100;
					output = output + myListVotos[0] + " teve " + myListVotos[1] + " votos; " + String.valueOf(percentagem) + "% dos votos\n";
				}

				return output;
			}	
			else{
				return "Ainda nao ha resultados para esta eleicao";
			}
		} catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		} catch (Exception re) {}
		return "nada";
	}

	public String localvotou(String IDpessoa, String IDeleicao){
		try{
		    BufferedReader readerfile = new BufferedReader(new FileReader("votos.txt"));
		    ArrayList<String> eleicoes = new ArrayList<String>();
		    String line = "";
		    String[] myList;
		    String[] myList2;

		    line = readerfile.readLine();
		    while(line != null){
		        eleicoes.add(line);
		        line = readerfile.readLine();
		    }
		    readerfile.close();

		    for(int i = 0; i < eleicoes.size(); i++){
		        myList = eleicoes.get(i).split("-");
		        if(IDeleicao.equals(myList[0])){
		            for(int j = 1; j < myList.length; j++){
		            	myList2 = myList[j].split(" ");
		                if(IDpessoa.equals(myList2[0])){
		                    return "O utilizador " + myList2[0] + " votou no " + myList2[1] + " no dia " + myList2[2] + " na hora " + myList2[3] + "\n";
		                }
		            }
		        }
		    }

		} catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		} catch (Exception re) {}

		return "O eleitor nao votou nesta eleicao\n";
	    }
	
	
	public int checkvotou(String username, String eleicao){
		try{
		    BufferedReader readerfile = new BufferedReader(new FileReader("votos.txt"));
		    ArrayList<String> eleicoes = new ArrayList<String>();
		    String line = "";
		    String[] myList;
		    String[] myList2;

		    line = readerfile.readLine();
		    while(line != null){
		        eleicoes.add(line);
		        line = readerfile.readLine();
		    }
		    readerfile.close();

		    for(int i = 0; i < eleicoes.size(); i++){
		        myList = eleicoes.get(i).split("-");
		        if(eleicao.equals(myList[0])){
		            for(int j = 1; j < myList.length; j++){
		            	myList2 = myList[j].split(" ");
		                if(username.equals(myList2[0])){
		                    return 0;
		                }
		            }
		        }
		    }

		} catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		} catch (Exception re) {}

		return 2;
	    }
	
	
	public String editar_eleicao(String id,String alteracao){
		try{
			BufferedReader readerfile = new BufferedReader(new FileReader("eleicoes.txt"));
			String[] myList = null;
			String[] auxiliar = null;
			String[] instrucao = null;
			String replacement = "";
			int existe = 0;
			String line = readerfile.readLine();
			ArrayList<String> eleicoes = new ArrayList<String>();
			while (line != null){ //conta numero de linhas do fich txt em questao e adiciona linhas do fich ao arraylist
				eleicoes.add(line);
				line = readerfile.readLine();
			}
			
			for(int i=0;i<eleicoes.size();i++){
				myList = eleicoes.get(i).split("-");
				if(myList[0].equals(id)){
					auxiliar = alteracao.split(";");
					existe = 1;
					for(int j=0;j<auxiliar.length;j++){
						instrucao = auxiliar[j].split(" \\| ");
						if(instrucao[0].equals("Titulo")){
							myList[1] = instrucao[1];
						}
						else if(instrucao[0].equals("Data Inicio")){
							myList[2] = instrucao[1];
						}
						else if(instrucao[0].equals("Hora Inicio")){
							myList[3] = instrucao[1];
						}
						else if(instrucao[0].equals("Data Fim")){
							myList[4] = instrucao[1];
						}
						else if(instrucao[0].equals("Hora Fim")){
							myList[5] = instrucao[1];
						}
						else if(instrucao[0].equals("Descricao")){
							myList[6] = instrucao[1];
							
						}
						else if(instrucao[0].equals("Departamento Restricao")){
							myList[7] = instrucao[1];
						}
						else if(instrucao[0].equals("Funcionario Restricao")){
							myList[8] = instrucao[1];
						}
					
					}
					for(int j=0;j<myList.length;j++){
						replacement = replacement + myList[j] + "-";
					}
					eleicoes.set(i,replacement);
					break;
				}
			
			}
			if(checkdate(id) == 0){
				if(existe == 1){
					BufferedWriter out = null;
					PrintWriter clean2 = new PrintWriter("eleicoes.txt");
					clean2.print("");
					clean2.close();
					try {
				    		FileWriter fstreameleicoes = new FileWriter("eleicoes.txt", true);
						out = new BufferedWriter(fstreameleicoes);
						for(int l = 0;l<eleicoes.size();l++){
							out.write(eleicoes.get(l)+"\n");
						}
						
						return "Eleicao editada com sucesso!"	;	    
						}

						catch (IOException e) {
							System.err.println("Error: " + e.getMessage());
						}

						finally {
							if(out != null) {
								out.close();
							}
						}
				}
				else{
					return "Esta eleicao nao existe!";
				}
			}
			else{
				return "Esta eleicao ja comecou!";
			}
			
			
		} catch (FileNotFoundException e) {
		System.out.println("An error occurred.");
		  e.printStackTrace();
		} catch (Exception re) {}
		return "nada";
	}
	
	public int checkdep(String eleicao, String username){
		try{
			BufferedReader readerfile = new BufferedReader(new FileReader("dados.txt"));
			BufferedReader readerfile2 = new BufferedReader(new FileReader("eleicoes.txt"));
			String[] myList = null;
			String line = null;
			String DEPeleicao = "";
			int lines = 0;
			String DEPpessoa = "";
			String line2 = readerfile2.readLine();
			int existe = 0;
			int existe2 = 0;
			while (readerfile.readLine() != null) lines++;
			readerfile.close();
				for(int i = 0;i<lines;i=i+8){
					line = Files.readAllLines(Paths.get("dados.txt")).get(i);
					if(line.equals(username)){
						existe = 1;
						
					}
					if(existe == 1){
						DEPpessoa = Files.readAllLines(Paths.get("dados.txt")).get(i+4);
						break;
					}

				}
			while(line2 != null){
				myList = line2.split("-");
				if(myList[0].equals(eleicao)){
					DEPeleicao = myList[7];
					break;
				}
				line2 = readerfile2.readLine();		
			}
			myList = DEPeleicao.split(" ");		
			for(int i=0;i<myList.length;i++){
				if(DEPpessoa.equals(myList[i])){
					existe2 = 1;
					break;
				}
			}
			
			readerfile2.close();
			
			if(existe == 1 && existe2 == 1){
				return 1;
			}
			else return 0;
			} catch (FileNotFoundException e) {
			  System.out.println("An error occurred.");
			  e.printStackTrace();
			} catch (Exception re) {}
		return 2;
	}
	

	public String Votar(String username, String eleicao, String candidato, String ID,String dep)
	{
		if(checkdate(eleicao) == 0 || checkdate(eleicao) == 3){
			return ID + "-" + "eleicao nao esta aberta para voto";
		}
		
		if(checkdep(eleicao,username) == 0){
			return ID + "-" + "Nao pode votar nesta eleicao";
		}
		if(checkvotou(username,eleicao) == 0){
			return ID + "-" + "Ja votou na eleicao";
		}
		
		
		try {
			BufferedReader readerfile = new BufferedReader(new FileReader("candidatos.txt"));
			ArrayList<String> cands =  new ArrayList<String>();
			ArrayList<String> votos =  new ArrayList<String>();
			String replacement = "";
			String votosaux = "";
			String line2 = null;
			int votes = 0;
			int votou = 0;
			String votesString = "";
			String[] myList = null;
			String[] aux = null;
			String line = readerfile.readLine();
			while (line != null){
				cands.add(line);
				line = readerfile.readLine();
			}
			for (int i = 0; i<cands.size();i++){
				myList = cands.get(i).split("-");
				replacement = myList[0] + "-";
				if(myList[0].equals(eleicao)){
					for(int j = 1;j<myList.length;j++){
						aux = myList[j].split(" ");
						replacement = replacement + aux[0] + " ";
						if(candidato.equals(aux[0])){
							votes = Integer.parseInt(aux[1]);
							votes = votes + 1;
							votesString = String.valueOf(votes);
							replacement = replacement + votesString + "-";
							votou = 1;
							
						}
						else{
							replacement = replacement + aux[1] + "-";
						}
					}
				}
				if(votou == 1) {
				
					try {
					cands.set(i,replacement); 
					BufferedReader readerfile2 = new BufferedReader(new FileReader("votos.txt"));
					GregorianCalendar data = new GregorianCalendar();
					SimpleDateFormat fmt = new SimpleDateFormat("dd/MMM/yyyy hh:mm");
    				fmt.setCalendar(data);
    				String dateFormatted = fmt.format(data.getTime());


					line2 = readerfile2.readLine();
					while (line2 != null){
						votos.add(line2);
						line2 = readerfile2.readLine();
					}
					for(int l =0;l<votos.size();l++){
						myList = votos.get(l).split("-");
						if(myList[0].equals(eleicao)){
							votosaux = votos.get(l) + username + " " + dep + " " + dateFormatted + "-";
							votos.set(l,votosaux);
						}
					}
					
					
					readerfile2.close();
					
					} catch (FileNotFoundException e) {
					System.out.println("An error occurred.");
					e.printStackTrace();
					} catch (Exception re) {}
					
					BufferedWriter out = null;
					PrintWriter clean2 = new PrintWriter("votos.txt");
					clean2.print("");
					clean2.close();
					try {
				    		FileWriter fstreamvotos = new FileWriter("votos.txt", true);
							out = new BufferedWriter(fstreamvotos);
							for(int l = 0;l<votos.size();l++){
								out.write(votos.get(l)+"\n");
						}
								    
						}

						catch (IOException e) {
							System.err.println("Error: " + e.getMessage());
						}

						finally {
							if(out != null) {
								out.close();
							}
						}
					
					break;
				}
			}
			BufferedWriter out = null;
			PrintWriter clean = new PrintWriter("candidatos.txt");
			clean.print("");
			clean.close();
			try {
		    		FileWriter fstream = new FileWriter("candidatos.txt", true);
				out = new BufferedWriter(fstream);
				for(int l = 0;l<cands.size();l++){
					out.write(cands.get(l)+"\n");
				}
						    
				}

				catch (IOException e) {
					System.err.println("Error: " + e.getMessage());
				}

				finally {
					if(out != null) {
						out.close();
					}
				}

			if(votou == 1){
				return ID + "-" + "Votar" + "-" + eleicao + "-" + "Voto efetuado com sucesso!";
			}
		
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		} catch (Exception re) {}
		
		
		return ID + "-" + "Houve um erro ao efutuar o voto";
	}
	
	
	public int checkdate(String eleicao)
	{
		try {
				//2-> data inicio 3-> hora inicio 4-> data fim 5-> hora fim
				BufferedReader readerfile2 = new BufferedReader(new FileReader("eleicoes.txt"));
				String[] myList = null;
				String[] data_ini, data_fim, hora_ini, hora_fim;
				String line2 = "";
				String dia = "", mes = "", ano = "", hora = "", minuto = ""; // dd/mm/yyyy  hh:mm
				String dia_f = "", mes_f = "", ano_f = "", hora_f = "", minuto_f = ""; // dd/mm/yyyy  hh:mm

				line2 = readerfile2.readLine();
				while(line2 != null){
					myList = line2.split("-");
					if(eleicao.equals(myList[0])){
						data_ini = myList[2].split("\\/");
						hora_ini = myList[3].split(":");
						data_fim = myList[4].split("\\/");
						hora_fim = myList[5].split(":");
						
						dia = data_ini[0];
						mes = data_ini[1];
						ano = data_ini[2];
						hora = hora_ini[0];
						minuto = hora_ini[1];
						
						dia_f = data_fim[0];
						mes_f = data_fim[1];
						ano_f = data_fim[2];
						hora_f = hora_fim[0];
						minuto_f = hora_fim[1];
						break;
					}
					line2 = readerfile2.readLine();
				}
				readerfile2.close(); 

				GregorianCalendar data_atual = new GregorianCalendar();
				GregorianCalendar data_inicial = new GregorianCalendar();
				GregorianCalendar data_final = new GregorianCalendar();

				data_inicial.set(Integer.parseInt(ano), Integer.parseInt(mes)-1, Integer.parseInt(dia), Integer.parseInt(hora), Integer.parseInt(minuto));
				data_final.set(Integer.parseInt(ano_f), Integer.parseInt(mes_f)-1, Integer.parseInt(dia_f), Integer.parseInt(hora_f), Integer.parseInt(minuto_f));

				if(data_atual.after(data_inicial) && data_atual.before(data_final)){
				    return 1;
				}
				else{
					if(data_atual.after(data_final)){
						return 3;
					}
				    return 0;
				}
				
			} catch (FileNotFoundException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			} catch (Exception re) {}
		return 2;

	}
	
	public String listar_eleicoes(String texto){
		
		String eleicoes = "";
		try{
		    String line = "";
		    BufferedReader readerfile = new BufferedReader(new FileReader("eleicoes.txt"));
		    eleicoes = eleicoes + texto + "-\n";
		    line = readerfile.readLine();
		    while (line != null){ //conta numero de linhas do fich txt em questao e adiciona linhas do fich ao arraylist
		        eleicoes = eleicoes + line + "\n";
		        line = readerfile.readLine();
		    }
		
		} catch (FileNotFoundException e) {
		    System.out.println("An error occurred.");
		    e.printStackTrace();

		} catch (Exception re) {}

		return eleicoes;
	    }


	public String selecionar_eleicao(String id, String IDpessoa, String dep){
		try{
			//1. verificar se de facto a mesa existe ou pertence a uma eleicao
			BufferedReader readerfile = new BufferedReader(new FileReader("mesas.txt"));
			String[] myList = null;
			String line = "";
			String eleicao = "";
			int presente = 0;

			line = readerfile.readLine();
			while (line != null){ 
					myList = line.split("-");
					for(int i = 1; i < myList.length; i++){
						if(dep.equals(myList[i])){ //se existir mesa
							presente = 1;
							eleicao = myList[0];
							break;
						}
					}
					if(presente == 1){
						break;
					}
					line = readerfile.readLine();
			}
			readerfile.close(); 
			
			if(presente == 0){
				return IDpessoa + "-" + "Esta mesa nao existe\n";
			}
			if(eleicao.equals(id)){
				try{
					BufferedReader readerfile2 = new BufferedReader(new FileReader("candidatos.txt"));
					String[] myList2 = null;
					String line2 = "";
					String[] aux = null;
					int existe = 0;
					String output = IDpessoa + "-";
					line2 = readerfile2.readLine();
					while (line2 != null){ 
							myList2 = line2.split("-");
							if(myList2[0].equals(id)){
								existe = 1;
								for(int i = 1;i<myList2.length;i++){
									aux = myList2[i].split(" ");
									output = output + "Candidato " + String.valueOf(i) + ": " + aux[0] + "\n";
								}
								break;
							}
							line2 = readerfile2.readLine();
					}
					readerfile2.close();
					if(existe == 1){
						return IDpessoa + "-" + "Selecionar" + "-" + id + "-" + output;
					}
					else{
						return IDpessoa + "-" + "Nao existe esta eleicao";
					}
				} catch (FileNotFoundException e) {
					System.out.println("An error occurred.");
					e.printStackTrace();
				}
			}
			else{
				return IDpessoa + "-" + "Nao pode votar nesta eleicao por esta mesa";
			}
		
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}catch (Exception re) {}

		return "nada";
	}	

	public String login(String texto){
		try{
			String[] aux = null;
			String[] aux2 = null;
			aux = texto.split(";");
			int existe = 0;
			int correta = 0;
			String line = null;
			BufferedReader readerfile = new BufferedReader(new FileReader("dados.txt"));
			int lines = 0;
			while (readerfile.readLine() != null) lines++;
			readerfile.close();
			String username = aux[1];
			aux2 = username.split("\\|");
			username = aux2[1];
			String password = aux[2];
			aux2 = password.split("\\|");
			password = aux2[1];
			try {
				for(int i = 0;i<lines;i=i+8){
					line = Files.readAllLines(Paths.get("dados.txt")).get(i);
					if((" " + line).equals(username)){
						existe = 1;
					}
					if(existe == 1){
						line = Files.readAllLines(Paths.get("dados.txt")).get(i+3);
						if((" " + line).equals(password)){
							correta = 1;
						}
						break;
					}
				}
				
				if (existe == 1 && correta == 1){
					return texto.split("-")[0] + "-" + "type | status; logged | on; msg | Welcome to eVoting";
				}
				if (existe == 0){
					return texto.split("-")[0] + "-" + "type | status; logged | off; msg | Username does not exist";
				}
				if (correta == 0){
					return texto.split("-")[0] + "-" + "type | status; logged | off; msg | Wrong password";
				}
			
			  
			} catch (FileNotFoundException e) {
			  System.out.println("An error occurred.");
			  e.printStackTrace();
			}
			
		} catch (Exception re) {}
		return "Nada";
	
	}
	
	public String mesa_voto(String texto, String texto2, String texto3){
		try{
			String[] myList = null;
			String line = "";
			String aux = "";
			String devolve = "";
			int lines = 0;
			BufferedReader readerfile = new BufferedReader(new FileReader("mesas.txt"));
			ArrayList<String> mesas = new ArrayList<String>();
			
			line = readerfile.readLine();
			while (line != null){ //conta numero de linhas do fich txt em questao e adiciona linhas do fich ao arraylist
					mesas.add(line);
					line = readerfile.readLine();
					lines++;
			}
			readerfile.close(); 
			
			int i=0, j=0;
			int existe = 0, existe_dep = 0; //var que diz se existe uma eleicao ou nao; 0 = nao ; 1 = sim
			try {
				while(i<lines && existe == 0){ //verifica se existe de facto a eleicao a que queres associar a mesa
					myList = null;
					line = Files.readAllLines(Paths.get("eleicoes.txt")).get(i);
					myList = line.split("-");
					if(texto.equals(myList[0])){
						existe = 1;
						break;
					}
					i = i+1; //i para na linha da eleicao encontrada
				}

				while(j<lines && existe_dep == 0){ //verifica se pode adicionar uma mesa de voto ao departamento pretendido
					myList = null;
					line = Files.readAllLines(Paths.get("mesas.txt")).get(j);
					myList = line.split("-");
					if(myList.length >= 2){
						if(texto2.equals(myList[1])){
							existe_dep = 1; //se existir nao pode adicionar pq cada departamento tem no max 1 mesa
							break;
						}
					}
					j = j+1; //i para na linha da eleicao encontrada
				}
				
				if(existe == 1){ //se a eleicao existe e o departamento nao tem mesa
					System.out.println("hello");
					aux = mesas.get(i);
					if(existe_dep == 0 && texto3.equals("Adicionar")){
						aux = aux + texto2 + "-";
						devolve = "Mesa de Voto Criada!";
					}
					else if(existe_dep == 1 && texto3.equals("Remover")){
						aux = aux.replace(texto2+"-", "");
						devolve = "Mesa de Voto Removida!";
					}
					else{
						devolve = "Nao aconteceu nada devido a parametros invalidos";
					}
					mesas.set(i,aux);
					BufferedWriter out = null;
					PrintWriter clean = new PrintWriter("mesas.txt");
					clean.print("");
					clean.close();
					try {
						FileWriter fstream = new FileWriter("mesas.txt", true);
						out = new BufferedWriter(fstream);
						for(int k = 0;k<mesas.size();k++){
						    out.write(mesas.get(k) + "\n");
						}
						return devolve;	    
					}

					catch (IOException e) {
						System.err.println("Error: " + e.getMessage());
					}

					finally {
						if(out != null) {
							out.close();
						}
					}	
				}
				else if(existe == 0){
					return "Esta eleicao nao existe";
				}
				else if(existe_dep == 1){
					return "Este departamento ja tem uma mesa de voto";
				}
			} catch (FileNotFoundException e) {
			  System.out.println("An error occurred.");
			  e.printStackTrace();
			}
		
	
		} catch (Exception re) {}



		return "nada";
	}
	

	public String add_candidato(String texto,String texto2){
		try{
			String[] myList = null; 
			String[] ListCand = null;
			String[] myList2 = null;
			String[] myList3 = null;
			String aux = "";
			ArrayList<String> cands = new ArrayList<String>();
			boolean count = true;
			String line = "";
			int lines = 0;
			int linesdados = 0;
			
			BufferedReader readerfile2 = new BufferedReader(new FileReader("candidatos.txt"));
			
			line = readerfile2.readLine();
			while (line != null){
				cands.add(line);
				line = readerfile2.readLine();
				lines++;
			}
			
			BufferedReader readerfiledados = new BufferedReader(new FileReader("dados.txt"));
			
			line = readerfiledados.readLine();
			while (line != null){
				line = readerfiledados.readLine();
				linesdados++;
			}
			int i=0;
			int existe = 0;
			try {
				while(i<lines && existe == 0){
					myList = null;
					line = Files.readAllLines(Paths.get("eleicoes.txt")).get(i);
					myList = line.split("-");
					if(texto.equals(myList[0])){
						existe = 1;
						break;
					}
					i = i+1;
				}
				String pessoa = null;
				String restricao = null;
				String wtv = null;
				int verifica = 0;
				int contador = 0;
				int contador2 = 0;
				for (int k = 0;k<linesdados;k=k+8){
					pessoa = Files.readAllLines(Paths.get("dados.txt")).get(k);
					myList3 = texto2.split(" ");
					
					if (pessoa.equals(myList3[0])){
						verifica = 1;
					}
					if(verifica == 1){
						restricao = Files.readAllLines(Paths.get("dados.txt")).get(k+4);
						wtv = myList[7];
						myList2 = wtv.split(" ");
						for(String a : myList2){
							if(a.equals(restricao)){
								contador = 1;
							}
						}
						myList2 = null;
						restricao = Files.readAllLines(Paths.get("dados.txt")).get(k+1);
						wtv = myList[8];
						myList2 = wtv.split(" ");
						for(String a : myList2){
							if(a.equals(restricao)){
								contador2 = 1;
							}
						}
					}
				}
				
				if(verifica == 0){
					return "Este Numero nao esta registado";
				}
				
				if(contador == 0 || contador2 == 0){
					return "Nao cumpre os requisitos";
				}
				
				if(existe == 1){
					line = Files.readAllLines(Paths.get("candidatos.txt")).get(i);
					ListCand = line.split("-");
					for(String a : ListCand){
						if(a.equals(texto2)){
							count = false;
							return "Este usuario ja esta na lista!";
						}
					}
					if(count){
						aux = cands.get(i);
						aux = aux + texto2 + "-";
						cands.set(i,aux);
						BufferedWriter out = null;
						PrintWriter clean = new PrintWriter("candidatos.txt");
						clean.print("");
						clean.close();
						try {
						    FileWriter fstream = new FileWriter("candidatos.txt", true);
						    out = new BufferedWriter(fstream);
						    for(int k = 0;k<cands.size();k++){
						    	out.write(cands.get(k)+"\n");
						    }
						    return "Candidatura Concluida!";
						    
						}

						catch (IOException e) {
						    System.err.println("Error: " + e.getMessage());
						}

						finally {
						    if(out != null) {
							out.close();
						    }
						}
					}
						
				}
				else{
					return "Esta eleicao nao existe";
				}
			} catch (FileNotFoundException e) {
			  System.out.println("An error occurred.");
			  e.printStackTrace();
			}
		
	
		} catch (Exception re) {}
		return "nada";
	}
	
	
	public String add_reuniao(String texto,int lulz){
		try {
			int existe = 0;
			String line = null;
			BufferedReader readerfile = new BufferedReader(new FileReader("eleicoes.txt"));
			int lines = 0;
			while (readerfile.readLine() != null) lines++;
			readerfile.close();
			try {
				for(int i = 0;i<lines;i=i+1){
					line = Files.readAllLines(Paths.get("eleicoes.txt")).get(i);
					if(line.split("-")[0].equals(texto)){
						System.out.println("Ja existe uma reuniao com este ID!");
						existe = 1;
						return "Ja existe uma reuniao com este ID!";
					}
				}
				if(lulz == 1 && existe == 0){
					BufferedWriter out = null;
					try {
					    	FileWriter fstream2 = new FileWriter("candidatos.txt", true);
                        			out = new BufferedWriter(fstream2);
                        			out.write(texto + "-" + "\n");
					}

					catch (IOException e) {
					    System.err.println("Error: " + e.getMessage());
					}

					finally {
					    if(out != null) {
						out.close();
					    }
					}

					try {
						FileWriter fstreamMesa = new FileWriter("mesas.txt", true);
						out = new BufferedWriter(fstreamMesa);
						out.write(texto + "-" + "\n");
					}

					catch (IOException e) {
					    System.err.println("Error: " + e.getMessage());
					}

					finally {
					    if(out != null) {
						out.close();
					    }
					}
					try {
					    	FileWriter fstreamvotos = new FileWriter("votos.txt", true);
                        			out = new BufferedWriter(fstreamvotos);
                        			out.write(texto + "-" + "Branco 0-\n");
					}

					catch (IOException e) {
					    System.err.println("Error: " + e.getMessage());
					}

					finally {
					    if(out != null) {
						out.close();
					    }
					}
				}
				if(lulz == 1 && existe == 0){
					BufferedWriter out2 = null;
					try {
					    FileWriter fstream4 = new FileWriter("eleicoes.txt", true);
					    out2 = new BufferedWriter(fstream4);
					    out2.write(texto + "-");
					}

					catch (IOException e) {
					    System.err.println("Error: " + e.getMessage());
					}

					finally {
					    if(out2 != null) {
						out2.close();
					    }
					}
				}
				if(existe == 0 && lulz == 2){
					BufferedWriter out = null;
					try {
					    FileWriter fstream3 = new FileWriter("eleicoes.txt", true);
					    out = new BufferedWriter(fstream3);
					    out.write(texto + "-" + "\n");
					    
					}

					catch (IOException e) {
					    System.err.println("Error: " + e.getMessage());
					}

					finally {
					    if(out != null) {
						out.close();
					    }
					}
				}
			} catch (FileNotFoundException e) {
			  System.out.println("An error occurred.");
			  e.printStackTrace();
			}
			if(existe == 0 && lulz == 0){
				BufferedWriter out = null;
				try {
				    FileWriter fstream = new FileWriter("eleicoes.txt", true);
				    out = new BufferedWriter(fstream);
				    out.write(texto + "-");
				    
				}

				catch (IOException e) {
				    System.err.println("Error: " + e.getMessage());
				}

				finally {
				    if(out != null) {
					out.close();
				    }
				}
			}
			
		} catch (Exception re) {}
		return "Nada";
			
	}
	
	
	public String write_file(String texto){
		try {
			int existe = 0;
			String line = null;
			BufferedReader readerfile = new BufferedReader(new FileReader("dados.txt"));
			int lines = 0;
			while (readerfile.readLine() != null) lines++;
			readerfile.close();
			try {
				for(int i = 0;i<lines;i=i+8){
					line = Files.readAllLines(Paths.get("dados.txt")).get(i);
					if(line.equals(texto)){
						System.out.println("Este Numero ja foi registado!");
						existe = 1;
						return "Este Numero ja foi registado!";
					}
				}
			  
			} catch (FileNotFoundException e) {
			  System.out.println("An error occurred.");
			  e.printStackTrace();
			}
			
			if(existe == 0){
				BufferedWriter out = null;
				try {
				    FileWriter fstream = new FileWriter("dados.txt", true);
				    out = new BufferedWriter(fstream);
				    out.write(texto + "\n");
				    
				}

				catch (IOException e) {
				    System.err.println("Error: " + e.getMessage());
				}

				finally {
				    if(out != null) {
					out.close();
				    }
				}
			}
			
		} catch (Exception re) {}
		return "Nada";
	}
	
	
	public RMIServer() throws RemoteException {
		super();
	}


	// =========================================================
	public static void main(String args[]) {

		try {
			RMIServer rmi = new RMIServer();
			Registry r = LocateRegistry.createRegistry(7000); //PRIMARIO 7000 ; SECONDARIO 6999
			r.rebind("RMIServer", rmi);
			System.out.println("RMI Server waiting for command");
			
	
		} catch (RemoteException re) {
			System.out.println("Exception in RMIServer.main: " + re);
		} 

	}

}

