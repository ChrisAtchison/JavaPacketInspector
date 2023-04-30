package org.example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class PacketBuffer {
    BlockingQueue<L2Packet> queue;
    Filter filter;

    OutputToFile output;
    AtomicInteger count;
    PacketBuffer(Filter filter, OutputToFile output){
        this.queue = new LinkedBlockingQueue<L2Packet>();;
        this.filter = filter;
        this.output = output;
        this.count = new AtomicInteger();
    }
    void addPacket(L2Packet l2){
        try{
            this.queue.put(l2);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    void handlePacketsBuffer(){

        while(true){
            try {
                if(!this.queue.isEmpty()){
                    count.getAndIncrement();

                    L2Packet Ethernet = this.queue.take();
                    L3Packet l3 = PacketFactory.parseL3Packet(Ethernet);
                    L4Packet l4 = PacketFactory.parseL4Packet(l3);

                    if (filter.check(l3) && filter.check(l4)) {
                    System.out.println("Frame " + count.get() + ": "
                            + Ethernet.rawHex.length()/2 + " bytes captured (" + Ethernet.rawHex.length() * 4 + " bits) on interface "
                            + Ethernet.nif);

                    Ethernet.printAll();

                    if(l3 != null) l3.printAll();

                    if(l4 != null) l4.printAll();
                    System.out.println("-------------------");

                    if(output != null && !output.closed){
                        if(output.rawHex){
                            output.writeToFile(Ethernet.getRawHex());
                        }
                        else{
                            String outputLine = getTime() + " " + Ethernet.getString() + "\n                " + l3.getString() + "\n                " + l4.getString();

                            output.writeToFile(outputLine);
                        }
                    }
                }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
    static String getTime(){
        try{
            Date now = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSSSSS");
            String formattedTime = formatter.format(now);
            return formattedTime;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
