import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class EchoClient {
	private DatagramSocket socket;
	private InetAddress address;

	private byte[] buf;

	public EchoClient() throws SocketException, UnknownHostException {
		socket = new DatagramSocket();
		address = InetAddress.getByName("localhost");
	}

	public String sendEcho(String msg) throws IOException {
		buf = msg.getBytes();
		byte[] stringBytes = msg.getBytes();
		byte[] allBytes = new byte[stringBytes.length + 4];
		allBytes[0] = (byte) 0xFF;
		allBytes[1] = (byte) 0xFF;
		allBytes[2] = (byte) 0xFF;
		allBytes[3] = (byte) 0xFF;

		System.arraycopy(stringBytes, 0, allBytes, 4, stringBytes.length);
		buf=allBytes;
		
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 29072);
		socket.send(packet);
		packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);
		String received = new String(packet.getData(), 0, packet.getLength());
		return received;
	}

	public void close() {
		socket.close();
	}
	
	public static void main(String[] args) throws IOException {
		EchoClient client = new EchoClient();
		String echo = client.sendEcho("rcon rconbobomg devmap legosw\n");
		System.out.println(echo);
	}
}