package br.com.iecapoeira.chat.model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;

import br.com.iecapoeira.IEApplication;
import br.com.iecapoeira.model.UserDetails;

@DatabaseTable
public class TableUserMessage extends BaseDaoEnabled<TableUserMessage, String> implements Serializable {

	private static final long serialVersionUID = 1L;

    public static final String COL_USERID = "userId";
	public static final String COL_LAST_MESSAGE = "lastMessage";
	public static final String COL_DATE_LAST_MESSAGE = "dateLastMessage";
	public static final String COL_QTD_MESSAGE = "qtdMessage";
	public static final String COL_QTD_UNREAD_MESSAGE = "qtdUnreadMessage";
    private UserDetails contact;

    public TableUserMessage() {
	}

	public TableUserMessage(String userId, String lastMessage, Date dateLastMessage) {
		this.userId = userId;
		this.lastMessage = lastMessage;
		this.dateLastMessage = dateLastMessage;
		qtdMessage = qtdUnreadMessage = 1;
	}

	@DatabaseField(id=true)
	private String userId;
	
	@DatabaseField
	private String lastMessage;

	@DatabaseField
	private int qtdMessage;

	@DatabaseField
	private int qtdUnreadMessage;

	@DatabaseField(dataType= DataType.DATE_LONG)
	private Date dateLastMessage;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public int getQtdMessage() {
		return qtdMessage;
	}

	public void setQtdMessage(int qtdMessage) {
		this.qtdMessage = qtdMessage;
	}

	public int getQtdUnreadMessage() {
		return qtdUnreadMessage;
	}

	public void setQtdUnreadMessage(int qtdUnreadMessage) {
		this.qtdUnreadMessage = qtdUnreadMessage;
	}

	public Date getDateLastMessage() {
		return dateLastMessage;
	}

	public void setDateLastMessage(Date dateLastMessage) {
		this.dateLastMessage = dateLastMessage;
	}
	
	@Override
	public int create() throws SQLException {
		setupDao();
		return super.create();
	}
	
	@Override
	public int update() throws SQLException {
		setupDao();
		return super.update();
	}
	
	@Override
	public int delete() throws SQLException {
		setupDao();
		return super.delete();
	}

	@SuppressWarnings("unchecked")
	private void setupDao() throws SQLException {
		if (getDao() == null) {
			setDao((Dao<TableUserMessage, String>) IEApplication.getOpenHelper().getDao(getClass()));
		}
	}

	public void incrementQtdUnreadMessage() {
		qtdUnreadMessage++;
	}

	public void incrementQtdMessage() {
		qtdMessage++;
	}

    public void setUserDetails(UserDetails contact) {
        this.contact = contact;
    }

    public UserDetails getUserDetails() {
        return contact;
    }
}