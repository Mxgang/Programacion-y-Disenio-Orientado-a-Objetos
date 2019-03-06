# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.
# encoding: utf­8
=begin
require_relative "qytetet"
require_relative "tipo_sorpresa"
require_relative "estado_juego"
require_relative "metodo_salir_carcel"

module ModeloQytetet
  class PruebaQytetet 
    @@juego = Qytetet.instance
    def self.filtro_valor_mayor_0
      lista_mayores = Array.new
      cartas = @@juego.mazo
      cartas.each do |carta|
        if carta.valor > 0
          lista_mayores << carta
        end
      end
      lista_mayores
    end

    def self.filtro_ir_a_casilla
      lista_ir_a_casilla = Array.new
      cartas = @@juego.mazo
      cartas.each do |carta|
        if carta.tipo_sorpresa == TipoSorpresa::IRACASILLA
          lista_ir_a_casilla << carta
        end
      end
      lista_ir_a_casilla
    end

    def self.filtro_tipo(t)
      lista_tipo = Array.new
      cartas = @@juego.mazo
      cartas.each do |carta|
        if carta.tipo_sorpresa == t
          lista_tipo << carta
        end
      end
      lista_tipo
    end

    def self.listar_propiedades(q)
      propiedades = q.jugador_actual.propiedades
      puts("\nEl jugador tiene las siguientes propiedades:\n")
      casillas = q.tablero.casillas
      casillas.each do |c|
        if(c.soy_edificable)
          propiedades.each do |p|
            if(c.titulo == p)
                puts(c.to_s)
            end
          end
        end
      end
    end
    
    def self.get_nombre_jugadores
        
      nombres = Array.new

      puts("Introduzca el numero de jugadores que desee")
      numero=gets.chomp.to_i

      i = 0
      while(i<numero)
        puts("Introduzca el jugador #{i+1}")
        cadena=gets
        nombres << cadena
        i=i+1
      end        
      nombres
    end

    def self.siguiente_jugador(q)
        q.siguiente_jugador
    end
    
    def self.vender_propiedad(q)
      listar_propiedades(q)
      entrada_valida2 = false
      puts("\nIntroduzca el indice de la propiedad que desea vender (introduzca -1 si quiere volver atras):\n")
      begin
          n = gets.chomp.to_i
          if(n==-1)
              entrada_valida2 = true
          elsif(q.vender_propiedad(n))
              entrada_valida2 = true
          else
              puts("\nIntroduzca el indice de una propiedad que posea")
          end
      end while(!entrada_valida2)
    end
    
    def self.comprar_propiedad(q)
      q.comprar_titulo_propiedad
    end
    
    def self.hipotecar_propiedad(q)
      listar_propiedades(q)
      entrada_valida2 = false
      puts("\nIntroduzca el indice de la propiedad que desea hipotecar (introduzca -1 si quiere volver atras):\n")
      begin
        n = gets.chomp.to_i
        if(n==-1)
          entrada_valida2 = true
        elsif(q.hipotecar_propiedad(n))
          entrada_valida2 = true
        else
          puts("\nIntroduzca el indice de una propiedad que posea y no este hipotecada")
        end
      end while(!entrada_valida2)
    end
    
    def self.cancelar_hipoteca(q)
      listar_propiedades(q)
      entrada_valida2 = false
      puts("\nIntroduzca el indice de la propiedad cuya hipoteca desea cancelar (introduzca -1 si quiere volver atras):\n")
      begin
        n = gets.chomp.to_i
        if(n==-1)
            entrada_valida2 = true
        elsif(q.cancelar_hipoteca(n))
            entrada_valida2 = true
        else
            puts("\nIntroduzca el indice de una propiedad que posea y este hipotecada")
        end
      end while(!entrada_valida2)
    end
            
    def self.edificar_casa(q)
      listar_propiedades(q)
      entrada_valida2 = false
      puts("\nIntroduzca el indice de la propiedad que desea edificar (introduzca -1 si quiere volver atras):\n")
      begin
        n = gets.chomp.to_i
        if(n==-1)
          entrada_valida2 = true
        elsif(q.edificar_casa(n))
          entrada_valida2 = true
        else
          puts("\nIntroduzca el indice de una propiedad en la que pueda edificar casas")
        end
      end while(!entrada_valida2)
    end
            
    def self.edificar_hotel(q)
      listar_propiedades(q)
      entrada_valida2 = false
      puts("\nIntroduzca el indice de la propiedad que desea edificar (introduzca -1 si quiere volver atras):\n")
      begin
        n = gets.chomp.to_i
        if(n==-1)
          entrada_valida2 = true
        elsif(q.edificar_hotel(n))
          entrada_valida2 = true
        else
          puts("\nIntroduzca el indice de una propiedad en la que pueda edificar hoteles")
        end
      end while(!entrada_valida2)
    end
  
    def self.main
      @@juego.inicializar_juego(get_nombre_jugadores)
      t = @@juego.tablero
      puts(t.to_s)
      terminado = false
      while(!terminado)
        puts("\n")
        if(@@juego.estado_juego == EstadoJuego::JA_PREPARADO)
            @@juego.jugar
        elsif(@@juego.estado_juego == EstadoJuego::JA_PUEDEGESTIONAR)
          entrada_valida = false
          begin
            puts("Saldo del jugador: " + @@juego.jugador_actual.saldo.to_s + "\n")
            puts("Elija una opcion:")
            puts("s: Pasar al siguiente jugador")
            puts("v: Vender una propiedad")
            puts("h: Hipotecar una propiedad")
            puts("cH: Cancelar una hipoteca")
            puts("eC: Edificar una casa")
            puts("eH: Edificar un hotel")
            puts("mJ: Mostrar juego")
            s = gets
            s.upcase!
            s.chomp!
            
            if(s == "S")
              siguiente_jugador(@@juego)
              entrada_valida = true
            elsif(s == "V")
              vender_propiedad(@@juego)
              entrada_valida = true
            elsif(s == "H")
              hipotecar_propiedad(@@juego)
              entrada_valida = true
            elsif(s == "CH")
              cancelar_hipoteca(@@juego)
              entrada_valida = true
            elsif(s == "EC")
              edificar_casa(@@juego)
              entrada_valida = true
            elsif(s == "EH")
              edificar_hotel(@@juego)
              entrada_valida = true
            elsif(s == "MJ")
              @@juego.jugadores.each do |jugador|
                puts(jugador.to_s)
              end
              puts("\n" + @@juego.tablero.to_s)
            else
              puts("\nNo se ha introducido una entrada valida")
            end
          end while(!entrada_valida)
        elsif(@@juego.estado_juego == EstadoJuego::JA_PUEDECOMPRAROGESTIONAR)
          entrada_valida = false
          begin
            puts("Saldo del jugador: " + @@juego.jugador_actual.saldo.to_s + "\n")
            puts("Elija una opcion:")
            puts("s: Pasar al siguiente jugador")
            puts("c: Comprar una propiedad")
            puts("v: Vender una propiedad")
            puts("h: Hipotecar una propiedad")
            puts("cH: Cancelar una hipoteca")
            puts("eC: Edificar una casa")
            puts("eH: Edificar un hotel")
            puts("mJ: Mostrar juego")
            s = gets
            s.upcase!
            s.chomp!

            if(s == "S")
              siguiente_jugador(@@juego)
              entrada_valida = true
            elsif(s == "V")
              vender_propiedad(@@juego)
              entrada_valida = true
            elsif(s == "C")
              comprar_propiedad(@@juego)
              entrada_valida = true
            elsif(s == "H")
              hipotecar_propiedad(@@juego)
              entrada_valida = true
            elsif(s == "CH")
              cancelar_hipoteca(@@juego)
              entrada_valida = true
            elsif(s == "EC")
              edificar_casa(@@juego)
              entrada_valida = true
            elsif(s == "EH")
              edificar_hotel(@@juego)
              entrada_valida = true
            elsif(s == "MJ")
              @@juego.jugadores.each do |jugador|
                puts(jugador.to_s)
              end
              puts("\n" + @@juego.tablero.to_s)
            else
              puts("\nNo se ha introducido una entrada valida")
            end
          end while(!entrada_valida)
          
        elsif(@@juego.estado_juego == EstadoJuego::JA_CONSORPRESA)
          @@juego.aplicar_sorpresa
          
        elsif(@@juego.estado_juego == EstadoJuego::JA_ENCARCELADO)
          entrada_valida = false
          puts("\nEsta en la carcel, elija una opcion:\n")
          fianza = @@juego.jugador_actual.fianza
          begin
            puts("s: Pasar al siguiente jugador")
            puts("p: Pagar fianza (#{fianza})")
                    
            s = gets
            s.upcase!
            s.chomp!
            
            if(s == "S")
              @@juego.siguiente_jugador
              entrada_valida = true
            elsif(s == "P")
              if(fianza != nil)
                especulador_actual = @@juego.jugador_actual
                @@juego.jugador_actual.pagar_fianza
                @@juego.estado_juego = EstadoJuego::JA_PREPARADO
                entrada_valida = true
              else
                puts("\nDebe ser especulador para poder librarse de la carcel")
              end
            else
              puts("Introduzca una entrada valida")
            end
          end while(!entrada_valida)
          
        elsif(@@juego.estado_juego == EstadoJuego::JA_ENCARCELADOCONOPCIONDELIBERTAD)
          entrada_valida = false
          puts("Esta en la carcel, como desea intentar salir?:\n")
          begin
            puts("t: Tirar dado")
            puts("p: Pagar libertad (200)")
            s = gets
            s.upcase!
            s.chomp!
            
            if(s == "T")
              @@juego.intentar_salir_carcel(MetodoSalirCarcel::TIRANDODADO)
              entrada_valida = true
            elsif(s == "P")
              @@juego.intentar_salir_carcel(MetodoSalirCarcel::PAGANDOLIBERTAD) 
              entrada_valida = true
            else
              puts("\nIntroduzca una entrada valida")
            end
          end while(!entrada_valida)
        else
          @@juego.obtener_ranking
          terminado = true
        end
      end
    end
  PruebaQytetet.main
  end
end
=end