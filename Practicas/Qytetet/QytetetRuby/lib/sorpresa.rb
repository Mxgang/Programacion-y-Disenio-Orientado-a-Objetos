# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.
# encoding: utf­8

module ModeloQytetet
  class Sorpresa

    attr_reader :texto, :valor, :tipo_sorpresa

    def initialize(t,v,ts)
      @texto = t
      @valor = v
      @tipo_sorpresa = ts
    end

    def to_s
      "Sorpresa{texto=#{@texto}, valor=#{@valor}, tipo=#{@tipo_sorpresa.to_s}}"
    end 
  end
end