package at.icnc.om.entitybeans;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Set;


/**
 * The persistent class for the OMCONCERN database table.
 * 
 */
@Entity
@Table(name="OMCONCERN")
public class TblConcern extends Selectable implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idConcern;
	private String concernmatchcode;
	private String concernname;
	private Set<TblCustomer> tblCustomers;
	// Variable for version for OptimisticLockException
	private Integer version;

    public TblConcern() {
    }


	@Id
	@SequenceGenerator(name="OMCONCERN_IDCONCERN_GENERATOR", sequenceName="OMCONCERN_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OMCONCERN_IDCONCERN_GENERATOR")
	@Column(name="ID_CONCERN", unique=true, nullable=false, precision=22)
	public long getIdConcern() {
		return this.idConcern;
	}

	public void setIdConcern(long idConcern) {
		this.idConcern = idConcern;
	}


	@Column(length=45)
	public String getConcernmatchcode() {
		return this.concernmatchcode;
	}

	public void setConcernmatchcode(String concernmatchcode) {
		this.concernmatchcode = concernmatchcode;
	}


	@Column(length=45)
	public String getConcernname() {
		return this.concernname;
	}

	public void setConcernname(String concernname) {
		this.concernname = concernname;
	}


	//bi-directional many-to-one association to TblCustomer
	@OneToMany(mappedBy="tblConcern")
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