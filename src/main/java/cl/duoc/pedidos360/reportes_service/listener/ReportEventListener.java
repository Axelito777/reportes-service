package cl.duoc.pedidos360.reportes_service.listener;

import cl.duoc.pedidos360.reportes_service.config.RabbitConfig;
import cl.duoc.pedidos360.reportes_service.event.OrderCreatedEvent;
import cl.duoc.pedidos360.reportes_service.model.*;
import cl.duoc.pedidos360.reportes_service.repository.*;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReportEventListener {

    private final OrderReportRepository orderReportRepository;
    private final ProductSaleReportRepository productSaleReportRepository;

    @RabbitListener(queues = RabbitConfig.REPORTS_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void registrarReporte(OrderCreatedEvent event, Channel channel,
                                  @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        try {
            LocalDateTime ahora = LocalDateTime.now();

            orderReportRepository.save(OrderReport.builder()
                    .orderId(event.getOrderId())
                    .total(event.getTotal())
                    .itemCount(event.getItems() != null ? event.getItems().size() : 0)
                    .createdAt(ahora)
                    .build());

            if (event.getItems() != null) {
                event.getItems().forEach(item ->
                        productSaleReportRepository.save(ProductSaleReport.builder()
                                .orderId(event.getOrderId())
                                .productId(item.getProductId())
                                .quantity(item.getQuantity())
                                .createdAt(ahora)
                                .build()));
            }

            log.info("Reporte guardado para orden {}", event.getOrderId());
            channel.basicAck(tag, false);

        } catch (Exception e) {
            log.error("Fallo guardando reporte de orden {}: {}", event.getOrderId(), e.getMessage());
            try {
                channel.basicNack(tag, false, false);
            } catch (IOException io) {
                log.error("Error confirmando nack: {}", io.getMessage());
            }
        }
    }
}