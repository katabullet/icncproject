package at.icnc.om.entitybeans;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the TBL_ORDERSTATE database table.
 * 
 */
@Entity
@Table(name="TBL_ORDERSTATE")
public class TblOrderstate implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idOrderstate;
	private String descriptionOs;
	private List<TblOrder> tblOrders;

    public TblOrderstate() {
    }


	@Id
	@SequenceGenerator(name="TBL_ORDERSTATE_IDORDERSTATE_GENERATOR", sequenceName="TBL_ORDERSTATE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_ORDERSTATE_IDORDERSTATE_GENERATOR")
	@Column(name="ID_ORDERSTATE", unique=true, nullable=false, precision=22)
	public long getIdOrderstate() {
		return this.idOrderstate;
	}

	public void setIdOrderstate(long idOrderstate) {
		this.idOrderstate = idOrderstate;
	}


	@Column(name="DESCRIPTION_OS", length=45)
	public String getDescriptionOs() {
		return this.descriptionOs;
	}

	public void setDescriptionOs(String descriptionOs) {
		this.descriptionOs = descriptionOs;
	}


	//bi-directional many-to-one association to TblOrder
	@OneToMany(mappedBy="tblOrderstate")
	public List<TblOrder> getTblOrders() {
		return this.tblOrders;
	}

	public void setTblOrders(List<TblOrder> tblOrders) {
		this.tblOrders = tblOrders;
	}
	
}