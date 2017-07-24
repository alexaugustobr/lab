import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class App {

	private byte[] buf;

	public static void main(String[] args) throws Exception {
		// String theString = "rcon pass map";
		//
		// byte[] stringBytes = theString.getBytes();
		// byte[] allBytes = new byte[stringBytes.length + 4];
		// allBytes[0] = (byte) 0xFF;
		// allBytes[1] = (byte) 0xFF;
		// allBytes[2] = (byte) 0xFF;
		// allBytes[3] = (byte) 0xFF;
		//
		// System.arraycopy(stringBytes, 0, allBytes, 4, stringBytes.length);
		//
		//// DatagramSocket socket = new DatagramSocket();
		//
		// for (int iByte = 0; iByte < allBytes.length; iByte++)
		// System.out.println(iByte+"\t"+(char) allBytes[iByte]);
		//
		// InetAddress address = InetAddress.getByName("192.168.1.5");
		// DatagramPacket packet = new DatagramPacket(allBytes, allBytes.length,
		// address, 28070);
		// socket.send(packet);
		//
		// //for (int iByte = 0; iByte < allBytes.length; iByte++)
		// // System.out.println((char) allBytes[iByte]);
		
		
		//echo = client.sendEcho("getstatus");
		//System.out.println(echo);
		//client.close();
	}

	public void setup() throws SocketException, UnknownHostException {
		
	}

	public void whenCanSendAndReceivePacket_thenCorrect() {
		
	}

	public void tearDown() {
		
	}

}
