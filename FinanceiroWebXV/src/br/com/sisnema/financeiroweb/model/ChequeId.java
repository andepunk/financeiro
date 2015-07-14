package br.com.sisnema.financeiroweb.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ChequeId implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2928375927540978464L;

	@Basic(optional=false)
	@Column(name="cheque", nullable = false)
	private Integer Nnumero;
	
	@Basic(optional=false)
	@Column(name="conta", nullable = false)
	private Integer conta;

	

	public Integer getNnumero() {
		return Nnumero;
	}

	public void setNnumero(Integer nnumero) {
		Nnumero = nnumero;
	}

	public Integer getConta() {
		return conta;
	}

	public void setConta(Integer conta) {
		this.conta = conta;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Nnumero == null) ? 0 : Nnumero.hashCode());
		result = prime * result + ((conta == null) ? 0 : conta.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ChequeId)) {
			return false;
		}
		ChequeId other = (ChequeId) obj;
		if (Nnumero == null) {
			if (other.Nnumero != null) {
				return false;
			}
		} else if (!Nnumero.equals(other.Nnumero)) {
			return false;
		}
		if (conta == null) {
			if (other.conta != null) {
				return false;
			}
		} else if (!conta.equals(other.conta)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ChequeId [Nnumero=" + Nnumero + ", conta=" + conta + "]";
	}

	
}
