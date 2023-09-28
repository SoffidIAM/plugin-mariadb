package com.soffid.iam.agent.mariadb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.soffid.iam.api.Account;
import com.soffid.iam.api.RoleGrant;

import es.caib.seycon.ng.exception.InternalErrorException;
import es.caib.seycon.ng.exception.UnknownRoleException;

/**
 * Agente SEYCON para gestionar bases de datos Oracle
 * <P>
 */

public class MysqlAgent extends MariadbAgent{
	public MysqlAgent() throws RemoteException {
		super();
	}

	public Connection getConnection() throws InternalErrorException {
		Connection conn = (Connection) hash.get(this.getSystem().getName());
		if (conn == null) {
			try {
				Driver d = (Driver) Class.forName("com.mysql.jdbc.Driver").newInstance();
				Properties p = new Properties();
				p.setProperty("user", user);
				p.setProperty("password", password.getPassword());
				conn = d.connect(db, null);
				hash.put(this.getSystem().getName(), conn);
			} catch (Throwable e) {
				return super.getConnection();
			}
		}
		return conn;
	}

	@Override
	protected String fetchUser() {
		return "SELECT 1 FROM mysql.user WHERE User=? and Host=?";
	}

	@Override
	protected String fetchRole() {
		return "SELECT User FROM mysql.user where User != User and User=?";
	}

	@Override
	protected String fetchUsers() {
		return "SELECT User, Host from mysql.user";
	}

	@Override
	protected String fetchRoles() {
		return "SELECT User FROM mysql.user where User != User";
	}

	protected void updateRoles(Connection sqlConnection, String user, Collection<RoleGrant> roles, Account account) throws SQLException, InternalErrorException, UnknownRoleException {
		// Nothing to do
	}

	public List<RoleGrant> getAccountGrants(String userAccount) {
		return new LinkedList<>();
	}
}
