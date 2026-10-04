package cl.duoc.pedidos360.reportes_service.dto;

import lombok.*;

@Getter @Setter @AllArgsConstructor
public class SummaryResponse {
    private long totalPedidos;
    private double totalVentas;
}
