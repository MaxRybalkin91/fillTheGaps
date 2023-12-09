package course.concurrency.m3_shared.immutable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OrderService {
    private final Map<Long, Order> currentOrders = new ConcurrentHashMap<>();

    public long createOrder(List<Item> items) {
        final Order order = new Order(items, null, false, Order.Status.NEW);
        currentOrders.put(order.getId(), order);
        return order.getId();
    }

    public void updatePaymentInfo(long orderId, PaymentInfo paymentInfo) {
        final Order paid = currentOrders.computeIfPresent(orderId, (key, value) -> value.pay(paymentInfo));
        if (paid != null && paid.isReady()) {
            paid.tryDeliver();
        }
    }

    public void setPacked(long orderId) {
        final Order packed = currentOrders.computeIfPresent(orderId, (key, value) ->
                value.pack());
        if (packed != null && packed.isReady()) {
            packed.tryDeliver();
        }
    }

    public boolean isDelivered(long orderId) {
        return currentOrders.get(orderId).isDelivered();
    }
}
