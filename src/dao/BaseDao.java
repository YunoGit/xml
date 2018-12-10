package dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BaseDao {
	private DbHelper dbHelper;
	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;

	public BaseDao() {
		this.dbHelper = new DbHelper();
	}

	public BaseDao(String database) {
		this.dbHelper = new DbHelper(database);
	}

	public long getCount(String sql, Object... args) {
		conn = dbHelper.getConn();
		try {
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbHelper.closeConn(conn, ps, rs);
		}
		return 0L;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> executeQuery(String sql, Class<T> clazz, Object... args) {
		conn = dbHelper.getConn();
		List<T> list = new ArrayList<T>();
		try {
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();
			Field[] fs = clazz.getDeclaredFields();
			String[] colNames = new String[fs.length];
			String[] rTypes = new String[fs.length];
			Method[] methods = clazz.getMethods();
			while (rs.next()) {
				for (int i = 0; i < fs.length; i++) {
					Field f = fs[i];
					String colName = f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
					colNames[i] = colName;
					String rType = f.getType().getSimpleName();
					rTypes[i] = rType;
				}

				Object object = (T) clazz.newInstance();
				for (int i = 0; i < colNames.length; i++) {
					String colName = colNames[i];
					String methodName = "set" + colName;
					for (Method m : methods) {
						if (methodName.equals((m.getName()))) {
							if ("int".equals(rTypes[i]) || "Integer".equals(rTypes[i])) {
								m.invoke(object, rs.getInt(colName));
							} else if ("Date".equals(rTypes[i])) {
								m.invoke(object, rs.getDate(colName));
							} else if ("Timestamp".equals(rTypes[i])) {
								m.invoke(object, rs.getTimestamp(colName));
							} else {
								m.invoke(object, rs.getObject(colName));
							}
							break;
						}
					}
				}
				list.add((T) object);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbHelper.closeConn(conn, ps, rs);
		}
		return null;
	}

	public int saveEntity(String sql, Object object, int... args) {
		conn = dbHelper.getConn();
		try {
			ps = conn.prepareStatement(sql);
			Class<? extends Object> c = object.getClass();
			Field[] fields = object.getClass().getDeclaredFields();
			int temp = 1;
			int colIndex = 1;
			int t = 0;
			for (int j = 0; j < fields.length; j++) {
				Field field = fields[j];
				String methodName = "get" + field.getName().substring(0, 1).toUpperCase()
						+ field.getName().substring(1);
				Method method = c.getMethod(methodName);
				String rType = field.getType().getSimpleName().toString();
				if (t < args.length && colIndex == args[t]) {
					t++;
				} else if ("int".equals(rType) || "INTEGER".equals(rType)) {
					ps.setInt(temp++, (Integer) method.invoke(object));
				} else {
					ps.setObject(temp++, method.invoke(object));
				}
				colIndex++;
			}
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbHelper.closeConn(conn, ps, null);
		}
		return 0;
	}

	public int saveOrUpdate(String sql, Object... args) {
		conn = dbHelper.getConn();
		try {
			ps = conn.prepareStatement(sql);
			for (int j = 0; j < args.length; j++) {
				ps.setObject(j + 1, args[j]);
			}
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbHelper.closeConn(conn, ps, null);
		}
		return 0;
	}
}