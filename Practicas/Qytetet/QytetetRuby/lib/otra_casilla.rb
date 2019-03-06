# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

require_relative "casilla"

module ModeloQytetet
  class OtraCasilla < Casilla

    attr_reader :tipo

    def initialize(n, c, type)
      super(n,c)
      @tipo = type
    end

    def to_s
      return super.to_s + ", tipo=#{@tipo}}\n"
    end

  end
end