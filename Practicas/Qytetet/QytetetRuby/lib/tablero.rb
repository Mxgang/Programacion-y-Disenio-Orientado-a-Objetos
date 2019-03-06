# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

require_relative "titulo_propiedad"
require_relative "calle"
require_relative "otra_casilla"

module ModeloQytetet
  class Tablero
    attr_accessor :casillas, :carcel, :salida
    def initialize
      @casillas = Array.new
      t0 = TituloPropiedad.inicializar_propiedad("Astera", 500, 50, 150, 250, 0.1)
      t1 = TituloPropiedad.inicializar_propiedad("Weyard", 500, 50, 150, 250, 0.1)
      t2 = TituloPropiedad.inicializar_propiedad("Sinnoh", 550, 55, 200, 275, 0.1)
      t3 = TituloPropiedad.inicializar_propiedad("Aselia", 600, 60, 300, 300, 0.1)
      t4 = TituloPropiedad.inicializar_propiedad("Gaia", 650, 65, 400, 350, 0.1)
      t5 = TituloPropiedad.inicializar_propiedad("Montepuerco", 650, 65, 400, 350, 0.1)
      t6 = TituloPropiedad.inicializar_propiedad("Carrera Blanca", 700, 70, 500, 400, 0.15)
      t7 = TituloPropiedad.inicializar_propiedad("Dalaran", 700, 70, 500, 400, 0.15)
      t8 = TituloPropiedad.inicializar_propiedad("Demacia", 800, 80, 600, 500, 0.15)
      t9 = TituloPropiedad.inicializar_propiedad("Reach", 900, 90, 800, 600, 0.2)
      t10 = TituloPropiedad.inicializar_propiedad("Rapture", 950, 95, 850, 700, 0.2)
      t11 = TituloPropiedad.inicializar_propiedad("Freedom Dive", 1000, 100, 1000, 750, 0.2)

      c0 = OtraCasilla.new(0, -1000, TipoCasilla::SALIDA)
      c1 = Calle.new(1, t0)
      c2 = Calle.new(2, t1)
      c3 = OtraCasilla.new(3, 0, TipoCasilla::SORPRESA)
      c4 = Calle.new(4, t2)
      c5 = OtraCasilla.new(5, 0, TipoCasilla::CARCEL)
      c6 = Calle.new(6, t3)
      c7 = Calle.new(7, t4)
      c8 = OtraCasilla.new(8, 0, TipoCasilla::SORPRESA)
      c9 = Calle.new(9, t5)
      c10 = OtraCasilla.new(10, 0, TipoCasilla::PARKING)
      c11 = Calle.new(11, t6)
      c12 = Calle.new(12, t7)
      c13 = OtraCasilla.new(13, 0, TipoCasilla::SORPRESA)
      c14 = Calle.new(14, t8)
      c15 = OtraCasilla.new(15, 0, TipoCasilla::JUEZ)
      c16 = Calle.new(16, t9)
      c17 = OtraCasilla.new(17, 500, TipoCasilla::IMPUESTO)
      c18 = Calle.new(18, t10)
      c19 = Calle.new(19, t11)

      @casillas  << c0 
      @casillas  << c1
      @casillas  << c2
      @casillas  << c3
      @casillas  << c4
      @casillas  << c5
      @casillas  << c6
      @casillas  << c7
      @casillas  << c8
      @casillas  << c9
      @casillas  << c10
      @casillas  << c11
      @casillas  << c12
      @casillas  << c13
      @casillas  << c14
      @casillas  << c15
      @casillas  << c16
      @casillas  << c17
      @casillas  << c18
      @casillas  << c19
      @carcel = c5
      @salida = c0
    end
    
    def es_casilla_carcel(numero_casilla)
        @casillas.at(numero_casilla).tipo == TipoCasilla::CARCEL
    end
    
    def obtener_casilla_final(casilla, desplazamiento)
      @casillas.at((casilla.numero_casilla + desplazamiento) % 20)
    end
    
    def obtener_casilla_numero(numero_casilla)
      @casillas.at(numero_casilla)
    end
    
    def to_s
      salida = "Tablero{casillas="
      @casillas.each do |casilla|
        salida = salida + "#{casilla.to_s}, "
      end
      salida = salida + "carcel=#{@carcel.to_s}}"
      
      salida
    end
    
  end
end
