package org.example;

import org.pcap4j.core.*;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.Packet;
import java.util.List;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) throws PcapNativeException, NotOpenException, InterruptedException {
        String chosenInterface = getInterface();//Using user input choose an interface
        PcapNetworkInterface networkInterface = Pcaps.getDevByName(chosenInterface);//Get the interface object


        //Params for incoming packets
        int snapshotLength = 65536;
        int readTimeout = 50;
        final PcapHandle handle = networkInterface.openLive(snapshotLength, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, readTimeout);


        //Deals with the packets. More specifically, gotPacket() does
        PacketListener listener = new PacketListener() {
            @Override
            public void gotPacket(Packet packet) {
                System.out.println(packet.toString());
                EthPacket ep = new EthPacket(packet.getRawData());


                /*byte[] bytes = packet.getRawData();
                StringBuilder binary = new StringBuilder();
                for (byte b : bytes) {
                    binary.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
                    binary.append(" ");
                }
                System.out.println(binary.toString() +"\n");
                */

            }
        };

        //Keeps the interface open and then uses the listener object to handle the packet
        handle.loop(0,listener);

    }

    /*
    This function lists the available interfaces on the device, and then you choose one
    This needs to be improved for invalid choice
     */
    static String getInterface() {
        try{
            List<PcapNetworkInterface> interfaces = Pcaps.findAllDevs();

            System.out.print("Choose one of the available interfaces: ");
            for(PcapNetworkInterface p : interfaces)System.out.println(p.getName());

            Scanner myObj = new Scanner(System.in);
            String nif = myObj.nextLine();
            return nif;

        } catch (PcapNativeException e){
            System.out.println(e.getStackTrace());
        }
        return "Invalid Interface";
    }

}