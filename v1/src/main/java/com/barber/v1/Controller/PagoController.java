package com.barber.v1.Controller;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barber.v1.Model.Reserva;
import com.barber.v1.Service.BoletaService;
import com.barber.v1.Service.ReservaService;
import com.barber.v1.Service.StripeService;
import com.barber.v1.dto.BoletaData;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "http://localhost:4200")

public class PagoController {


    private final StripeService stripeService;
    private final BoletaService boletaService;
    private final ReservaService reservaService;

    public PagoController(StripeService stripeService, BoletaService boletaService, ReservaService reservaService) {
        this.stripeService = stripeService;
        this.boletaService = boletaService;
        this.reservaService = reservaService;
    }

    @PostMapping("/crear-intento")
    public ResponseEntity<?> crearIntento(@RequestBody Map<String, Object> body) {
    try {
        Long reservaId = Long.valueOf(body.get("reservaId").toString());
        Reserva reserva = reservaService.findById(reservaId)
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        double precio = reserva.getServicio().getPrecio();
        long montoEnCentavos = Math.round(precio * 100);
        PaymentIntent intent = stripeService.crearIntentoPago(montoEnCentavos, "pen");
        return ResponseEntity.ok(Map.of("clientSecret", intent.getClientSecret()));
    } catch (Exception e) {
        return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
    }
    }


    @PostMapping("/boleta")
    public ResponseEntity<byte[]> generarBoleta(@RequestBody BoletaData data) {
        try {
            byte[] pdf = boletaService.generarBoleta(data);

            return ResponseEntity.ok()
                    .header("Content-Type", "application/pdf")
                    .header("Content-Disposition", "inline; filename=boleta.pdf")
                    .body(pdf);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/recibo/{paymentIntentId}")
    public ResponseEntity<?> getStripeReceiptUrl(@PathVariable String paymentIntentId) {
        try {
            String receiptUrl = stripeService.obtenerReceiptUrl(paymentIntentId);
            Map<String, String> response = new HashMap<>();
            response.put("receiptUrl", receiptUrl);
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener el recibo: " + e.getMessage());
        }
    }

    @GetMapping("/boleta/{reservaId}")
public ResponseEntity<byte[]> generarBoletaDesdeReserva(@PathVariable Long reservaId) {
    try {
        BoletaData data = boletaService.obtenerDatosBoletaDesdeReserva(reservaId);
        byte[] pdf = boletaService.generarBoleta(data);

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "inline; filename=boleta_" + reservaId + ".pdf")
                .body(pdf);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

@PostMapping("/confirmar-pago/{reservaId}")
public ResponseEntity<?> confirmarPago(@PathVariable Long reservaId, @RequestBody Map<String, String> body) {
    try {
        Reserva reserva = reservaService.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        reserva.setEstado(Reserva.Estado.PAGADO);
        reservaService.actualizarReserva(reservaId, reserva);

        return ResponseEntity.ok(Map.of("message", "Pago confirmado y reserva actualizada"));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
    }
}
    
}
