package net.codejava;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class currency_equivalent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private float TL_Value;
	private String Date;
	private String currency_type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public float getTL_Value() {
		return TL_Value;
	}

	public void setTL_Value(float tL_Value) {
		TL_Value = tL_Value;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public String getCurrency_type() {
		return currency_type;
	}

	public void setCurrency_type(String currency_type) {
		this.currency_type = currency_type;
	}

}
