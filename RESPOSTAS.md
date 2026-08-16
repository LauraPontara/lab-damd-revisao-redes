# Respostas — Revisão de Redes de Computadores

## Parte A — TCP

1. Testei rodando o cliente antes do servidor, em Java e em Python, e ambos falham imediatamente ao tentar conectar:

   - Python: `ConnectionRefusedError: [WinError 10061] Nenhuma conexão pôde ser feita porque a máquina de destino as recusou ativamente`
   - Java: `java.net.ConnectException: Connection refused: connect`

   Isso acontece porque, sem o servidor rodando, não existe nenhum processo em estado de escuta (`LISTEN`) na porta 5000. O TCP exige que a conexão seja estabelecida por um *three-way handshake* (SYN → SYN-ACK → ACK) antes de qualquer troca de dados. Como não há um `ServerSocket` aberto naquela porta, o sistema operacional recusa o pedido na hora (pacote RST), e essa recusa chega até a aplicação cliente como a exceção de "connection refused". Isso é diferente do UDP, que não tem handshake prévio e por isso não gera esse erro imediato (ver comparação na Parte B).

2. O mecanismo é o **número de sequência** (sequence number) presente no cabeçalho de cada segmento TCP. Cada byte enviado numa conexão recebe um número de sequência único; o receptor usa esses números para reordenar segmentos que chegam fora de ordem (a rede pode entregá-los por caminhos e em tempos diferentes) antes de repassar os dados pra aplicação, e também pra detectar perdas e duplicações. A retransmissão (baseada em timeout/ACK) é um mecanismo relacionado, mas ela garante a **entrega confiável** (que o dado chegue), não a **ordem**. Quem garante a ordem especificamente é o número de sequência, que diz ao receptor em que posição cada byte deve entrar antes de remontar o fluxo original.

3. Testei esse caso: o primeiro cliente conecta e troca mensagens normalmente com o servidor; o segundo cliente chega a imprimir "[TCP] Conectado ao servidor. Digite 'sair' para encerrar.", mas nunca recebe resposta quando envia mensagens. O código atual **não suporta** múltiplos clientes simultâneos, e dá pra justificar olhando o `ServidorTCP.java`: o método `servidor.accept()` é chamado **uma única vez**, fora de qualquer laço, dentro do try-with-resources. `accept()` é o método que efetivamente retira uma conexão da fila de handshakes pendentes (backlog) do sistema operacional e entrega um `Socket` utilizável pra aplicação.

   Quando o segundo cliente tenta conectar, o handshake TCP a nível de sistema operacional é aceito, por isso ele consegue imprimir a mensagem de conectado (a conexão existe e fica na fila de backlog do `ServerSocket`), mas a aplicação Java nunca chama `accept()` de novo pra retirar essa segunda conexão da fila. Ela fica parada ali, sem que o servidor leia ou responda nada que esse cliente envie. Pra suportar múltiplos clientes seria necessário um laço em volta do `accept()` (ex.: `while (true) { Socket cliente = servidor.accept(); ... }`), tratando cada conexão aceita em uma thread separada.

## Parte B — UDP

1. Testei o cenário com o servidor desligado nas duas linguagens e o resultado foi diferente em cada uma:

   - Python: o cliente recebeu `ConnectionResetError: [WinError 10054] Foi forçado o cancelamento de uma conexão existente pelo host remoto`, ao tentar `recvfrom()` depois de mandar a mensagem.
   - Java: o cliente não recebeu erro nenhum, simplesmente travou parado em `socket.receive(resposta)`, esperando indefinidamente uma resposta que nunca chega.

   Depois testei o mesmo cenário (matar o servidor à força e mandar mais uma mensagem) na versão TCP em Java, pra comparar, e o cliente TCP recebeu imediatamente `java.net.SocketException: Connection reset`.

   A diferença faz sentido pelo conceito de "sem conexão": o TCP mantém estado de conexão nos dois lados, então quando o servidor cai, o sistema operacional consegue sinalizar isso ativamente ao cliente, que percebe a quebra na hora. Já o UDP não estabelece nem mantém nenhum estado de conexão entre as duas pontas, então não existe, no nível do protocolo, nenhum mecanismo padronizado pra avisar "o outro lado sumiu". O que aconteceu no Python (erro via ICMP "porta inacessível", traduzido pelo Windows numa exceção) foi um comportamento do sistema operacional, não do protocolo UDP em si. Por isso o mesmo cenário deu resultados diferentes em cada linguagem/implementação.

2. Dois exemplos de aplicações reais que usam UDP:

   - **Chamadas de vídeo/áudio em tempo real** (ex.: videochamadas, streaming ao vivo): se um pacote de áudio/vídeo se perde, não faz sentido esperar a retransmissão do TCP: quando o pacote perdido chegasse, o momento da conversa já teria passado. É preferível perder um quadro/frame e seguir em frente do que travar a chamada esperando confirmação de entrega.
   - **DNS** (consulta de nomes de domínio): é uma troca curta, de uma pergunta e uma resposta pequenas. O overhead de abrir uma conexão TCP (handshake) pra uma única troca rápida seria desproporcional; se a resposta se perder, é mais simples e rápido a aplicação simplesmente reenviar a consulta do que manter toda a estrutura de uma conexão TCP só pra isso.

3. Sim, seria possível implementar um registro de clientes conectados, mesmo o UDP não tendo conceito de conexão. Cada `DatagramPacket` recebido já carrega o endereço e a porta de quem enviou (`pacoteRecebido.getAddress()` / `getPort()` em Java, a tupla `endereco_cliente` em Python). Então o servidor poderia guardar esses endereços numa estrutura própria (ex.: um `Set` ou `Map`) toda vez que recebesse uma mensagem de um remetente novo.

   O que mudaria na arquitetura: como o UDP não avisa quando um cliente "desconecta" (não existe `accept()`/`close()` gerando esse evento automaticamente, como em TCP), a aplicação precisaria implementar sua própria lógica pra isso, por exemplo mensagens de heartbeat periódicas, ou remover da lista quem não manda nada por um tempo. Seria a própria aplicação recriando, na camada de cima, um controle de sessão que o TCP já oferece de graça na camada de transporte.

## Parte C — Multicast

1.
2.
3.

## Parte D — WebSocket

1.
2.
3.
