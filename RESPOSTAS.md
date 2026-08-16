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

1.
2.
3.

## Parte C — Multicast

1.
2.
3.

## Parte D — WebSocket

1.
2.
3.
