# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.
# encoding: utf­8

require_relative "sorpresa"
require_relative "tablero"
require_relative "tipo_sorpresa"
require_relative "dado"
require_relative "jugador"
require_relative "estado_juego"
require_relative "metodo_salir_carcel"
require_relative "calle"
require_relative "otra_casilla"
require "singleton"

module ModeloQytetet
  class Qytetet
    attr_reader :mazo, :tablero, :dado, :jugador_actual, :jugadores, :valor_dado
    attr_accessor :carta_actual, :estado_juego
    include Singleton

    @@MAX_JUGADORES = 4
    @@NUM_SORPRESAS = 20
    @@NUM_CASILLAS = 20
    @@PRECIO_LIBERTAD = 200
    @@SALDO_SALIDA = 1000
    
    def initialize
      @dado = Dado.instance
      @jugadores = Array.new
    end
    
    def actuar_si_en_casilla_edificable
      debo_pagar = @jugador_actual.debo_pagar_alquiler
      bancarrota = false
      #puts("El jugador cae en una casilla edificable.")
      if(debo_pagar)
        titulo = @jugador_actual.casilla_actual.titulo
        #puts("El jugador debe pagar al propietario de la casilla (" +
        #titulo.propietario.nombre +
        #      ") la cantidad de " + titulo.calcular_importe_alquiler.to_s)
        @jugador_actual.pagar_alquiler
        if(@jugador_actual.saldo <= 0)
          @estado_juego = EstadoJuego::ALGUNJUGADORENBANCARROTA
          bancarrota = true
        end
      end
      if(!bancarrota)
        casilla = obtener_casilla_jugador_actual
        tengo_propietario = casilla.tengo_propietario
        if(tengo_propietario)
          @estado_juego = EstadoJuego::JA_PUEDEGESTIONAR
        else
          @estado_juego = EstadoJuego::JA_PUEDECOMPRAROGESTIONAR
        end
      end
    end
    
    def actuar_si_en_casilla_no_edificable
      @estado_juego = EstadoJuego::JA_PUEDEGESTIONAR
      casilla_actual = @jugador_actual.casilla_actual
      if(casilla_actual.tipo == TipoCasilla::IMPUESTO)
          puts("\nEl jugador debe pagar " + casilla_actual.coste.to_s)
          @jugador_actual.pagar_impuesto
          if(@jugador_actual.saldo <= 0)
            @estado_juego = EstadoJuego::ALGUNJUGADORENBANCARROTA
          end
      elsif(casilla_actual.tipo == TipoCasilla::JUEZ)
          encarcelar_jugador
      elsif(casilla_actual.tipo == TipoCasilla::SORPRESA)
          @carta_actual = @mazo.delete_at(0)
          @estado_juego = EstadoJuego::JA_CONSORPRESA
      end
      
    end
    
    def aplicar_sorpresa
      @estado_juego = EstadoJuego::JA_PUEDEGESTIONAR
      puts(@carta_actual.to_s)
      if(@carta_actual.tipo_sorpresa == TipoSorpresa::SALIRCARCEL)
        @jugador_actual.carta_libertad = carta_actual
        @jugador_actual.tiene_carta_libertad = true
      else
          @mazo << @carta_actual
      end
      if(@carta_actual.tipo_sorpresa == TipoSorpresa::PAGARCOBRAR)
        @jugador_actual.modificar_saldo(@carta_actual.valor)
        if(@jugador_actual.saldo <= 0)
            @estado_juego = EstadoJuego::ALGUNJUGADORENBANCARROTA
        end
      elsif(@carta_actual.tipo_sorpresa == TipoSorpresa::IRACASILLA)
        valor = @carta_actual.valor
        casilla_carcel = @tablero.es_casilla_carcel(valor)
        if(casilla_carcel)
            encarcelar_jugador
        else
            mover(valor)
        end
      elsif(@carta_actual.tipo_sorpresa == TipoSorpresa::PORCASAHOTEL)
        cantidad = @carta_actual.valor
        numero_total = @jugador_actual.cuantas_casas_hoteles_tengo
        @jugador_actual.modificar_saldo(cantidad*numero_total)
        if(@jugador_actual.saldo <= 0)
          @estado_juego =EstadoJuego::ALGUNJUGADORENBANCARROTA
        end
      elsif(@carta_actual.tipo_sorpresa == TipoSorpresa::PORJUGADOR)
        @jugadores.each do |j|
          if(j != @jugador_actual)
            j.modificar_saldo(@carta_actual.valor)
            @jugador_actual.modificar_saldo(-@carta_actual.valor)
          end
          if(j.saldo <= 0)
            @estado_juego = EstadoJuego::ALGUNJUGADORENBANCARROTA
          end
          if(@jugador_actual.saldo <= 0)
            @estado_juego = EstadoJuego::ALGUNJUGADORENBANCARROTA
          end
        end
      elsif(@carta_actual.tipo_sorpresa == TipoSorpresa::CONVERTIRME)
        tmp = @jugador_actual.convertirme(@carta_actual.valor)
        @jugadores[@jugadores.index(@jugador_actual)] =  tmp
        @jugador_actual = tmp
      end
    end
    
    def cancelar_hipoteca(numero_casilla)
      result = false
      titulo = @tablero.obtener_casilla_numero(numero_casilla).titulo
      if(titulo != nil)
        result = @jugador_actual.cancelar_hipoteca(titulo)
      end
      result
    end
    
    def comprar_titulo_propiedad
      comprado = @jugador_actual.comprar_titulo_propiedad
      if(comprado)
        #puts("\nEl jugador compra la casilla")
        @estado_juego = EstadoJuego::JA_PUEDEGESTIONAR
      else
        #puts("\nEl jugador no tiene saldo suficiente para comprar la casilla")
      end
      comprado
    end
    
    def edificar_casa(numero_casilla)
      edificada = false
      casilla = @tablero.obtener_casilla_numero(numero_casilla)
      titulo = casilla.titulo
      if(titulo != nil)
        edificada = @jugador_actual.edificar_casa(titulo)
      end
      if(edificada)
        @estado_juego = EstadoJuego::JA_PUEDEGESTIONAR
      end
      edificada
    end
    
    def edificar_hotel(numero_casilla)
      edificada = false
      casilla = @tablero.obtener_casilla_numero(numero_casilla)
      titulo = casilla.titulo
      if(titulo != nil)
        edificada = @jugador_actual.edificar_hotel(titulo)
      end
      if(edificada)
        @estado_juego = EstadoJuego::JA_PUEDEGESTIONAR
      end
      edificada
    end
    
    def encarcelar_jugador
      if(!@jugador_actual.debo_ir_a_carcel)
        if(@jugador_actual.tengo_carta_libertad)
          #puts("\nEl jugador se libra de entrar en la carcel ya que tiene una carta de libertad")
          carta = @jugador_actual.devolver_carta_libertad
          @mazo << carta
          @estado_juego = EstadoJuego::JA_PUEDEGESTIONAR
        end
        @estado_juego = EstadoJuego::JA_PUEDEGESTIONAR
      else
        casilla_carcel = @tablero.carcel
        @jugador_actual.ir_a_carcel(casilla_carcel)
        @estado_juego = EstadoJuego::JA_ENCARCELADO
      end
    end
    
    def hipotecar_propiedad(numero_casilla)
      titulo = @tablero.obtener_casilla_numero(numero_casilla).titulo
      hipotecada = false
      if(titulo != nil)
          hipotecada = @jugador_actual.hipotecar_propiedad(titulo)
      end
      hipotecada
    end
    
    def inicializar_cartas_sorpresa
      
      @mazo = Array.new      
      
      s1 = Sorpresa.new("Te conviertes en un especulador de la casa de subastas de Orgrimmar. Las fianzas se pagan a 3000",
          3000, TipoSorpresa::CONVERTIRME)
      s2 = Sorpresa.new("Te conviertes en un especulador de la casa de subastas de Ventormenta. Las fianzas se pagan a 5000",
          5000, TipoSorpresa::CONVERTIRME)
      s3 = Sorpresa.new("El ejercito de Shinra te ha capturado, debes ir a la carcel.",
            @tablero.carcel.numero_casilla, TipoSorpresa::IRACASILLA)
      s4 = Sorpresa.new("Un Big Daddy irrumpe en la prision y te libera.",
            @tablero.carcel.numero_casilla, TipoSorpresa::SALIRCARCEL)
      s5 = Sorpresa.new("Los datos de la partida estan corruptos, apareces en la casilla de salida.",
            @tablero.salida.numero_casilla, TipoSorpresa::IRACASILLA)
      s6 = Sorpresa.new("Has encontrado un huevo de Rathalos Celeste, cobras 250.",
            250 , TipoSorpresa::PAGARCOBRAR)
      s7 = Sorpresa.new("Un dragon lanza una bola de fuego que te hace perder 200.",
            -200 , TipoSorpresa::PAGARCOBRAR)
      s8 = Sorpresa.new("Tom Nook te obliga a pagar 100 por cada propiedad.",
            -100 , TipoSorpresa::PORCASAHOTEL)
      s9 = Sorpresa.new("Hay un error en la casa de subastas, el resto de jugadores pierde 150 y tu ganas el total de las perdidas.",
            -150 , TipoSorpresa::PORJUGADOR)
      s10 = Sorpresa.new("Te pillan robandole un pokemon a su entrenador, vas a la carcel.",
            @tablero.carcel.numero_casilla, TipoSorpresa::IRACASILLA)
      s11 = Sorpresa.new("Utilizas cuerda huida y escapas de la carcel.",
            @tablero.carcel.numero_casilla, TipoSorpresa::SALIRCARCEL)
      s12 = Sorpresa.new("Un montapuercos ataca tus edificios!, tienes perdidas por valor de 50 por cada propiedad.",
            -50 , TipoSorpresa::PORCASAHOTEL)
      
      @mazo << s1
      @mazo << s2
      @mazo << s3
      @mazo << s4
      @mazo << s5
      @mazo << s6
      @mazo << s7
      @mazo << s8
      @mazo << s9
      @mazo << s10
      @mazo << s11
      @mazo << s12
      #@mazo.shuffle!
    end
    
    def inicializar_juego(nombres)
      inicializar_tablero
      inicializar_cartas_sorpresa
      inicializar_jugadores(nombres)
      salida_jugadores
    end
    
    def inicializar_jugadores(nombres)
      n = 0
      @jugadores = Array.new
        nombres.each do |nombre|
          @jugadores << Jugador.nuevo(nombre,7500)
        end
    end
    
    def inicializar_tablero
      @tablero = Tablero.new
    end
    
    def intentar_salir_carcel(metodo)
      if(metodo == MetodoSalirCarcel::TIRANDODADO)
        resultado = tirar_dado
        #s = "El jugador tira el dado y saca un " + resultado.to_s + " por lo que "
        if(resultado >= 5)
          @jugador_actual.encarcelado = false
        end
      elsif(metodo == MetodoSalirCarcel::PAGANDOLIBERTAD)
          #s = "El jugador paga " + @@PRECIO_LIBERTAD.to_s + " por lo que sale de la carcel"
          @jugador_actual.pagar_libertad(@@PRECIO_LIBERTAD)
      end
      
      libre = !@jugador_actual.encarcelado
      
      if(libre)
          @estado_juego = EstadoJuego::JA_PREPARADO
      else
          @estado_juego = EstadoJuego::JA_ENCARCELADO
      end
      libre
    end
    
    def jugar
      resultado_dado = tirar_dado
      #puts("\nSe tira el dado y sale un " + resultado_dado.to_s)
      mover(@tablero.obtener_casilla_final(@jugador_actual.casilla_actual, resultado_dado).numero_casilla)
      resultado_dado
    end
    
    def mover(num_casilla_destino)
      casilla_inicial = @jugador_actual.casilla_actual
      casilla_final = @tablero.obtener_casilla_numero(num_casilla_destino)
      @jugador_actual.casilla_actual = casilla_final

      #puts("\nEl jugador se mueve de la casilla "  + 
      #casilla_inicial.to_s + " a la casilla " + casilla_final.to_s)

      if(num_casilla_destino < casilla_inicial.numero_casilla)
          @jugador_actual.modificar_saldo(@@SALDO_SALIDA)
          #puts("\nEl jugador pasa por la casilla de salida, " +
          #     "por lo que cobra " + @@SALDO_SALIDA.to_s)
      end

      if(casilla_final.soy_edificable)
          actuar_si_en_casilla_edificable
      else
          actuar_si_en_casilla_no_edificable
      end
    end
    
    def obtener_casilla_jugador_actual
      @jugador_actual.casilla_actual
    end
    
    def obtener_casillas_tablero
      @tablero.casillas
    end
    
    def obtener_propiedades_jugador
      indices_propiedades = Array.new
      @jugador_actual.propiedades.each do |p|
        @tablero.casillas.each do |c|
          if(c.titulo == p)
              indices_propiedades << c.numero_casilla
              break
          end
        end
      end
      indices_propiedades
    end
    
    def obtener_propiedades_jugador_segun_estado_hipoteca(estado_hipoteca)
      indices_propiedades_segun_estado_hipoteca = Array.new
      @jugador_actual.obtener_propiedades(estado_hipoteca).each do |p|
        @tablero.casillas.each do |c|
          if(c.titulo == p)
            indices_propiedades_segun_estado_hipoteca << c.numero_casilla
            break
          end
        end
      end
      indices_propiedades_segun_estado_hipoteca
    end
    
    def obtener_ranking
      jugadores_ordenados = @jugadores.sort
      i = 1
      
      ranking = "\nRANKING:\n"
      jugadores_ordenados.each do |j|
        ranking += "Puesto numero " + i.to_s + ": " + j.to_s + "\n"
        i+=1
      end
      ranking += "\nEnhorabuena " + jugadores_ordenados.at(0).nombre.to_s + ", has ganado la partida!"
      ranking
    end
    
    def obtener_saldo_jugador_actual
      @jugador_actual.saldo
    end
    
    def obtener_valor_dado
      @dado.valor
    end
    
    def salida_jugadores
      @jugadores.each do |j|
        j.casilla_actual = @tablero.salida
      end
        aleatorio = rand(@jugadores.length)
        @jugador_actual = @jugadores.at(aleatorio)
        @estado_juego = EstadoJuego::JA_PREPARADO
        #puts("\nSe sortea que jugador sale")
        #puts("\nSale el jugador " + @jugador_actual.to_s + "\n")
    end
    
    def siguiente_jugador
      indice_actual=@jugadores.index(@jugador_actual)
      indice_nuevo = (indice_actual+1) % @jugadores.length
      @jugador_actual = @jugadores.at(indice_nuevo)

      if(@jugador_actual.encarcelado)
          @estado_juego = EstadoJuego::JA_ENCARCELADOCONOPCIONDELIBERTAD
      else
          @estado_juego = EstadoJuego::JA_PREPARADO
      end
      #puts("\nSe pasa el turno al jugador " + @jugador_actual.to_s)
    end
    
    def tirar_dado
      @dado.tirar
    end
    
    def vender_propiedad(numero_casilla)
      casilla = @tablero.obtener_casilla_numero(numero_casilla)
      vendida = @jugador_actual.vender_propiedad(casilla)
        if(vendida)
            puts("\nEl jugador vende la casilla " + numero_casilla.to_s)
        end
        @estado_juego = EstadoJuego::JA_PUEDEGESTIONAR
        vendida
    end
    private :encarcelar_jugador, :inicializar_cartas_sorpresa, :inicializar_jugadores,
      :inicializar_tablero, :salida_jugadores
  end
end