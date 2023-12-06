package com.example.demo.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.type.descriptor.jdbc.TimestampWithTimeZoneJdbcType;

import jakarta.persistence.*; // for Spring Boot 3
// import javax.persistence.*; // for Spring Boot 2

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "purchaser")
	private String purchaser;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "product")
	private String product;

	@Column(name = "created_at")
	private String created_at;

	@Column(name = "order_id")
	private Integer order_id;

	@Column(name = "producer")
	private String producer;

	@Column(name = "country")
	private String country;

	@Column(name = "email")
	private String email;

	public Order() {

	}

	public Order(String purchaser, Integer quantity, String product, String created_at, Integer order_id, String producer, String country, String email) {
		this.purchaser = purchaser;
		this.quantity = quantity;
		this.product = product;
		this.created_at = created_at;
		this.order_id = order_id;
		this.producer = producer;
		this.country = country;
		this.email = email;
	}

	public long getId() {
		return id;
	}

	public String getPurchaser() {
		return purchaser;
	}

	public void setPurchaser(String purchaser) {
		this.purchaser = purchaser;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public Integer getOrderId() {
		return order_id;
	}

	public void setOrderId(Integer order_id) {
		this.order_id = order_id;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	

	@Override
	public String toString() {
		return "Order [id=" + id + ", purchaser=" + purchaser + ", quantity=" + quantity + ", product=" + product
				+ ", created_at=" + created_at + ", order_id=" + order_id + ", producer=" + producer + ", country="
				+ country + ", email=" + email + "]";
	}

}


/* @Entity
@Table(name = "Order_Lines")
class OrderLines {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long orderLineID;

	@OneToOne
    @PrimaryKeyJoinColumn(name = "orderLineID")
    Order order;

	@Column(name = "product")
	private String product;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "price")
	private double price;

	@Column(name = "created_at")
	private String created_at;

	@Column(name = "updated_at")
	private String updated_at;

	@Column(name = "deleted_at")
	private String deleted_at;

	@Column(name = "is_active")
	private Boolean is_active;

	public OrderLines() {

	}

	public OrderLines(String product, Integer quantity, double price, String created_at, String updated_at, String deleted_at, boolean is_active) {
		this.product = product;
		this.quantity = quantity;
		this.price = price;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.is_active = is_active;
	}

	public long getOrderLineID() {
		return orderLineID;
	}

	public void setOrderLineID(long orderLineID) {
		this.orderLineID = orderLineID;
	}
		
	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCreatedAt() {
		return created_at;
	}

	public void setCreatedAt(String created_at) {
		this.created_at = created_at;
	}

	public String getUpdatedAt() {
		return updated_at;
	}

	public void setUpdatedAt(String updated_at) {
		this.updated_at = updated_at;
	}

	public String getDeletedAt() {
		return deleted_at;
	}

	public void setDeletedAt(String deleted_at) {
		this.deleted_at = deleted_at;
	}

	public Boolean getIsActive() {
		return is_active;
	}

	public void setIsActive(Boolean is_active) {
		this.is_active = is_active;
	}

	@Override
	public String toString() {
		return "OrderLines [orderLineID=" + orderLineID + ", product=" + product
				+ ", quantity=" + quantity + ", price=" + price + ", created_at=" + created_at + ", updated_at="
				+ updated_at + ", deleted_at=" + deleted_at + ", is_active=" + is_active + "]";
	}

} */


/* @Entity
@Table(name = "Customer")
class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "purchaser")
	private String purchaser;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "product")
	private String product;

	@Column(name = "created_at")
	private String created_at;

	@Column(name = "order_id")
	private Integer order_id;

	@Column(name = "producer")
	private String producer;

	@Column(name = "country")
	private String country;

	@Column(name = "email")
	private String email;

	public Order() {

	}

	public Order(String purchaser, Integer quantity, String product, String created_at, Integer order_id, String producer, String country, String email) {
		this.purchaser = purchaser;
		this.quantity = quantity;
		this.product = product;
		this.created_at = created_at;
		this.order_id = order_id;
		this.producer = producer;
		this.country = country;
		this.email = email;
	}

	public long getId() {
		return id;
	}

	public String getPurchaser() {
		return purchaser;
	}

	public void setPurchaser(String purchaser) {
		this.purchaser = purchaser;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public Integer getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Integer order_id) {
		this.order_id = order_id;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", purchaser=" + purchaser + ", quantity=" + quantity + ", product=" + product + ", created_at=" + created_at + ", order_id=" + order_id + ", producer=" + producer + ", country=" + country + ", email=" + email + "]";
	}

}
 */