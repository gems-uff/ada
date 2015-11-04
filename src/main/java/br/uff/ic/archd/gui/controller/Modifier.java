/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.gui.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 *
 * @author wallace
 */
public class Modifier {
//    public static void main(String args[]){
//        try{
//            PrintWriter writer0 = new PrintWriter("/home/wallace/pessoal/tokusatsu/sentai/11-maskman/mask.txt", "UTF-8");
//            BufferedReader br = new BufferedReader(new  InputStreamReader(
//                      new FileInputStream("/home/wallace/pessoal/tokusatsu/sentai/11-maskman/maskman_port_01.srt"), "UTF-8"));
//            String sCurrentLine;
//            int i = 0;
//            while ((sCurrentLine = br.readLine()) != null) {
//                if(sCurrentLine.contains("-->")){
//                    i++;
//                    String sizes[] = sCurrentLine.split("-->");
//                    String part[] = sizes[0].split(",");
//                    String numeros[] = part[0].split(":");
//                    Integer segundos = Integer.valueOf(numeros[2]);
//                    Integer minutos = Integer.valueOf(numeros[1]);
//                    segundos = segundos + 15;
//                    if(segundos >= 60){
//                        segundos = segundos%60;
//                        minutos = minutos + 1;
//                    }
//                    writer0.print(numeros[0]+":");
//                    if(minutos < 10 ){
//                        writer0.print("0"+minutos+":");
//                    }else{
//                        writer0.print(minutos+":");
//                    }
//                    if(segundos < 10){
//                        writer0.print("0"+segundos+",");
//                    }else{
//                        writer0.print(segundos+",");
//                    }
//                    writer0.print(part[1]);
//                    
//                    writer0.print("-->");
//                    
//                    
//                    
//                    part = sizes[1].split(",");
//                    numeros = part[0].split(":");
//                    segundos = Integer.valueOf(numeros[2]);
//                    minutos = Integer.valueOf(numeros[1]);
//                    segundos = segundos + 15;
//                    if(segundos >= 60){
//                        segundos = segundos%60;
//                        minutos = minutos + 1;
//                    }
//                    writer0.print(numeros[0]+":");
//                    if(minutos < 10 ){
//                        writer0.print("0"+minutos+":");
//                    }else{
//                        writer0.print(minutos+":");
//                    }
//                    if(segundos < 10){
//                        writer0.print("0"+segundos+",");
//                    }else{
//                        writer0.print(segundos+",");
//                    }
//                    writer0.println(part[1]);
//                    
//                    System.out.println("Printalndo linha: "+i);
//                    
//                    
//                }else{
//                    writer0.println(sCurrentLine);
//                }
//            }
//            writer0.close();
//            br.close();
//        }catch(Exception e){
//            System.out.println("Erro: "+e.getMessage());
//            e.printStackTrace();
//        }
//    }
}
