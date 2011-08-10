package at.icnc.om.entitybeans;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * The persistent class for the TBL_INCOMETYPE database table.
 * 
 */
@Entity
@Table(name="TBL_INCOMETYPE")
public class TblIncometype extends Selectable implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idIncometype;
	private String descriptionIt;
	private Set<TblOrder> tblOrders;
	private Set<TblSettlement> tblSettlements;

    public TblIncometype() {
    }


	@Id
	@SequenceGenerator(name="TBL_INCOMETYPE_IDINCOMETYPE_GENERATOR", sequenceName="TBL_INCOMETYPE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_INCOMETYPE_IDINCOMETYPE_GENERATOR")
	@Column(name="ID_INCOMETYPE", unique=true, nullable=false, precision=22)
	public long getIdIncometype() {
		return this.idIncometype;
	}

	public void setIdIncometype(long idIncometype) {
		this.idIncometype = idIncometype;
	}


	@Column(name="DESCRIPTION_IT", length=45)
	public String getDescriptionIt() {
		return this.descriptionIt;
	}

	public void setDescriptionIt(String descriptionIt) {
		this.descriptionIt = descriptionIt;
	}


	//bi-directional many-to-many association to TblOrder
	@ManyToMany(mappedBy="tblIncometypes")
	public Set<TblOrder> getTblOrders() {
		return this.tblOrders;
	}

	public void setTblOrders(Set<TblOrder> tblOrders) {
		this.tblOrders = tblOrders;
	}
	

	//bi-directional many-to-one association to TblSettlement
	@OneToMany(mappedBy="tblIncometype")
	public Set<TblSettlement> getTblSettlements() {
		return this.tblSettlements;
	}

	public void setTblSettlements(Set<TblSettlement> tblSettlements) {
		this.tblSettlements = tblSettlements;
	}
	
}