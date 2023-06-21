package csf.pizzapriceapi.controllers;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import csf.pizzapriceapi.models.Pizza;
import csf.pizzapriceapi.services.PricingService;

@Controller
@RequestMapping
public class PizzaPriceController {
    
    @Autowired
    private PricingService pSvc;

    @PostMapping(path="/order", consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces=MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public ResponseEntity<String> postPizza(@RequestBody MultiValueMap<String, String> form) {

        Pizza pizza = new Pizza();
        pizza.setName(form.getFirst("name"));
        pizza.setEmail(form.getFirst("email"));
        pizza.setSauce(form.getFirst("sauce"));
        pizza.setSize(Integer.parseInt(form.getFirst("size")));
        pizza.setThickCrust(Boolean.parseBoolean(form.getFirst("thickCrust")));
        String toppingsStr = form.getFirst("toppings").replaceAll("[\"]", "");
        String[] toppings = toppingsStr.trim().split("[, ]");
        pizza.setToppings(Arrays.asList((toppings)));
        pizza.setComments(form.getFirst("comments"));
        
        String quote = pSvc.getQuote(pizza);

        return ResponseEntity.status(HttpStatus.OK)
                .body(quote.toString());
    }

}
