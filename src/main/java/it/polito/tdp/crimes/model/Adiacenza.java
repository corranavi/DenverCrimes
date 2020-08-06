package it.polito.tdp.crimes.model;

public class Adiacenza implements Comparable<Adiacenza> {
	private String e1;
	private String e2;
	private Double peso;
	public Adiacenza(String e1, String e2, Double peso) {
		super();
		this.e1 = e1;
		this.e2 = e2;
		this.peso = peso;
	}
	public String getE1() {
		return e1;
	}
	public void setE1(String e1) {
		this.e1 = e1;
	}
	public String getE2() {
		return e2;
	}
	public void setE2(String e2) {
		this.e2 = e2;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((e1 == null) ? 0 : e1.hashCode());
		result = prime * result + ((e2 == null) ? 0 : e2.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Adiacenza other = (Adiacenza) obj;
		if (e1 == null) {
			if (other.e1 != null)
				return false;
		} else if (!e1.equals(other.e1))
			return false;
		if (e2 == null) {
			if (other.e2 != null)
				return false;
		} else if (!e2.equals(other.e2))
			return false;
		return true;
	}
	@Override
	public int compareTo(Adiacenza other) {
		return other.getPeso().compareTo(this.getPeso());
	}
	
	public String toString() {
		return this.e1+" - "+this.e2+" (Peso: "+this.peso+")";
	}
	
	
}
