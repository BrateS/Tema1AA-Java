package com.turingMachines;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.HashMap;
public class Main {

    public static void main(String[] args) {
        long start=System.currentTimeMillis();
        DoubleBandTM tm = new DoubleBandTM("task1.in");
        boolean notBlocked=tm.Run(true);
		if(notBlocked){
            System.out.println("Victory!");
        }else System.out.println("Don't lose hope.");
        System.out.println(tm.getTape1());
        System.out.println(tm.getTape2());
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
			String content="";
			if(notBlocked)content=(tm.getTape1()+"\n"+tm.getTape2()+"\n")
						.replace("#","").replace(" ","");
			else content="The machine has blocked!\n";

            fw = new FileWriter("task1.out");
            bw = new BufferedWriter(fw);
            bw.write(content);

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        long stop=System.currentTimeMillis();
        System.out.println(stop-start);
    }
}
