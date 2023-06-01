package ibf2022.batch3.assessment.csf.orderbackend.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import ibf2022.batch3.assessment.csf.orderbackend.models.PizzaOrder;
import ibf2022.batch3.assessment.csf.orderbackend.respositories.OrdersRepository;
import ibf2022.batch3.assessment.csf.orderbackend.respositories.PendingOrdersRepository;

@Service
public class OrderingService {

	@Autowired
	private OrdersRepository ordersRepo;

	@Autowired
	private PendingOrdersRepository pendingOrdersRepo;

	private final String URL = "https://pizza-pricing-production.up.railway.app";

	// TODO: Task 5
	// WARNING: DO NOT CHANGE THE METHOD'S SIGNATURE
	public PizzaOrder placeOrder(PizzaOrder order) throws OrderException {
		System.out.println(">>> Service - Placing order");

		RestTemplate template = new RestTemplate();

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("name", order.getName());
		map.add("email", order.getEmail());
		map.add("sauce", order.getSauce());
		map.add("size", order.getSize().toString());
		map.add("thickCrust", order.getThickCrust().toString());
		map.add("toppings", order.getToppings().toString());
		map.add("comments", order.getComments());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

		ResponseEntity<String> payload = template.exchange(URL, HttpMethod.POST, entity, String.class);

		String[] payloadArr = payload.getBody().split("[,]");
		System.out.println(">>> Service Payload: " + payloadArr);
		order.setOrderId(payloadArr[0]);
		long epoch = Long.parseLong(payloadArr[1]);
		order.setDate(new Date(epoch * 1000));
		order.setTotal(Float.parseFloat(payloadArr[2]));

		return order;
	}

	// For Task 6
	// WARNING: Do not change the method's signature or its implemenation
	public List<PizzaOrder> getPendingOrdersByEmail(String email) {
		return ordersRepo.getPendingOrdersByEmail(email);
	}

	// For Task 7
	// WARNING: Do not change the method's signature or its implemenation
	public boolean markOrderDelivered(String orderId) {
		return ordersRepo.markOrderDelivered(orderId) && pendingOrdersRepo.delete(orderId);
	}


}
