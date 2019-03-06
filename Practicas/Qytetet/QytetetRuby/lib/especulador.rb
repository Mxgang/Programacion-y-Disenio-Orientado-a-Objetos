# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

module ModeloQytetet
  class Jugador
  end
  
  class Especulador < Jugador
    attr_accessor :fianza

    def self.copia(j, f)
      especulador = super(j)
      especulador.fianza = f
      especulador
    end

    
    def pagar_impuesto
      coste_impuesto =  @casilla_actual.coste / 2
      modificar_saldo(-coste_impuesto)
    end

    def debo_ir_a_carcel
      debo = super && !pagar_fianza
      debo
    end

    def convertirme(fianza)
      self
    end

    def pagar_fianza
      tengo_s = tengo_saldo(@fianza)
      if(tengo_s)
        modificar_saldo(-@fianza)
        @encarcelado = false
      end
      tengo_s
    end

    def puedo_edificar_casa(titulo)
      num_casas = titulo.num_casas
      ((num_casas < 8) && es_de_mi_propiedad(titulo))
    end

    def puedo_edificar_hotel(titulo)
      num_casas = titulo.num_casas
      num_hoteles = titulo.num_hoteles
      (num_casas >=4 && num_hoteles < 8 && es_de_mi_propiedad(titulo))
    end  

    def to_s
      "ESPECULADOR (#{@fianza}) -> " + super.to_s
    end

  end
end