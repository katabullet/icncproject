package at.icnc.om.entitybeans;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.Set;
import at.icnc.om.entitybeans.TblSettlement;


/**
 * The persistent class for the OMORDER database table.
 * 
 */
@Entity
@Table(name="OMORDER")
public class TblOrder extends Selectable implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idOrder;
	private Date orderdate;
	private String ordernumber;
	private Integer travelcosts;
	private Set<TblCostcentre> tblCostcentres;
	private TblCustomer tblCustomer;
	private Set<TblIncometype> tblIncometypes;
	private TblOrderstate tblOrderstate;
	private Set<TblSettlement> tblSettlements;
	private TblDocument omdocument;
	private Set<TblDocument> omdocuments;
	// Variable for version for OptimisticLockException
	private Integer version;

    public TblOrder() {
    }


	@Id
	@SequenceGenerator(name="OMORDER_IDORDER_GENERATOR", sequenceName="OMORDER_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OMORDER_IDORDER_GENERATOR")
	@Column(name="ID_ORDER", unique=true, nullable=false, precision=22)
	public long getIdOrder() {
		return this.idOrder;
	}
	
	/**
	 * Method which puts all Incometypes of an order
	 * in one String to be displayed in the table
	 */
	@Transient
	public String getIncometypesString() {
		String incometypes = "";
		
		
		if (tblIncometypes != null) {
			for (TblIncometype item : tblIncometypes) {
				incometypes = incometypes + item.getDescriptionIt() + ", ";
			}
			if (incometypes != "") {
				incometypes = incometypes.substring(0,
						incometypes.lastIndexOf(','));
			}
		}
		return incometypes;
	}
	
	/**
	 * Method which puts all Costcentres of an order
	 * in one String to be displayed in the table
	 */
	@Transient
	public String getCostcentresString() {
		String costcentres = "";
		
		
		if (tblCostcentres != null) {
			for (TblCostcentre item : tblCostcentres) {
				costcentres = costcentres + item.getDescriptionCc() + ", ";
			}
			if (costcentres != "") {
				costcentres = costcentres.substring(0,
						costcentres.lastIndexOf(','));
			}
		}
		return costcentres;
	}
	
	public void setIdOrder(long idOrder) {
		this.idOrder = idOrder;
	}


    @Temporal( TemporalType.DATE)
	public Date getOrderdate() {
		return this.orderdate;
	}

	public void setOrderdate(Date orderdate) {
		this.orderdate = orderdate;
	}


	@Column(length=20)
	public String getOrdernumber() {
		return this.ordernumber;
	}

	public void setOrdernumber(String ordernumber) {
		this.ordernumber = ordernumber;
	}


	@Column(precision=1)
	public Integer getTravelcosts() {
		return this.travelcosts;
	}

	public void setTravelcosts(Integer travelcosts) {
		this.travelcosts = travelcosts;
	}


	//bi-directional many-to-many association to TblCostcentre
    @ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
		name="OMORDER_HAS_COSTCENTRE"
		, joinColumns={
			@JoinColumn(name="FK_ORDER")
			}
		, inverseJoinColumns={
			@JoinColumn(name="FK_COSTCENTRE")
			}
		)
	public Set<TblCostcentre> getTblCostcentres() {
		return this.tblCostcentres;
	}

	public void setTblCostcentres(Set<TblCostcentre> tblCostcentres) {
		this.tblCostcentres = tblCostcentres;
	}
	

	//bi-directional many-to-one association to TblCustomer
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FK_CUSTOMER")
	public TblCustomer getTblCustomer() {
		return this.tblCustomer;
	}

	public void setTblCustomer(TblCustomer tblCustomer) {
		this.tblCustomer = tblCustomer;
	}
	

	//bi-directional many-to-many association to TblIncometype
    @ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
		name="OMORDER_HAS_INCOMETYPE"
		, joinColumns={
			@JoinColumn(name="FK_ORDER")
			}
		, inverseJoinColumns={
			@JoinColumn(name="FK_INCOMETYPE")
			}
		)
	public Set<TblIncometype> getTblIncometypes() {
		return this.tblIncometypes;
	}

	public void setTblIncometypes(Set<TblIncometype> tblIncometypes) {
		this.tblIncometypes = tblIncometypes;
	}
	

	//bi-directional many-to-one association to TblOrderstate
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FK_ORDERSTATE")
	public TblOrderstate getTblOrderstate() {
		return this.tblOrderstate;
	}

	public void setTblOrderstate(TblOrderstate tblOrderstate) {
		this.tblOrderstate = tblOrderstate;
	}
	

	//bi-directional many-to-one association to TblSettlement
	@OneToMany(mappedBy="tblOrder")
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

	@Transient
	public void setTravelcostsBoolean(Boolean travelcostsBoolean) {
		this.travelcosts = (travelcostsBoolean) ? 1 : 0;
	}

	@Transient
	public Boolean getTravelcostsBoolean() {
		return (travelcosts == 1);
	}
	//bi-directional one-to-one association to TblDocument
	@OneToOne(mappedBy="omorder1", fetch=FetchType.LAZY)
	public TblDocument getOmdocument() {
		return this.omdocument;
	}

	public void setOmdocument(TblDocument omdocument) {
		this.omdocument = omdocument;
	}
	

	//bi-directional many-to-one association to TblDocument
	@OneToMany(mappedBy="omorder2")
	public Set<TblDocument> getOmdocuments() {
		return this.omdocuments;
	}

	public void setOmdocuments(Set<TblDocument> omdocuments) {
		this.omdocuments = omdocuments;
	}
}