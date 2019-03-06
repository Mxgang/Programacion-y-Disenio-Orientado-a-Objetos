/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorqytetet;

import java.util.ArrayList;
import modeloqytetet.MetodoSalirCarcel;
import modeloqytetet.EstadoJuego;
import modeloqytetet.Qytetet;
import modeloqytetet.Jugador;
import modeloqytetet.Casilla;
import java.util.Collections;
/**
 *
 * @author enrique
 */
public class ControladorQytetet {
    
    private ArrayList<String> nombreJugadores = new ArrayList<>();
    private Qytetet modelo = new Qytetet();
    public static final ControladorQytetet instance = new ControladorQytetet();
    
    public ControladorQytetet(){
        
    }
      
    public static ControladorQytetet getInstance()
    {
        return instance;
    }
    
    public void setNombreJugadores(ArrayList<String> nombreJugadores)
    {
        for (String nombre: nombreJugadores)
        {
            this.nombreJugadores.add(nombre);
        }
    }
    
    public ArrayList<Integer> obtenerOperacionesJuegoValidas()
    {
        ArrayList<Integer> operacionesValidas = new ArrayList<>();
        if(modelo.getJugadores().isEmpty())
        {
            operacionesValidas.add(OpcionMenu.INICIARJUEGO.ordinal());
        }
        else
        {
            EstadoJuego estadoJuego = modelo.getEstadoJuego();
            operacionesValidas.add(OpcionMenu.MOSTRARJUGADORACTUAL.ordinal());
            operacionesValidas.add(OpcionMenu.MOSTRARJUGADORES.ordinal());
            operacionesValidas.add(OpcionMenu.MOSTRARTABLERO.ordinal());
            operacionesValidas.add(OpcionMenu.TERMINARJUEGO.ordinal());
            switch(estadoJuego)
            {
                case JA_CONSORPRESA:
                {
                    operacionesValidas.add(OpcionMenu.APLICARSORPRESA.ordinal());
                    break;
                }
                case ALGUNJUGADORENBANCARROTA:
                {
                    operacionesValidas.add(OpcionMenu.OBTENERRANKING.ordinal());
                    break;
                }
                case JA_PUEDECOMPRAROGESTIONAR:
                {
                    operacionesValidas.add(OpcionMenu.PASARTURNO.ordinal());
                    operacionesValidas.add(OpcionMenu.VENDERPROPIEDAD.ordinal());
                    operacionesValidas.add(OpcionMenu.COMPRARTITULOPROPIEDAD.ordinal());
                    operacionesValidas.add(OpcionMenu.HIPOTECARPROPIEDAD.ordinal());
                    operacionesValidas.add(OpcionMenu.CANCELARHIPOTECA.ordinal());
                    operacionesValidas.add(OpcionMenu.EDIFICARCASA.ordinal());
                    operacionesValidas.add(OpcionMenu.EDIFICARHOTEL.ordinal());
                    break;
                }
                case JA_PUEDEGESTIONAR:
                {
                    operacionesValidas.add(OpcionMenu.PASARTURNO.ordinal());
                    operacionesValidas.add(OpcionMenu.VENDERPROPIEDAD.ordinal());
                    operacionesValidas.add(OpcionMenu.HIPOTECARPROPIEDAD.ordinal());
                    operacionesValidas.add(OpcionMenu.CANCELARHIPOTECA.ordinal());
                    operacionesValidas.add(OpcionMenu.EDIFICARCASA.ordinal());
                    operacionesValidas.add(OpcionMenu.EDIFICARHOTEL.ordinal());
                    break;
                }
                case JA_PREPARADO:
                {
                    operacionesValidas.add(OpcionMenu.JUGAR.ordinal());
                    break;
                }
                case JA_ENCARCELADO:
                {
                    operacionesValidas.add(OpcionMenu.PASARTURNO.ordinal());
                    break;
                }
                case JA_ENCARCELADOCONOPCIONDELIBERTAD:
                {
                    operacionesValidas.add(OpcionMenu.INTENTARSALIRCARCELPAGANDOLIBERTAD.ordinal());
                    operacionesValidas.add(OpcionMenu.INTENTARSALIRCARCELTIRANDODADO.ordinal());
                    break;
                }
            }
            Collections.sort(operacionesValidas);
        }
        return operacionesValidas;
    }
    
    public boolean necesitaElegirCasilla(int opcionMenu)
    {
        if(opcionMenu == OpcionMenu.HIPOTECARPROPIEDAD.ordinal() || 
           opcionMenu == OpcionMenu.CANCELARHIPOTECA.ordinal()   ||
           opcionMenu == OpcionMenu.EDIFICARCASA.ordinal()       ||
           opcionMenu == OpcionMenu.EDIFICARHOTEL.ordinal()      ||
           opcionMenu == OpcionMenu.VENDERPROPIEDAD.ordinal())
            return true;
        return false;
    }
    
    public ArrayList<Integer> obtenerCasillasValidas(int opcionMenu)
    {
        ArrayList<Integer> propiedades = new ArrayList<>();
        if(opcionMenu == OpcionMenu.HIPOTECARPROPIEDAD.ordinal())
            propiedades = modelo.obtenerPropiedadesJugadorSegunEstadoHipoteca(false);
        else if(opcionMenu == OpcionMenu.CANCELARHIPOTECA.ordinal())
            propiedades = modelo.obtenerPropiedadesJugadorSegunEstadoHipoteca(true);
        else if(opcionMenu == OpcionMenu.EDIFICARCASA.ordinal()  ||
                opcionMenu == OpcionMenu.EDIFICARHOTEL.ordinal() ||
                opcionMenu == OpcionMenu.VENDERPROPIEDAD.ordinal())
            propiedades = modelo.obtenerPropiedadesJugador();
        return propiedades;
    }
    
    public String realizarOperacion(int opcionElegida, int casillaElegida)
    {
        OpcionMenu opcionMenu = OpcionMenu.values()[opcionElegida];
        String mensaje = "";
        switch(opcionMenu)
        {
            case INICIARJUEGO:
            {
                mensaje =  "Se inicializa el juego";
                modelo.inicializarJuego(nombreJugadores);
                mensaje += "\nEmpieza el jugador " + modelo.getJugadorActual().toString();
                break;
            }
            case JUGAR:
            {
                Casilla casilla_inicial = modelo.getJugadorActual().getCasillaActual();
                mensaje = "Se tira el dado y sale un " + modelo.jugar();
                Casilla casilla_final = modelo.getJugadorActual().getCasillaActual();
                mensaje += "\nEl jugador se mueve de la casilla " + casilla_inicial.toString() +
                           " a la casilla " + casilla_final.toString();
                if(casilla_final.getNumeroCasilla() < casilla_inicial.getNumeroCasilla())
                    mensaje += "\nAdemás, el jugador pasa por la casilla de salida, " +
                    "por lo que cobra " + Qytetet.SALDO_SALIDA;
                break;
            }
            case APLICARSORPRESA:
            {
                mensaje = "Se saca la carta sorpresa:\n" + modelo.getCartaActual().toString();
                modelo.aplicarSorpresa();
                break;
            }
            case INTENTARSALIRCARCELPAGANDOLIBERTAD:
            {
                if(modelo.intentarSalirCarcel(MetodoSalirCarcel.PAGANDOLIBERTAD))
                    mensaje = "El jugador paga " + Qytetet.PRECIO_LIBERTAD + " por lo que sale de la cárcel";
                else
                    mensaje = "El jugador no tiene suficiente dinero para pagar la libertad";
                break;
            }
            case INTENTARSALIRCARCELTIRANDODADO:
            {
                if(modelo.intentarSalirCarcel(MetodoSalirCarcel.TIRANDODADO))
                    mensaje = "El jugador tira un dado y saca más de un 5, por lo que sale de la cárcel";
                else
                    mensaje =  "El jugador tira un dado y saca menos de un 5, por lo que no sale de la cárcel";
                break;
            }
            case COMPRARTITULOPROPIEDAD:
            {
                if(modelo.comprarTituloPropiedad())
                    mensaje = "El jugador compra la casilla";
                else
                    mensaje = "El jugador no tiene saldo suficiente para comprar la casilla";
                break;
            }
            case HIPOTECARPROPIEDAD:
            {
                if(modelo.hipotecarPropiedad(casillaElegida))
                    mensaje = "El jugador hipoteca la casilla elegida";
                else
                    mensaje = "No se puede hipotecar la casilla elegida";
                break;
            }
            case CANCELARHIPOTECA:
            {
                if(modelo.cancelarHipoteca(casillaElegida))
                    mensaje = "El jugador cancela la hipoteca de la casilla elegida";
                else
                    mensaje = "No se puede cancelar la hipoteca de la casilla elegida";
                break;
            }
            case EDIFICARCASA:
            {
                if(modelo.edificarCasa(casillaElegida))
                    mensaje = "El jugador edifica una casa en la casilla elegida";
                else
                    mensaje = "No se puede edificar una casa en la casilla elegida";
                break;
            }
            case EDIFICARHOTEL:
            {
                if(modelo.edificarHotel(casillaElegida))
                    mensaje = "El jugador edifica un hotel en la casilla elegida";
                else
                    mensaje = "No se puede edificar un hotel en la casilla elegida";
                break;
            }
            case VENDERPROPIEDAD:
            {
                if(modelo.venderPropiedad(casillaElegida))
                    mensaje = "El jugador vende la casilla elegida";
                else
                    mensaje = "No se puede vender la casilla elegida";
                break;
            }
            case PASARTURNO:
            {
                modelo.siguienteJugador();
                mensaje = "Se pasa el turno al jugador " + modelo.getJugadorActual().toString();
                break;
            }            
            case OBTENERRANKING:
            {
                mensaje = modelo.obtenerRanking();
                break;
            }  
            case MOSTRARTABLERO:
            {
                mensaje = modelo.getTablero().toString();
                break;
            }
            case MOSTRARJUGADORACTUAL:
            {
                mensaje = modelo.getJugadorActual().toString();
                break;
            }
            case MOSTRARJUGADORES:
            {
                mensaje = "Listado de jugadores:";
                for(Jugador j:modelo.getJugadores())
                    mensaje+= "\n" + j.toString();
                break;
            }
            case TERMINARJUEGO:
            {
                mensaje = "Partida abortada por el jugador";
                break;
            }
        }
        return mensaje;
    }
}
