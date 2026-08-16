import java.net.*;
import java.io.IOException;

public class ClienteMulticast {
    static final int OFFSET = 30;

    public static void main(String[] args) throws IOException {
        String grupoMulticast = "230.0.0.1";
        int porta = 4446 + OFFSET;

        try (MulticastSocket socket = new MulticastSocket(porta)) {
            InetAddress grupo = InetAddress.getByName(grupoMulticast);
            InetSocketAddress endpointGrupo = new InetSocketAddress(grupo, porta);

            // Testando servidor e cliente na MESMA máquina: usar a interface de loopback
            // explicitamente, porque InetAddress.getLocalHost() pode resolver para uma
            // interface sem rota real (ex.: adaptador virtual desconectado).
            NetworkInterface interfaceRede = NetworkInterface.getByInetAddress(InetAddress.getLoopbackAddress());

            socket.joinGroup(endpointGrupo, interfaceRede);
            System.out.println("[Multicast] Inscrito no grupo " + grupoMulticast + ":" + porta + ". Aguardando avisos...");

            byte[] buffer = new byte[1024];
            while (true) {
                DatagramPacket pacote = new DatagramPacket(buffer, buffer.length);
                socket.receive(pacote);
                String mensagem = new String(pacote.getData(), 0, pacote.getLength());
                System.out.println("[Multicast] Recebido: " + mensagem);
            }
        }
    }
}
