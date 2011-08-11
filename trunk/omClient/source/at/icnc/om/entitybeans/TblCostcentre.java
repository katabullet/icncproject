package at.icnc.om.entitybeans;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * The persistent class for the OMCOSTCENTRE database table.
 * 
 */
@Entity
@Table(name="OMCOSTCENTRE")
public class TblCostcentre extends Selectable implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idCostcentre;
	private String costcentrecode;
	private String descriptionCc;
	private Set<TblOrder> tblOrders;

    public TblCostcentre() {
    }


	@Id
	@SequenceGenerator(name="OMCOSTCENTRE_IDCOSTCENTRE_GENERATOR", sequenceName="OMCOSTCENTRE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OMCOSTCENTRE_IDCOSTCENTRE_GENERATOR")
	@Column(name="ID_COSTCENTRE", unique=true, nullable=false, precision=22)
	public long getIdCostcentre() {
		return this.idCostcentre;
	}

	public void setIdCostcentre(long idCostcentre) {
		this.idCostcentre = idCostcentre;
	}


	@Column(length=45)
	public String getCostcentrecode() {
		return this.costcentrecode;
	}

	public void setCostcentrecode(String costcentrecode) {
		this.costcentrecode = costcentrecode;
	}


	@Column(name="DESCRIPTION_CC", length=45)
	public String getDescriptionCc() {
		return this.descriptionCc;
	}

	public void setDescriptionCc(String descriptionCc) {
		this.descriptionCc = descriptionCc;
	}


	//bi-directional many-to-many association to TblOrder
	@ManyToMany(mappedBy="tblCostcentres")
	public Set<TblOrder> getTblOrders() {
		return this.tblOrders;
	}

	public void setTblOrders(Set<TblOrder> tblOrders) {
		this.tblOrders = tblOrders;
	}
	
}