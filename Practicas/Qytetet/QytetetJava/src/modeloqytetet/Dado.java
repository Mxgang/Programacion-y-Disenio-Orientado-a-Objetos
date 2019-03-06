/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;
import java.util.Random;
/**
 *
 * @author maxigang
 */
public class Dado {
    
    int valor;
    public static final Dado instance = new Dado();
    Random rand = new Random();
    
    public Dado(){
        
    }
      
    public static Dado getInstance()
    {
        return instance;
    }
    
    int tirar(){
        int val = rand.nextInt(5)+1;
        valor = val;
        return val;
    }
    
    public int getValor(){
        return valor;
    }
}
