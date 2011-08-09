package at.icnc.om.entitybeans;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the TBL_CUSTOMERSTATE database table.
 * 
 */
@Entity
@Table(name="TBL_CUSTOMERSTATE")
public class TblCustomerstate implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idCustomerstate;
	private String descriptionCs;
	private List<TblCustomer> tblCustomers;

    public TblCustomerstate() {
    }


	@Id
	@SequenceGenerator(name="TBL_CUSTOMERSTATE_IDCUSTOMERSTATE_GENERATOR", sequenceName="TBL_CUSTOMERSTATE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CUSTOMERSTATE_IDCUSTOMERSTATE_GENERATOR")
	@Column(name="ID_CUSTOMERSTATE", unique=true, nullable=false, precision=22)
	public long getIdCustomerstate() {
		return this.idCustomerstate;
	}

	public void setIdCustomerstate(long idCustomerstate) {
		this.idCustomerstate = idCustomerstate;
	}


	@Column(name="DESCRIPTION_CS", length=45)
	public String getDescriptionCs() {
		return this.descriptionCs;
	}

	public void setDescriptionCs(String descriptionCs) {
		this.descriptionCs = descriptionCs;
	}


	//bi-directional many-to-one association to TblCustomer
	@OneToMany(mappedBy="tblCustomerstate")
	public List<TblCustomer> getTblCustomers() {
		return this.tblCustomers;
	}

	public void setTblCustomers(List<TblCustomer> tblCustomers) {
		this.tblCustomers = tblCustomers;
	}
	
}