package br.com.iecapoeira.chat.model;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBOpenHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "chat.db";
    private static final int DATABASE_VERSION = 1;
	
	public DBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			TableUtils.createTable(arg1, ChatMessage.class);
			TableUtils.createTable(arg1, TableUserMessage.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2, int arg3) {
		try {
			TableUtils.dropTable(arg1, ChatMessage.class, true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		onCreate(arg0, arg1);
	}
	
	public List<ChatMessage> getMessages(String sender) throws SQLException {
		QueryBuilder<ChatMessage, ?> queryBuilder = getDao(ChatMessage.class).queryBuilder();
		queryBuilder.where().eq(ChatMessage.COL_SENDER, sender).or().eq(ChatMessage.COL_RECEIVER, sender);
		queryBuilder.orderBy(ChatMessage.COL_DATE, true);
		return queryBuilder.query();
	}
	
	public List<String> getUserHistory(String sender) throws SQLException {
		List<String> ret = new ArrayList<String>();
		QueryBuilder<ChatMessage, ?> queryBuilder = getDao(ChatMessage.class).queryBuilder();
		queryBuilder.orderBy(ChatMessage.COL_DATE, false);
		queryBuilder.selectColumns(ChatMessage.COL_RECEIVER);
		queryBuilder.groupBy(ChatMessage.COL_RECEIVER);
		List<ChatMessage> query = queryBuilder.query();
		for (ChatMessage chatMessage : query) {
			String s = chatMessage.getReceiver();
			if (!sender.equals(s)) {
				ret.add(s);
			}
		}
		queryBuilder.selectColumns(ChatMessage.COL_SENDER);
		queryBuilder.groupBy(ChatMessage.COL_SENDER);
		List<ChatMessage> query2 = queryBuilder.query();
		for (ChatMessage chatMessage : query2) {
			String s = chatMessage.getSender();
			if (!sender.equals(s)) {
				ret.add(s);
			}
		}
		return ret;
	}
	
	public List<TableUserMessage> getUserHistory() throws SQLException {
		List<TableUserMessage> ret;
		QueryBuilder<TableUserMessage, ?> queryBuilder = getDao(TableUserMessage.class).queryBuilder();
		queryBuilder.orderBy(TableUserMessage.COL_DATE_LAST_MESSAGE, false);
		ret = queryBuilder.query();
		return ret;
	}

	public void clearUnreadTableUserMessage(String userId) {
		try {
			Dao<TableUserMessage, String> dao = getDao(TableUserMessage.class);
			TableUserMessage obj = dao.queryForId(userId);
			obj.setQtdUnreadMessage(0);
			dao.update(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}