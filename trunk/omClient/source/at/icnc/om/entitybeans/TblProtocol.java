package at.icnc.om.entitybeans;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the OMPROTOCOL database table.
 * 
 */
@Entity
@Table(name="OMPROTOCOL")
public class TblProtocol extends Selectable implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idProtocol;
	private String change;
	private Date date;
	private TblUser tblUser;

    public TblProtocol() {
    }


	@Id
	@SequenceGenerator(name="OMPROTOCOL_IDPROTOCOL_GENERATOR", sequenceName="OMPROTOCOL_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OMPROTOCOL_IDPROTOCOL_GENERATOR")
	@Column(name="ID_PROTOCOL", unique=true, nullable=false, precision=22)
	public long getIdProtocol() {
		return this.idProtocol;
	}

	public void setIdProtocol(long idProtocol) {
		this.idProtocol = idProtocol;
	}


	@Column(length=45)
	public String getChange() {
		return this.change;
	}

	public void setChange(String change) {
		this.change = change;
	}


    @Temporal( TemporalType.DATE)
	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}


	//bi-directional many-to-one association to TblUser
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FK_USER")
	public TblUser getTblUser() {
		return this.tblUser;
	}

	public void setTblUser(TblUser tblUser) {
		this.tblUser = tblUser;
	}
	
}