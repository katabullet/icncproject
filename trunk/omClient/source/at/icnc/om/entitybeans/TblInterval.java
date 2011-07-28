package at.icnc.om.entitybeans;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;


/**
 * The persistent class for the TBL_INTERVAL database table.
 * 
 */
@Entity
@Table(name="TBL_INTERVAL")
public class TblInterval implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idInterval;
	private String descriptionIv;
	private BigDecimal months;
	private Set<TblSettlement> tblSettlements;

    public TblInterval() {
    }


	@Id
	@SequenceGenerator(name="TBL_INTERVAL_IDINTERVAL_GENERATOR", sequenceName="TBL_INTERVAL_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_INTERVAL_IDINTERVAL_GENERATOR")
	@Column(name="ID_INTERVAL")
	public long getIdInterval() {
		return this.idInterval;
	}

	public void setIdInterval(long idInterval) {
		this.idInterval = idInterval;
	}


	@Column(name="DESCRIPTION_IV")
	public String getDescriptionIv() {
		return this.descriptionIv;
	}

	public void setDescriptionIv(String descriptionIv) {
		this.descriptionIv = descriptionIv;
	}


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
	
}