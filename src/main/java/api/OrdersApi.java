package api;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import entities.Orders;
import services.OrdersService;

@RestController
@RequestMapping("/api")
public class OrdersApi {

	@Autowired
	OrdersService ordersService;
	
	@PostMapping("/storeOrderDetails")
	public ResponseEntity<String> savingOrdersInDb(@RequestBody Orders orders) throws RazorpayException {
		RazorpayClient razorpayClient = new RazorpayClient("rzp_test_SzVDVY5ghbYE3M", "Cd9OBQhdNnKk3MdXEQk5Hnvx");

		JSONObject orderRequest = new JSONObject();
		orderRequest.put("amount", orders.getCourseAmount());
		orderRequest.put("currency","INR");
		orderRequest.put("receipt", "rcpt_id_"+System.currentTimeMillis());

		Order order = razorpayClient.orders.create(orderRequest);
		
		String orderId = order.get("id");
		orders.setOrderId(orderId);
		
		ordersService.saveOrders(orders);
		return ResponseEntity.ok("Order details stored successfully");
	}
	
}
