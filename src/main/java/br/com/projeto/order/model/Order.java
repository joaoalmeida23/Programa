package br.com.projeto.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@Builder
@ToString
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrder;

    private Long customerId; // ID do cliente associado ao pedido
    private LocalDateTime orderDateTime;
    private String deliveryAddress;
    private OrderStatus status;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items;

    private boolean paymentConfirmed;

    public boolean isPaymentConfirmed(boolean paymentConfirmed){
        return paymentConfirmed;
    }
    public void setPaymentConfirmed(boolean paymentConfirmed){
        this.paymentConfirmed = paymentConfirmed;
    }

}
