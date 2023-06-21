package csf.pizzapriceapi.services;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import csf.pizzapriceapi.models.Pizza;
import csf.pizzapriceapi.models.Quote;

@Service
public class PricingService {
    private final float[] SIZE = {1f, 1.2f, 1.5f, 2f};

    // private final String[] SAUCE = {"classic", "signature", "salsa", "smoky",
    // "napolitana"};

    private final List<String> NORMAL_TOPPINGS = Arrays.asList(new String[]{"chicken", "vegetables", "pineapple"});

    private final List<String> PREMIUM_TOPPINGS = Arrays.asList(new String[]{"seafood", "beef", "cheese", "arugula"});

    public String getQuote(Pizza pizza) {
        Quote q = new Quote();
        q.setOrderId(UUID.randomUUID().toString().substring(0, 8));
        new DateTime();
        q.setDate(DateTime.now().getMillis());
        q.setTotal(getPrice(pizza));
        return "" + q.getOrderId() + "," + q.getDate() + "," 
                + String.format("%.2f",q.getTotal());
    }

    public float getPrice(Pizza pizza) {
        float price = 0f;

        switch(pizza.getSauce()) {
            case "signature":
                price = 15f;
                break;
            case "salsa":
                price = 14f;
                break;
            case "smoky":
                price = 13f;
                break;
            case "napolitana":
                price = 13f;
            default:
                price = 12f;
        }

        for (int i = 0; i < SIZE.length; i++) {
            if (pizza.getSize() ==  i) {
                price *= SIZE[i];
                break;
            }
        }

        if (pizza.getThickCrust()) {
            price += 2;
        }


        List<String> toppings = pizza.getToppings();
        for (int i = 0; i < toppings.size(); i++) {
            if (NORMAL_TOPPINGS.contains(toppings.get(i))) {
                price += 1;
            }
            if (PREMIUM_TOPPINGS.contains(toppings.get(i))) {
                price += 2;
            }
        }

        return price;
    }

}
