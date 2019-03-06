# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

require_relative "tipo_casilla"

module ModeloQytetet
  class Casilla
    attr_reader :numero_casilla, :coste, :titulo, :tipo
    def initialize(n, c)
      @numero_casilla = n
      @coste = c
    end
    def soy_edificable
      @tipo == TipoCasilla::CALLE 
    end
    
    def to_s
      "Casilla{numero_casilla=#{@numero_casilla}, coste=#{@coste}"
    end
  end
end