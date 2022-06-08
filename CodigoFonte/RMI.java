import java.rmi.*;
public interface RMI extends Remote {
	public String sendCommand(String texto) throws java.rmi.RemoteException;
	public String write_file(String texto) throws java.rmi.RemoteException;
	public String add_reuniao(String texto,int lulz) throws java.rmi.RemoteException;
	public String add_candidato(String texto,String texto2) throws java.rmi.RemoteException;
	public String mesa_voto(String texto,String texto2, String texto3) throws java.rmi.RemoteException;
	public String login(String texto) throws java.rmi.RemoteException;
	public String listar_eleicoes(String texto) throws java.rmi.RemoteException;
	public String selecionar_eleicao(String id,String IDpessoa, String dep) throws java.rmi.RemoteException;
	public String Votar(String username, String eleicao, String candidato, String ID,String dep) throws java.rmi.RemoteException;
	public String editar_eleicao(String ID,String alteracao) throws java.rmi.RemoteException;
	public String localvotou(String IDpessoa, String IDeleicao) throws java.rmi.RemoteException;
	public String resultados_eleicao(String eleicao) throws java.rmi.RemoteException;
	public int set_nova_ligacao(String tipo, String id) throws java.rmi.RemoteException;
	public String get_nova_ligacao() throws java.rmi.RemoteException;
	public int set_num_eleitores(String id, int eleitores) throws java.rmi.RemoteException;
	public String get_num_eleitores(String eleicao) throws java.rmi.RemoteException;
}
