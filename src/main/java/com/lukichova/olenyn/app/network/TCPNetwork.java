package com.lukichova.olenyn.app.network;


import com.lukichova.olenyn.app.Exceptions.*;
import com.lukichova.olenyn.app.classes.PacketProcessing;
import com.lukichova.olenyn.app.classes.Processor;
import com.lukichova.olenyn.app.entities.Packet;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.lukichova.olenyn.app.resoures.Resoures.NETWORK_HOST;
import static com.lukichova.olenyn.app.resoures.Resoures.NETWORK_PORT;

public class TCPNetwork implements Network {

    Socket socket;
    ServerSocket serverSocket;

    OutputStream socketOutputStream;
    InputStream serverInputStream;


    public TCPNetwork(Socket socket) throws wrongConnectionException {

        this.socket = socket;
        try {
            socketOutputStream = socket.getOutputStream();
            serverInputStream = socket.getInputStream();
        } catch (IOException e) {
            throw new wrongConnectionException("Wrong TCPNetwork connection");

        }

    }

    @Override
    public void listen() throws IOException {

    }

    @Override
    public Packet receive() throws IOException, interruptedConnectionException {
        InputStream serverInputStream = socket.getInputStream();

        try {
            byte maxPacketBuffer[] = new byte[Packet.packetMaxSize];
            PacketProcessing pr = new PacketProcessing();
            byte fullPacket[] = pr.processing(serverInputStream, maxPacketBuffer);

           // TimeUnit.SECONDS.sleep(1000);

            System.out.println("Received");
            System.out.println(Arrays.toString(fullPacket) + "\n");

            Packet packet = new Packet(fullPacket);
            System.err.println(packet.getBMsq().getMessage());

            if (serverSocket != null) {
                Processor.process(packet);
                send(packet);

            } else {
                return packet;
            }
        } catch (IOException e) {

            for(int i=0;i<5;i++)
             {
                try {
                    this.socket = new Socket(NETWORK_HOST, NETWORK_PORT);
                    return receive();

                } catch(IOException S) {

                    try {
                        TimeUnit.MINUTES.sleep(3);
                    } catch(InterruptedException ie) {

                    }
                }

            }
            throw new interruptedConnectionException();



        } catch (Exception | wrongDecryptException e) {
            System.err.println("Error:" + socket);
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void connect() throws IOException {

    }

    @Override
    public void send(Packet packet) throws wrongSendException {
        try {
            byte[] packetBytes = new byte[0];

            packetBytes = packet.toPacket();


        socketOutputStream.write(packetBytes);
        socketOutputStream.flush();

        System.out.println("Send");
      }
        catch (Exception e) {
           throw new wrongSendException();
        }

    }

    @Override
    public void close() throws wrongCloseSocketException {
        try {
            socket.close();
        } catch (IOException e) {
            throw new wrongCloseSocketException();
        }
    }

}
