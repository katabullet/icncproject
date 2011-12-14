package at.icnc.om.entitybeans;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Set;


/**
 * The persistent class for the OMCUSTOMERSTATE database table.
 * 
 */
@Entity
@Table(name="OMCUSTOMERSTATE")
public class TblCustomerstate extends Selectable implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idCustomerstate;
	private String descriptionCs;
	private Set<TblCustomer> tblCustomers;
	// Variable for version for OptimisticLockException
	private Integer version;

    public TblCustomerstate() {
    }


	@Id
	@SequenceGenerator(name="OMCUSTOMERSTATE_IDCUSTOMERSTATE_GENERATOR", sequenceName="OMCUSTOMERSTATE_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OMCUSTOMERSTATE_IDCUSTOMERSTATE_GENERATOR")
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
	public Set<TblCustomer> getTblCustomers() {
		return this.tblCustomers;
	}

	public void setTblCustomers(Set<TblCustomer> tblCustomers) {
		this.tblCustomers = tblCustomers;
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