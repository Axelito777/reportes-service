package cl.duoc.pedidos360.reportes_service.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_sale_reports")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductSaleReport {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private Long productId;
    private Integer quantity;
    private LocalDateTime createdAt;
}
