/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;
import java.util.ArrayList;

/**
 *
 * @author maxigang
 */
public class Tablero {
    
    private ArrayList <Casilla> casillas;
    private Casilla salida;
    private Casilla carcel;
    private int NUM_CASILLAS;
    
    
    public Tablero(){
        inicializar();
    }
    
    boolean esCasillaCarcel(int numeroCasilla){
        return casillas.get(numeroCasilla).getTipo() == TipoCasilla.CARCEL;
    }
    
    ArrayList<Casilla> getCasillas() {
        return casillas;
    }

    Casilla getCarcel() {
        return carcel;
    }
    
    Casilla getSalida() {
        return salida;
    }
    
    Casilla obtenerCasillaFinal(Casilla casilla, int desplazamiento){
        return casillas.get((casilla.getNumeroCasilla()+desplazamiento)%20);
    }
    
    Casilla obtenerCasillaNumero(int numeroCasilla){
        return casillas.get(numeroCasilla);
    }

    @Override
    public String toString() {
        return "Tablero{" + "casillas=" + casillas + ", carcel=" + carcel + '}';
    }
    
    private void inicializar() {
        
        casillas = new ArrayList<>();
        
        
        
        TituloPropiedad t0 = new TituloPropiedad("Astera", 500, 50, 
        150, 250, 0.1);
        TituloPropiedad t1 = new TituloPropiedad("Weyard", 500, 50, 
        150, 250, 0.1);
        TituloPropiedad t2 = new TituloPropiedad("Sinnoh", 550, 55, 
        200, 275, 0.1);
        TituloPropiedad t3 = new TituloPropiedad("Aselia", 600, 60, 
        300, 300, 0.1);
        TituloPropiedad t4 = new TituloPropiedad("Gaia", 650, 65, 
        400, 350, 0.1);
        TituloPropiedad t5 = new TituloPropiedad("Montepuerco", 650, 65, 
        400, 350, 0.1);
        TituloPropiedad t6 = new TituloPropiedad("Carrera Blanca", 700, 70, 
        500, 400, 0.15);
        TituloPropiedad t7 = new TituloPropiedad("Dalaran", 700, 70, 
        500, 400, 0.15);
        TituloPropiedad t8 = new TituloPropiedad("Demacia", 800, 80, 
        600, 500, 0.15);
        TituloPropiedad t9 = new TituloPropiedad("Reach", 900, 90, 
        800, 600, 0.2);
        TituloPropiedad t10 = new TituloPropiedad("Rapture", 950, 95, 
        850, 700, 0.2);
        TituloPropiedad t11 = new TituloPropiedad("Freedom Dive", 1000, 100, 
        1000, 750, 0.2);
        
        Casilla c0 = new OtraCasilla(0, -1000, TipoCasilla.SALIDA);
        Casilla c1 = new Calle(1, t0);
        Casilla c2 = new Calle(2, t1);
        Casilla c3 = new OtraCasilla(3, 0, TipoCasilla.SORPRESA);
        Casilla c4 = new Calle(4, t2);
        Casilla c5 = new OtraCasilla(5, 0, TipoCasilla.CARCEL);
        Casilla c6 = new Calle(6, t3);
        Casilla c7 = new Calle(7, t4);
        Casilla c8 = new OtraCasilla(8, 0, TipoCasilla.SORPRESA);
        Casilla c9 = new Calle(9, t5);
        Casilla c10 = new OtraCasilla(10, 0, TipoCasilla.PARKING);
        Casilla c11 = new Calle(11, t6);
        Casilla c12 = new Calle(12, t7);
        Casilla c13 = new OtraCasilla(13, 0, TipoCasilla.SORPRESA);
        Casilla c14 = new Calle(14, t8);
        Casilla c15 = new OtraCasilla(15, 0, TipoCasilla.JUEZ);
        Casilla c16 = new Calle(16, t9);
        Casilla c17 = new OtraCasilla(17, 500, TipoCasilla.IMPUESTO);
        Casilla c18 = new Calle(18, t10);
        Casilla c19 = new Calle(19, t11);
        
        casillas.add(c0); casillas.add(c1); casillas.add(c2); casillas.add(c3);
        casillas.add(c4); casillas.add(c5); casillas.add(c6); casillas.add(c7);
        casillas.add(c8); casillas.add(c9); casillas.add(c10); casillas.add(c11);
        casillas.add(c12); casillas.add(c13); casillas.add(c14); casillas.add(c15);
        casillas.add(c16); casillas.add(c17); casillas.add(c18); casillas.add(c19);
        
        carcel = c5;
        salida = c0;
    }
}
