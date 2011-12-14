package at.icnc.om.entitybeans;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;


/**
 * The persistent class for the OMCUSTOMER database table.
 * 
 */
@Entity
@Table(name="OMCUSTOMER")
public class TblCustomer extends Selectable implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idCustomer;
	private String customername;
	private Date customerstatedate;
	private String sapcnr;
	private TblConcern tblConcern;
	private TblContactperson tblContactperson;
	private TblCustomerstate tblCustomerstate;
	private TblUser tblUser;
	private Set<TblOrder> tblOrders;
	// Variable for version for OptimisticLockException
	private Integer version;

    public TblCustomer() {
    }


	@Id
	@SequenceGenerator(name="OMCUSTOMER_IDCUSTOMER_GENERATOR", sequenceName="OMCUSTOMER_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OMCUSTOMER_IDCUSTOMER_GENERATOR")
	@Column(name="ID_CUSTOMER", unique=true, nullable=false, precision=22)
	public long getIdCustomer() {
		return this.idCustomer;
	}

	public void setIdCustomer(long idCustomer) {
		this.idCustomer = idCustomer;
	}


	@Column(length=45)
	public String getCustomername() {
		return this.customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}


    @Temporal( TemporalType.DATE)
	public Date getCustomerstatedate() {
		return this.customerstatedate;
	}

	public void setCustomerstatedate(Date customerstatedate) {
		this.customerstatedate = customerstatedate;
	}


	@Column(length=45)
	public String getSapcnr() {
		return this.sapcnr;
	}

	public void setSapcnr(String sapcnr) {
		this.sapcnr = sapcnr;
	}


	//bi-directional many-to-one association to TblConcern
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FK_CONCERN")
	public TblConcern getTblConcern() {
		return this.tblConcern;
	}

	public void setTblConcern(TblConcern tblConcern) {
		this.tblConcern = tblConcern;
	}
	

	//bi-directional many-to-one association to TblContactperson
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FK_CONTACTPERSON")
	public TblContactperson getTblContactperson() {
		return this.tblContactperson;
	}

	public void setTblContactperson(TblContactperson tblContactperson) {
		this.tblContactperson = tblContactperson;
	}
	

	//bi-directional many-to-one association to TblCustomerstate
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FK_CUSTOMERSTATE")
	public TblCustomerstate getTblCustomerstate() {
		return this.tblCustomerstate;
	}

	public void setTblCustomerstate(TblCustomerstate tblCustomerstate) {
		this.tblCustomerstate = tblCustomerstate;
	}
	

	//bi-directional many-to-one association to TblUser
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FK_SALESMAN")
	public TblUser getTblUser() {
		return this.tblUser;
	}

	public void setTblUser(TblUser tblUser) {
		this.tblUser = tblUser;
	}
	

	//bi-directional many-to-one association to TblOrder
	@OneToMany(mappedBy="tblCustomer")
	public Set<TblOrder> getTblOrders() {
		return this.tblOrders;
	}

	public void setTblOrders(Set<TblOrder> tblOrders) {
		this.tblOrders = tblOrders;
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