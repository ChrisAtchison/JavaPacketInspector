package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class OutputToFile {
    FileWriter writer;
    boolean rawHex;
    int amountToRecord;
    OutputToFile(String fileName, boolean rawHex, int amountToRecord){
        try {
            this.writer = new FileWriter(fileName);
            this.rawHex = rawHex;
            this.amountToRecord = amountToRecord;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    void writeToFile(String packetToWrite){
        try{
            writer.write(packetToWrite);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    void close(){
        try{
            writer.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    static OutputToFile getOutputToFile(){
        Scanner inputScanner = new Scanner(System.in);
        boolean rawHex;
        int amountToRecord;
        try{
            System.out.println("Would you like to output the packets to a file? y/n: ");
            String ans = inputScanner.nextLine();

            if(!ans.toLowerCase().equals("y"))return null;

            System.out.println("Would you like the packets in Raw Hex [1], or already decoded [2]? ");
            ans = inputScanner.nextLine();

            if(ans.equals("1"))rawHex = true;
            else rawHex = false;

            System.out.println("Would you like to record a certain amount of packets? Either input the amount to record, or press Enter for no.");
            ans = inputScanner.nextLine();

            if(ans.equals(""))amountToRecord = -1;
            else amountToRecord = Integer.parseInt(ans);

            System.out.println("What would you like to call the file? ");
            ans = inputScanner.nextLine();

            return new OutputToFile(ans, rawHex, amountToRecord);

        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
