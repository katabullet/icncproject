package iCNC.EntityBeans;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the TBL_SETTLEMENT database table.
 * 
 */
@Entity
@Table(name="TBL_SETTLEMENT")
public class TblSettlement implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idSettlement;
	private BigDecimal runtime;
	private TblIncometype tblIncometype;
	private TblInterval tblInterval;
	private TblOrder tblOrder;

    public TblSettlement() {
    }


	@Id
	@SequenceGenerator(name="TBL_SETTLEMENT_IDSETTLEMENT_GENERATOR", sequenceName="TBL_SETTLEMENT_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_SETTLEMENT_IDSETTLEMENT_GENERATOR")
	@Column(name="ID_SETTLEMENT")
	public long getIdSettlement() {
		return this.idSettlement;
	}

	public void setIdSettlement(long idSettlement) {
		this.idSettlement = idSettlement;
	}


	public BigDecimal getRuntime() {
		return this.runtime;
	}

	public void setRuntime(BigDecimal runtime) {
		this.runtime = runtime;
	}


	//bi-directional many-to-one association to TblIncometype
    @ManyToOne
	@JoinColumn(name="FK_INCOMETYPE")
	public TblIncometype getTblIncometype() {
		return this.tblIncometype;
	}

	public void setTblIncometype(TblIncometype tblIncometype) {
		this.tblIncometype = tblIncometype;
	}
	

	//bi-directional many-to-one association to TblInterval
    @ManyToOne
	@JoinColumn(name="FK_INTERVAL")
	public TblInterval getTblInterval() {
		return this.tblInterval;
	}

	public void setTblInterval(TblInterval tblInterval) {
		this.tblInterval = tblInterval;
	}
	

	//bi-directional many-to-one association to TblOrder
    @ManyToOne
	@JoinColumn(name="FK_ORDER")
	public TblOrder getTblOrder() {
		return this.tblOrder;
	}

	public void setTblOrder(TblOrder tblOrder) {
		this.tblOrder = tblOrder;
	}
	
}