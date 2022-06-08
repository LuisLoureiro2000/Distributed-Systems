Esta pasta deve conter:
-PDF do relátorio
-README.TXT
-5 ficheiros código fonte .java: RMI.java, RMIServer.java, MulticastServer.java, MulticastClient.java e AdminConsole.java
-5 ficheiros .jar que contêm os ficheiros .class gerados a partir dos ficheiros .java: rmi.jav, server.jar, terminal.jar, rmiserver.jar e console.jar
-7 ficheiros .txt:
*dados.txt - contém registos de utilizadores
*eleicoes.txt - contém registos de eleições
*candidatos.txt - contém listas de candidatos para as eleições criadas
*mesas.txt - contém as mesas de voto para as eleições criadas
*votos.txt - contém informação sobre todos os votos das eleições criadas (quem votou, em que departamento, a que hora)
*ligacoes.txt - contém informação sobre as mesas e terminais de voto online e offline para satisfazer o ponto 11 do enunciado
*eleitores.txt - contém informação sobre o número de eleitores que votou numa certa mesa de voto de todas as eleições para satisfazer o ponto 12 do enunciado

-----------
EXECUÇÃO

Para conseguir executar o projeto deve bastar compilar os ficheiros .java, desde que contenha os ficheiros .txt na mesma pasta que os ficheiros .java.
No entanto, como pedido também existem os 5 ficheiros .jar que contêm as os ficheiros .class.

Os ficheiros ligacoes.txt e eleitores.txt devem estar sempre em branco quando se executa o projeto.

No admin console, os comandos possíveis são:
-"Registo" 
-"Criar eleicao"
-"Candidatar a eleicao"
-"Mesa de voto"
-"Editar eleicao"
-"Verificar onde votou"
-"Consultar eleicao" seguido de id de eleição na linha seguinte
-"Mostra ligacoes"
-"Mostra numero de eleitores" seguido de id de eleição na linha seguinte

No multicast client, os comandos possíveis são:
-"type | login; username | <username>; password | <password>"
-"type | Listar eleicoes"
-"type | Selecionar eleicao; eleicao | <id de eleicao>"
-"type | Votar; username | <username>; eleicao | <id de eleicao>; candidato | <id de candidato>"
