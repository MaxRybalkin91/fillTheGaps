package course.concurrency.m3_shared.immutable;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class OrderService {

    private final ConcurrentHashMap<Long, Order> currentOrders = new ConcurrentHashMap<>();

    public long createOrder(List<Item> items) {
        final Order order = new Order(items, null, false, Order.Status.NEW);
        currentOrders.put(order.getId(), order);
        return order.getId();
    }

    public void updatePaymentInfo(long orderId, PaymentInfo paymentInfo) {
        final Order paid = currentOrders.computeIfPresent(orderId, (key, o) -> o.pay(paymentInfo));
        if (paid != null && paid.isReady()) {
            deliver(paid);
        }
    }

    public void setPacked(long orderId) {
        final Order packed = currentOrders.computeIfPresent(orderId, (key, o) ->
                o.pack());
        if (packed != null && packed.isReady()) {
            deliver(packed);
        }
    }

    private void deliver(Order order) {
        currentOrders.computeIfPresent(order.getId(), (key, o) -> o.deliver());
    }

    public boolean isDelivered(long orderId) {
        return currentOrders.get(orderId).isDelivered();
    }
}
