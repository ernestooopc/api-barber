package com.barber.v1.Service;
import com.barber.v1.Model.Reserva;
import com.barber.v1.Model.Servicio;
import com.barber.v1.Repository.ReservaRepository;
import com.barber.v1.dto.BoletaData;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoletaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public byte[] generarBoleta(BoletaData data) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4, 36, 36, 72, 36);
        doc.open();

        // -------------------------
        // 1) CABECERA SUPERIOR
        // -------------------------
        PdfPTable encabezado = new PdfPTable(2);
        encabezado.setWidthPercentage(100);
        encabezado.setWidths(new float[] { 3, 2 });
        encabezado.getDefaultCell().setBorder(Rectangle.BOX);
        encabezado.getDefaultCell().setPadding(6);

        // 1.1) Empresa (izquierda)
        PdfPCell celdaEmpresa = new PdfPCell();
        celdaEmpresa.setBorder(Rectangle.BOX);
        celdaEmpresa.setPadding(4);
        celdaEmpresa.addElement(new Phrase("APPbarber S.A.C.",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        celdaEmpresa.addElement(new Phrase("CAL. JOAQUÍN CAPELLO 547 203 ZONA",
                FontFactory.getFont(FontFactory.HELVETICA, 9)));
        celdaEmpresa.addElement(new Phrase("MIRAFLORES - LIMA - LIMA",
                FontFactory.getFont(FontFactory.HELVETICA, 9)));
        celdaEmpresa.addElement(new Phrase("Tel: 987654321",
                FontFactory.getFont(FontFactory.HELVETICA, 9)));
        celdaEmpresa.addElement(new Phrase("Web: www.appbarber.com",
                FontFactory.getFont(FontFactory.HELVETICA, 9)));
        encabezado.addCell(celdaEmpresa);

        // 1.2) Boleta (derecha)
        PdfPCell celdaBoleta = new PdfPCell();
        celdaBoleta.setBorder(Rectangle.BOX);
        celdaBoleta.setPadding(4);
        celdaBoleta.setHorizontalAlignment(Element.ALIGN_CENTER);
        celdaBoleta.setBackgroundColor(new Color(230, 230, 230));
        celdaBoleta.addElement(new Phrase("BOLETA DE VENTA ELECTRÓNICA",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11)));
        celdaBoleta.addElement(new Phrase("RUC: 20556106909",
                FontFactory.getFont(FontFactory.HELVETICA, 9)));
        celdaBoleta.addElement(new Phrase(data.getNumeroBoleta(),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Font.ITALIC, Color.RED)));
        encabezado.addCell(celdaBoleta);

        doc.add(encabezado);
        doc.add(Chunk.NEWLINE);

        // -------------------------
        // 2) DATOS DE FECHAS Y CLIENTE
        // -------------------------
        PdfPTable datos = new PdfPTable(2);
        datos.setWidthPercentage(100);
        datos.setWidths(new float[] { 1, 2 });
        datos.getDefaultCell().setBorder(Rectangle.BOX);
        datos.getDefaultCell().setPadding(4);

        PdfPTable subIzq = new PdfPTable(2);
        subIzq.setWidthPercentage(100);
        subIzq.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        subIzq.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

        subIzq.addCell(makeLabel("Fecha de Vencimiento:"));
        subIzq.addCell(new Phrase("-"));

        subIzq.addCell(makeLabel("Fecha de Emisión:"));
        subIzq.addCell(new Phrase(data.getFechaEmision()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        // insertar la tabla pequeña en la celda izquierda
        PdfPCell cIzq = new PdfPCell(subIzq);
        cIzq.setBorder(Rectangle.BOX);
        datos.addCell(cIzq);

        // Celda derecha: datos del cliente
        PdfPCell cDer = new PdfPCell();
        cDer.setBorder(Rectangle.BOX);
        cDer.setPadding(4);
        cDer.addElement(makeLabel("Señor(es):"));
        cDer.addElement(new Phrase(data.getClienteNombre(),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
        cDer.addElement(makeLabel("Tipo de Moneda:"));
        cDer.addElement(new Phrase("NUEVOS SOLES"));
        cDer.addElement(makeLabel("Observación:"));
        cDer.addElement(new Phrase("SERVICIO DE CORTE PRESTADO EN APPbarber SAC",
                FontFactory.getFont(FontFactory.HELVETICA, 9)));
        datos.addCell(cDer);

        doc.add(datos);
        doc.add(Chunk.NEWLINE);

        // -------------------------
        // 3) TABLA DE DETALLE
        // -------------------------
        PdfPTable detalle = new PdfPTable(new float[] { 1f, 1.2f, 1.2f, 3f, 1.5f, 1f, 1.5f });
        detalle.setWidthPercentage(100);
        detalle.setHeaderRows(1);

        // Encabezados
        for (String h : new String[] {
                "Cantidad", "Unidad Medida", "Código", "Descripción",
                "Valor Unitario", "Descuento", "Importe de Venta"
        }) {
            PdfPCell th = new PdfPCell(new Phrase(h,
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9)));
            th.setHorizontalAlignment(Element.ALIGN_CENTER);
            th.setBackgroundColor(new Color(200, 200, 200));
            th.setPadding(4);
            detalle.addCell(th);
        }

        // Fila única
        detalle.addCell(makeCell("1.00", false, Element.ALIGN_CENTER));
        detalle.addCell(makeCell("UNIDAD", false, Element.ALIGN_CENTER));
        detalle.addCell(makeCell("SERV01", false, Element.ALIGN_CENTER));
        detalle.addCell(makeCell(data.getServicio() + "\n" + data.getDescripcion(), false, Element.ALIGN_LEFT));
        detalle.addCell(makeCell(String.format("S/ %.2f", data.getPrecio()), false, Element.ALIGN_RIGHT));
        detalle.addCell(makeCell("0.00", false, Element.ALIGN_RIGHT));
        detalle.addCell(makeCell(String.format("S/ %.2f", data.getPrecio()), false, Element.ALIGN_RIGHT));

        doc.add(detalle);
        doc.add(Chunk.NEWLINE);
        double igv = data.getPrecio() - (data.getPrecio() / 1.18);
        // -------------------------
        // 4) RESUMEN FINAL (otros cargos, tributos, total)
        // -------------------------
        PdfPTable resumen = new PdfPTable(2);
        resumen.setWidthPercentage(40);
        resumen.setHorizontalAlignment(Element.ALIGN_RIGHT);
        resumen.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        resumen.setSpacingBefore(6);

        // Otros cargos
        resumen.addCell(makeLabel("Otros Cargos:"));
        resumen.addCell(makeCell("0.00", false, Element.ALIGN_RIGHT));

        // Otros tributos
        resumen.addCell(makeLabel("Otros Tributos:"));
        resumen.addCell(makeCell("0.00", false, Element.ALIGN_RIGHT));

        // IGV 18%
        resumen.addCell(makeLabel("IGV (18%):"));
        resumen.addCell(makeCell(String.format("S/ %.2f", igv), false, Element.ALIGN_RIGHT));

        // Importe Total
        PdfPCell labelTotal = new PdfPCell(
                new Phrase("Importe Total:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
        labelTotal.setBorder(Rectangle.TOP);
        labelTotal.setPaddingTop(6);
        resumen.addCell(labelTotal);

        PdfPCell valorTotal = new PdfPCell(
                new Phrase(String.format("S/ %.2f", data.getPrecio()),
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
        valorTotal.setBorder(Rectangle.TOP);
        valorTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        valorTotal.setPaddingTop(6);
        resumen.addCell(valorTotal);

        doc.add(resumen);

        doc.close();
        return output.toByteArray();
    }

    public BoletaData obtenerDatosBoletaDesdeReserva(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        Servicio tipoCorte = reserva.getServicio();

        BoletaData data = new BoletaData();
        data.setClienteNombre(reserva.getUsuario().getNombre());
        data.setNumeroBoleta("B001-" + reserva.getId());
        data.setFechaEmision(reserva.getFechaHora());
        data.setServicio(tipoCorte.getNombre());
        data.setDescripcion(tipoCorte.getDescripcion());
        data.setPrecio(tipoCorte.getPrecio());
        return data;
    }

    private PdfPCell makeCell(String text, boolean bold, int alignment) {
        Font f = bold
                ? FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)
                : FontFactory.getFont(FontFactory.HELVETICA, 10);
        PdfPCell c = new PdfPCell(new Phrase(text, f));
        c.setPadding(5);
        c.setBorder(Rectangle.NO_BORDER);
        c.setHorizontalAlignment(alignment);
        return c;
    }

    private Phrase makeLabel(String text) {
        return new Phrase(text + " ",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9));
    }

}
