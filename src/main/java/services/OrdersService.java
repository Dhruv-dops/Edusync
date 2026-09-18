package services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entities.Orders;
import repositories.OrdersRepo;

@Service
public class OrdersService {
	
	@Autowired
	OrdersRepo ordersRepo;
	
	public void saveOrders(Orders orders) {
		ordersRepo.save(orders);
	}
	
	public List<Orders> getUserCourses(String email){
        return ordersRepo.findByUserEmail(email);
    }

}
