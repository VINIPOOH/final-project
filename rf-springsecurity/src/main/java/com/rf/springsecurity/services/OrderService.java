package com.rf.springsecurity.services;

import com.rf.springsecurity.dto.OrdersDTO;
import com.rf.springsecurity.entity.cruise.Cruise;
import com.rf.springsecurity.entity.order.Order;
import com.rf.springsecurity.entity.user.User;
import com.rf.springsecurity.exceptions.NotEnoughMoney;
import com.rf.springsecurity.repository.OrderRepository;
import com.rf.springsecurity.repository.UserRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    //TODO find out if ship current capacity allows and add current capacity if allows

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;


    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public List<Order> findAllOrdersByCruise(Cruise cruise){
        return orderRepository.findAllByCruise(cruise);
    }

    public List<OrdersDTO> findAllOrdersByUser(User user){
        return orderRepository.findAllByUser(user);
    }
    
    public boolean buyCruise(@NonNull Order order, @NonNull Cruise cruise, @NonNull User user) throws NotEnoughMoney {
        order.setCruise(cruise);
        order.setUser(user);
        subBalance(user, order.getOrderPrice());
        buyDbChanges(order, user);
        return true;
    }

    @Transactional
    public void buyDbChanges(@NonNull Order order, @NonNull User user) {
        userRepository.save(user);
        orderRepository.save(order);
    }


    private User subBalance(User user, long orderSum) throws NotEnoughMoney {
        long totalBalance = user.getBalance() - orderSum;
        if (totalBalance < 0) {
            throw new NotEnoughMoney("Not Enough Money " + user.getBalance());
        }
        user.setBalance(user.getBalance() - orderSum);
        return user;
    }

    //    private long getTicketPriceWithDiscount(Ticket ticket){
//        return ticket.getPrice() -  Math.round(((double)ticket.getPrice() * ticket.getDiscount()/ONE_HUNDRED_PERCENT));
//    }

//    private long getTotalPriceOfExcursions(Set<Excursion> excursions){
//        return excursions.stream().mapToLong(Excursion::getPrice).sum();
//    }

//    private long getTotalCruisePrice(@NonNull Ticket ticket, @NonNull Set<Excursion> excursions){
//        return  getTicketPriceWithDiscount(ticket) + getTotalPriceOfExcursions(excursions);
//    }

}
