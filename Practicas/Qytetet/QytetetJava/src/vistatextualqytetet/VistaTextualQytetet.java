/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistatextualqytetet;
import controladorqytetet.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author enrique
 */
public class VistaTextualQytetet {
    private ControladorQytetet controlador;
    private static final Scanner in = new Scanner(System.in);
    
    public VistaTextualQytetet()
    {
        controlador = ControladorQytetet.getInstance();
    }
    
    public ArrayList<String> obtenerNombreJugadores() {
        
        ArrayList<String> nombres = new ArrayList<>();
        
        System.out.println("Introduzca el número de jugadores que desee");
        boolean numeroValido = false;
        int n;
        do
        {
            n = in.nextInt();
            if(n>1 && n<10)
                numeroValido = true;
            else
            {
                System.out.println("\nIntroduzca un número de jugadores entre 2 y 10");
            }
        } while(!numeroValido);
        

        for(int i = 0; i < n; ++i)
        {
            int numjugador = i+1;
            System.out.println("Introduzca el jugador " + numjugador);
            String s = in.next();
            nombres.add(s);
        }        
        return nombres; 
    }
    
    public int elegirCasilla(int opcionMenu)
    {
        ArrayList<Integer> casillasValidas = controlador.obtenerCasillasValidas(opcionMenu);
        if(casillasValidas.isEmpty())
            return -1;
        else
        {
            ArrayList<String> cadenasCasillasValidas = new ArrayList<>();
            System.out.println("\nCASILLAS VÁLIDAS (-1 para volver atrás):");
            for(int i=0; i < casillasValidas.size(); ++i)
            {
                String opcion = Integer.toString(casillasValidas.get(i));
                cadenasCasillasValidas.add(opcion);
                System.out.println(opcion);
            }
            cadenasCasillasValidas.add("-1"); // Para poder volver atrás
            int opcionValida = Integer.parseInt(leerValorCorrecto(cadenasCasillasValidas));
            return opcionValida;
        }
    }
    
    public String leerValorCorrecto(ArrayList<String> valoresCorrectos)
    {
        String s;
        do
        {
            System.out.println("Introduzca una opción: ");
            s = in.next();
        } while(!valoresCorrectos.contains(s));
        return s;
    }
    
    public int elegirOperacion()
    {
        ArrayList<Integer> operacionesValidas = controlador.obtenerOperacionesJuegoValidas();
        ArrayList<String> cadenasOperacionesValidas = new ArrayList<>();
        System.out.println("\nOPERACIONES:");
        for(int i=0; i < operacionesValidas.size(); ++i)
        {
            String opcion = Integer.toString(operacionesValidas.get(i));
            cadenasOperacionesValidas.add(opcion);
            System.out.println(opcion + ": " + OpcionMenu.values()[operacionesValidas.get(i)]);
        }
        int opcionValida = Integer.parseInt(leerValorCorrecto(cadenasOperacionesValidas));
        return opcionValida;
    }
    
    public static void main(String args[]) {
        VistaTextualQytetet ui = new VistaTextualQytetet();
        ui.controlador.setNombreJugadores(ui.obtenerNombreJugadores());
        int operacionElegida, casillaElegida = 0;
        boolean necesitaElegirCasilla;
        do {
            operacionElegida = ui.elegirOperacion();
            necesitaElegirCasilla = ui.controlador.necesitaElegirCasilla(operacionElegida);
            if (necesitaElegirCasilla)
                casillaElegida = ui.elegirCasilla(operacionElegida);
            if (!necesitaElegirCasilla || casillaElegida >= 0)
                System.out.println(ui.controlador.realizarOperacion(operacionElegida,casillaElegida));
        } while (operacionElegida != OpcionMenu.TERMINARJUEGO.ordinal() &&
                (operacionElegida != OpcionMenu.OBTENERRANKING.ordinal()));
    }
}
