import java.net.*;
import java.io.IOException;

public class ServidorMulticast {
    static final int OFFSET = 30;

    public static void main(String[] args) throws IOException, InterruptedException {
        String grupoMulticast = "230.0.0.1";
        int porta = 4446 + OFFSET;

        InetAddress grupo = InetAddress.getByName(grupoMulticast);
        try (MulticastSocket socket = new MulticastSocket()) {
            // Testando servidor e cliente na MESMA máquina: força a saída dos pacotes
            // pela interface de loopback, já que o ClienteMulticast entra no grupo por ela.
            socket.setNetworkInterface(NetworkInterface.getByInetAddress(InetAddress.getLoopbackAddress()));
            int contador = 1;
            System.out.println("[Multicast] Enviando avisos para o grupo " + grupoMulticast + ":" + porta);
            while (contador <= 5) {
                String mensagem = "Aviso #" + contador + ": a aula começa em " + (5 - contador) + " minuto(s)!";
                byte[] dados = mensagem.getBytes();
                DatagramPacket pacote = new DatagramPacket(dados, dados.length, grupo, porta);
                socket.send(pacote);
                System.out.println("[Multicast] Enviado: " + mensagem);
                contador++;
                Thread.sleep(2000);
            }
        }
    }
}
