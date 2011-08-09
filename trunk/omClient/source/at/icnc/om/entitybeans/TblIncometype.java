package at.icnc.om.entitybeans;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the TBL_INCOMETYPE database table.
 * 
 */
@Entity
@Table(name="TBL_INCOMETYPE")
public class TblIncometype implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idIncometype;
	private String descriptionIt;
	private List<TblOrder> tblOrders;
	private List<TblSettlement> tblSettlements;

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
	public List<TblOrder> getTblOrders() {
		return this.tblOrders;
	}

	public void setTblOrders(List<TblOrder> tblOrders) {
		this.tblOrders = tblOrders;
	}
	

	//bi-directional many-to-one association to TblSettlement
	@OneToMany(mappedBy="tblIncometype")
	public List<TblSettlement> getTblSettlements() {
		return this.tblSettlements;
	}

	public void setTblSettlements(List<TblSettlement> tblSettlements) {
		this.tblSettlements = tblSettlements;
	}
	
}