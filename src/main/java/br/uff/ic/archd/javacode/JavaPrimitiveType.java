/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.javacode;

/**
 *
 * @author wallace
 */
public class JavaPrimitiveType extends JavaData{
    
    public static int BYTE = 1;
    public static int SHORT = 2;
    public static int INT = 3;
    public static int LONG = 4;
    public static int FLOAT = 5;
    public static int DOUBLE = 6;
    public static int BOOLEAN = 7;
    public static int CHAR = 8;
        
    private int type;
    
    public JavaPrimitiveType(int type){
        this.type = type;
        if(type == 1){
            this.setName("byte");
        }else if(type == 2){
            this.setName("short");
        }else if(type == 3){
            this.setName("int");
        }else if(type == 4){
            this.setName("long");
        }else if(type == 5){
            this.setName("float");
        }else if(type == 6){
            this.setName("double");
        }else if(type == 7){
            this.setName("boolean");
        }else if(type == 8){
            this.setName("char");
        }
    }
    
    
    public int getType(){
        return type;
    }
    
    public static int getType(String type){
        if(type.equals("byte")){
            return BYTE;
        }else if(type.equals("short")){
            return SHORT;
        }else if(type.equals("int")){
            return INT;
        }else if(type.equals("long")){
            return LONG;
        }else if(type.equals("float")){
            return FLOAT;
        }else if(type.equals("double")){
            return DOUBLE;
        }else if(type.equals("boolean")){
            return BOOLEAN;
        }else if(type.equals("char")){
            return CHAR;
        }
        return 0;
    }
}
