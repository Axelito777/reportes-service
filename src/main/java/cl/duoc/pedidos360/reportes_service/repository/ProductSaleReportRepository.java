package cl.duoc.pedidos360.reportes_service.repository;

import cl.duoc.pedidos360.reportes_service.model.ProductSaleReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductSaleReportRepository extends JpaRepository<ProductSaleReport, Long> {

    @Query("SELECT p.productId, SUM(p.quantity) FROM ProductSaleReport p GROUP BY p.productId ORDER BY SUM(p.quantity) DESC")
    List<Object[]> topProductsRaw();
}
