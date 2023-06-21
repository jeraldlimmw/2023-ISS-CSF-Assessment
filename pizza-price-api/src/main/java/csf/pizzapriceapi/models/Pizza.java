package csf.pizzapriceapi.models;

import java.util.LinkedList;
import java.util.List;

public class Pizza {
	private String name;
	private String email;
	private String sauce;
	private Integer size;
	private Boolean thickCrust = false;
	private List<String> toppings = new LinkedList<>();
	private String comments;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSauce() {
        return sauce;
    }
    public void setSauce(String sauce) {
        this.sauce = sauce;
    }
    public Integer getSize() {
        return size;
    }
    public void setSize(Integer size) {
        this.size = size;
    }
    public Boolean getThickCrust() {
        return thickCrust;
    }
    public void setThickCrust(Boolean thickCrust) {
        this.thickCrust = thickCrust;
    }
    public List<String> getToppings() {
        return toppings;
    }
    public void setToppings(List<String> toppings) {
        this.toppings = toppings;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Pizza [name=" + name + ", email=" + email + ", sauce=" + sauce + ", size=" + size + ", thickCrust="
                + thickCrust + ", toppings=" + toppings + ", comments=" + comments + "]";
    }

}
