package cl.duoc.pedidos360.reportes_service.controller;

import cl.duoc.pedidos360.reportes_service.dto.*;
import cl.duoc.pedidos360.reportes_service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final OrderReportRepository orderReportRepository;
    private final ProductSaleReportRepository productSaleReportRepository;

    @GetMapping("/summary")
    public ResponseEntity<SummaryResponse> summary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        long totalPedidos = orderReportRepository.countBetween(from, to);
        double totalVentas = orderReportRepository.sumTotalBetween(from, to);
        return ResponseEntity.ok(new SummaryResponse(totalPedidos, totalVentas));
    }

    @GetMapping("/top-products")
    public ResponseEntity<List<TopProductDto>> topProducts() {
        List<TopProductDto> result = productSaleReportRepository.topProductsRaw().stream()
                .map(row -> new TopProductDto((Long) row[0], (Long) row[1]))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}