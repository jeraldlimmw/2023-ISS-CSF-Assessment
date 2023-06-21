package ibf2022.batch3.assessment.csf.orderbackend.respositories;

import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
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
		email: "jerald.mw.lim@gmail.com"
	}).sort({date: -1})
	*/
	/*
	db.orders.aggregate([
		{$match:
			{
				delivered: {$exists: false},
				email: "jerald.mw.lim@gmail.com"
			}
		},
		{$project:
			{_id:1, total:1, date:1}
		},
		{$sort:
			{date: -1}
		}	
	])
	*/
	public List<PizzaOrder> getPendingOrdersByEmail(String email) {
		List<PizzaOrder> pizzas = new LinkedList<>();
		
		// Query query = new Query();
		// query.addCriteria(Criteria.where("delivered").exists(false)
		// 		.and("email").is(email))
		// 		.with(Sort.by(Sort.Direction.DESC, "date"));
				
		// List<Document> docs = mongoTemplate.find(query, Document.class, "orders");

		MatchOperation mOp = Aggregation.match(
				Criteria.where("delivered").exists(false)
				.and("email").is(email));
		ProjectionOperation pOp = Aggregation.project("_id", "total", "date");
		SortOperation sOp = Aggregation.sort(Sort.by(Direction.DESC, "date"));

		Aggregation pipeline = Aggregation.newAggregation(mOp, pOp, sOp);
		AggregationResults<Document> docs = mongoTemplate.aggregate(
				pipeline, "orders", Document.class); 
		
		for (Document d : docs) {
			PizzaOrder p = new PizzaOrder();
			p.setOrderId(d.getString("_id"));
			p.setTotal(d.getDouble("total").floatValue());
			p.setDate(d.getDate("date"));
			pizzas.add(p);
			System.out.println(">>>> Pizza " + p + "added to list");
		}
		return pizzas;
	}

	// TODO: Task 7
	// WARNING: Do not change the method's signature.
	// Write the native MongoDB query in the comment below
	//   Native MongoDB query here for markOrderDelivered()
	/*
	db.orders.updateOne(
		{_id : "6a9cc744"},
		{$set: {delivered: "true"}}
	)
	*/
	public boolean markOrderDelivered(String orderId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(orderId));

		Update updateOps = new Update().set("delivered", "true");
		UpdateResult result = mongoTemplate.updateFirst(query, updateOps, Document.class, "orders");

		System.out.println(">>>> Updated " + result.getMatchedCount() + " result in Mongo");
		return result.getModifiedCount() > 0;
	}


}
