/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
/**
 *
 * @author maxigang
 */
public class Qytetet {
    
    private String texto;
    private int valor;
    private TipoSorpresa tipoSorpresa;
    private EstadoJuego estadoJuego;
    private ArrayList <Sorpresa> mazo = new ArrayList<>();
    private Tablero tablero;
    private Sorpresa cartaActual;
    private Jugador jugadorActual;
    private ArrayList<Jugador> jugadores = new ArrayList<>();
    private Dado dado;
    
    static public int MAX_JUGADORES = 4;
    static int NUM_SORPRESAS = 20;
    static public int NUM_CASILLAS = 20;
    static public int PRECIO_LIBERTAD = 200;
    static public int SALDO_SALIDA = 1000;
    
    //singleton
    private static final Qytetet instance = new Qytetet();
    
    public Qytetet(){
        dado = Dado.getInstance();
    }
    
    void actuarSiEnCasillaEdificable(){
        boolean deboPagar = jugadorActual.deboPagarAlquiler();
        boolean bancarrota = false;
        //System.out.println("El jugador cae en una casilla edificable.");
        if(deboPagar)
        {
            TituloPropiedad titulo = jugadorActual.getCasillaActual().getTitulo();
            //System.out.println("El jugador debe pagar al propietario de la casilla (" +
            //titulo.getPropietario().getNombre() +
            //        ") la cantidad de " + titulo.calcularImporteAlquiler());
            jugadorActual.pagarAlquiler();
            //System.out.println("\nEl jugador tiene ahora de saldo " + jugadorActual.getSaldo());
            if(jugadorActual.getSaldo() <= 0)
            {
                setEstadoJuego(EstadoJuego.ALGUNJUGADORENBANCARROTA);
                bancarrota = true;
            }
        }
        if(!bancarrota)
        {
            Calle casilla = (Calle) obtenerCasillaJugadorActual();
            boolean tengoPropietario = casilla.tengoPropietario();
            if(tengoPropietario)
                setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
            else
                setEstadoJuego(EstadoJuego.JA_PUEDECOMPRAROGESTIONAR);
        }
    }
    
    void actuarSiEnCasillaNoEdificable(){
        setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
        Casilla casillaActual = jugadorActual.getCasillaActual();
        if(casillaActual.getTipo() == TipoCasilla.IMPUESTO)
        {
            //System.out.println("\nEl jugador debe pagar " + casillaActual.getCoste());
            jugadorActual.pagarImpuesto();
            if(jugadorActual.getSaldo() <= 0)   // NO SALIA EN EL DIAGRAMA!!!!!!!!!!!
            {
                setEstadoJuego(EstadoJuego.ALGUNJUGADORENBANCARROTA);
            }
        }
        else if(casillaActual.getTipo() == TipoCasilla.JUEZ)
        {
            encarcelarJugador();
        }
        else if(casillaActual.getTipo() == TipoCasilla.SORPRESA)
        {
            cartaActual = mazo.remove(0);
            setEstadoJuego(EstadoJuego.JA_CONSORPRESA);
        }
    }
    
    public void aplicarSorpresa(){
        setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
        //System.out.println(cartaActual.toString());
        if(cartaActual.getTipoSorpresa() == TipoSorpresa.SALIRCARCEL)
            jugadorActual.setCartaLibertad(cartaActual);
        else
        {
            mazo.add(cartaActual);
        }
        if(cartaActual.getTipoSorpresa() == TipoSorpresa.PAGARCOBRAR)
        {
            jugadorActual.modificarSaldo(cartaActual.getValor());
            if(jugadorActual.getSaldo() <= 0)
            {
                setEstadoJuego(EstadoJuego.ALGUNJUGADORENBANCARROTA);
            }
        }
        else if(cartaActual.getTipoSorpresa() == TipoSorpresa.IRACASILLA)
        {
            int valor = cartaActual.getValor();
            boolean casillaCarcel = tablero.esCasillaCarcel(valor);
            if(casillaCarcel)
            {
                encarcelarJugador();
            }
            else
            {
                mover(valor);
            }
        }
        else if(cartaActual.getTipoSorpresa() == TipoSorpresa.PORCASAHOTEL)
        {
            int cantidad = cartaActual.getValor();
            int numeroTotal = jugadorActual.cuantasCasasHotelesTengo();
            jugadorActual.modificarSaldo(cantidad*numeroTotal);
            if(jugadorActual.getSaldo() <= 0)
                setEstadoJuego(EstadoJuego.ALGUNJUGADORENBANCARROTA);
        }
        else if(cartaActual.getTipoSorpresa() == TipoSorpresa.PORJUGADOR)
        {
            for (Jugador j:jugadores)
            {
                if(j != jugadorActual)
                {
                    j.modificarSaldo(cartaActual.getValor());
                    jugadorActual.modificarSaldo(-cartaActual.getValor());
                }
                if(j.getSaldo() <= 0)
                {
                    setEstadoJuego(EstadoJuego.ALGUNJUGADORENBANCARROTA);
                }
                if(jugadorActual.getSaldo() <= 0)
                {
                    setEstadoJuego(EstadoJuego.ALGUNJUGADORENBANCARROTA);
                }
            }
        }
        else if(cartaActual.getTipoSorpresa() == TipoSorpresa.CONVERTIRME)
        {
            Especulador tmp = jugadorActual.convertirme(cartaActual.getValor());
            jugadores.set(jugadores.indexOf(jugadorActual), tmp);
            jugadorActual = tmp;
        }
    }
    
    public boolean cancelarHipoteca(int numeroCasilla){
        TituloPropiedad titulo = tablero.obtenerCasillaNumero(numeroCasilla).getTitulo();
        if(titulo != null)
        {
            return jugadorActual.cancelarHipoteca(titulo);
        }
        return false;
    }
    
    public boolean comprarTituloPropiedad(){
        boolean comprado = jugadorActual.comprarTituloPropiedad();
        if(comprado)
        {
            //System.out.println("\nEl jugador compra la casilla");
            setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
        }
        else
        {
            //System.out.println("\nEl jugador no tiene saldo suficiente para comprar la casilla");
        }
        return comprado;
    }
    
    public boolean edificarCasa(int numeroCasilla){
        boolean edificada = false;
        Casilla casilla = tablero.obtenerCasillaNumero(numeroCasilla);
        TituloPropiedad titulo = casilla.getTitulo();
        if(titulo != null)
        {
            edificada = jugadorActual.edificarCasa(titulo);
        }
        if(edificada)
            setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
        return edificada;
    }
    
    public boolean edificarHotel(int numeroCasilla){
        boolean edificada = false;
        Casilla casilla = tablero.obtenerCasillaNumero(numeroCasilla);
        TituloPropiedad titulo = casilla.getTitulo();
        if(titulo != null)
        {
            edificada = jugadorActual.edificarHotel(titulo);
        }
        if(edificada)
            setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
        return edificada;
    }
    
    private void encarcelarJugador(){
        if(!jugadorActual.deboIrACarcel())
        {
            if(jugadorActual.tengoCartaLibertad())
            {
                Sorpresa carta = jugadorActual.devolverCartaLibertad();
                mazo.add(carta);
            }
            setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
        }
        else
        {
            //System.out.println("\nEl jugador va a la cárcel");
            Casilla casillaCarcel = tablero.getCarcel();
            jugadorActual.irACarcel(casillaCarcel);
            setEstadoJuego(EstadoJuego.JA_ENCARCELADO);
        }
    }
    
    public Sorpresa getCartaActual(){
        return cartaActual;
    }
    
    Dado getDado(){
        return dado;
    }
    
    public EstadoJuego getEstadoJuego(){
        return estadoJuego;
    }
    
    public Jugador getJugadorActual(){
        return jugadorActual;
    }
    
    public ArrayList<Jugador> getJugadores(){
        ArrayList<Jugador> listadojugadores = new ArrayList<>();
        for(Jugador jugador: jugadores)
        {
            listadojugadores.add(jugador);
        }
        return listadojugadores;
    }

    public ArrayList<Sorpresa> getMazo(){
        return mazo;
    }
    
    public boolean hipotecarPropiedad(int numeroCasilla){ // Lo he cambiado de void a boolean
        TituloPropiedad titulo = tablero.obtenerCasillaNumero(numeroCasilla).getTitulo();
        if(titulo != null)
        {
            return jugadorActual.hipotecarPropiedad(titulo);
        }
        return false;
    }
    
    private void inicializarCartasSorpresa(){
        mazo.add(new Sorpresa ("Te conviertes en un especulador de la casa de subastas de Orgrimmar. Las fianzas se pagan a 3000",
            3000, TipoSorpresa.CONVERTIRME));
        mazo.add(new Sorpresa ("Te conviertes en un especulador de la casa de subastas de Ventormenta. Las fianzas se pagan a 5000",
            5000, TipoSorpresa.CONVERTIRME));
        mazo.add(new Sorpresa ("El ejercito de Shinra te ha capturado, debes ir a la carcel.",
            tablero.getCarcel().getNumeroCasilla(), TipoSorpresa.IRACASILLA));
        mazo.add(new Sorpresa ("Un Big Daddy irrumpe en la prisión y te libera.",
            tablero.getCarcel().getNumeroCasilla(), TipoSorpresa.SALIRCARCEL));
        mazo.add(new Sorpresa ("Los datos de la partida están corruptos, apareces en la casilla de salida.",
            tablero.getSalida().getNumeroCasilla(), TipoSorpresa.IRACASILLA));
        mazo.add(new Sorpresa ("Has encontrado un huevo de Rathalos Celeste, cobras 250.",
            250 , TipoSorpresa.PAGARCOBRAR));
        mazo.add(new Sorpresa ("Un dragón lanza una bola de fuego que te hace perder 200.",
            -200 , TipoSorpresa.PAGARCOBRAR));
        mazo.add(new Sorpresa ("Tom Nook te obliga a pagar 100 por cada propiedad.",
            -100 , TipoSorpresa.PORCASAHOTEL));
        mazo.add(new Sorpresa ("Hay un error en la casa de subastas, el resto de jugadores pierde 150 y tú ganas el total de las pérdidas.",
            -150 , TipoSorpresa.PORJUGADOR));
        mazo.add(new Sorpresa ("Te pillan robandole un pokémon a su entrenador, vas a la carcel.",
            tablero.getCarcel().getNumeroCasilla(), TipoSorpresa.IRACASILLA));
        mazo.add(new Sorpresa ("Utilizas cuerda huida y escapas de la cárcel.",
            tablero.getCarcel().getNumeroCasilla(), TipoSorpresa.SALIRCARCEL));
        mazo.add(new Sorpresa ("¡Un montapuercos ataca tus edificios!, tienes pérdidas por valor de 50 por cada propiedad.",
            -50 , TipoSorpresa.PORCASAHOTEL));
        Collections.shuffle(mazo);
    }
    
    public void inicializarJuego(ArrayList<String> nombres){
        inicializarJugadores(nombres);
        inicializarTablero();
        inicializarCartasSorpresa();
        salidaJugadores();
    }
    
    private void inicializarJugadores(ArrayList<String> nombres){
        for (String nombre: nombres)
        {
            jugadores.add(new Jugador(nombre, 7500));
        }
    }
    
        
    private void inicializarTablero(){
        tablero = new Tablero();
    }
    
    public boolean intentarSalirCarcel(MetodoSalirCarcel metodo){
        int resultado;
        if(metodo == MetodoSalirCarcel.TIRANDODADO)
        {
            resultado = tirarDado();
            if(resultado >= 5)
            {
                jugadorActual.setEncarcelado(false);
            }
        }
        else if(metodo == MetodoSalirCarcel.PAGANDOLIBERTAD)
        {
            //String s = "El jugador paga " + PRECIO_LIBERTAD + " por lo que sale de la cárcel";
            jugadorActual.pagarLibertad(PRECIO_LIBERTAD);
            //System.out.println("\nEl jugador tiene ahora de saldo " + jugadorActual.getSaldo());
        }
        boolean libre = !jugadorActual.getEncarcelado();
        if(libre)
        {
            setEstadoJuego(EstadoJuego.JA_PREPARADO);
        }
        else
        {
            setEstadoJuego(EstadoJuego.JA_ENCARCELADO);
        }
        return libre;
    }
    
    public int jugar(){
        int resultadoDado = tirarDado();
        //System.out.println("\nSe tira el dado y sale un " + resultadoDado);
        mover(tablero.obtenerCasillaFinal(jugadorActual.getCasillaActual(),resultadoDado).getNumeroCasilla());
        return resultadoDado;
    }
    
    void mover(int numCasillaDestino){
        Casilla casillaInicial = jugadorActual.getCasillaActual();
        Casilla casillaFinal = tablero.obtenerCasillaNumero(numCasillaDestino);
        jugadorActual.setCasillaActual(casillaFinal);
        
        //System.out.println("\nEl jugador se mueve de la casilla " + 
        //        casillaInicial.toString() + " a la casilla " +
        //        casillaFinal.toString());
        
        if(numCasillaDestino < casillaInicial.getNumeroCasilla())
        {
            jugadorActual.modificarSaldo(SALDO_SALIDA);
            //System.out.println("\nEl jugador pasa por la casilla de salida, " +
            //        "por lo que cobra " + SALDO_SALIDA);
            //System.out.println("\nEl jugador tiene ahora de saldo " + jugadorActual.getSaldo());
        }
            
        if(casillaFinal.soyEdificable())
        {
            actuarSiEnCasillaEdificable();
        }
        else
        {
            actuarSiEnCasillaNoEdificable();
        }
    }
    
    public Casilla obtenerCasillaJugadorActual(){
        return jugadorActual.getCasillaActual();
    }
    
    public ArrayList<Casilla> obtenerCasillasTablero(){
        return tablero.getCasillas();
    }
    
    public ArrayList<Integer> obtenerPropiedadesJugador(){
        ArrayList<Integer> indices_propiedades = new ArrayList<>();
        for(TituloPropiedad p:jugadorActual.getPropiedades())
            for (Casilla c:tablero.getCasillas())
            {
                if(c.getTitulo() == p)
                {
                    indices_propiedades.add(c.getNumeroCasilla());
                    break;
                }
            }    
        return indices_propiedades;
    }
    
    public ArrayList<Integer> obtenerPropiedadesJugadorSegunEstadoHipoteca(boolean estadoHipoteca){
        ArrayList<Integer> indices_propiedades_segun_estado_hipoteca = new ArrayList<>();
        
        for(TituloPropiedad p:jugadorActual.obtenerPropiedades(estadoHipoteca))
            for (Casilla c:tablero.getCasillas())
            {
                if(c.getTitulo() == p)
                {
                    indices_propiedades_segun_estado_hipoteca.add(c.getNumeroCasilla());
                    break;
                }
            }    
        return indices_propiedades_segun_estado_hipoteca;
    }
    
    public String obtenerRanking(){
        String ranking;
        ArrayList<Jugador> jugadoresOrdenados = jugadores;
        Collections.sort(jugadoresOrdenados);
        int i = 1;
        ranking = "\nRANKING:\n";
        for(Jugador j:jugadoresOrdenados)
        {
            ranking += "Puesto número " + i + ": " + j.toString() + "\n";
            i++;
        }
        ranking += "\n¡Enhorabuena " + jugadoresOrdenados.get(0).getNombre() +
                   ", has ganado la partida!";
        return ranking;
    }
    
    public int obtenerSaldoJugadorActual(){
        return jugadorActual.getSaldo();
    }
    
    public int obtenerValorDado(){
        return dado.getValor();
    }
    
    private void salidaJugadores(){
        for(Jugador j: jugadores)
            j.setCasillaActual(tablero.getSalida());
        Random rand = new Random();
        int aleatorio = rand.nextInt(jugadores.size());
        jugadorActual = jugadores.get(aleatorio);
        estadoJuego = EstadoJuego.JA_PREPARADO;
        //System.out.println("\nSe sortea qué jugador sale");
        //System.out.println("\nSale el jugador " + jugadorActual.toString() + "\n");
    }
    
    private void setCartaActual(Sorpresa cartaActual){
        this.cartaActual = cartaActual;
    }
    
    public void setEstadoJuego(EstadoJuego estadoJuego){
        this.estadoJuego = estadoJuego;
    } 
    
    public void siguienteJugador(){
        int indice_actual = jugadores.indexOf(jugadorActual);
        int indice_nuevo = (indice_actual+1) % jugadores.size();
        
        //System.out.println("\nEl jugador " + jugadorActual.toString() + " pasa el turno\n");
        
        jugadorActual = jugadores.get(indice_nuevo);
        
        if(jugadorActual.getEncarcelado())
            estadoJuego = EstadoJuego.JA_ENCARCELADOCONOPCIONDELIBERTAD;
        else
            estadoJuego = EstadoJuego.JA_PREPARADO;
        //System.out.println("\nSe pasa el turno al jugador " + jugadorActual.toString());
    }
    
    int tirarDado(){
        return dado.tirar();
    }
    
    public boolean venderPropiedad(int numeroCasilla){
        Calle casilla = (Calle) tablero.obtenerCasillaNumero(numeroCasilla);
        boolean vendida = jugadorActual.venderPropiedad(casilla);
        if(vendida)
            //System.out.println("\nEl jugador vende la casilla " + numeroCasilla);
        // System.out.println("\nEl jugador tiene ahora de saldo " + jugadorActual.getSaldo());
        setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
        return vendida;
    }
    
    public Tablero getTablero(){
        return tablero;
    }
}