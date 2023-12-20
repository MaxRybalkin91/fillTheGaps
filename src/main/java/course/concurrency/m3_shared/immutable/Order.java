package course.concurrency.m3_shared.immutable;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Order {
    public enum Status {NEW, IN_PROGRESS, DELIVERED}

    private final AtomicLong id = new AtomicLong(0L);
    private final List<Item> items;
    private final PaymentInfo paymentInfo;
    private final Boolean isPacked;
    private Status status;

    public Order(List<Item> items, PaymentInfo paymentInfo, Boolean isPacked, Status status) {
        id.getAndIncrement();
        this.items = items;
        this.paymentInfo = paymentInfo;
        this.isPacked = isPacked;
        this.status = status == null ? Status.NEW : status;
    }

    public Order pay(PaymentInfo paymentInfo) {
        return new Order(this.items, paymentInfo, this.isPacked, Status.IN_PROGRESS);
    }

    public Order pack() {
        return new Order(this.items, this.paymentInfo, true, Status.IN_PROGRESS);
    }

    public void tryDeliver() {
        if (this.isReady()) {
            this.status = Status.DELIVERED;
        }
    }

    public boolean isReady() {
        return this.items != null
                && !this.items.isEmpty()
                && this.paymentInfo != null
                && this.isPacked;
    }

    public Long getId() {
        return id.get();
    }

    public boolean isDelivered() {
        return this.status == Status.DELIVERED;
    }
}
