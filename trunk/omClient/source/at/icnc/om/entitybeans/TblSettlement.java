package at.icnc.om.entitybeans;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;


/**
 * The persistent class for the OMSETTLEMENT database table.
 * 
 */
@Entity
@Table(name="OMSETTLEMENT")
public class TblSettlement extends Selectable implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idSettlement;
	private BigDecimal runtime;
	private Set<TblInvoice> tblInvoices;
	private TblIncometype tblIncometype;
	private TblInterval tblInterval;
	private TblOrder tblOrder;

    public TblSettlement() {
    }


	@Id
	@SequenceGenerator(name="OMSETTLEMENT_IDSETTLEMENT_GENERATOR", sequenceName="OMSETTLEMENT_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OMSETTLEMENT_IDSETTLEMENT_GENERATOR")
	@Column(name="ID_SETTLEMENT", unique=true, nullable=false, precision=22)
	public long getIdSettlement() {
		return this.idSettlement;
	}

	public void setIdSettlement(long idSettlement) {
		this.idSettlement = idSettlement;
	}


	@Column(precision=22)
	public BigDecimal getRuntime() {
		return this.runtime;
	}

	public void setRuntime(BigDecimal runtime) {
		this.runtime = runtime;
	}


	//bi-directional many-to-one association to TblInvoice
	@OneToMany(mappedBy="tblSettlement")
	public Set<TblInvoice> getTblInvoices() {
		return this.tblInvoices;
	}

	public void setTblInvoices(Set<TblInvoice> tblInvoices) {
		this.tblInvoices = tblInvoices;
	}
	

	//bi-directional many-to-one association to TblIncometype
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FK_INCOMETYPE")
	public TblIncometype getTblIncometype() {
		return this.tblIncometype;
	}

	public void setTblIncometype(TblIncometype tblIncometype) {
		this.tblIncometype = tblIncometype;
	}
	

	//bi-directional many-to-one association to TblInterval
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FK_INTERVAL")
	public TblInterval getTblInterval() {
		return this.tblInterval;
	}

	public void setTblInterval(TblInterval tblInterval) {
		this.tblInterval = tblInterval;
	}
	

	//bi-directional many-to-one association to TblOrder
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FK_ORDER")
	public TblOrder getTblOrder() {
		return this.tblOrder;
	}

	public void setTblOrder(TblOrder tblOrder) {
		this.tblOrder = tblOrder;
	}
	
}