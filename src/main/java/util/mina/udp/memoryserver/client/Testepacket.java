package util.mina.udp.memoryserver.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Testepacket {

	public static void main(String args[]) throws Exception {

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

		DatagramSocket clientSocket = new DatagramSocket();

		String servidor = "179.113.253.219";
		int porta = 29071;
		
		InetAddress IPAddress = InetAddress.getByName(servidor);

		byte[] sendData = new byte[1024];
		sendData[0] = (byte) 0xFF;
		sendData[1] = (byte) 0xFF;
		sendData[2] = (byte) 0xFF;
		sendData[3] = (byte) 0xFF;
		byte[] receiveData = new byte[1024];

		System.out.println("Digite o texto a ser enviado ao servidor: ");
		String sentence = inFromUser.readLine();
		
		sendData = sentence.getBytes();
		
		byte[] stringBytes = sentence.getBytes();

		byte[] allBytes = new byte[stringBytes.length + 4];
		allBytes[0] = (byte) 0xFF;
		allBytes[1] = (byte) 0xFF;
		allBytes[2] = (byte) 0xFF;
		allBytes[3] = (byte) 0xFF;

		byte[] ba = (sentence).getBytes();

		System.arraycopy(ba, 0, allBytes, 4, stringBytes.length);
		System.out.println("mensagem enviada "+new String(allBytes));
		DatagramPacket sendPacket = new DatagramPacket(allBytes, allBytes.length, IPAddress, porta);

		System.out.println("Enviando pacote UDP para " + servidor + ":" + porta);
		clientSocket.send(sendPacket);

		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

		clientSocket.receive(receivePacket);
		System.out.println("Pacote UDP recebido...");

		String modifiedSentence = new String(receivePacket.getData());

		System.out.println("Texto recebido do servidor:" + modifiedSentence);
		clientSocket.close();
		System.out.println("Socket cliente fechado!");
	}

}
