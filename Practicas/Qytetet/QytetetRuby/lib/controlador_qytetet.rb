# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

require_relative "opcion_menu"
require_relative "qytetet"
require_relative "estado_juego"
require_relative "metodo_salir_carcel"
require_relative "jugador"
require_relative "casilla"

require "singleton"

module ControladorQytetet
  class ControladorQytetet
    attr_writer :nombre_jugadores
    include Singleton

    def initialize
      @modelo = ModeloQytetet::Qytetet.instance
      @nombre_jugadores = Array.new
    end

    def obtener_operaciones_juego_validas
      operaciones_validas = Array.new
      if(@modelo.jugadores.empty?)
        operaciones_validas << OpcionMenu.index(:INICIARJUEGO)
      else
        estado_juego = @modelo.estado_juego
        operaciones_validas << OpcionMenu.index(:MOSTRARJUGADORACTUAL)
        operaciones_validas << OpcionMenu.index(:MOSTRARJUGADORES)
        operaciones_validas << OpcionMenu.index(:MOSTRARTABLERO)
        operaciones_validas << OpcionMenu.index(:TERMINARJUEGO)
        case estado_juego
          when ModeloQytetet::EstadoJuego::JA_CONSORPRESA
            operaciones_validas << OpcionMenu.index(:APLICARSORPRESA)
          when ModeloQytetet::EstadoJuego::ALGUNJUGADORENBANCARROTA
            operaciones_validas << OpcionMenu.index(:OBTENERRANKING)
          when ModeloQytetet::EstadoJuego::JA_PUEDECOMPRAROGESTIONAR
            operaciones_validas << OpcionMenu.index(:PASARTURNO)
            operaciones_validas << OpcionMenu.index(:VENDERPROPIEDAD)
            operaciones_validas << OpcionMenu.index(:COMPRARTITULOPROPIEDAD)
            operaciones_validas << OpcionMenu.index(:HIPOTECARPROPIEDAD)
            operaciones_validas << OpcionMenu.index(:CANCELARHIPOTECA)
            operaciones_validas << OpcionMenu.index(:EDIFICARCASA)
            operaciones_validas << OpcionMenu.index(:EDIFICARHOTEL)
          when ModeloQytetet::EstadoJuego::JA_PUEDEGESTIONAR
            operaciones_validas << OpcionMenu.index(:PASARTURNO)
            operaciones_validas << OpcionMenu.index(:VENDERPROPIEDAD)
            operaciones_validas << OpcionMenu.index(:HIPOTECARPROPIEDAD)
            operaciones_validas << OpcionMenu.index(:CANCELARHIPOTECA)
            operaciones_validas << OpcionMenu.index(:EDIFICARCASA)
            operaciones_validas << OpcionMenu.index(:EDIFICARHOTEL)
          when ModeloQytetet::EstadoJuego::JA_PREPARADO
            operaciones_validas << OpcionMenu.index(:JUGAR)
          when ModeloQytetet::EstadoJuego::JA_ENCARCELADO
            operaciones_validas << OpcionMenu.index(:PASARTURNO)
          when ModeloQytetet::EstadoJuego::JA_ENCARCELADOCONOPCIONDELIBERTAD
            operaciones_validas << OpcionMenu.index(:INTENTARSALIRCARCELPAGANDOLIBERTAD)
            operaciones_validas << OpcionMenu.index(:INTENTARSALIRCARCELTIRANDODADO)
        end
      end
      operaciones_validas.sort!
      operaciones_validas
    end

    def necesita_elegir_casilla(opcion_menu)
      necesita = false
      if  (opcion_menu == OpcionMenu.index(:HIPOTECARPROPIEDAD) || 
           opcion_menu == OpcionMenu.index(:CANCELARHIPOTECA)   ||
           opcion_menu == OpcionMenu.index(:EDIFICARCASA)       ||
           opcion_menu == OpcionMenu.index(:EDIFICARHOTEL)      ||
           opcion_menu == OpcionMenu.index(:VENDERPROPIEDAD))
        necesita = true
      end
        necesita
    end

    def obtener_casillas_validas(opcion_menu)
      propiedades = Array.new
      if(opcion_menu == OpcionMenu.index(:HIPOTECARPROPIEDAD))
        propiedades = @modelo.obtener_propiedades_jugador_segun_estado_hipoteca(false)
      elsif(opcion_menu == OpcionMenu.index(:CANCELARHIPOTECA))
        propiedades = @modelo.obtener_propiedades_jugador_segun_estado_hipoteca(true)
      elsif(opcion_menu == OpcionMenu.index(:EDIFICARCASA)   ||
            opcion_menu == OpcionMenu.index(:EDIFICARHOTEL) ||
            opcion_menu == OpcionMenu.index(:VENDERPROPIEDAD))
        propiedades = @modelo.obtener_propiedades_jugador
      end
      propiedades
    end

    def realizar_operacion(opcion_elegida, casilla_elegida)
      opcion_menu = OpcionMenu.at(opcion_elegida)

      case opcion_menu
        when :INICIARJUEGO
          mensaje =  "Se inicializa el juego"
          @modelo.inicializar_juego(@nombre_jugadores)
          mensaje += "\nEmpieza el jugador " + @modelo.jugador_actual.to_s
        when :JUGAR
          casilla_inicial = @modelo.jugador_actual.casilla_actual
          mensaje = "Se tira el dado y sale un " + @modelo.jugar.to_s
          casilla_final = @modelo.jugador_actual.casilla_actual
          mensaje += "\nEl jugador se mueve de la casilla " + casilla_inicial.to_s +
                     " a la casilla " + casilla_final.to_s
          if(casilla_final.numero_casilla < casilla_inicial.numero_casilla)
            mensaje += "\nAdemas, el jugador pasa por la casilla de salida, por lo que cobra "
          end
        when :APLICARSORPRESA
          mensaje = "Se saca la carta sorpresa:\n" + @modelo.carta_actual.to_s
          @modelo.aplicar_sorpresa
        when :INTENTARSALIRCARCELPAGANDOLIBERTAD
          if(@modelo.intentar_salir_carcel(ModeloQytetet::MetodoSalirCarcel::PAGANDOLIBERTAD))
            mensaje = "El jugador paga, por lo que sale de la carcel"
          else
            mensaje = "El jugador no tiene suficiente dinero para pagar la libertad"
          end
        when :INTENTARSALIRCARCELTIRANDODADO
          if(@modelo.intentar_salir_carcel(ModeloQytetet::MetodoSalirCarcel::TIRANDODADO))
            mensaje = "El jugador tira un dado y saca mas de un 5, por lo que sale de la carcel"
          else
            mensaje =  "El jugador tira un dado y saca menos de un 5, por lo que no sale de la carcel"
          end
        when :COMPRARTITULOPROPIEDAD
          if(@modelo.comprar_titulo_propiedad)
            mensaje = "El jugador compra la casilla"
          else
            mensaje = "El jugador no tiene saldo suficiente para comprar la casilla"
          end
        when :HIPOTECARPROPIEDAD
          if(@modelo.hipotecar_propiedad(casilla_elegida))
            mensaje = "El jugador hipoteca la casilla elegida"
          else
            mensaje = "No se puede hipotecar la casilla elegida"
          end
        when :CANCELARHIPOTECA
          if(@modelo.cancelar_hipoteca(casilla_elegida))
            mensaje = "El jugador cancela la hipoteca de la casilla elegida"
          else
            mensaje = "No se puede cancelar la hipoteca de la casilla elegida"
          end
        when :EDIFICARCASA
          if(@modelo.edificar_casa(casilla_elegida))
            mensaje = "El jugador edifica una casa en la casilla elegida"
          else
            mensaje = "No se puede edificar una casa en la casilla elegida"
          end
        when :EDIFICARHOTEL
          if(@modelo.edificar_hotel(casilla_elegida))
            mensaje = "El jugador edifica un hotel en la casilla elegida"
          else
            mensaje = "No se puede edificar un hotel en la casilla elegida"
          end
        when :VENDERPROPIEDAD
          if(@modelo.vender_propiedad(casilla_elegida))
            mensaje = "El jugador vende la casilla elegida"
          else
            mensaje = "No se puede vender la casilla elegida"
          end
        when :PASARTURNO
          @modelo.siguiente_jugador
          mensaje = "Se pasa el turno al jugador " + @modelo.jugador_actual.to_s
        when :OBTENERRANKING
          mensaje = @modelo.obtener_ranking
        when :MOSTRARTABLERO
          mensaje = @modelo.tablero.to_s
        when :MOSTRARJUGADORACTUAL
          mensaje = @modelo.jugador_actual.to_s
        when :MOSTRARJUGADORES
          mensaje = "Listado de jugadores:"
          @modelo.jugadores.each do |j|
            mensaje += "\n" + j.to_s
          end
        when :TERMINARJUEGO
          mensaje = "Partida abortada por el jugador"
      end
      mensaje
    end
  end
end
    