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


@DatabaseTable
public class ChatMessage extends BaseDaoEnabled<ChatMessage, Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_PHOTO = 1;

    public static final String COL_SENDER = "sender";
	public static final String COL_RECEIVER = "receiver";
	public static final String COL_DATE = "date";
	public static final String COL_TEXT = "text";
	public static final String COL_MESSAGE_TYPE = "type";
    public static final String SYSTEM_SENDER = "SYSTEM_SENDER";

    public ChatMessage() {
	}
	
	public ChatMessage(String sender, String receiver, String text) {
        this.id = System.currentTimeMillis();
		this.sender = sender;
		this.receiver = receiver;
		this.text = text;
	}

	public ChatMessage(String sender, String receiver, String text, int type) {
		this(sender, receiver, text);
		this.type = type;
	}

	@DatabaseField(id=true)
	private long id;

	@DatabaseField
	private String sender;
	
	@DatabaseField
	private String receiver;

    @DatabaseField
    private String text;

    @DatabaseField
    private String senderName;

	@DatabaseField
	private int type; // 0 - Message ; 1 - Photo
	
	@DatabaseField(dataType=DataType.DATE_LONG)
	private Date date;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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
			setDao((Dao<ChatMessage, Long>) IEApplication.getOpenHelper().getDao(getClass()));
		}
	}
	
	public boolean isPhoto() {
		return type == TYPE_PHOTO;
	}
	
	@Override
	public String toString() {
		return getSender() + ": " + getText();
	}
}