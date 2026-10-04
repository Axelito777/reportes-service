package cl.duoc.pedidos360.reportes_service.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_reports")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderReport {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private Double total;
    private Integer itemCount;
    private LocalDateTime createdAt;
}
