package com.barber.v1.Security;

import java.util.UUID;

// En tu BarberoServiceImpl o en una clase utilitaria de seguridad:
public class PasswordUtils {

    // 1. Longitud y aleatoriedad por encima de complejidad artificial
    public static String generarContrasenaTemporal(String nombre) {
        // Tomamos las iniciales del barbero para darle contexto
        String iniciales = (nombre != null && nombre.length() >= 2) 
            ? nombre.substring(0, 2).toUpperCase() 
            : "BB";
        
        // Generamos un UUID corto para garantizar alta entropía y aleatoriedad
        String tokenAleatorio = UUID.randomUUID().toString().substring(0, 8);
        
        return iniciales + "-" + tokenAleatorio; // Ejemplo resultante: "JO-f3b2a19e"
    }
}