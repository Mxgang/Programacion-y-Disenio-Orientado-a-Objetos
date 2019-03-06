# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

require_relative "casilla"

module ModeloQytetet
  class Calle < Casilla

    attr_accessor :titulo
    attr_reader :tipo

    @tipo = TipoCasilla::CALLE
      
    def initialize(numero_c, t)
      super(numero_c, t.precio_compra)
      @titulo = t
    end

    def asignar_propietario(jugador)
      @titulo.propietario = jugador
      @titulo
    end

    def pagar_alquiler
      coste_alquiler = @titulo.pagar_alquiler
      coste_alquiler
    end

    def propietario_encarcelado
      @titulo.propietario_encarcelado
    end

    def soy_edificable
      true
    end

    def tengo_propietario
      @titulo.tengo_propietario
    end

    def to_s
      super.to_s + ", tipo=TipoCasilla::CALLE, titulo=#{@titulo}"
    end
  
  end
end