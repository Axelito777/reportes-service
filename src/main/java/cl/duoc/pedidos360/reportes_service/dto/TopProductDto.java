package cl.duoc.pedidos360.reportes_service.dto;

import lombok.*;

@Getter @Setter @AllArgsConstructor
public class TopProductDto {
    private Long productId;
    private Long cantidadVendida;
}