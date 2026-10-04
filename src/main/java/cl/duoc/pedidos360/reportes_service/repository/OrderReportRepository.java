package cl.duoc.pedidos360.reportes_service.repository;

import cl.duoc.pedidos360.reportes_service.model.OrderReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;

public interface OrderReportRepository extends JpaRepository<OrderReport, Long> {

    @Query("SELECT COUNT(o) FROM OrderReport o WHERE o.createdAt BETWEEN :from AND :to")
    long countBetween(LocalDateTime from, LocalDateTime to);

    @Query("SELECT COALESCE(SUM(o.total), 0) FROM OrderReport o WHERE o.createdAt BETWEEN :from AND :to")
    Double sumTotalBetween(LocalDateTime from, LocalDateTime to);
}
