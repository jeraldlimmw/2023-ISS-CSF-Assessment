package ibf2022.batch3.assessment.csf.orderbackend.respositories;

import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;

import ibf2022.batch3.assessment.csf.orderbackend.models.PizzaOrder;

@Repository
public class OrdersRepository {

	@Autowired
	private MongoTemplate mongoTemplate;

	// TODO: Task 3
	// WARNING: Do not change the method's signature.
	// Write the native MongoDB query in the comment below
	//   Native MongoDB query here for add()
	/*
	db.orders.insert({
		_id: <id>
		date: <date>,
		total: <total price>,
		name: <name>,
		email: <email>,
		sauce: <sauce>,
		size: <size>,
		comments: <comments>,
		topppings: [<topping1>, <topping2>]
	})
	*/
	public void add(PizzaOrder order) {
		Document doc = new Document();
		doc.put("_id", order.getOrderId());
		doc.put("date", order.getDate());
		doc.put("total", order.getTotal());
		doc.put("name", order.getName());
		doc.put("email", order.getEmail());
		doc.put("thickCrust", order.getThickCrust());
		doc.put("sauce", order.getSauce());
		doc.put("size", order.getSize());
		if(!order.getComments().isEmpty()) {
			doc.put("comments", order.getComments());
		}
		doc.put("toppings", order.getToppings().toArray());
		mongoTemplate.insert(doc, "orders");
	}
	
	// TODO: Task 6
	// WARNING: Do not change the method's signature.
	// Write the native MongoDB query in the comment below
	//   Native MongoDB query here for getPendingOrdersByEmail()
	/*
	db.orders.find({
		delivered: {$exists: false},
		email: <email>
	}).sort({date: -1})
	*/
	public List<PizzaOrder> getPendingOrdersByEmail(String email) {
		List<PizzaOrder> pizzas = new LinkedList<>();
		
		Query query = new Query();
		query.addCriteria(Criteria.where("delivered").exists(false)
				.and("email").is(email))
				.with(Sort.by(Sort.Direction.DESC, "date"));
				
		List<Document> docs = mongoTemplate.find(query, Document.class, "orders");
		
		for (Document d : docs) {
			PizzaOrder p = new PizzaOrder();
			p.setOrderId(d.getString("_id"));
			p.setTotal(Float.parseFloat(d.getString("total")));
			p.setDate(d.getDate("date"));
		}
		return pizzas;
	}

	// TODO: Task 7
	// WARNING: Do not change the method's signature.
	// Write the native MongoDB query in the comment below
	//   Native MongoDB query here for markOrderDelivered()
	/*
	db.orders.updateOne(
		{_id : <orderId>},
		{$set: delivered: "true"}
	)
	*/
	public boolean markOrderDelivered(String orderId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is("orderId"));

		Update updateOps = new Update().set("delivered", "true");
		UpdateResult result = mongoTemplate.upsert(query, updateOps, Document.class, "orders");

		return  result.getModifiedCount() > 0;
	}


}
