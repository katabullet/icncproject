package at.icnc.om.entitybeans;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Set;


/**
 * The persistent class for the OMINTERVAL database table.
 * 
 */
@Entity
@Table(name="OMINTERVAL")
public class TblInterval extends Selectable implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idInterval;
	private String descriptionIv;
	private BigDecimal months;
	private Set<TblSettlement> tblSettlements;
	// Variable for version for OptimisticLockException
	private Integer version;

    public TblInterval() {
    }


	@Id
	@SequenceGenerator(name="OMINTERVAL_IDINTERVAL_GENERATOR", sequenceName="OMINTERVAL_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OMINTERVAL_IDINTERVAL_GENERATOR")
	@Column(name="ID_INTERVAL", unique=true, nullable=false, precision=22)
	public long getIdInterval() {
		return this.idInterval;
	}

	public void setIdInterval(long idInterval) {
		this.idInterval = idInterval;
	}


	@Column(name="DESCRIPTION_IV", length=45)
	public String getDescriptionIv() {
		return this.descriptionIv;
	}

	public void setDescriptionIv(String descriptionIv) {
		this.descriptionIv = descriptionIv;
	}


	@Column(precision=22)
	public BigDecimal getMonths() {
		return this.months;
	}

	public void setMonths(BigDecimal months) {
		this.months = months;
	}


	//bi-directional many-to-one association to TblSettlement
	@OneToMany(mappedBy="tblInterval")
	public Set<TblSettlement> getTblSettlements() {
		return this.tblSettlements;
	}

	public void setTblSettlements(Set<TblSettlement> tblSettlements) {
		this.tblSettlements = tblSettlements;
	}
	
	/*
	 * Getter and Setter of Version for OptimisticLockException
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	@Version
	public Integer getVersion() {
		return version;
	}
}