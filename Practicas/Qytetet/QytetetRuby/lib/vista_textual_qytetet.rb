# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

require_relative "controlador_qytetet"
require_relative "opcion_menu"

module VistaTextualQytetet
  class VistaTextualQytetet
    attr_reader :controlador
    def initialize
      @controlador = ControladorQytetet::ControladorQytetet.instance
    end
    
    def obtener_nombre_jugadores
      nombres = Array.new
      puts("Introduzca el numero de jugadores que desee")
      numero_valido = false
      begin
        n = gets.chomp.to_i
        if(n>1 && n<10)
          numero_valido = true
        else
          puts("\nIntroduzca un numero de jugadores entre 2 y 10")
        end
      end while not numero_valido

      i = 0
      while(i<n)
        num_jugador = i+1
        puts("Introduzca el jugador " + num_jugador.to_s)
        s = gets.chomp
        nombres << s
        i+=1
      end
      nombres
    end

    def elegir_casilla(opcion_menu)
      casillas_validas = @controlador.obtener_casillas_validas(opcion_menu)
      if(casillas_validas.empty?)
        return -1
      else
        cadenas_casillas_validas = Array.new
        puts("\nCASILLAS VALIDAS (-1 para volver atras):")
        i = 0
        while(i < casillas_validas.length)
          opcion = casillas_validas.at(i).to_s
          cadenas_casillas_validas << opcion
          puts(opcion)
          i+=1
        end
        cadenas_casillas_validas << "-1" # Para poder volver atras
        opcion_valida = leer_valor_correcto(cadenas_casillas_validas).to_i
        opcion_valida
      end
    end

    def leer_valor_correcto(valores_correctos)
      begin
        puts("Introduzca una opcion: ")
        s = gets.chomp
      end while not valores_correctos.include?(s)
      s
    end

    def elegir_operacion
      operaciones_validas = @controlador.obtener_operaciones_juego_validas
      cadenas_operaciones_validas = Array.new
      puts("\nOPERACIONES:")
      i = 0
      while(i < operaciones_validas.length)
        opcion = operaciones_validas.at(i).to_s
        cadenas_operaciones_validas << opcion
        puts "#{opcion}: #{ControladorQytetet::OpcionMenu[opcion.to_i]}"
        i+=1
      end
      opcion_valida = leer_valor_correcto(cadenas_operaciones_validas).to_i
      opcion_valida
    end

    def self.main
      ui = VistaTextualQytetet.new
      ui.controlador.nombre_jugadores = ui.obtener_nombre_jugadores
      operacion_elegida = 0
      casilla_elegida = 0
      begin
        operacion_elegida = ui.elegir_operacion
        necesita_elegir_casilla = ui.controlador.necesita_elegir_casilla(operacion_elegida)
        if (necesita_elegir_casilla)
            casilla_elegida = ui.elegir_casilla(operacion_elegida)
        end
        if (!necesita_elegir_casilla || casilla_elegida >= 0)
            puts(ui.controlador.realizar_operacion(operacion_elegida,casilla_elegida))
        end
      end while(operacion_elegida != ControladorQytetet::OpcionMenu.index(:TERMINARJUEGO) &&
                operacion_elegida != ControladorQytetet::OpcionMenu.index(:OBTENERRANKING))
    end  
    VistaTextualQytetet.main
  end
end