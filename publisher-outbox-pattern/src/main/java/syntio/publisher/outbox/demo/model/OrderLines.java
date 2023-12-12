package syntio.publisher.outbox.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "order lines")
public class OrderLines {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "product")
	private String product;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "price")
	private double price;

	@Column(name = "created_at")
	private OffsetDateTime createdAt;

	@Column(name = "updated_at")
	private Timestamp updatedAt;

	@Column(name = "deleted_at")
	private Timestamp deletedAt;

	@Column(name = "is_active")
	private Boolean isActive;

	@ManyToOne
    @JsonIgnore
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

	public OrderLines() {

	}

	 public OrderLines(Order order, String product, Integer quantity, double price) {
        this.order = order;
		this.product = product;
		this.quantity = quantity;
		this.price = price;
	} 

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

	public long getId() {
		return id;
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

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Timestamp getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Timestamp deletedAt) {
		this.deletedAt = deletedAt;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	

	@Override
	public String toString() {
		return "OrderLines [id=" + id + ", product=" + product + ", quantity=" + quantity + ", price=" + price
		+ ", created_at=" + createdAt + ", updated_at=" + updatedAt + ", deleted_at=" + deletedAt
		+ ", is_active=" + isActive + "]";
	}
    
}
