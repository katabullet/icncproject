package at.icnc.om.entitybeans;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;


/**
 * The persistent class for the TBL_ORDER database table.
 * 
 */
@Entity
@Table(name="TBL_ORDER")
public class TblOrder extends Selectable implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idOrder;
	private Date orderdate;
	private String ordernumber;
	private boolean travelcosts;
	private Set<TblCostcentre> tblCostcentres;
	private TblCustomer tblCustomer;
	private Set<TblIncometype> tblIncometypes;
	private TblOrderstate tblOrderstate;
	private Set<TblSettlement> tblSettlements;

    public TblOrder() {
    }


	@Id
	@SequenceGenerator(name="TBL_ORDER_IDORDER_GENERATOR", sequenceName="TBL_ORDER_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_ORDER_IDORDER_GENERATOR")
	@Column(name="ID_ORDER", unique=true, nullable=false, precision=22)
	public long getIdOrder() {
		return this.idOrder;
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
	public boolean getTravelcosts() {
		return this.travelcosts;
	}

	public void setTravelcosts(boolean travelcosts) {
		this.travelcosts = travelcosts;
	}


	//bi-directional many-to-many association to TblCostcentre
    @ManyToMany
	@JoinTable(
		name="TBL_ORDER_HAS_COSTCENTRE"
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
    @ManyToMany
	@JoinTable(
		name="TBL_ORDER_HAS_INCOMETYPE"
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
	
}