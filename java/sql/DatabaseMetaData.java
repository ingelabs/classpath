/*************************************************************************
/* DatabaseMetaData.java -- Information about the database itself.
/*
/* Copyright (c) 1999 Free Software Foundation, Inc.
/* Written by Aaron M. Renn (arenn@urbanophile.com)
/*
/* This library is free software; you can redistribute it and/or modify
/* it under the terms of the GNU Library General Public License as published 
/* by the Free Software Foundation, either version 2 of the License, or
/* (at your option) any later verion.
/*
/* This library is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU Library General Public License for more details.
/*
/* You should have received a copy of the GNU Library General Public License
/* along with this library; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package java.sql;

/**
  * This interface provides a mechanism for obtaining information about
  * the database itself, as opposed to data in it.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract interface DatabaseMetaData
{

/**
  * The best row may or may not be a pseudo-column.
  */
public static final int bestRowUnknown = 0;

/**
  * The best row identifier is not a pseudo-column.
  */
public static final int bestRowNotPseudo = 1;

/**
  * The best row identifer is a pseudo-column.
  */
public static final int bestRowPseudo = 2;

/**
  * The best row's scope is only guaranteed to be valid so long as the
  * row is actually being used.
  */
public static final int bestRowTemporary = 0;

/**
  * The best row identifer is valid to the end of the transaction.
  */
public static final int bestRowTransaction = 1;

/**
  * The best row identifer is valid to the end of the session.
  */
public static final int bestRowSession = 2;

/**
  * It is unknown whether or not the procedure returns a result.
  */
public static final int procedureResultUnknown = 0;

/**
  * The procedure does not return a result.
  */
public static final int procedureNoResult = 1;

/**
  * The procedure returns a result.
  */
public static final int procedureReturnsResult = 2;

/**
  * The column type is unknown.
  */
public static final int procedureColumnUnknown = 0;

/**
  * The column type is input.
  */
public static final int procedureColumnIn = 1;

/**
  * The column type is input/output.
  */
public static final int procedureColumnInOut = 2;

/**
  * The column type is output
  */
public static final int procedureColumnOut = 4;

/**
  * The column is used for return values.
  */
public static final int procedureColumnReturn = 5;

/**
  * The column is used for storing results
  */
public static final int procedureColumnResult = 3;

/**
  * NULL values are not allowed.
  */
public static final int procedureNoNulls = 0;

/**
  * NULL values are allowed.
  */
public static final int procedureNullable = 1;

/**
  * It is unknown whether or not NULL values are allowed.
  */
public static final int procedureNullableUnknown = 2;

/**
  * The column does not allow NULL
  */
public static final int columnNoNulls = 0;

/**
  * The column does allow NULL
  */
public static final int columnNullable = 1;

/**
  * It is unknown whether or not the column allows NULL
  */
public static final int columnNullableUnknown = 2;

/**
  * It is unknown whether or not the version column is a pseudo-column.
  */
public static final int versionColumnUnknown = 0;

/**
  * The version column is not a pseudo-column
  */
public static final int versionColumnNotPseudo = 1;

/**
  * The version column is a pseudo-column
  */
public static final int versionColumnPseudo = 2;

/**
  * Foreign key changes are cascaded in updates or deletes.
  */
public static final int importedKeyCascade = 0;

/**
  * Column may not be updated or deleted in use as a foreign key.
  */
public static final int importedKeyRestrict = 1;

/**
  * When primary key is updated or deleted, the foreign key is set to NULL.
  */
public static final int importedKeySetNull = 2;

/**
  * If the primary key is a foreign key, it cannot be udpated or deleted.
  */
public static final int importedKeyNoAction = 3;

/**
  * If the primary key is updated or deleted, the foreign key is set to
  * a default value.
  */
public static final int importedKeySetDefault = 4;

/**
  * Wish I knew what this meant.
  */
public static final int importedKeyInitiallyDeferred = 5;

/**
  * Wish I knew what this meant.
  */
public static final int importedKeyInitiallyImmediate = 6;

/**
  * Wish I knew what this meant.
  */
public static final int importedKeyNotDeferrable = 7;

/**
  * A NULL value is not allowed for this data type.
  */
public static final int typeNoNulls = 0;

/**
  * A NULL value is allowed for this data type.
  */
public static final int typeNullable = 1;

/**
  * It is unknown whether or not NULL values are allowed for this data type.
  */
public static final int typeNullableUnknown = 2;

/**
  * Where clauses are not supported for this type.
  */
public static final int typePredNone = 0;

/**
  * Only "WHERE..LIKE" style WHERE clauses are allowed on this data type.
  */
public static final int typePredChar = 1;

/**
  * All WHERE clauses except "WHERE..LIKE" style are allowed on this data type.
  */
public static final int typePredBasic = 2;

/**
  * Any type of WHERE clause is allowed for this data type.
  */
public static final int typeSearchable = 3;

/**
  * This column contains table statistics.
  */
public static final short tableIndexStatistic = 0;

/**
  * This table index is clustered.
  */
public static final short tableIndexClustered = 1;

/**
  * This table index is hashed.
  */
public static final short tableIndexHashed = 2;

/**
  * This table index is of another type.
  */
public static final short tableIndexOther = 3;

/*************************************************************************/

/**
  * This method tests whether or not all the procedures returned by
  * the <code>getProcedures</code> method can be called by this user.
  *
  * @return <code>true</code> if all the procedures can be called,
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
allProceduresAreCallable() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not all the table returned by the
  * <code>getTables</code> method can be selected by this user.
  *
  * @return <code>true</code> if all the procedures can be called,
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
allTablesAreSelectable() throws SQLException;

/*************************************************************************/

/**
  * This method returns the URL for this database.
  *
  * @return The URL string for this database, or <code>null</code> if it
  * is not known.
  *
  * @exception SQLException If an error occurs.
  */
public abstract String
getURL() throws SQLException;

/*************************************************************************/

/**
  * This method returns the database username for this connection.
  *
  * @return The database username.
  *
  * @exception SQLException If an error occurs.
  */
public abstract String
getUserName() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the database is in read only mode.
  *
  * @return <code>true</code> if the database is in read only mode,
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
isReadOnly() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not NULL's sort as high values.
  *
  * @return <code>true</code> if NULL's sort as high values, <code>false</code>
  * otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
nullsAreSortedHigh() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not NULL's sort as low values.
  *
  * @return <code>true</code> if NULL's sort as low values, <code>false</code>
  * otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
nullsAreSortedLow() throws SQLException;

/*************************************************************************/

/**
  * This method test whether or not NULL's are sorted to the beginning
  * of the list regardless of ascending or descending sort order.
  *
  * @return <code>true</code> if NULL's always sort to the beginning,
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
nullsAreSortedAtStart() throws SQLException;

/*************************************************************************/

/**
  * This method test whether or not NULL's are sorted to the end
  * of the list regardless of ascending or descending sort order.
  *
  * @return <code>true</code> if NULL's always sort to the end,
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
nullsAreSortedAtEnd() throws SQLException;

/*************************************************************************/

/**
  * This method returns the name of the database product.
  *
  * @return The database product.
  *
  * @exception SQLException If an error occurs.
  */
public abstract String
getDatabaseProductName() throws SQLException;

/*************************************************************************/

/**
  * This method returns the version of the database product.
  *
  * @return The version of the database product.
  *
  * @exception SQLException If an error occurs.
  */
public abstract String
getDatabaseProductVersion() throws SQLException;

/*************************************************************************/

/**
  * This method returns the name of the JDBC driver.
  *
  * @return The name of the JDBC driver.
  *
  * @exception SQLException If an error occurs.
  */
public abstract String
getDriverName() throws SQLException;

/*************************************************************************/

/**
  * This method returns the version of the JDBC driver.
  *
  * @return The version of the JDBC driver.
  *
  * @exception SQLException If an error occurs.
  */
public abstract String
getDriverVersion() throws SQLException;

/*************************************************************************/

/**
  * This method returns the major version number of the JDBC driver.
  *
  * @return The major version number of the JDBC driver.
  */
public abstract int
getDriverMajorVersion();

/*************************************************************************/

/**
  * This method returns the minor version number of the JDBC driver.
  *
  * @return The minor version number of the JDBC driver.
  */
public abstract int
getDriverMinorVersion();

/*************************************************************************/

/**
  * This method tests whether or not the database uses local files to
  * store tables.
  *
  * @return <code>true</code> if the database uses local files, 
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
usesLocalFiles() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the database uses a separate file for
  * each table.
  *
  * @return <code>true</code> if the database uses a separate file for each
  * table </code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
pubilc abstract boolean
usesLocalFilePerTable() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the database supports identifiers
  * with mixed case.
  *
  * @return <code>true</code> if the database supports mixed case identifiers,
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsMixedCaseIdentifiers() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the database treats mixed case
  * identifiers as all upper case.
  *
  * @exception <code>true</code> if the database treats all identifiers as
  * upper case, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
storesUpperCaseIdentifiers() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the database treats mixed case
  * identifiers as all lower case.
  *
  * @exception <code>true</code> if the database treats all identifiers as
  * lower case, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
storesLowerCaseIdentifiers() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the database stores mixed case 
  * identifers even if it treats them as case insensitive.
  *
  * @return <code>true</code> if the database stores mixed case identifiers,
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
storesMixedCaseIdentifiers() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the database supports quoted identifiers
  * with mixed case.
  *
  * @return <code>true</code> if the database supports mixed case quoted
  * identifiers, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsMixedCaseQuotedIdentifiers() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the database treats mixed case
  * quoted identifiers as all upper case.
  *
  * @exception <code>true</code> if the database treats all quoted identifiers 
  * as upper case, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
storesUpperCaseQuotedIdentifiers() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the database treats mixed case
  * quoted identifiers as all lower case.
  *
  * @exception <code>true</code> if the database treats all quoted identifiers 
  * as lower case, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
storesLowerCaseQuotedIdentifiers() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the database stores mixed case 
  * quoted identifers even if it treats them as case insensitive.
  *
  * @return <code>true</code> if the database stores mixed case quoted 
  * identifiers, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
storesMixedCaseQuotedIdentifiers() throws SQLException;

/*************************************************************************/

/**
  * This metohd returns the quote string for SQL identifiers.
  *
  * @return The quote string for SQL identifers, or a space if quoting
  * is not supported.
  *
  * @exception SQLException If an error occurs.
  */
public abstract String
getIdentiferQuoteString() throws SQLException;

/*************************************************************************/

/**
  * This method returns a comma separated list of all the SQL keywords in
  * the database that are not in SQL92.
  *
  * @return The list of SQL keywords not in SQL92.
  *
  * @exception SQLException If an error occurs.
  */
public abstract String
getSQLKeywords() throws SQLException;

/*************************************************************************/

/**
  * This method returns a comma separated list of math functions.
  *
  * @return The list of math functions.
  *
  * @exception SQLException If an error occurs.
  */
public abstract String
getNumericFunctions() throws SQLException;

/*************************************************************************/

/**
  * This method returns a comma separated list of string functions.
  *
  * @return The list of string functions.
  * 
  * @exception SQLException If an error occurs.
  */
public abstract String
getStringFunctions() throws SQLException;

/*************************************************************************/

/**
  * This method returns a comma separated list of of system functions.
  *
  * @return A comma separated list of system functions.
  *
  * @exception SQLException If an error occurs.
  */
public abstract String
getSystemFunctions() throws SQLException;

/*************************************************************************/

/**
  * This method returns comma separated list of date/time functions.
  * 
  * @return The list of date/time functions.
  *
  * @exception SQLException If an error occurs.
  */
public abstract String
getDateTimeFunctions throws SQLException;

/*************************************************************************/

/**
  * This method returns the string used to escape wildcards in search strings.
  *
  * @return The string used to escape wildcards in search strings.
  *
  * @exception SQLException If an error occurs.
  */
public abstract String
getSearchStringEscape() throws SQLException;

/*************************************************************************/

/**
  * This methods returns non-standard characters that can appear in 
  * unquoted identifiers.
  *
  * @return Non-standard characters that can appear in unquoted identifiers.
  *
  * @exception SQLException If an error occurs.
  */
public abstract String
getExtraNameCharacters() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the database supports
  * "ALTER TABLE ADD COLUMN"
  *
  * @return <code>true</code> if column add supported, <code>false</code>
  * otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsAlterTableWithAddColumn() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the database supports
  * "ALTER TABLE DROP COLUMN"
  *
  * @return <code>true</code> if column drop supported, <code>false</code>
  * otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsAlterTableWithDropColumn() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not column aliasing is supported.
  *
  * @return <code>true</code> if column aliasing is supported,
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsColumnAliasing() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether the concatenation of a NULL and non-NULL
  * value results in a NULL.  This will always be true in fully JDBC compliant
  * drivers.
  *
  * @return <code>true</code> if concatenating NULL and a non-NULL value
  * returns a NULL, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
nullPlusNonNullIsNull() throws SQLException;

/*************************************************************************/

/**
  * Tests whether or not CONVERT is supported.
  *
  * @return <code>true</code> if CONVERT is supported, <code>false</code>
  * otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsConvert() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not CONVERT can be performed between the
  * specified types.  The types are contants from <code>Types</code>.
  *
  * @param fromType The SQL type to convert from.
  * @param toType The SQL type to convert to.
  * 
  * @return <code>true</code> if the conversion can be performed,
  * <code>false</code> otherwise.
  *
  * @see Types
  */
public abstract boolean
supportsConvert(int fromType, int toType) throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not table correlation names are 
  * supported.  This will be always be <code>true</code> in a fully JDBC
  * compliant driver.
  *
  * @return <code>true</code> if table correlation names are supported,
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsTableCorrelationNames() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether correlation names must be different from the
  * name of the table.
  *
  * @return <code>true</code> if the correlation name must be different from
  * the table name, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsDifferentTableCorrelationNames() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not expressions are allowed in an
  * ORDER BY lists.
  *
  * @return <code>true</code> if expressions are allowed in ORDER BY
  * lists, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsExpressionsInOrderBy() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or ORDER BY on a non-selected column is
  * allowed.
  *
  * @return <code>true</code> if a non-selected column can be used in an
  * ORDER BY, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsOrderByUnrelated() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not GROUP BY is supported.
  *
  * @return <code>true</code> if GROUP BY is supported, <code>false</code>
  * otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsGroupBy() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether GROUP BY on a non-selected column is
  * allowed.
  *
  * @return <code>true</code> if a non-selected column can be used in a
  * GROUP BY, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsGroupByUnrelated() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not a GROUP BY can add columns not in the
  * select if it includes all the columns in the select.
  *
  * @return <code>true</code> if GROUP BY an add columns provided it includes
  * all columns in the select, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsGroupByBeyondSelect() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the escape character is supported in
  * LIKE expressions.  A fully JDBC compliant driver will always return
  * <code>true</code>.
  *
  * @return <code>true</code> if escapes are supported in LIKE expressions,
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean supportsLikeEscapeClause() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether multiple result sets for a single statement are
  * supported.
  *
  * @return <code>true</code> if multiple result sets are supported for a 
  * single statement, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsMultipleResultSets() throws SQLException;

/*************************************************************************/

/**
  * This method test whether or not multiple transactions may be open
  * at once, as long as they are on different connections.
  *
  * @return <code>true</code> if multiple transactions on different
  * connections are supported, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsMultipleTransactions() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not columns can be defined as NOT NULL.  A
  * fully JDBC compliant driver always returns <code>true</code>.
  *
  * @return <code>true</code> if NOT NULL columns are supported,
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsNonNullableColumns() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the minimum grammer for ODBC is supported.
  * A fully JDBC compliant driver will always return <code>true</code>.
  *
  * @return <code>true</code> if the ODBC minimum grammar is supported,
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsMinimumSQLGrammar() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the core grammer for ODBC is supported.
  *
  * @return <code>true</code> if the ODBC core grammar is supported,
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsCoreSQLGrammar() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the extended grammer for ODBC is supported.
  *
  * @return <code>true</code> if the ODBC extended grammar is supported,
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsExtendedSQLGrammar() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the ANSI92 entry level SQL
  * grammar is supported.  A fully JDBC compliant drivers must return
  * <code>true</code>.
  *
  * @return <code>true</code> if the ANSI92 entry level SQL grammar is
  * supported, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsANSI92EntryLevelSQL() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the ANSI92 intermediate SQL
  * grammar is supported.  
  *
  * @return <code>true</code> if the ANSI92 intermediate SQL grammar is
  * supported, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsANSI92IntermediateSQL() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the ANSI92 full SQL
  * grammar is supported.  
  *
  * @return <code>true</code> if the ANSI92 full SQL grammar is
  * supported, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsANSI92FullSQL() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the SQL integrity enhancement
  * facility is supported.
  *
  * @return <code>true</code> if the integrity enhancement facility is
  * supported, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsIntegrityEnhancementFacility() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the database supports outer joins.
  *
  * @return <code>true</code> if outer joins are supported, <code>false</code>
  * otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsOuterJoins throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the database supports full outer joins.
  *
  * @return <code>true</code> if full outer joins are supported, 
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsFullOuterJoins throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the database supports limited outer joins.
  *
  * @return <code>true</code> if limited outer joins are supported, 
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsLimitedOuterJoins throws SQLException;

/*************************************************************************/

/**
  * This method returns the vendor's term for "schema".
  *
  * @return The vendor's term for schema.
  *
  * @exception SQLException if an error occurs.
  */
public abstract String
getSchemaTerm() throws SQLException;

/*************************************************************************/

/**
  * This method returns the vendor's term for "procedure".
  *
  * @return The vendor's term for procedure.
  *
  * @exception SQLException if an error occurs.
  */
public abstract String
getProcedureTerm() throws SQLException;

/*************************************************************************/

/**
  * This method returns the vendor's term for "catalog".
  *
  * @return The vendor's term for catalog.
  *
  * @exception SQLException if an error occurs.
  */
public abstract String
getCatalogTerm() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether a catalog name appears at the beginning of
  * a fully qualified table name.
  *
  * @return <code>true</code> if the catalog name appears at the beginning,
  * <code>false</code> if it appears at the end.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
isCatalogAtStart() throws SQLException;

/*************************************************************************/

/**
  * This method returns the separator between the catalog name and the
  * table name.
  *
  * @return The separator between the catalog name and the table name.
  *
  * @exception SQLException If an error occurs.
  */
public abstract String
getCatalogSeparator() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether a catalog name can appear in a data
  * manipulation statement.
  *
  * @return <code>true</code> if a catalog name can appear in a data
  * manipulation statement, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsSchemaInDataManipulation() thows SQLException;

/*************************************************************************/

/**
  * This method tests whether a catalog name can appear in a procedure
  * call
  *
  * @return <code>true</code> if a catalog name can appear in a procedure
  * call, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsSchemaInProcedureCalls() thows SQLException;

/*************************************************************************/

/**
  * This method tests whether a catalog name can appear in a table definition.
  *
  * @return <code>true</code> if a catalog name can appear in a table
  * definition, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsSchemaInTableDefinitions() thows SQLException;

/*************************************************************************/

/**
  * This method tests whether a catalog name can appear in an index definition.
  *
  * @return <code>true</code> if a catalog name can appear in an index
  * definition, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsSchemaInIndexDefinitions() thows SQLException;

/*************************************************************************/

/**
  * This method tests whether a catalog name can appear in privilege definitions.
  *
  * @return <code>true</code> if a catalog name can appear in privilege
  * definition, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsSchemaInPrivilegeDefinitions() thows SQLException;

/*************************************************************************/

/**
  * This method tests whether a catalog name can appear in a data
  * manipulation statement.
  *
  * @return <code>true</code> if a catalog name can appear in a data
  * manipulation statement, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsSchemaInDataManipulation() thows SQLException;

/*************************************************************************/

/**
  * This method tests whether a catalog name can appear in a procedure
  * call
  *
  * @return <code>true</code> if a catalog name can appear in a procedure
  * call, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsSchemaInProcedureCalls() thows SQLException;

/*************************************************************************/

/**
  * This method tests whether a catalog name can appear in a table definition.
  *
  * @return <code>true</code> if a catalog name can appear in a table
  * definition, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsSchemaInTableDefinitions() thows SQLException;

/*************************************************************************/

/**
  * This method tests whether a catalog name can appear in an index definition.
  *
  * @return <code>true</code> if a catalog name can appear in an index
  * definition, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsSchemaInIndexDefinitions() thows SQLException;

/*************************************************************************/

/**
  * This method tests whether a catalog name can appear in privilege definitions.
  *
  * @return <code>true</code> if a catalog name can appear in privilege
  * definition, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsCatalogInPrivilegeDefinitions() thows SQLException;

/*************************************************************************/

/**
  * This method tests whether or not that database supports positioned
  * deletes.
  *
  * @return <code>true</code> if positioned deletes are supported,
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsPositionedDelete() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not that database supports positioned
  * updates.
  *
  * @return <code>true</code> if positioned updates are supported,
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsPositionedUpdate() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not SELECT FOR UPDATE is supported by the
  * database.
  *
  * @return <code>true</code> if SELECT FOR UPDATE is supported 
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsSelectForUpdate() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not stored procedures are supported on
  * this database.
  *
  * @return <code>true</code> if stored procedures are supported,
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsStoredProcedures() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not subqueries are allowed in comparisons.
  * A fully JDBC compliant driver will always return <code>true</code>.
  *
  * @return <code>true</code> if subqueries are allowed in comparisons,
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsSubqueriesInComparisons() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not subqueries are allowed in exists
  * expressions.  A fully JDBC compliant driver will always return
  * <code>true</code>.
  *
  * @return <code>true</code> if subqueries are allowed in exists 
  * expressions, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsSubqueriesInExists() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether subqueries are allowed in IN statements.
  * A fully JDBC compliant driver will always return <code>true</code>.
  *
  * @return <code>true</code> if the driver supports subqueries in IN
  * statements, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsSubqueriesInIns() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not subqueries are allowed in quantified
  * expressions.  A fully JDBC compliant driver will always return
  * <code>true</code>.
  *
  * @return <code>true</code> if subqueries are allowed in quantified 
  * expressions, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsSubqueriesInQuantifieds() throws SQLException;

/*************************************************************************/

/**
  * This method test whether or not correlated subqueries are allowed. A
  * fully JDBC compliant driver will always return <code>true</code>.
  *
  * @return <code>true</code> if correlated subqueries are allowed,
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsCorrelatedSubqueries() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the UNION statement is supported.
  *
  * @return <code>true</code> if UNION is supported, <code>false</code>
  * otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsUnion() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the UNION ALL statement is supported.
  *
  * @return <code>true</code> if UNION ALL is supported, <code>false</code>
  * otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsUnionAll() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the database supports cursors
  * remaining open across commits.
  *
  * @return <code>true</code> if cursors can remain open across commits,
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsOpenCursorsAcrossCommit() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the database supports cursors
  * remaining open across rollbacks.
  *
  * @return <code>true</code> if cursors can remain open across rollbacks,
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsOpenCursorsAcrossRollback() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the database supports statements
  * remaining open across commits.
  *
  * @return <code>true</code> if statements can remain open across commits,
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsOpenStatementsAcrossCommit() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the database supports statements
  * remaining open across rollbacks.
  *
  * @return <code>true</code> if statements can remain open across rollbacks,
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsOpenStatementsAcrossRollback() throws SQLException;

/*************************************************************************/

/**
  * This method returns the number of hex characters allowed in an inline
  * binary literal.
  *
  * @return The number of hex characters allowed in a binary literal, 0 meaning
  * either an unknown or unlimited number.
  *
  * @exception SQLException If an error occurs.
  */
public abstract int
getMaxBinaryLiteralLength() throws SQLException;

/*************************************************************************/

/**
  * This method returns the maximum length of a character literal.
  * 
  * @return The maximum length of a character literal.
  *
  * @exception SQLException If an error occurs.
  */
public abstract int
getMaxCharLiteralLength() throws SQLException;

/*************************************************************************/

/**
  * This method returns the maximum length of a column name.
  *
  * @return The maximum length of a column name.
  *
  * @exception SQLException If an error occurs.
  */
public abstract int
getMaxColumnNameLength() throws SQLException;

/*************************************************************************/

/**
  * This method returns the maximum number of columns in a GROUP BY statement.
  *
  * @return The maximum number of columns in a GROUP BY statement.
  *
  * @exception SQLException If an error occurs.
  */
public abstract int
getMaxColumnsInGroupBy() throws SQLException;

/*************************************************************************/

/**
  * This method returns the maximum number of columns in an ORDER BY statement.
  *
  * @return The maximum number of columns in an ORDER BY statement.
  *
  * @exception SQLException If an error occurs.
  */
public abstract int
getMaxColumnsInOrderBy() throws SQLException;

/*************************************************************************/

/**
  * This method returns the maximum number of columns in an index.
  *
  * @return The maximum number of columns in an index.
  *
  * @exception SQLException If an error occurs.
  */
public abstract int
getMaxColumnsInIndex() throws SQLException;

/*************************************************************************/

/**
  * This method returns the maximum number of columns in an ORDER BY statement.
  *
  * @return The maximum number of columns in an ORDER BY statement.
  *
  * @exception SQLException If an error occurs.
  */
public abstract int
getMaxColumnsInOrderBy() throws SQLException;

/*************************************************************************/

/**
  * This method returns the maximum number of columns in a SELECT statement.
  *
  * @return The maximum number of columns in a SELECT statement.
  *
  * @exception SQLException If an error occurs.
  */
public abstract int
getMaxColumnsInSelect() throws SQLException;

/*************************************************************************/

/**
  * This method returns the maximum number of columns in a table.
  *
  * @return The maximum number of columns in a table.
  *
  * @exception SQLException If an error occurs.
  */
public abstract int
getMaxColumnsInTable() throws SQLException;

/*************************************************************************/

/**
  * This method returns the maximum number of connections this client
  * can have to the database.
  *
  * @return The maximum number of database connections.
  *
  * @SQLException If an error occurs.
  */
public abstract int
getMaxConnections() throws SQLException;

/*************************************************************************/

/**
  * This method returns the maximum length of a cursor name.
  *
  * @return The maximum length of a cursor name.
  *
  * @exception SQLException If an error occurs.
  */
public abstract int
getMaxCursorNameLength() throws SQLException;

/*************************************************************************/

/**
  * This method returns the maximum length of an index.
  *
  * @return The maximum length of an index.
  *
  * @exception SQLException If an error occurs.
  */
public abstract int
getMaxIndexLength() throws SQLException;

/*************************************************************************/

/**
  * This method returns the maximum length of a schema name.
  *
  * @return The maximum length of a schema name.
  *
  * @exception SQLException If an error occurs.
  */
public abstract int
getMaxSchemaNameLength() throws SQLException;

/*************************************************************************/

/**
  * This method returns the maximum length of a procedure name.
  *
  * @return The maximum length of a procedure name.
  *
  * @exception SQLException If an error occurs.
  */
public abstract int
getMaxProcedureNameLength() throws SQLException;

/*************************************************************************/

/**
  * This method returns the maximum length of a catalog name.
  *
  * @return The maximum length of a catalog name.
  *
  * @exception SQLException If an error occurs.
  */
public abstract int
getMaxCatalogNameLength() throws SQLException;

/*************************************************************************/

/**
  * This method returns the maximum size of a row in bytes.
  *
  * @return The maximum size of a row.
  *
  * @exception SQLException If an error occurs.
  */
public abstract int
getMaxRowSize() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the maximum row size includes BLOB's
  *
  * @return <code>true</code> if the maximum row size includes BLOB's,
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
doesMaxRowSizeIncludeBlobs() throws SQLException;

/*************************************************************************/

/**
  * This method includes the maximum length of a SQL statement.
  *
  * @return The maximum length of a SQL statement.
  *
  * @exception SQLException If an error occurs.
  */
public abstract int
getMaxStatementLength() throws SQLException;

/*************************************************************************/

/**
  * This method returns the maximum number of statements that can be
  * active at any time.
  *
  * @return The maximum number of statements that can be active at any time.
  * 
  * @exception SQLException If an error occurs.
  */
public abstract int
getMaxStatements() throws SQLException;

/*************************************************************************/

/**
  * This method returns the maximum length of a table name.
  *
  * @return The maximum length of a table name.
  *
  * @exception SQLException If an error occurs.
  */
public abstract int
getMaxTableNameLength() throws SQLException;

/*************************************************************************/

/**
  * This method returns the maximum number of tables that may be referenced
  * in a SELECT statement.
  *
  * @return The maximum number of tables allowed in a SELECT statement.
  *
  * @exception SQLException If an error occurs.
  */
public abstract int
getMaxTablesInSelect() throws SQLException;

/*************************************************************************/

/**
  * This method returns the maximum length of a user name.
  *
  * @return The maximum length of a user name.
  *
  * @exception SQLException If an error occurs.
  */
public abstract int
getMaxTableUserLength() throws SQLException;

/*************************************************************************/

/**
  * This method returns the default transaction isolation level of the
  * database.
  *
  * @return The default transaction isolation level of the database.
  *
  * @exception SQLException If an error occurs.
  *
  * @see Connection
  */
public abstract int
getDefaultTransactionIsolation() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the database supports transactions.
  *
  * @return <code>true</code> if the database supports transactions,
  * <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsTransactions() throws SQLException;

/*************************************************************************/

/**
  * This method tests whether or not the database supports the specified
  * transaction isolation level.
  *
  * @param level The transaction isolation level.
  *
  * @return <code>true</code> if the specified transaction isolation level
  * is supported, <code>false</code> otherwise.
  *
  * @exception SQLException If an error occurs.
  */
public abstract boolean
supportsTransactionIsolationLevel(int level) throws SQLException;

/*************************************************************************/











} // interface DatabaseMetaData

