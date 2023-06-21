package ibf2022.batch3.assessment.csf.orderbackend.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ibf2022.batch3.assessment.csf.orderbackend.models.PizzaOrder;
import ibf2022.batch3.assessment.csf.orderbackend.services.OrderException;
import ibf2022.batch3.assessment.csf.orderbackend.services.OrderingService;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

@Controller
@RequestMapping(path="/api")
public class OrderController {

	@Autowired
	private OrderingService orderSvc;

	// TODO: Task 3 - POST /api/order
	@PostMapping(path="/order")
	@ResponseBody
	public ResponseEntity<String> postOrder(@RequestBody String order) throws IOException {
		PizzaOrder po = this.createOrder(order);
		System.out.println(">>> Incoming PizzaOrder:" + po);
		
		PizzaOrder updatedPo;
		try {
			updatedPo = orderSvc.placeOrder(po);
			System.out.println(updatedPo);
		} catch (OrderException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("error: " + e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED)
				.body(Json.createObjectBuilder()
						.add("orderId", updatedPo.getOrderId())
						.add("date", updatedPo.getDate().toInstant().toEpochMilli())
						.add("name", updatedPo.getName())
						.add("email", updatedPo.getEmail())
						.add("total", updatedPo.getTotal())
						.build().toString());
	}

	// TODO: Task 6 - GET /api/orders/<email>
	@GetMapping(path="/orders/{email}")
	@ResponseBody
	public ResponseEntity<String> getOrdersByEmail (@PathVariable String email) {
		System.out.println(">>> Get orders for email: " + email);
		List<PizzaOrder> pList = orderSvc.getPendingOrdersByEmail(email);
		JsonArrayBuilder ab = Json.createArrayBuilder();

		for (PizzaOrder p : pList) {
			DateTimeFormatter fmt = DateTimeFormat.forPattern("MMMM d, yyyy");
			JsonObject o = Json.createObjectBuilder()
				.add("orderId", p.getOrderId())
				.add("date", fmt.print(p.getDate().toInstant().toEpochMilli()))
				.add("total", String.format("%.02f", p.getTotal()))
				.build();
			ab.add(o);
		}	
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(ab.build().toString());
	}

	// TODO: Task 7 - DELETE /api/order/<orderId>
	@DeleteMapping(path="/order/{orderId}")
	@ResponseBody
	public ResponseEntity<String> deleteOrderById (@PathVariable String orderId){
		System.out.println(">>> Delete orderId: " + orderId);
		
		boolean result = orderSvc.markOrderDelivered(orderId);

		if(!result) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("error: orderId not found");
		}
		return ResponseEntity.status(HttpStatus.OK)
					.body("");
	}


	private PizzaOrder createOrder(String json) throws IOException {
		PizzaOrder po = new PizzaOrder();

		try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
			JsonReader r = Json.createReader(is);
			JsonObject o = r.readObject();

			po.setName(o.getString("name"));
			po.setEmail(o.getString("email"));
			po.setSauce(o.getString("sauce"));
			po.setSize(o.getInt("size"));
			po.setComments(o.getString("comments"));
			JsonArray arr = o.getJsonArray("toppings");
			List<String> t = new LinkedList<>();
			for (JsonValue jsonValue : arr) {
				t.add(jsonValue.toString());
			}
			po.setToppings(t);
			po.setThickCrust(o.getString("base").contains("thick"));
		}
		return po;
	}
}
