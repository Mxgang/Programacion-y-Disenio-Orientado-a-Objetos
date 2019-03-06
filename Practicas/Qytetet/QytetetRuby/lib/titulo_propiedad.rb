# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

module ModeloQytetet
  class TituloPropiedad
    attr_accessor :hipotecada, :propietario
    attr_reader :num_hoteles, :num_casas, :nombre, :precio_compra, :alquiler_base,
      :hipoteca_base, :precio_edificar, :factor_revalorizacion
    def initialize(hip, num_h, num_c, name, pr_com, al_b, hip_b, pr_ed, fac_rev)
      @hipotecada = hip
      @num_hoteles = num_h
      @num_casas = num_c
      @nombre = name
      @precio_compra = pr_com
      @alquiler_base = al_b
      @hipoteca_base = hip_b
      @precio_edificar = pr_ed
      @factor_revalorizacion = fac_rev
    end
    
    def self.inicializar_propiedad(name, pr_com, al_b, hip_b, pr_ed, fac_rev)
      new(false, 0, 0, name, pr_com, al_b, hip_b, pr_ed, fac_rev)
    end
    
    def calcular_coste_cancelar
      (calcular_coste_hipotecar * 1.1).to_i 
    end
    
    def calcular_coste_hipotecar
      (@hipoteca_base + @num_casas * 0.5 * @hipoteca_base + @num_hoteles * @hipoteca_base).to_i
    end
    
    def calcular_importe_alquiler
      @alquiler_base + ((@num_casas * 0.5 + @num_hoteles * 2) * @precio_edificar).to_i 
    end
    
    def calcular_precio_venta
      (@precio_compra + (@num_casas + @num_hoteles) * @precio_edificar * @factor_revalorizacion).to_i
    end
    
    def cancelar_hipoteca
      @hipotecada = false
    end
    
    def edificar_casa
      @num_casas = @num_casas + 1
    end
    
    def edificar_hotel
      @num_casas = @num_casas - 4
      @num_hoteles = @num_hoteles + 1 
    end
    
    def hipotecar
      coste_hipoteca = calcular_coste_hipotecar
      @hipotecada = true
      coste_hipoteca
    end
    
    def pagar_alquiler
      coste_alquiler = calcular_importe_alquiler
      @propietario.modificar_saldo(coste_alquiler)
      coste_alquiler
    end
    
    def propietario_encarcelado
      @propietario.encarcelado
    end
    
    def tengo_propietario
      @propietario != nil
    end
    
    def to_s
      "TituloPropiedad{nombre=#{@nombre}, hipotecada=#{@hipotecada}, " +
      "precio_compra=#{@precio_compra}, alquiler_base=#{@alquiler_base}, " +
      "hipoteca_base=#{@hipoteca_base}, precio_edificar=#{@precio_edificar}, " +
      "num_hoteles=#{@num_hoteles}, num_casas=#{@num_casas}, " +
      "factor_revalorizacion=#{@factor_revalorizacion}}"
    end
  private_class_method :new
  end
end