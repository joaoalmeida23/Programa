package br.com.projeto.order.service;


import br.com.projeto.order.exception.OrderNotFoundException;
import br.com.projeto.order.model.Order;
import br.com.projeto.order.model.OrderStatus;
import br.com.projeto.order.repository.OrderItemRepository;
import br.com.projeto.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.openmbean.OpenDataException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id){
        return orderRepository.findById(id).orElse(null);
    }

    public Order createOrder(Order order) throws OpenDataException {
       try {
           order.setStatus(OrderStatus.PENDING);
           order.setOrderDateTime(LocalDateTime.now());

           return orderRepository.save(order);
       } catch (Exception ex){
           throw new OpenDataException("Erro ao criar o pedido: " + ex.getMessage());
       }

    }

    public Order updateOrder(Long id, Order updatedOrder){
        Order existingOrder = orderRepository.findById(id).orElse(null);
         if (existingOrder != null){

             existingOrder.setCustomerId(updatedOrder.getCustomerId());
             existingOrder.setDeliveryAddress(updatedOrder.getDeliveryAddress());

             if (updatedOrder.isPaymentConfirmed()){
                 existingOrder.setStatus(OrderStatus.PROCESSING);
             }

             return orderRepository.save(existingOrder);
         } else {
             return null;
         }
    }

    public void markOrderAsDelivered(Long idOrder) throws OrderNotFoundException {
        Order order = orderRepository.findById(idOrder).orElse(null);
         if (order != null){
             order.setStatus(OrderStatus.DELIVERED);
             orderRepository.save(order);
         } else {
             throw new OrderNotFoundException("Pedido não encontrado com o ID: " + idOrder);
         }
    }

    public void cancelOrder(Long id) throws OrderNotFoundException {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            if (order.getStatus() != OrderStatus.DELIVERED) {
                throw new OrderNotFoundException("Não é possível cancelar um pedido já entregue.");

            }
                order.setStatus(OrderStatus.CANCELED);
                orderRepository.save(order);

        } else {
            throw new OrderNotFoundException("Pedido não encontrado com o ID: " + id);
        }
    }

    public void deleteOrder(Long id){
        orderItemRepository.deleteById(id);
        orderRepository.deleteById(id);
    }

}
