/*
package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.CommandHandler;
import com.basoft.eorder.domain.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

//@CommandHandler.AutoCommandHandler("OrderCommandHandler")
public class EOrderCommandHandler {


    @Autowired
    private OrderRepository orderRepository;

    EOrderCommandHandler(){
    }

    @Autowired
    public EOrderCommandHandler(OrderRepository or){
        this.orderRepository = or;
    }

    @CommandHandler.AutoCommandHandler(SaveOrder.NAME)
    @Transactional
    public Object saveOrder(SaveOrder sp){
        System.out.println("\n\n\n");
        System.out.println("Transactional --------");
        return "ok";
    }


    @CommandHandler.AutoCommandHandler("saveProduct")
    @Transactional
    public Object confirmOrder(SaveOrder sp){
        System.out.println("\n\n\n");
        System.out.println("Transactional --------");
        return "ok";
    }




}
*/
