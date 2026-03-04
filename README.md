<h1 align="center">☕ JDBC — Java Database Connectivity <img src="https://media.giphy.com/media/hvRJCLFzcasrR4ia7z/giphy.gif" width="30px"></h1>

<h3 align="center">The Most Detailed Guide to Understanding & Implementing JDBC with MySQL / MariaDB in IntelliJ IDEA</h3>

---

## 📚 Table of Contents

| # | Section |
|---|---------|
| 1 | [What is JDBC?](#-what-is-jdbc) |
| 2 | [Why JDBC? — The Problem It Solves](#-why-jdbc--the-problem-it-solves) |
| 3 | [JDBC Architecture — How It All Works](#-jdbc-architecture--how-it-all-works) |
| 4 | [Core Components of JDBC — In Depth](#-core-components-of-jdbc--in-depth) |
| 5 | [JDBC Drivers — All 4 Types Explained](#-jdbc-drivers--all-4-types-explained) |
| 6 | [DriverManager — The Connection Gateway](#-drivermanager--the-connection-gateway) |
| 7 | [Setting Up IntelliJ IDEA from Scratch](#-setting-up-intellij-idea-from-scratch) |
| 8 | [Downloading & Adding the .jar Connector](#-downloading--adding-the-jar-connector) |
| 9 | [Database & Table Setup in MySQL/MariaDB](#-database--table-setup-in-mysqlmariadb) |
| 10 | [Establishing a JDBC Connection — Full Code](#-establishing-a-jdbc-connection--full-code) |
| 11 | [CRUD Operations — Detailed with Code](#-crud-operations--detailed-with-code) |
| 12 | [Transaction Management](#-transaction-management) |
| 13 | [ResultSet Types & Scrollability](#-resultset-types--scrollability) |
| 14 | [JDBC vs Hibernate — When to Use What](#-jdbc-vs-hibernate--when-to-use-what) |
| 15 | [Project Structure](#-project-structure) |
| 16 | [Common Errors & Fixes](#-common-errors--fixes) |
| 17 | [JDBC Quick Reference Cheat Sheet](#-jdbc-quick-reference-cheat-sheet) |
| 18 | [Connect with Me](#-connect-with-me) |

---

## 🔍 What is JDBC?

**JDBC** stands for **Java Database Connectivity**. It is a **standard Java API** (Application Programming Interface) introduced by **Sun Microsystems** in **1997** and now maintained by **Oracle**. It is part of the **Java Standard Edition (Java SE)** platform.

JDBC provides a **universal data access layer** in Java that lets your Java programs communicate with virtually any **relational database management system (RDBMS)** — regardless of the underlying database vendor or platform.

In simple terms:

> 💡 JDBC is the **official Java way** to talk to databases using SQL.

### 📦 Where is JDBC Located in Java?

JDBC lives in two packages:

| Package | Purpose |
|---------|---------|
| `java.sql` | Core JDBC API — Connection, Statement, ResultSet, etc. |
| `javax.sql` | Extended API — DataSource, connection pooling, distributed transactions |

You do **not** need to install anything extra — both packages are bundled with every JDK installation.

### ✅ What Can JDBC Do?

- 🔗 **Connect** a Java application to any relational database (MySQL, Oracle, PostgreSQL, MariaDB, SQLite, etc.)
- 📤 **Send SQL commands** — SELECT, INSERT, UPDATE, DELETE, CREATE, DROP
- 📥 **Retrieve and navigate** through query results row-by-row
- 🔄 **Manage transactions** — commit, rollback, savepoints
- 📞 **Call stored procedures** and database functions
- 🔐 **Handle exceptions** gracefully using `SQLException`
- 📊 **Retrieve database metadata** — table names, column types, row counts, etc.

---

## ❓ Why JDBC? — The Problem It Solves

Before JDBC existed, connecting Java to a database was a **nightmare**:

- Every database vendor had its own proprietary API
- If you wrote code for Oracle, it **would not work** on MySQL
- Switching databases meant **rewriting your entire data access layer**

### 🔧 How JDBC Solves This

JDBC introduces a **standard interface**. Your Java code talks to the JDBC API, and the JDBC API talks to a **driver** specific to the database. If you switch databases, you **only swap the driver** — your Java code stays largely the same.

```
Without JDBC:   Java → [Oracle-specific code]   → Oracle DB
                Java → [MySQL-specific code]    → MySQL DB    ← Need to rewrite!

With JDBC:      Java → JDBC API → Oracle Driver → Oracle DB
                Java → JDBC API → MySQL Driver  → MySQL DB    ← Just swap the driver!
```

This is the **Write Once, Run Anywhere** principle applied to database programming.

---

## 🏗️ JDBC Architecture — How It All Works

JDBC follows a **layered architecture**. Each layer has a specific responsibility and talks only to the layer directly below it.

```
╔══════════════════════════════════════════════════════════╗
║              YOUR JAVA APPLICATION                       ║
║   (writes SQL, calls JDBC methods, processes results)    ║
╠══════════════════════════════════════════════════════════╣
║                   JDBC API LAYER                         ║
║  java.sql.* — DriverManager, Connection, Statement,      ║
║               PreparedStatement, ResultSet, etc.         ║
╠══════════════════════════════════════════════════════════╣
║              JDBC DRIVER MANAGER                         ║
║  Loads the right driver, manages multiple drivers,       ║
║  creates Connection objects on request                   ║
╠══════════════════════════════════════════════════════════╣
║                 JDBC DRIVER LAYER                        ║
║  The actual implementation — MySQL Connector/J,          ║
║  MariaDB Connector/J, Oracle JDBC Driver, etc.           ║
║  (distributed as .jar files)                             ║
╠══════════════════════════════════════════════════════════╣
║                    DATABASE                              ║
║    MySQL  |  MariaDB  |  Oracle  |  PostgreSQL  |  etc.  ║
╚══════════════════════════════════════════════════════════╝
```

### Two Architectural Models

**🔵 Two-Tier Architecture (Direct)**
```
[Java Application]  ←→  [JDBC Driver]  ←→  [Database Server]
```
- The Java app communicates **directly** with the database
- Simple, fast, suitable for **desktop applications** or **local apps**
- The client machine must have the JDBC driver installed

**🟣 Three-Tier Architecture (Middleware)**
```
[Java Application] ←→ [App Server / Middleware] ←→ [JDBC Driver] ←→ [Database]
```
- An **application server** (like Tomcat, JBoss, WildFly) sits in between
- The Java client talks to the app server via HTTP or RMI
- The app server handles database connections using **connection pooling**
- Used in **enterprise web applications** (Spring Boot, Jakarta EE, etc.)
- More scalable and secure

---

## 🧩 Core Components of JDBC — In Depth

JDBC is built around **7 core interfaces and classes**. Understanding each one thoroughly is the key to mastering JDBC.

---

### 1. 🔌 `DriverManager` — The Entry Point

`DriverManager` is a **static utility class** in `java.sql`. It is the very first thing you interact with in JDBC.

**Responsibilities:**
- Maintains a list of registered JDBC drivers
- When you request a connection, it iterates through the registered drivers and finds the one that can handle your connection URL
- Returns a `Connection` object

**Key Methods:**

| Method | Description |
|--------|-------------|
| `getConnection(url, user, pass)` | Opens and returns a database connection |
| `getConnection(url, Properties)` | Opens connection using a Properties object |
| `registerDriver(Driver d)` | Manually registers a driver |
| `getDrivers()` | Returns an enumeration of all registered drivers |
| `setLoginTimeout(int seconds)` | Sets max wait time for a connection |

```java
// Basic usage
Connection con = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/testdb",
    "root",
    "password"
);
```

> 🔑 In **JDBC 4.0+** (Java 6+), drivers are **auto-loaded** using the Service Provider mechanism. You no longer need `Class.forName()`. But it is still good practice to include it for clarity and backward compatibility.

---

### 2. 🔗 `Connection` — The Active Session

The `Connection` interface represents an **open session** with the database. Every SQL operation in JDBC flows through a `Connection` object. It is the most important object in JDBC.

**Responsibilities:**
- Creates `Statement`, `PreparedStatement`, and `CallableStatement` objects
- Manages **transaction control** (commit, rollback, savepoints)
- Provides access to database metadata
- Must always be **closed** after use to release database resources

**Key Methods:**

| Method | Description |
|--------|-------------|
| `createStatement()` | Creates a basic Statement object |
| `prepareStatement(sql)` | Creates a precompiled PreparedStatement |
| `prepareCall(sql)` | Creates a CallableStatement for stored procedures |
| `commit()` | Commits the current transaction |
| `rollback()` | Rolls back the current transaction |
| `setAutoCommit(boolean)` | Enables/disables auto-commit mode |
| `close()` | Closes the connection and releases resources |
| `isClosed()` | Returns true if connection is closed |
| `getMetaData()` | Returns DatabaseMetaData about the database |

```java
Connection con = DriverManager.getConnection(url, user, password);

System.out.println("Auto-commit: " + con.getAutoCommit()); // true by default
System.out.println("DB Product : " + con.getMetaData().getDatabaseProductName());
System.out.println("DB Version : " + con.getMetaData().getDatabaseProductVersion());

con.close(); // Always close!
```

> ⚠️ **Important:** A `Connection` is NOT thread-safe. Do not share one `Connection` object across multiple threads. In production, use a **Connection Pool** (HikariCP, c3p0).

---

### 3. 📝 `Statement` — For Static SQL

The `Statement` interface is used to execute **simple, static SQL queries** — queries that have no parameters and are known at compile time.

**When to use:** Quick queries, one-time operations, or DDL statements (CREATE, DROP, ALTER).

**Key Methods:**

| Method | Returns | Use For |
|--------|---------|---------|
| `executeQuery(sql)` | `ResultSet` | SELECT queries |
| `executeUpdate(sql)` | `int` (rows affected) | INSERT, UPDATE, DELETE, DDL |
| `execute(sql)` | `boolean` | Any SQL — true if first result is ResultSet |
| `addBatch(sql)` | void | Add SQL to batch |
| `executeBatch()` | `int[]` | Execute all batched SQL at once |
| `close()` | void | Release Statement resources |

```java
Statement stmt = con.createStatement();

// DDL — Create a table
stmt.executeUpdate("CREATE TABLE IF NOT EXISTS students (" +
    "id INT PRIMARY KEY, " +
    "name VARCHAR(50), " +
    "marks INT, " +
    "grade CHAR(1)" +
")");
System.out.println("Table created!");

// DML — Insert a row
int rows = stmt.executeUpdate("INSERT INTO students VALUES(1, 'Gopal', 95, 'A')");
System.out.println(rows + " row(s) inserted.");

// DQL — Select rows
ResultSet rs = stmt.executeQuery("SELECT * FROM students");
while (rs.next()) {
    System.out.println(rs.getInt("id") + " | " + rs.getString("name"));
}

stmt.close();
```

> ❌ **Warning:** Never use `Statement` with user-provided input! It is **vulnerable to SQL Injection**. Always use `PreparedStatement` when parameters are involved.

---

### 4. 🛡️ `PreparedStatement` — Safe & Precompiled

`PreparedStatement` is a **subinterface of `Statement`** and is the **recommended way** to execute SQL in JDBC. It supports **parameterized queries** using `?` placeholders.

**Advantages over `Statement`:**
- 🔒 **Prevents SQL Injection** — parameters are always treated as data, never as SQL code
- ⚡ **Better Performance** — SQL is **precompiled** once and can be **reused** with different parameters
- 📖 **Cleaner Code** — easier to read and maintain with complex queries
- 🛡️ **Type Safety** — you explicitly set each parameter type via `setInt()`, `setString()`, etc.

**Key Methods:**

| Method | Description |
|--------|-------------|
| `setInt(index, value)` | Set an integer parameter |
| `setString(index, value)` | Set a string parameter |
| `setDouble(index, value)` | Set a double parameter |
| `setDate(index, value)` | Set a java.sql.Date parameter |
| `setNull(index, sqlType)` | Set a NULL parameter |
| `setObject(index, value)` | Set any Java object as parameter |
| `executeQuery()` | Execute SELECT — returns ResultSet |
| `executeUpdate()` | Execute INSERT/UPDATE/DELETE — returns row count |
| `addBatch()` | Add current parameters to batch |
| `clearParameters()` | Reset all parameter values |

```java
// ✅ SAFE — Using PreparedStatement
String insertSQL = "INSERT INTO students(id, name, marks, grade) VALUES(?, ?, ?, ?)";
PreparedStatement ps = con.prepareStatement(insertSQL);

// Insert first student
ps.setInt(1, 2);
ps.setString(2, "Rahul");
ps.setInt(3, 87);
ps.setString(4, "B");
ps.executeUpdate();

// Reuse the same PreparedStatement for second student (efficient!)
ps.setInt(1, 3);
ps.setString(2, "Priya");
ps.setInt(3, 91);
ps.setString(4, "A");
ps.executeUpdate();

System.out.println("Both students inserted successfully!");
ps.close();
```

**SQL Injection Demo — Why PreparedStatement Matters:**
```java
// ❌ DANGEROUS — Using Statement with user input
String userInput = "' OR '1'='1"; // Malicious input
String sql = "SELECT * FROM users WHERE username = '" + userInput + "'";
// This becomes: SELECT * FROM users WHERE username = '' OR '1'='1'
// Returns ALL users! Security breach!

// ✅ SAFE — PreparedStatement escapes input automatically
PreparedStatement ps = con.prepareStatement(
    "SELECT * FROM users WHERE username = ?"
);
ps.setString(1, userInput); // Treated as literal string, not SQL code
// Safe! Returns no rows (no user with that weird name)
```

---

### 5. ⚙️ `CallableStatement` — For Stored Procedures

`CallableStatement` extends `PreparedStatement` and is used to **call stored procedures** or **functions** stored inside the database.

**When to use:**
- When business logic is encapsulated in database stored procedures
- When you need to call a procedure that returns multiple result sets
- For performance-critical operations handled at the DB level

```sql
-- First, create a stored procedure in MySQL
DELIMITER //
CREATE PROCEDURE getStudentsByGrade(IN grade_input CHAR(1))
BEGIN
    SELECT * FROM students WHERE grade = grade_input;
END //
DELIMITER ;
```

```java
// Call the stored procedure from Java
CallableStatement cs = con.prepareCall("{call getStudentsByGrade(?)}");
cs.setString(1, "A");

ResultSet rs = cs.executeQuery();
System.out.println("Grade A Students:");
while (rs.next()) {
    System.out.println(rs.getString("name") + " — " + rs.getInt("marks"));
}

rs.close();
cs.close();
```

**Stored Procedure with OUT parameter:**
```sql
-- Procedure that returns a value via OUT parameter
DELIMITER //
CREATE PROCEDURE getStudentCount(OUT total INT)
BEGIN
    SELECT COUNT(*) INTO total FROM students;
END //
DELIMITER ;
```

```java
CallableStatement cs = con.prepareCall("{call getStudentCount(?)}");
cs.registerOutParameter(1, java.sql.Types.INTEGER); // Register OUT param
cs.execute();

int count = cs.getInt(1); // Retrieve OUT parameter value
System.out.println("Total students: " + count);
cs.close();
```

---

### 6. 📦 `ResultSet` — The Result Container

`ResultSet` is the object that holds the **data returned from a SELECT query**. Think of it as a **table in memory** — it has rows and columns, and a **cursor** that you move through it row by row.

**Cursor Behavior:**
- Initially, the cursor is positioned **before the first row**
- `rs.next()` moves it **forward one row** and returns `true` if a row exists
- When no more rows are available, `rs.next()` returns `false`

**Key Getter Methods:**

| Method | Returns |
|--------|---------|
| `getInt(columnName or index)` | int value |
| `getString(columnName or index)` | String value |
| `getDouble(columnName or index)` | double value |
| `getBoolean(columnName or index)` | boolean value |
| `getDate(columnName or index)` | java.sql.Date value |
| `getTimestamp(columnName or index)` | java.sql.Timestamp value |
| `getObject(columnName or index)` | Any Java Object |
| `wasNull()` | true if the last column read was SQL NULL |

**Navigation Methods** (for scrollable ResultSets):

| Method | Description |
|--------|-------------|
| `next()` | Move to next row |
| `previous()` | Move to previous row |
| `first()` | Move to first row |
| `last()` | Move to last row |
| `absolute(n)` | Move to nth row |
| `relative(n)` | Move n rows from current position |
| `beforeFirst()` | Move before first row |
| `afterLast()` | Move after last row |

```java
Statement stmt = con.createStatement();
ResultSet rs = stmt.executeQuery("SELECT * FROM students ORDER BY marks DESC");

System.out.println("╔════╦══════════╦═══════╦═══════╗");
System.out.println("║ ID ║ Name     ║ Marks ║ Grade ║");
System.out.println("╠════╬══════════╬═══════╬═══════╣");

while (rs.next()) {
    System.out.printf("║ %-2d ║ %-8s ║  %-4d ║   %-3s ║%n",
        rs.getInt("id"),
        rs.getString("name"),
        rs.getInt("marks"),
        rs.getString("grade")
    );
}

System.out.println("╚════╩══════════╩═══════╩═══════╝");
rs.close();
stmt.close();
```

**Checking ResultSet Metadata:**
```java
ResultSetMetaData meta = rs.getMetaData();
int colCount = meta.getColumnCount();
System.out.println("Number of columns: " + colCount);

for (int i = 1; i <= colCount; i++) {
    System.out.println("Column " + i + ": " +
        meta.getColumnName(i) + " (" + meta.getColumnTypeName(i) + ")");
}
```

---

### 7. 🔐 `SQLException` — Database Error Handling

`SQLException` is the main **checked exception** in JDBC. It is thrown by almost every JDBC method and contains detailed information about what went wrong.

**Key Methods:**

| Method | Returns |
|--------|---------|
| `getMessage()` | Human-readable error message |
| `getSQLState()` | 5-character SQL state code (XOPEN standard) |
| `getErrorCode()` | Vendor-specific error code |
| `getNextException()` | Next exception in the chain |

```java
try {
    Connection con = DriverManager.getConnection(url, user, pass);
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM non_existent_table");

} catch (SQLException e) {
    System.out.println("Error Message : " + e.getMessage());
    System.out.println("SQL State     : " + e.getSQLState());
    System.out.println("Error Code    : " + e.getErrorCode());

    // Check for chained exceptions
    SQLException next = e.getNextException();
    while (next != null) {
        System.out.println("  → Chained: " + next.getMessage());
        next = next.getNextException();
    }
}
```

**Common SQL State Codes:**

| SQL State | Meaning |
|-----------|---------|
| `08001` | Cannot connect to database server |
| `08006` | Connection failure during transaction |
| `28000` | Invalid authorization / wrong password |
| `42000` | Syntax error or access rule violation |
| `42S02` | Table not found |
| `23000` | Integrity constraint violation (duplicate key, etc.) |

---

## 🚗 JDBC Drivers — All 4 Types Explained

A **JDBC Driver** is the concrete implementation that does the actual work of communicating with the database. It translates the abstract JDBC API calls into database-specific network protocol messages.

Without a driver, JDBC is just an interface — useless. The driver is what makes the connection real.

---

### 🔴 Type 1 — JDBC-ODBC Bridge Driver

```
┌─────────────────┐
│  Java App       │
│  (JDBC calls)   │
└────────┬────────┘
         │  Java
         ▼
┌─────────────────┐
│  JDBC-ODBC      │
│  Bridge Driver  │  ← Java layer
└────────┬────────┘
         │  JNI (Java Native Interface)
         ▼
┌─────────────────┐
│  ODBC Driver    │  ← C/C++ native library (must be installed on OS)
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│    Database     │
└─────────────────┘
```

**How it works:**
- Uses **ODBC (Open Database Connectivity)** — Microsoft's older standard for database access
- Java calls → translated to ODBC calls → translated to database-specific calls
- Requires the **ODBC driver** to be installed and configured on the client machine's operating system

**Characteristics:**
| Feature | Detail |
|---------|--------|
| Pure Java? | ❌ No — depends on native ODBC driver |
| Platform Independent? | ❌ No — ODBC is Windows-centric |
| Speed | 🐢 Very slow (2 translation layers) |
| Setup | Easy (was bundled with early JDKs) |
| Status | **Deprecated since Java 8 — removed in Java 9** |

**When it was used:** Legacy enterprise systems in the early 2000s that already had ODBC infrastructure.

> ⛔ Do NOT use Type 1 in any modern project. It's completely obsolete.

---

### 🟠 Type 2 — Native API Driver (Partial Java Driver)

```
┌─────────────────┐
│  Java App       │
└────────┬────────┘
         │  Java
         ▼
┌─────────────────┐
│  Type 2 Driver  │  ← Java wrapper layer
└────────┬────────┘
         │  JNI (Java Native Interface)
         ▼
┌─────────────────────────────┐
│  Database Vendor Native Lib │  ← C/C++ native library (e.g., Oracle OCI)
│  (must be installed locally)│
└────────────┬────────────────┘
         │
         ▼
┌─────────────────┐
│    Database     │
└─────────────────┘
```

**How it works:**
- The driver converts JDBC calls into **database-specific native API calls** using JNI
- It wraps native client libraries (C/C++) provided by the database vendor
- The **native library** must be installed on every client machine

**Characteristics:**
| Feature | Detail |
|---------|--------|
| Pure Java? | ❌ No — partially native |
| Platform Independent? | ❌ No — needs native library on each machine |
| Speed | 🚶 Medium — faster than Type 1 (only 1 translation layer) |
| Setup | Complex — native libraries must be installed |
| Status | Rarely used today |

**Examples:**
- **Oracle OCI (Oracle Call Interface) Driver**
- **DB2 Native Driver** by IBM

**When to use:** Legacy Oracle enterprise systems where the native OCI driver gives performance benefits, and the client machines are all the same platform.

---

### 🟡 Type 3 — Network Protocol Driver (Middleware / Pure Java)

```
┌─────────────────┐
│  Java App       │
└────────┬────────┘
         │  Java
         ▼
┌─────────────────────┐
│  Type 3 JDBC Driver │  ← 100% Pure Java
│  (on client)        │
└────────┬────────────┘
         │  Network Protocol (HTTP, IIOP, etc.)
         ▼
┌────────────────────────────┐
│  Middleware / App Server   │  ← Translates to DB-specific protocol
│  (on separate server)      │
└────────┬───────────────────┘
         │  Database-specific protocol
         ▼
┌─────────────────┐
│    Database     │
└─────────────────┘
```

**How it works:**
- The client-side driver is **100% Pure Java** — no native libraries on the client
- It communicates with a **middleware server** using a generic network protocol
- The **middleware server** then translates those calls into database-specific protocol
- The middleware can connect to **multiple different databases** simultaneously

**Characteristics:**
| Feature | Detail |
|---------|--------|
| Pure Java? | ✅ Yes (client side) |
| Platform Independent? | ✅ Yes |
| Speed | 🚶 Medium — extra hop through middleware |
| Flexibility | ✅ High — one driver can target multiple databases |
| Setup | Complex — requires middleware server |
| Status | Rarely used in modern apps |

**When to use:** Large enterprise systems that need to connect to **multiple different databases** through a centralized connection manager, or internet-facing applications where client-side native code is not feasible.

---

### 🟢 Type 4 — Thin Driver / Pure Java Driver ⭐ THE GOLD STANDARD

```
┌─────────────────┐
│  Java App       │
└────────┬────────┘
         │  Java
         ▼
┌─────────────────────────┐
│  Type 4 JDBC Driver     │  ← 100% Pure Java (.jar file)
│  MySQL Connector/J      │
│  MariaDB Connector/J    │
└────────┬────────────────┘
         │  Database-native protocol DIRECTLY over TCP/IP
         │  (e.g., MySQL Protocol on port 3306)
         ▼
┌─────────────────┐
│    Database     │  ← MySQL / MariaDB / PostgreSQL / Oracle
└─────────────────┘
```

**How it works:**
- The driver is written in **100% Pure Java** — just a `.jar` file
- It communicates **directly** with the database using the database's **own native network protocol** over TCP/IP
- No intermediary layers, no native code, no middleware
- This is the driver you **download from MySQL's website** as `mysql-connector-j-x.x.x.jar`

**Characteristics:**
| Feature | Detail |
|---------|--------|
| Pure Java? | ✅ Yes — 100% Pure Java |
| Platform Independent? | ✅ Yes — runs on any OS with JVM |
| Speed | 🚀 Fastest — direct DB protocol communication |
| Setup | Very easy — just add the `.jar` to classpath |
| Status | **Standard for all modern Java applications** |
| Examples | MySQL Connector/J, MariaDB Connector/J, PostgreSQL JDBC Driver |

---

### 📊 All 4 Driver Types — Side by Side Comparison

| Feature | Type 1 | Type 2 | Type 3 | Type 4 |
|---------|--------|--------|--------|--------|
| **Name** | JDBC-ODBC Bridge | Native API | Network Protocol | Thin / Pure Java |
| **Pure Java?** | ❌ No | ❌ Partial | ✅ Yes | ✅ Yes |
| **Platform Independent?** | ❌ No | ❌ No | ✅ Yes | ✅ Yes |
| **Speed** | 🐢 Slowest | 🚶 Medium | 🚶 Medium | 🚀 Fastest |
| **Native Library Needed?** | ✅ Yes (ODBC) | ✅ Yes (DB lib) | ❌ No | ❌ No |
| **Middleware Needed?** | ❌ No | ❌ No | ✅ Yes | ❌ No |
| **Ease of Setup** | Medium | Hard | Hard | ✅ Easy |
| **Recommended?** | ⛔ Never | ⚠️ Rare | ⚠️ Rare | ✅ Always |

---

## 🎯 DriverManager — The Connection Gateway

`DriverManager` is the **central manager** of the entire JDBC connection process. It is a **final class** with only **static methods** — you never instantiate it.

### 🔄 Internal Working — Step by Step

```
Step 1: Your app calls DriverManager.getConnection(url, user, pass)

Step 2: DriverManager iterates over all registered drivers

Step 3: For each driver, it calls driver.connect(url, props)

Step 4: The driver checks if it understands the URL prefix
        → MySQL driver checks for "jdbc:mysql://"
        → If URL matches, it opens a TCP connection to MySQL server on port 3306
        → Sends authentication credentials
        → MySQL server validates and grants access

Step 5: Driver returns a Connection object to DriverManager

Step 6: DriverManager returns that Connection object to your app

Step 7: Your app uses the Connection to create Statements and run SQL
```

### 📡 Connection URL — Deep Dive

The connection URL is how JDBC knows **which database**, on **which server**, on **which port**, to connect to.

**Full URL Structure:**
```
jdbc : <subprotocol> :// <host> : <port> / <database> ? <properties>
  ↑          ↑             ↑         ↑          ↑              ↑
JDBC    driver type      server    port      DB name     optional config
prefix   identifier      name                           key=value pairs
```

**MySQL Examples:**
```java
// Minimal — local MySQL on default port
String url = "jdbc:mysql://localhost:3306/testdb";

// With MySQL 8+ recommended options
String url = "jdbc:mysql://localhost:3306/testdb" +
             "?useSSL=false" +
             "&allowPublicKeyRetrieval=true" +
             "&serverTimezone=UTC" +
             "&characterEncoding=UTF-8" +
             "&autoReconnect=true";

// Remote server
String url = "jdbc:mysql://192.168.1.100:3306/production_db?useSSL=true";
```

**MariaDB Example:**
```java
String url = "jdbc:mariadb://localhost:3306/testdb?useSSL=false";
```

### ⚙️ Driver Registration — Old vs New

**Old Way (JDBC 3.x — before Java 6):**
```java
// Manually load and register the driver class
// Class.forName() triggers the static initializer in the driver class
// which calls DriverManager.registerDriver(new Driver())
Class.forName("com.mysql.cj.jdbc.Driver");  // MySQL 8+
// or
Class.forName("com.mysql.jdbc.Driver");     // MySQL 5.x (old)
// or
Class.forName("org.mariadb.jdbc.Driver");   // MariaDB
```

**New Way (JDBC 4.0+ — Java 6+, recommended):**
```java
// Drivers are automatically discovered and registered!
// The .jar file contains META-INF/services/java.sql.Driver
// which lists the driver class name
// DriverManager reads this file and loads the driver automatically
// You just call getConnection() directly!
Connection con = DriverManager.getConnection(url, user, password);
```

### 🕒 Setting Connection Timeout

```java
// Set max wait time for getting a connection (in seconds)
DriverManager.setLoginTimeout(10); // Wait max 10 seconds

try {
    Connection con = DriverManager.getConnection(url, user, pass);
} catch (SQLException e) {
    // If connection not established in 10 seconds, exception is thrown
    System.out.println("Connection timed out!");
}
```

---

## 🛠️ Setting Up IntelliJ IDEA from Scratch

IntelliJ IDEA is the most powerful Java IDE. Here is a complete setup guide from zero.

### Step 1 — Download & Install IntelliJ IDEA

1. Go to: [https://www.jetbrains.com/idea/download/](https://www.jetbrains.com/idea/download/)
2. Choose **Community Edition** (free and open source) — more than enough for JDBC
3. Download the installer for your OS:
   - **Windows**: `.exe` installer
   - **macOS**: `.dmg` disk image
   - **Linux**: `.tar.gz` archive or via JetBrains Toolbox
4. Run the installer and follow the prompts
5. When asked, select **"Add to PATH"** (Windows) and create a desktop shortcut

### Step 2 — Install / Verify JDK

JDBC requires **Java 11 or above** (Java 17 LTS is recommended).

1. In IntelliJ, go to **File → Project Structure → SDKs**
2. If no JDK is listed, click **`+`** → **"Download JDK"**
3. Select **Version 17** → **Vendor: Eclipse Temurin (AdoptOpenJDK)** → **Download**
4. Click **Apply**

Or install JDK separately from: [https://adoptium.net/](https://adoptium.net/)

### Step 3 — Create a New Java Project

1. Launch IntelliJ IDEA
2. Click **"New Project"** on the Welcome screen
3. In the left panel, select **"Java"**
4. Configure:
   ```
   Name     : db_conn
   Location : C:\Users\YourName\Projects\db_conn   (or any path)
   JDK      : 17 (or whichever you installed)
   ```
5. **Uncheck** "Add sample code" (we'll write our own)
6. Click **"Create"** ✅

### Step 4 — Understand the Project Layout

After creation, your project looks like this:
```
db_conn/                     ← Project root
├── .idea/                   ← IntelliJ config (don't touch)
│   ├── misc.xml
│   ├── modules.xml
│   └── workspace.xml
├── src/                     ← All Java source files go here
│   └── (empty for now)
└── db_conn.iml              ← IntelliJ module file
```

### Step 5 — Create Source Files

1. Right-click `src/` folder → **New → Java Class**
2. Name it `Main` → press Enter
3. IntelliJ creates `src/Main.java` with a basic class structure

---

## 📦 Downloading & Adding the .jar Connector

### Step 1 — Download MySQL Connector/J

1. Visit: [https://dev.mysql.com/downloads/connector/j/](https://dev.mysql.com/downloads/connector/j/)
2. Under **"Select Operating System"**, choose **"Platform Independent"**
3. Download the **ZIP Archive** (e.g., `mysql-connector-j-8.3.0.zip`)
4. Extract the ZIP — you'll find `mysql-connector-j-8.3.0.jar` inside
5. This `.jar` file is your **Type 4 JDBC driver**

> 🐬 **For MariaDB**, download from: [https://mariadb.com/downloads/connectors/](https://mariadb.com/downloads/connectors/)
> Get `mariadb-java-client-3.x.x.jar`

### Step 2 — Create a `lib` Folder in Your Project

1. In IntelliJ's Project Explorer, right-click the **project root** (`db_conn`)
2. Select **New → Directory**
3. Name it `lib` → press Enter
4. Copy/paste (or drag & drop) the downloaded `.jar` file into the `lib/` folder

Your project structure now:
```
db_conn/
├── .idea/
├── lib/
│   └── mysql-connector-j-8.3.0.jar   ← Connector placed here ✅
├── src/
│   └── Main.java
└── db_conn.iml
```

### Step 3 — Add the .jar to the Project Classpath

**Method A — Via Project Structure (Recommended & Permanent)**

1. Press `Ctrl + Alt + Shift + S` (or **File → Project Structure**)
2. In the left panel, click **"Modules"**
3. Go to the **"Dependencies"** tab on the right
4. Click the **`+`** button (bottom of the list) → Select **"JARs or Directories..."**
5. Navigate to `your-project/lib/` → select `mysql-connector-j-8.3.0.jar`
6. Click **OK** → make sure it's checked (✅) in the list
7. Click **Apply** → **OK**

**Method B — Right-Click Shortcut**

1. In the Project Explorer, expand `lib/`
2. Right-click `mysql-connector-j-8.3.0.jar`
3. Select **"Add as Library..."**
4. Leave settings as default → click **OK**

### Step 4 — Verify the Connector Was Added

In the Project Explorer, expand **"External Libraries"** at the bottom:
```
External Libraries
└── mysql-connector-j-8.3.0     ← This should appear ✅
    └── mysql-connector-j-8.3.0.jar
```

If you see the connector here, you are ready to write JDBC code! 🎉

---

## 🗄️ Database & Table Setup in MySQL/MariaDB

Before connecting from Java, you need to have MySQL/MariaDB running and a database ready.

### Start MySQL/MariaDB Service

**Windows:**
```cmd
:: Via Services (search "Services" in Start Menu → find MySQL → Start)
:: Or via Command Prompt as Administrator:
net start mysql
```

**Linux:**
```bash
sudo systemctl start mysql    # MySQL
sudo systemctl start mariadb  # MariaDB

sudo systemctl enable mysql   # Auto-start on boot
```

**macOS (via Homebrew):**
```bash
brew services start mysql
```

### Open MySQL Shell & Create Everything

```sql
-- Open MySQL command line and log in
mysql -u root -p
-- Enter your password when prompted

-- ─────────────────────────────────────────────────────
-- Step 1: Create a new database
-- ─────────────────────────────────────────────────────
CREATE DATABASE IF NOT EXISTS testdb;
USE testdb;

-- ─────────────────────────────────────────────────────
-- Step 2: Create a students table
-- ─────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS students (
    id       INT          PRIMARY KEY AUTO_INCREMENT,
    name     VARCHAR(50)  NOT NULL,
    marks    INT          NOT NULL,
    grade    CHAR(1)      NOT NULL,
    city     VARCHAR(30),
    enrolled DATE         DEFAULT (CURRENT_DATE)
);

-- ─────────────────────────────────────────────────────
-- Step 3: Insert sample data
-- ─────────────────────────────────────────────────────
INSERT INTO students (name, marks, grade, city) VALUES
('Gopal Tayade', 95, 'A', 'Pune'),
('Rahul Sharma', 87, 'B', 'Mumbai'),
('Priya Desai',  91, 'A', 'Nashik'),
('Anjali Patil', 78, 'C', 'Pune'),
('Rohan Gupta',  83, 'B', 'Nagpur');

-- ─────────────────────────────────────────────────────
-- Step 4: Verify the data
-- ─────────────────────────────────────────────────────
SELECT * FROM students;
```

Expected output:
```
+----+--------------+-------+-------+---------+------------+
| id | name         | marks | grade | city    | enrolled   |
+----+--------------+-------+-------+---------+------------+
|  1 | Gopal Tayade |    95 | A     | Pune    | 2025-01-01 |
|  2 | Rahul Sharma |    87 | B     | Mumbai  | 2025-01-01 |
|  3 | Priya Desai  |    91 | A     | Nashik  | 2025-01-01 |
|  4 | Anjali Patil |    78 | C     | Pune    | 2025-01-01 |
|  5 | Rohan Gupta  |    83 | B     | Nagpur  | 2025-01-01 |
+----+--------------+-------+-------+---------+------------+
```

---

## 🔗 Establishing a JDBC Connection — Full Code

Now let's write the complete, production-grade JDBC connection code:

### DBConfig.java — Centralize Your Credentials

```java
// src/DBConfig.java
// Best practice: keep all DB config in one place

public class DBConfig {

    // ── MySQL Settings ──────────────────────────────────────
    public static final String HOST     = "localhost";
    public static final String PORT     = "3306";
    public static final String DATABASE = "testdb";
    public static final String USER     = "root";
    public static final String PASSWORD = "yourpassword";   // ← Change this

    // ── Full Connection URL (MySQL 8+ with recommended options) ──
    public static final String URL =
        "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE +
        "?useSSL=false" +
        "&allowPublicKeyRetrieval=true" +
        "&serverTimezone=UTC" +
        "&characterEncoding=UTF-8" +
        "&autoReconnect=true";

    // ── For MariaDB, use this URL instead ───────────────────
    // public static final String URL =
    //     "jdbc:mariadb://" + HOST + ":" + PORT + "/" + DATABASE +
    //     "?useSSL=false&characterEncoding=UTF-8";
}
```

### DBConnection.java — Connection Manager

```java
// src/DBConnection.java

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static void main(String[] args) {

        Connection con = null;

        try {
            // ── Step 1: Load the Driver ────────────────────────────────
            // For JDBC 4.0+ (Java 6+), this line is optional
            // The driver is auto-discovered from the .jar's META-INF/services/
            // But it's good practice to include it for clarity
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ Driver loaded successfully.");

            // ── Step 2: Request a Connection from DriverManager ────────
            System.out.println("⏳ Connecting to database...");
            con = DriverManager.getConnection(
                DBConfig.URL,
                DBConfig.USER,
                DBConfig.PASSWORD
            );
            System.out.println("✅ Connection established successfully!\n");

            // ── Step 3: Print Database Info using Metadata ─────────────
            DatabaseMetaData meta = con.getMetaData();
            System.out.println("════════ DATABASE INFO ════════");
            System.out.println("Product Name    : " + meta.getDatabaseProductName());
            System.out.println("Product Version : " + meta.getDatabaseProductVersion());
            System.out.println("Driver Name     : " + meta.getDriverName());
            System.out.println("Driver Version  : " + meta.getDriverVersion());
            System.out.println("JDBC URL        : " + meta.getURL());
            System.out.println("Username        : " + meta.getUserName());
            System.out.println("Auto-commit     : " + con.getAutoCommit());
            System.out.println("Read Only       : " + con.isReadOnly());
            System.out.println("═══════════════════════════════\n");

        } catch (ClassNotFoundException e) {
            // Driver .jar not added to classpath
            System.out.println("❌ Driver class not found!");
            System.out.println("   → Make sure mysql-connector-j-x.x.x.jar is in your classpath");
            System.out.println("   → Error: " + e.getMessage());

        } catch (SQLException e) {
            // SQL-level connection failure
            System.out.println("❌ Database connection failed!");
            System.out.println("   → Message  : " + e.getMessage());
            System.out.println("   → SQLState  : " + e.getSQLState());
            System.out.println("   → ErrorCode : " + e.getErrorCode());

        } finally {
            // ── Step 4: ALWAYS close the connection ───────────────────
            // Even if an exception was thrown above, this block always runs
            if (con != null) {
                try {
                    con.close();
                    System.out.println("🔒 Connection closed gracefully.");
                } catch (SQLException e) {
                    System.out.println("⚠️ Failed to close connection: " + e.getMessage());
                }
            }
        }
    }
}
```

**Expected Output:**
```
✅ Driver loaded successfully.
⏳ Connecting to database...
✅ Connection established successfully!

════════ DATABASE INFO ════════
Product Name    : MySQL
Product Version : 8.0.35
Driver Name     : MySQL Connector/J
Driver Version  : mysql-connector-j-8.3.0 (Revision: ...)
JDBC URL        : jdbc:mysql://localhost:3306/testdb
Username        : root@localhost
Auto-commit     : true
Read Only       : false
═══════════════════════════════

🔒 Connection closed gracefully.
```

---

## 💻 CRUD Operations — Detailed with Code

### 📖 C1 — READ with `Statement` (SELECT)

```java
// src/ReadStudents.java

import java.sql.*;

public class ReadStudents {

    public static void main(String[] args) {

        String sql = "SELECT id, name, marks, grade, city FROM students ORDER BY marks DESC";

        // try-with-resources — auto-closes Connection, Statement, ResultSet
        try (
            Connection con   = DriverManager.getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD);
            Statement  stmt  = con.createStatement();
            ResultSet  rs    = stmt.executeQuery(sql)
        ) {
            System.out.println("╔════╦══════════════════╦═══════╦═══════╦══════════╗");
            System.out.println("║ ID ║ Name             ║ Marks ║ Grade ║ City     ║");
            System.out.println("╠════╬══════════════════╬═══════╬═══════╬══════════╣");

            int rowCount = 0;

            while (rs.next()) {
                rowCount++;
                System.out.printf("║ %-2d ║ %-16s ║  %-4d ║   %-3s ║ %-8s ║%n",
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("marks"),
                    rs.getString("grade"),
                    rs.getString("city")
                );
            }

            System.out.println("╚════╩══════════════════╩═══════╩═══════╩══════════╝");
            System.out.println("Total rows fetched: " + rowCount);

        } catch (SQLException e) {
            System.out.println("❌ Error reading data: " + e.getMessage());
        }
        // Connection, Statement, ResultSet are all automatically closed here ✅
    }
}
```

---

### 🔍 C2 — READ with Filter using `PreparedStatement`

```java
// src/SearchByGrade.java

import java.sql.*;
import java.util.Scanner;

public class SearchByGrade {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter grade to search (A / B / C): ");
        String inputGrade = sc.next().toUpperCase();

        String sql = "SELECT * FROM students WHERE grade = ? ORDER BY name";

        try (
            Connection       con = DriverManager.getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD);
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, inputGrade);   // Safe! Input is parameterized
            ResultSet rs = ps.executeQuery();

            System.out.println("\nStudents with Grade: " + inputGrade);
            System.out.println("─".repeat(40));

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("ID: %d | Name: %-16s | Marks: %d | City: %s%n",
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("marks"),
                    rs.getString("city")
                );
            }

            if (!found) {
                System.out.println("No students found with grade: " + inputGrade);
            }

            rs.close();

        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}
```

---

### ➕ C3 — INSERT a New Student with `PreparedStatement`

```java
// src/InsertStudent.java

import java.sql.*;

public class InsertStudent {

    // Reusable insert method — returns the generated auto-increment ID
    public static int insertStudent(Connection con, String name, int marks, String grade, String city)
            throws SQLException {

        String sql = "INSERT INTO students (name, marks, grade, city) VALUES (?, ?, ?, ?)";

        // RETURN_GENERATED_KEYS tells JDBC to give us back the auto-generated id
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, name);
        ps.setInt(2, marks);
        ps.setString(3, grade);
        ps.setString(4, city);

        int affectedRows = ps.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Insert failed — no rows affected.");
        }

        // Retrieve the auto-generated key
        int generatedId = -1;
        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            generatedId = generatedKeys.getInt(1);
        }
        generatedKeys.close();
        ps.close();

        return generatedId;
    }

    public static void main(String[] args) {

        try (Connection con = DriverManager.getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD)) {

            // Insert one student
            int id1 = insertStudent(con, "Sneha Kulkarni", 92, "A", "Kolhapur");
            System.out.println("✅ Inserted: Sneha Kulkarni  → Assigned ID: " + id1);

            int id2 = insertStudent(con, "Arjun Mehta", 76, "C", "Aurangabad");
            System.out.println("✅ Inserted: Arjun Mehta     → Assigned ID: " + id2);

            System.out.println("\nAll students now in DB:");
            // Show updated table
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, name, marks FROM students ORDER BY id");
            while (rs.next()) {
                System.out.printf("  ID %d: %-20s → %d marks%n",
                    rs.getInt("id"), rs.getString("name"), rs.getInt("marks"));
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("❌ Insert error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

---

### ✏️ C4 — UPDATE Records

```java
// src/UpdateStudent.java

import java.sql.*;

public class UpdateStudent {

    public static void main(String[] args) {

        // ── Example 1: Update marks for a specific student ───────────
        String updateMarksSql = "UPDATE students SET marks = ?, grade = ? WHERE id = ?";

        // ── Example 2: Bulk update — increase all marks by 2 ─────────
        String bulkUpdateSql = "UPDATE students SET marks = marks + 2 WHERE marks < 100";

        try (Connection con = DriverManager.getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD)) {

            // ── Update single student ─────────────────────────────────
            PreparedStatement ps = con.prepareStatement(updateMarksSql);
            ps.setInt(1, 99);        // New marks
            ps.setString(2, "A");   // New grade
            ps.setInt(3, 4);        // WHERE id = 4 (Anjali Patil)
            int updated = ps.executeUpdate();
            System.out.println("✅ Single update — rows affected: " + updated);
            ps.close();

            // ── Verify the update ─────────────────────────────────────
            PreparedStatement verify = con.prepareStatement(
                "SELECT id, name, marks, grade FROM students WHERE id = ?"
            );
            verify.setInt(1, 4);
            ResultSet rs = verify.executeQuery();
            if (rs.next()) {
                System.out.printf("   Updated record → ID: %d | Name: %s | Marks: %d | Grade: %s%n",
                    rs.getInt("id"), rs.getString("name"),
                    rs.getInt("marks"), rs.getString("grade"));
            }
            rs.close();
            verify.close();

            // ── Bulk update all students ──────────────────────────────
            Statement bulkStmt = con.createStatement();
            int bulkUpdated = bulkStmt.executeUpdate(bulkUpdateSql);
            System.out.println("\n✅ Bulk update — rows affected: " + bulkUpdated);
            bulkStmt.close();

        } catch (SQLException e) {
            System.out.println("❌ Update error: " + e.getMessage());
        }
    }
}
```

---

### ❌ C5 — DELETE Records

```java
// src/DeleteStudent.java

import java.sql.*;

public class DeleteStudent {

    public static void main(String[] args) {

        try (Connection con = DriverManager.getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD)) {

            // ── Check student exists before deleting ──────────────────
            int deleteId = 6; // ID of student to delete
            PreparedStatement checkPs = con.prepareStatement(
                "SELECT name FROM students WHERE id = ?"
            );
            checkPs.setInt(1, deleteId);
            ResultSet checkRs = checkPs.executeQuery();

            if (!checkRs.next()) {
                System.out.println("⚠️ No student found with ID: " + deleteId);
                checkRs.close();
                checkPs.close();
                return;
            }

            String studentName = checkRs.getString("name");
            checkRs.close();
            checkPs.close();

            // ── Confirm and delete ────────────────────────────────────
            PreparedStatement deletePs = con.prepareStatement(
                "DELETE FROM students WHERE id = ?"
            );
            deletePs.setInt(1, deleteId);
            int deleted = deletePs.executeUpdate();
            deletePs.close();

            if (deleted > 0) {
                System.out.println("✅ Successfully deleted: " + studentName + " (ID: " + deleteId + ")");
            }

            // ── Delete by condition ───────────────────────────────────
            PreparedStatement condDelete = con.prepareStatement(
                "DELETE FROM students WHERE grade = ? AND marks < ?"
            );
            condDelete.setString(1, "C");
            condDelete.setInt(2, 70);
            int condDeleted = condDelete.executeUpdate();
            System.out.println("✅ Deleted " + condDeleted + " student(s) with grade C and marks below 70");
            condDelete.close();

        } catch (SQLException e) {
            System.out.println("❌ Delete error: " + e.getMessage());
        }
    }
}
```

---

### ⚡ C6 — Batch Operations (Insert Multiple Rows Fast)

Batch processing lets you **group multiple SQL statements** and send them to the database in **one network round-trip** — dramatically faster than executing them one by one.

```java
// src/BatchInsert.java

import java.sql.*;

public class BatchInsert {

    public static void main(String[] args) {

        // Sample data to insert in batch
        String[][] newStudents = {
            {"Kavya Joshi",    "88", "B", "Pune"},
            {"Nikhil Bane",    "72", "C", "Latur"},
            {"Shruti Pawar",   "96", "A", "Pune"},
            {"Omkar Jadhav",   "81", "B", "Solapur"},
            {"Tanvi Kulkarni", "93", "A", "Nashik"},
        };

        String sql = "INSERT INTO students (name, marks, grade, city) VALUES (?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD)) {

            con.setAutoCommit(false); // ← Disable auto-commit for batching

            PreparedStatement ps = con.prepareStatement(sql);

            for (String[] student : newStudents) {
                ps.setString(1, student[0]);
                ps.setInt(2, Integer.parseInt(student[1]));
                ps.setString(3, student[2]);
                ps.setString(4, student[3]);
                ps.addBatch(); // ← Add to batch instead of executing
            }

            long startTime = System.currentTimeMillis();
            int[] results = ps.executeBatch(); // ← Execute ALL at once
            con.commit(); // ← Commit the transaction
            long endTime = System.currentTimeMillis();

            System.out.println("✅ Batch insert completed!");
            System.out.println("   Rows inserted : " + results.length);
            System.out.println("   Time taken    : " + (endTime - startTime) + " ms");

            ps.close();

        } catch (SQLException e) {
            System.out.println("❌ Batch insert error: " + e.getMessage());
        }
    }
}
```

---

## 🔄 Transaction Management

A **transaction** is a sequence of database operations that must be treated as a **single unit**. Either ALL operations succeed (commit), or ALL are undone (rollback).

JDBC supports transactions using:
- `con.setAutoCommit(false)` — disables auto-commit (every statement is now part of a transaction)
- `con.commit()` — saves all changes permanently
- `con.rollback()` — undoes all changes since the last commit
- `con.setSavepoint("name")` — creates a partial rollback point

```java
// src/TransactionDemo.java

import java.sql.*;

public class TransactionDemo {

    public static void main(String[] args) {

        Connection con = null;
        Savepoint savepoint = null;

        try {
            con = DriverManager.getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD);
            con.setAutoCommit(false); // ← Start manual transaction mode
            System.out.println("🔄 Transaction started...");

            Statement stmt = con.createStatement();

            // ── Operation 1: Update Gopal's marks ─────────────────────
            stmt.executeUpdate("UPDATE students SET marks = marks + 5 WHERE id = 1");
            System.out.println("✅ Operation 1: Gopal's marks updated.");

            // ── Create a Savepoint after op 1 ─────────────────────────
            savepoint = con.setSavepoint("AfterGopalUpdate");
            System.out.println("📍 Savepoint created: AfterGopalUpdate");

            // ── Operation 2: Update Rahul's marks ─────────────────────
            stmt.executeUpdate("UPDATE students SET marks = marks - 3 WHERE id = 2");
            System.out.println("✅ Operation 2: Rahul's marks updated.");

            // ── Operation 3: Intentionally cause an error ──────────────
            // Uncomment next line to simulate a failure:
            // stmt.executeUpdate("UPDATE non_existent_table SET x = 1");

            // ── Commit ALL operations ──────────────────────────────────
            con.commit();
            System.out.println("✅ Transaction committed — all changes saved permanently!");

        } catch (SQLException e) {
            System.out.println("❌ Error during transaction: " + e.getMessage());

            try {
                if (savepoint != null) {
                    // ── Partial rollback — undo only after savepoint ───
                    con.rollback(savepoint);
                    con.commit(); // Commit what was before the savepoint
                    System.out.println("⚠️ Partial rollback to savepoint 'AfterGopalUpdate'.");
                    System.out.println("   → Op 1 (Gopal) kept. Op 2 (Rahul) rolled back.");
                } else {
                    // ── Full rollback — undo everything ───────────────
                    con.rollback();
                    System.out.println("❌ Full rollback — all changes undone.");
                }
            } catch (SQLException rollbackEx) {
                System.out.println("❌ Rollback also failed: " + rollbackEx.getMessage());
            }

        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true); // Restore default behavior
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
```

---

## 📜 ResultSet Types & Scrollability

By default, `ResultSet` is **forward-only** — you can only call `rs.next()` and move forward. But JDBC supports **scrollable and updatable** ResultSets.

### ResultSet Types

| Constant | Description |
|----------|-------------|
| `TYPE_FORWARD_ONLY` | Default. Can only move forward with `next()` |
| `TYPE_SCROLL_INSENSITIVE` | Can scroll in any direction. Does NOT see DB changes made after the ResultSet was created |
| `TYPE_SCROLL_SENSITIVE` | Can scroll in any direction. DOES see DB changes in real-time |

### ResultSet Concurrency

| Constant | Description |
|----------|-------------|
| `CONCUR_READ_ONLY` | Default. Cannot modify data through the ResultSet |
| `CONCUR_UPDATABLE` | Can use ResultSet methods to update rows directly |

```java
// Create a scrollable, read-only ResultSet
Statement stmt = con.createStatement(
    ResultSet.TYPE_SCROLL_INSENSITIVE,
    ResultSet.CONCUR_READ_ONLY
);

ResultSet rs = stmt.executeQuery("SELECT * FROM students");

// Move to last row to get total count
rs.last();
int totalRows = rs.getRow();
System.out.println("Total students: " + totalRows);

// Move back to first row
rs.first();
System.out.println("First student: " + rs.getString("name"));

// Jump to a specific row (row 3)
rs.absolute(3);
System.out.println("Third student: " + rs.getString("name"));

// Move backward
rs.previous();
System.out.println("Second student: " + rs.getString("name"));

// Back to start, iterate normally
rs.beforeFirst();
while (rs.next()) {
    System.out.println(rs.getString("name"));
}

rs.close();
stmt.close();
```

---

## 📊 JDBC vs Hibernate — When to Use What

| Feature | JDBC | Hibernate (ORM) |
|---------|------|-----------------|
| **Abstraction** | Low — you write raw SQL | High — works with Java objects |
| **Learning Curve** | Low | Higher |
| **Performance** | Maximum — full control | Slightly lower (overhead from ORM mapping) |
| **Boilerplate Code** | High (lots of `try-catch`, `close()`) | Low (framework handles it) |
| **Database Portability** | Medium — may need SQL tweaks per DB | High — Hibernate generates DB-specific SQL |
| **Complex Queries** | ✅ Great — raw SQL is powerful | ⚠️ Complex joins can be tricky |
| **Best For** | Small projects, learning, performance-critical apps | Large enterprise apps, rapid development |

> 🎯 **Learn JDBC first.** Hibernate and Spring Data JPA are built on top of JDBC. Understanding JDBC makes you a much stronger developer.

---

## 🗂️ Project Structure

```
JDBC--java_database_connection-/
│
├── 📁 .idea/                         ← IntelliJ IDEA project config (auto-generated)
│   ├── misc.xml
│   ├── modules.xml
│   └── workspace.xml
│
├── 📁 lib/
│   └── 📄 mysql-connector-j-8.3.0.jar   ← Type 4 JDBC Driver
│
├── 📁 src/
│   ├── 📄 DBConfig.java              ← DB credentials & URL config
│   ├── 📄 DBConnection.java          ← Connection + metadata demo
│   ├── 📄 ReadStudents.java          ← SELECT + ResultSet traversal
│   ├── 📄 SearchByGrade.java         ← Parameterized SELECT query
│   ├── 📄 InsertStudent.java         ← INSERT with generated keys
│   ├── 📄 UpdateStudent.java         ← Single & bulk UPDATE
│   ├── 📄 DeleteStudent.java         ← Conditional DELETE
│   ├── 📄 BatchInsert.java           ← Batch INSERT for performance
│   └── 📄 TransactionDemo.java       ← Commit, Rollback & Savepoints
│
├── 📄 db_conn.iml                    ← IntelliJ module file
└── 📄 .gitignore
```

---

## ⚠️ Common Errors & Fixes

| # | ❌ Error Message | 🔍 Root Cause | ✅ Solution |
|---|----------------|--------------|------------|
| 1 | `ClassNotFoundException: com.mysql.cj.jdbc.Driver` | `.jar` file not added to project classpath | Add via **File → Project Structure → Dependencies → + JAR** |
| 2 | `Communications link failure` | MySQL server is not running, or wrong host/port | Start MySQL service; verify `localhost:3306` in URL |
| 3 | `Access denied for user 'root'@'localhost'` | Wrong username or password in `getConnection()` | Double-check DB credentials |
| 4 | `Unknown database 'testdb'` | The specified database does not exist | Run `CREATE DATABASE testdb;` in MySQL shell |
| 5 | `Table 'testdb.students' doesn't exist` | Table was not created | Run the `CREATE TABLE` SQL script provided above |
| 6 | `Public Key Retrieval is not allowed` | MySQL 8+ auth plugin issue | Add `?allowPublicKeyRetrieval=true` to URL |
| 7 | `SSL connection warning` | MySQL 8+ requires SSL by default | Add `?useSSL=false` to URL for local dev |
| 8 | `The server time zone value is unrecognized` | JVM timezone doesn't match server | Add `?serverTimezone=UTC` to URL |
| 9 | `ResultSet is closed` | Accessing `rs` after `stmt.close()` | Close ResultSet before Statement; or use try-with-resources |
| 10 | `No suitable driver found for jdbc:mysql://...` | Driver not registered / wrong URL prefix | Ensure `.jar` is in classpath; check URL starts with `jdbc:mysql://` |

**Universal Fix URL for MySQL 8+ (Development):**
```java
String URL = "jdbc:mysql://localhost:3306/testdb" +
             "?useSSL=false" +
             "&allowPublicKeyRetrieval=true" +
             "&serverTimezone=UTC" +
             "&characterEncoding=UTF-8";
```

---

## 🔑 JDBC Quick Reference Cheat Sheet

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  JDBC 6-STEP CONNECTION FLOW
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  1. Load Driver     →  Class.forName("com.mysql.cj.jdbc.Driver")
                        [Optional in JDBC 4.0+]
  2. Get Connection  →  DriverManager.getConnection(url, user, pass)
  3. Create Statement→  con.createStatement()
                        con.prepareStatement(sql)
                        con.prepareCall("{call procedure(?)}")
  4. Execute SQL     →  stmt.executeQuery()     ← SELECT
                        stmt.executeUpdate()    ← INSERT/UPDATE/DELETE
                        stmt.executeBatch()     ← Batch operations
  5. Process Result  →  while(rs.next()) { rs.getString("col"); }
  6. Close Resources →  rs.close() → stmt.close() → con.close()
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  STATEMENT TYPES
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Statement          → Static SQL, no parameters
  PreparedStatement  → Parameterized SQL (use ?)  ← PREFER THIS
  CallableStatement  → Stored procedures

  DRIVER TYPES SUMMARY
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Type 1 → JDBC-ODBC Bridge      (Deprecated ⛔)
  Type 2 → Native API            (Avoid ⚠️)
  Type 3 → Network Protocol      (Rare ⚠️)
  Type 4 → Pure Java / Thin      (USE THIS ✅)

  TRANSACTION CONTROL
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  con.setAutoCommit(false)     → Begin manual transaction
  con.commit()                 → Save changes permanently
  con.rollback()               → Undo all changes
  con.setSavepoint("name")     → Partial rollback point
  con.rollback(savepoint)      → Rollback to specific point
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

---

## 📊 GitHub Stats

<p align="center">
  <img src="https://github-readme-stats.vercel.app/api?username=gt102005&show_icons=true&theme=tokyonight"/>
  <img src="https://github-readme-stats.vercel.app/api/top-langs/?username=gt102005&layout=compact&theme=tokyonight"/>
</p>

---

## 🤝 Connect with Me

<p align="left">
📧 <strong>Email</strong> &nbsp;: <a href="mailto:tayadegopalsuresh@gmail.com">tayadegopalsuresh@gmail.com</a><br/>
📱 <strong>Phone</strong> &nbsp;: +91 9579962097<br/>
🔗 <strong>GitHub</strong> &nbsp;: <a href="https://github.com/gt102005">gt102005</a><br/>
👤 <strong>LinkedIn</strong> : <a href="https://www.linkedin.com/in/gopal-tayade-075961350">Gopal Tayade</a>
</p>

---

<p align="center">
  <img src="https://img.shields.io/badge/Made%20with-Java%20%2B%20JDBC-ED8B00?style=for-the-badge&logo=openjdk"/>
  <img src="https://img.shields.io/badge/Database-MySQL%20%2F%20MariaDB-4479A1?style=for-the-badge&logo=mysql"/>
  <img src="https://img.shields.io/badge/IDE-IntelliJ%20IDEA-000000?style=for-the-badge&logo=intellijidea"/>
  <img src="https://img.shields.io/badge/Driver-Type%204%20Pure%20Java-brightgreen?style=for-the-badge"/>
</p>

<p align="center">
  ⭐ <strong>If this repo helped you understand JDBC deeply, please drop a star!</strong> ⭐
</p>

> *"Code with purpose | Build with security | Grow with curiosity"* — Gopal Tayade
