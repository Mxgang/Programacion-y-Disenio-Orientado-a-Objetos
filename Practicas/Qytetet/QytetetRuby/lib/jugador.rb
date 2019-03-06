# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

require_relative "especulador"

module ModeloQytetet
  class Jugador
    attr_reader :nombre, :saldo, :propiedades, :fianza
    attr_accessor :carta_libertad, :casilla_actual, :encarcelado, :tiene_carta_libertad
    
    def initialize(e, n, s, tcl, cl, p, ca)
      @encarcelado = e
      @nombre = n
      @saldo = s
      @tiene_carta_libertad = tcl
      @carta_libertad = cl
      @propiedades = p
      @casilla_actual = ca
    end
    
    def self.nuevo(n,s)
      self.new(false, n, s, false, nil, Array.new, 0)
    end
    
    def self.copia(j)
      self.new(j.encarcelado, j.nombre, j.saldo, j.tiene_carta_libertad,
               j.carta_libertad, j.propiedades, j.casilla_actual)
    end

    def <=>(otro_jugador)            
      otro_jugador.obtener_capital <=> obtener_capital     
    end
    
    def cancelar_hipoteca(titulo)
      coste_cancelar_hipoteca = titulo.calcular_coste_cancelar
      cancelada = false
      if(tengo_saldo(coste_cancelar_hipoteca) && es_de_mi_propiedad_hipotecada(titulo,true))
          modificar_saldo(-titulo.calcular_coste_cancelar)
          titulo.cancelar_hipoteca
          cancelada = true
      end
      cancelada
    end

    def comprar_titulo_propiedad
      comprado = false
      coste_compra = @casilla_actual.coste
      if(tengo_saldo(coste_compra))
        titulo = @casilla_actual.asignar_propietario(self)
        comprado = true
        @propiedades << titulo
        modificar_saldo(-coste_compra)
      end
      comprado
    end

    def convertirme(fianza)
      esp = Especulador.copia(self,fianza)
      esp
    end
    
    def cuantas_casas_hoteles_tengo
      contador = 0
      @propiedades.each do |p|
          contador = contador + p.num_casas + p.num_hoteles
      end
      contador
    end

    def debo_ir_a_carcel
      !@tiene_carta_libertad
    end
    
    def debo_pagar_alquiler
      es_de_mi_propiedad = es_de_mi_propiedad(@casilla_actual.titulo)
      tiene_propietario = false
      esta_encarcelado = false
      esta_hipotecada = false
      if(!es_de_mi_propiedad)
          tiene_propietario = @casilla_actual.tengo_propietario
          if(tiene_propietario)
              esta_encarcelado = @casilla_actual.propietario_encarcelado
              esta_hipotecada = @casilla_actual.titulo.hipotecada
          end
      end
      !es_de_mi_propiedad & tiene_propietario & !esta_encarcelado & !esta_hipotecada
    end

    def devolver_carta_libertad
      @tiene_carta_libertad = false
      @carta_libertad
    end

    def edificar_casa(titulo)
      edificada = false
      if(puedo_edificar_casa(titulo))
        coste_edificar_casa = titulo.precio_edificar
        tengo_saldo = tengo_saldo(coste_edificar_casa)
        if(tengo_saldo)
            titulo.edificar_casa
            modificar_saldo(-coste_edificar_casa)
            edificada = true
        end
      end
      edificada
    end

    def edificar_hotel(titulo)
      edificada = false
      if(puedo_edificar_hotel(titulo))
        coste_edificar_hotel = titulo.precio_edificar
        tengo_saldo = tengo_saldo(coste_edificar_hotel)
        if(tengo_saldo)
            titulo.edificar_hotel
            modificar_saldo(-coste_edificar_hotel)
            edificada = true
        end
      end
      edificada
    end

    def eliminar_de_mis_propiedades(titulo)
      @propiedades.delete(titulo)
      titulo.propietario = nil
    end

    def es_de_mi_propiedad(titulo)
      resultado = false
      @propiedades.each do |p|
        if(p == titulo)
            resultado = true
        end
      end
      resultado
    end

    def es_de_mi_propiedad_hipotecada(titulo, hipotecada)
      resultado = false
      propiedades_hipotecadas = obtener_propiedades(hipotecada)
      propiedades_hipotecadas.each do |p|
        if(p == titulo)
            resultado = true
        end
      end
      resultado
    end
=begin
    def estoy_en_calle_libre

    end
=end
    def hipotecar_propiedad(titulo)
      hipotecada = false
      if(es_de_mi_propiedad_hipotecada(titulo,false))
        coste_hipoteca = titulo.hipotecar
        modificar_saldo(coste_hipoteca)
        hipotecada = true
      end
      hipotecada
    end
    
    def ir_a_carcel(casilla)
      @casilla_actual = casilla
      @encarcelado = true
    end
    
    def modificar_saldo(cantidad)
      @saldo = @saldo + cantidad
      @saldo
    end
    
    def obtener_capital
      valor_propiedades = 0
      @propiedades.each do |p|
        valor_propiedades = valor_propiedades + p.precio_compra +
                             p.num_casas * p.precio_edificar +
                             4 * p.num_hoteles * p.precio_edificar
        if(p.hipotecada)
          valor_propiedades = valor_propiedades - p.hipoteca_base
        end
      end
      valor_propiedades + @saldo
    end
    
    def obtener_propiedades(hipotecada)
      properties = Array.new
      @propiedades.each do |p|
        if(p.hipotecada == hipotecada)
            properties << p
        end
      end
      properties
    end
    
    def pagar_alquiler
      coste_alquiler = @casilla_actual.pagar_alquiler
      modificar_saldo(-coste_alquiler)
    end
    
    def pagar_impuesto
      coste_impuesto = @casilla_actual.coste 
      modificar_saldo(-coste_impuesto)
    end
    
    def pagar_libertad(cantidad)
      tengo_saldo = tengo_saldo(cantidad)
      if(tengo_saldo)
          @encarcelado = false
          modificar_saldo(-cantidad)
      end
    end
    
    def puedo_edificar_casa(titulo)
      num_casas = titulo.num_casas
      ((num_casas < 4) && es_de_mi_propiedad(titulo))
    end
    
    def puedo_edificar_hotel(titulo)
      num_casas = titulo.num_casas
      num_hoteles = titulo.num_hoteles
      (num_casas == 4 && num_hoteles < 4 && es_de_mi_propiedad(titulo))
    end
    
    def tengo_carta_libertad
      @tiene_carta_libertad 
    end
    
    def tengo_saldo(cantidad)
      @saldo > cantidad
    end
    
    def vender_propiedad(casilla)
      resultado = false
      titulo = casilla.titulo
      if(titulo != nil && es_de_mi_propiedad(titulo))
          eliminar_de_mis_propiedades(titulo)
          precio_venta = titulo.calcular_precio_venta
          modificar_saldo(precio_venta)
          casilla.titulo = nil
          resultado = true
      end
      resultado
    end
    
    def to_s
      "Jugador{encarcelado=#{@texto}, nombre=#{@nombre}, saldo=#{@saldo}, carta_libertad=#{@carta_libertad}, propiedades=#{@propiedades.join}, casilla_actual=#{@casilla_actual}}"
    end
    private :eliminar_de_mis_propiedades, :es_de_mi_propiedad, :tengo_saldo
  end
end
