# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

require "singleton"

module ModeloQytetet
  class Dado
    attr_reader :valor
    include Singleton
    
    def initialize
      
    end
    
    def tirar
      @valor = rand(6) + 1
      valor
    end
  end
end