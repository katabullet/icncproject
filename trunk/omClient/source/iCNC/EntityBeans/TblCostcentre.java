package iCNC.EntityBeans;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * The persistent class for the TBL_COSTCENTRE database table.
 * 
 */
@Entity
@Table(name="TBL_COSTCENTRE")
public class TblCostcentre implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idCostcentre;
	private String costcentrecode;
	private String descriptionCc;
	private Set<TblOrder> tblOrders;

    public TblCostcentre() {
    }


	@Id
	@SequenceGenerator(name="TBL_COSTCENTRE_IDCOSTCENTRE_GENERATOR", sequenceName="TBL_COSTCENTRE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_COSTCENTRE_IDCOSTCENTRE_GENERATOR")
	@Column(name="ID_COSTCENTRE")
	public long getIdCostcentre() {
		return this.idCostcentre;
	}

	public void setIdCostcentre(long idCostcentre) {
		this.idCostcentre = idCostcentre;
	}


	public String getCostcentrecode() {
		return this.costcentrecode;
	}

	public void setCostcentrecode(String costcentrecode) {
		this.costcentrecode = costcentrecode;
	}


	@Column(name="DESCRIPTION_CC")
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