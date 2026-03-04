<h1 align="center">☕ JDBC — Java Database Connectivity <img src="https://media.giphy.com/media/hvRJCLFzcasrR4ia7z/giphy.gif" width="30px"></h1>



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
| 9 | [Database & Table Setup in MariaDB](#-database--table-setup-in-mariadb) |
| 10 | [The Connection Code — Explained Line by Line](#-the-connection-code--explained-line-by-line) |
| 11 | [Project Structure](#-project-structure) |
| 12 | [Common Errors & Fixes](#-common-errors--fixes) |
| 13 | [JDBC Quick Reference Cheat Sheet](#-jdbc-quick-reference-cheat-sheet) |


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

You do **not** need to install anything extra — both packages are bundled with every standard JDK installation.

### ✅ What Can JDBC Do?

- 🔗 **Connect** a Java application to any relational database (MySQL, MariaDB, Oracle, PostgreSQL, SQLite, etc.)
- 📤 **Send SQL commands** — SELECT, INSERT, UPDATE, DELETE, CREATE, DROP
- 📥 **Retrieve and navigate** through query results row by row
- 🔄 **Manage transactions** — commit, rollback, savepoints
- 📞 **Call stored procedures** and database functions
- 🔐 **Handle exceptions** gracefully using `SQLException`
- 📊 **Retrieve database metadata** — table names, column types, row counts, etc.

---

## ❓ Why JDBC? — The Problem It Solves

Before JDBC existed, connecting Java to a database was a **nightmare**:

- Every database vendor had its own proprietary API
- If you wrote code for Oracle, it **would not work** on MySQL or MariaDB
- Switching databases meant **rewriting your entire data access layer**

### 🔧 How JDBC Solves This

JDBC introduces a **standard interface**. Your Java code talks to the JDBC API, and the JDBC API talks to a **driver** specific to the database. If you switch databases, you **only swap the driver** — your Java code stays largely the same.

```
Without JDBC:   Java → [Oracle-specific code]   → Oracle DB
                Java → [MariaDB-specific code]  → MariaDB   ← Need to rewrite!

With JDBC:      Java → JDBC API → Oracle Driver  → Oracle DB
                Java → JDBC API → MariaDB Driver → MariaDB  ← Just swap the driver!
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
║  The actual implementation — MariaDB Connector/J,        ║
║  MySQL Connector/J, Oracle JDBC Driver, etc.             ║
║  (distributed as .jar files)                             ║
╠══════════════════════════════════════════════════════════╣
║                    DATABASE                              ║
║    MariaDB  |  MySQL  |  Oracle  |  PostgreSQL  |  etc.  ║
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
| `isClosed()` | Returns true if connection is already closed |
| `getMetaData()` | Returns DatabaseMetaData about the connected database |

> ⚠️ **Important:** A `Connection` is NOT thread-safe. Do not share one `Connection` object across multiple threads. In production apps, always use a **Connection Pool** (HikariCP, c3p0).

---

### 3. 📝 `Statement` — For Static SQL

The `Statement` interface is used to execute **simple, static SQL queries** — queries that have no parameters and are fully known at the time of writing.

**When to use:** Quick queries, one-time operations, or DDL statements (CREATE, DROP, ALTER).

**Key Methods:**

| Method | Returns | Use For |
|--------|---------|---------|
| `executeQuery(sql)` | `ResultSet` | SELECT queries |
| `executeUpdate(sql)` | `int` (rows affected) | INSERT, UPDATE, DELETE, DDL |
| `execute(sql)` | `boolean` | Any SQL — true if result is a ResultSet |
| `addBatch(sql)` | void | Add SQL to a batch queue |
| `executeBatch()` | `int[]` | Execute all batched SQL at once |
| `close()` | void | Release Statement resources |

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
| `executeQuery()` | Execute SELECT — returns ResultSet |
| `executeUpdate()` | Execute INSERT/UPDATE/DELETE — returns row count |
| `clearParameters()` | Reset all parameter values |

---

### 5. ⚙️ `CallableStatement` — For Stored Procedures

`CallableStatement` extends `PreparedStatement` and is used to **call stored procedures** or **functions** stored inside the database.

**When to use:**
- When business logic is encapsulated in database stored procedures
- When you need to call a procedure that returns multiple result sets
- For performance-critical operations handled at the DB level

**Syntax to call a stored procedure:**
```
{call procedure_name(param1, param2, ...)}
```

**Types of parameters:**
| Parameter Type | Direction | Description |
|---------------|-----------|-------------|
| `IN` | Input | Value passed from Java to the DB procedure |
| `OUT` | Output | Value returned from DB procedure to Java |
| `IN OUT` | Both | Value passed in and also returned |

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
| `getObject(columnName or index)` | Any Java Object |
| `wasNull()` | true if the last column read was SQL NULL |

**Navigation Methods** (for scrollable ResultSets):

| Method | Description |
|--------|-------------|
| `next()` | Move to next row |
| `previous()` | Move to previous row |
| `first()` | Move to first row |
| `last()` | Move to last row |
| `absolute(n)` | Move to the nth row |
| `beforeFirst()` | Move before the first row (reset) |

**ResultSet Types:**

| Constant | Description |
|----------|-------------|
| `TYPE_FORWARD_ONLY` | Default. Can only move forward with `next()` |
| `TYPE_SCROLL_INSENSITIVE` | Can scroll in any direction. Does NOT reflect DB changes made after the ResultSet was opened |
| `TYPE_SCROLL_SENSITIVE` | Can scroll in any direction. DOES reflect live DB changes |

---

### 7. 🔐 `SQLException` — Database Error Handling

`SQLException` is the main **checked exception** in JDBC. It is thrown by almost every JDBC method and contains detailed information about what went wrong at the database level.

**Key Methods:**

| Method | Returns |
|--------|---------|
| `getMessage()` | Human-readable error message |
| `getSQLState()` | 5-character SQL state code (XOPEN standard) |
| `getErrorCode()` | Vendor-specific integer error code |
| `getNextException()` | Next chained exception in the chain |

**Common SQL State Codes:**

| SQL State | Meaning |
|-----------|---------|
| `08001` | Cannot connect to the database server |
| `08006` | Connection failure during an active transaction |
| `28000` | Invalid authorization — wrong username or password |
| `42000` | Syntax error or access rule violation |
| `42S02` | Table or view not found |
| `23000` | Integrity constraint violation (duplicate key, etc.) |

---

## 🚗 JDBC Drivers — All 4 Types Explained

A **JDBC Driver** is the concrete implementation that does the actual work of communicating with the database. It translates the abstract JDBC API calls into database-specific network protocol messages.

Without a driver, JDBC is just an interface — useless on its own. The driver is what makes the connection real.

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
- Java calls get translated to ODBC calls, which then get translated to database-specific calls
- Requires the **ODBC driver** to be installed and configured on the client machine's OS

**Characteristics:**

| Feature | Detail |
|---------|--------|
| Pure Java? | ❌ No — depends on native ODBC driver |
| Platform Independent? | ❌ No — ODBC is Windows-centric |
| Speed | 🐢 Very slow (2 translation layers) |
| Setup | Easy (was bundled with early JDKs) |
| Status | **Deprecated since Java 8 — removed in Java 9** |

> ⛔ Do NOT use Type 1 in any modern project. It is completely obsolete.

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
┌──────────────────────────────┐
│  DB Vendor Native Library    │  ← C/C++ native library (e.g., Oracle OCI)
│  (must be installed locally) │
└────────┬─────────────────────┘
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

**Examples:** Oracle OCI Driver, IBM DB2 Native Driver

---

### 🟡 Type 3 — Network Protocol Driver (Middleware / Pure Java)

```
┌─────────────────┐
│  Java App       │
└────────┬────────┘
         │  Java
         ▼
┌─────────────────────┐
│  Type 3 JDBC Driver │  ← 100% Pure Java (on client)
└────────┬────────────┘
         │  Generic Network Protocol (HTTP, IIOP, etc.)
         ▼
┌───────────────────────────┐
│  Middleware / App Server  │  ← Translates to DB-specific protocol
│  (on a separate server)   │
└────────┬──────────────────┘
         │  Database-specific protocol
         ▼
┌─────────────────┐
│    Database     │
└─────────────────┘
```

**How it works:**
- The client-side driver is **100% Pure Java** — no native libraries required on the client
- It communicates with a **middleware server** using a generic network protocol
- The **middleware server** then translates those calls into database-specific protocol
- One middleware can talk to **multiple different databases** simultaneously

**Characteristics:**

| Feature | Detail |
|---------|--------|
| Pure Java? | ✅ Yes (client side) |
| Platform Independent? | ✅ Yes |
| Speed | 🚶 Medium — extra network hop through middleware |
| Flexibility | ✅ High — one driver can target multiple databases |
| Setup | Complex — requires middleware server |
| Status | Rarely used in modern apps |

---

### 🟢 Type 4 — Thin Driver / Pure Java Driver ⭐ THE GOLD STANDARD

```
┌─────────────────┐
│  Java App       │
└────────┬────────┘
         │  Java
         ▼
┌──────────────────────────┐
│  Type 4 JDBC Driver      │  ← 100% Pure Java (.jar file)
│  MariaDB Connector/J     │
└────────┬─────────────────┘
         │  MariaDB native protocol DIRECTLY over TCP/IP (port 3306)
         ▼
┌─────────────────┐
│    MariaDB      │
└─────────────────┘
```

**How it works:**
- The driver is written in **100% Pure Java** — packaged as a single `.jar` file
- It communicates **directly** with the database using the database's **own native network protocol** over TCP/IP
- No intermediate layers, no native code, no middleware needed at all
- This is exactly the `mariadb-java-client-x.x.x.jar` file you download and add to your project

**Characteristics:**

| Feature | Detail |
|---------|--------|
| Pure Java? | ✅ Yes — 100% Pure Java |
| Platform Independent? | ✅ Yes — runs on any OS with a JVM |
| Speed | 🚀 Fastest — direct native protocol communication |
| Setup | Very easy — just add the `.jar` to your classpath |
| Status | **Standard for all modern Java applications** |

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

`DriverManager` is the **central manager** of the entire JDBC connection process. It is a **final class** with only **static methods** — you never instantiate it directly.

### 🔄 Internal Working — Step by Step

```
Step 1: Your app calls DriverManager.getConnection(url, user, pass)

Step 2: DriverManager iterates over all registered drivers

Step 3: For each driver, it calls driver.connect(url, props)

Step 4: The driver checks if it understands the URL prefix
        → MariaDB driver checks for "jdbc:mariadb://"
        → If URL matches, opens a TCP connection to MariaDB on port 3306
        → Sends authentication credentials
        → MariaDB server validates and grants access

Step 5: Driver returns a Connection object to DriverManager

Step 6: DriverManager returns that Connection object to your app

Step 7: Your app uses the Connection to create Statements and run SQL
```

### 📡 Connection URL — Deep Dive

The connection URL is how JDBC knows **which database**, on **which server**, on **which port**, to connect to.

**Full URL Structure:**
```
jdbc : <subprotocol> :// <host> : <port> / <database>
  ↑          ↑             ↑         ↑          ↑
JDBC    driver type      server    port      DB name
prefix   identifier      name
```

**MariaDB Example (as used in this project):**
```
jdbc:mariadb://localhost:3306/jdbc
     ↑              ↑     ↑    ↑
  mariadb        server  port  database
  driver         host          name
```

### ⚙️ Driver Registration — Old vs New

**Old Way (JDBC 3.x — before Java 6):**
```java
// Manually load and register the driver class.
// Class.forName() triggers the static initializer in the driver,
// which internally calls DriverManager.registerDriver(new Driver())
Class.forName("org.mariadb.jdbc.Driver");
```

**New Way (JDBC 4.0+ — Java 6 and above):**
```java
// Drivers are automatically discovered using the Service Provider mechanism.
// The .jar file contains META-INF/services/java.sql.Driver
// listing the driver class. DriverManager reads this and loads it automatically.
// You can skip Class.forName() entirely!
Connection con = DriverManager.getConnection(url, user, password);
```

> 🔑 In this project, `Class.forName("org.mariadb.jdbc.Driver")` is used explicitly — this is the **old but clear and explicit** style. It still works perfectly in modern Java and makes the code easy to understand.

---

## 🛠️ Setting Up IntelliJ IDEA from Scratch

IntelliJ IDEA is the most powerful Java IDE. Here is a complete setup guide from zero.

### Step 1 — Download & Install IntelliJ IDEA

1. Go to: [https://www.jetbrains.com/idea/download/](https://www.jetbrains.com/idea/download/)
2. Choose **Community Edition** (free and open source) — more than enough for JDBC
3. Download the installer for your OS (Windows `.exe` / macOS `.dmg` / Linux `.tar.gz`)
4. Run the installer and follow the prompts
5. When asked, select **"Add to PATH"** (Windows) and create a desktop shortcut

### Step 2 — Install / Verify JDK

1. In IntelliJ, go to **File → Project Structure → SDKs**
2. If no JDK is listed, click **`+`** → **"Download JDK"**
3. Select **Version 17** → **Vendor: Eclipse Temurin** → **Download**
4. Click **Apply**

### Step 3 — Create a New Java Project

1. Launch IntelliJ IDEA → Click **"New Project"**
2. In the left panel, select **"Java"**
3. Configure:
   ```
   Name     : db_conn
   Location : C:\Users\YourName\Projects\db_conn
   JDK      : 17
   ```
4. Uncheck **"Add sample code"**
5. Click **"Create"** ✅

### Step 4 — Understand the Project Layout

```
db_conn/
├── .idea/          ← IntelliJ config files (auto-generated, don't touch)
├── src/            ← All your Java source files go here
│   └── Main.java
└── db_conn.iml     ← IntelliJ module file
```

---

## 📦 Downloading & Adding the .jar Connector

### Step 1 — Download MariaDB Connector/J

1. Visit: [https://mariadb.com/downloads/connectors/](https://mariadb.com/downloads/connectors/)
2. Select **Connector/J** (the Java connector)
3. Download the `.jar` file (e.g., `mariadb-java-client-3.x.x.jar`)
4. This `.jar` is your **Type 4 Pure Java JDBC driver**

### Step 2 — Create a `lib` Folder in Your Project

1. In IntelliJ's Project Explorer, right-click the **project root**
2. Select **New → Directory** → name it `lib`
3. Copy/paste the downloaded `.jar` file into the `lib/` folder

```
db_conn/
├── .idea/
├── lib/
│   └── mariadb-java-client-3.x.x.jar   ← Placed here ✅
├── src/
│   └── Main.java
└── db_conn.iml
```

### Step 3 — Add the .jar to the Project Classpath

**Method A — Via Project Structure (Recommended)**

1. Press `Ctrl + Alt + Shift + S` → opens **Project Structure**
2. Click **"Modules"** → go to **"Dependencies"** tab
3. Click **`+`** → **"JARs or Directories..."**
4. Navigate to `lib/` → select the `.jar` → click **OK**
5. Make sure it is checked ✅ → click **Apply** → **OK**

**Method B — Right-Click Shortcut**

1. Expand `lib/` in Project Explorer
2. Right-click the `.jar` file
3. Select **"Add as Library..."** → click **OK**

### Step 4 — Verify the Connector Was Added

In Project Explorer, expand **"External Libraries"** at the bottom:
```
External Libraries
└── mariadb-java-client-3.x.x   ✅  ← Should appear here
```

---

## 🗄️ Database & Table Setup in MariaDB

Before running the Java code, you need MariaDB running and the `jdbc` database with a `student` table ready.

### Start MariaDB Service

**Windows:**
```cmd
net start mariadb
```

**Linux:**
```bash
sudo systemctl start mariadb
```

### Create Database, User & Table

```sql
-- Log in as root
mysql -u root -p

-- Create the database used in this project
CREATE DATABASE IF NOT EXISTS jdbc;
USE jdbc;

-- Create a dedicated user (matching the credentials in the code)
CREATE USER IF NOT EXISTS 'devuser'@'localhost' IDENTIFIED BY 'devuser';
GRANT ALL PRIVILEGES ON jdbc.* TO 'devuser'@'localhost';
FLUSH PRIVILEGES;

-- Create the student table
CREATE TABLE IF NOT EXISTS student (
    id   INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL
);

-- Insert sample data
INSERT INTO student (name) VALUES
('Gopal Tayade'),
('Rahul Sharma'),
('Priya Desai');

-- Verify
SELECT * FROM student;
```

Expected output:
```
+----+--------------+
| id | name         |
+----+--------------+
|  1 | Gopal Tayade |
|  2 | Rahul Sharma |
|  3 | Priya Desai  |
+----+--------------+
```

---

## 💻 The Connection Code — Explained Line by Line

This is the actual code written in this project. It demonstrates **loading the MariaDB driver**, **establishing a connection**, **executing a SELECT query**, and **printing results** from the `student` table.

```java
/* Program to demonstrate the database connection and the fetching of the data from the database */
import java.sql.*;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {

        // ── Step 1: Load the MariaDB JDBC Driver ──────────────────────────────
        // The driver class name for MariaDB Connector/J
        // Class.forName() loads this class into memory, which triggers its
        // static initializer block — that block registers the driver with DriverManager
        String driver_load = "org.mariadb.jdbc.Driver";
        try {
            Class.forName(driver_load);
            System.out.println("-----Driver Loaded Successfully -----");
        }
        catch (ClassNotFoundException e) {
            // Thrown if the .jar is not added to the classpath
            System.out.println("-----Driver Load Issue -----");
        }

        // ── Step 2: Define Connection Parameters ──────────────────────────────
        // URL format → jdbc:mariadb://<host>:<port>/<database_name>
        // "jdbc"     = JDBC protocol prefix
        // "mariadb"  = tells DriverManager to use the MariaDB driver
        // "localhost" = DB server is on the same machine
        // "3306"     = default port for MariaDB
        // "jdbc"     = the database name created in MariaDB
        String url      = "jdbc:mariadb://localhost:3306/jdbc";
        String user     = "devuser";    // MariaDB username
        String password = "devuser";    // MariaDB password
        String query    = "select * from student";   // SQL to execute

        // ── Step 3: Connect, Query and Read Results ────────────────────────────
        try {
            // DriverManager uses the URL to find the right registered driver
            // and opens a live TCP connection to MariaDB on port 3306
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("------Connection Successful -------");

            // Statement is used to send static SQL queries to the database
            Statement stmt = conn.createStatement();

            // executeQuery() sends the SELECT to MariaDB and returns a ResultSet
            // ResultSet holds all matching rows in memory, with a cursor before row 1
            ResultSet rs = stmt.executeQuery(query);

            // rs.next() moves the cursor forward one row and returns true if a row exists
            // Loop runs once per row until no more rows remain
            while (rs.next()) {
                // Read the "id" column of the current row as an int
                int id = rs.getInt("id");

                // Read the "name" column of the current row as a String
                String name = rs.getString("name");

                System.out.print(id + "  ");
                System.out.print(name);
                System.out.println();
            }
        }
        catch (Exception e) {
            // Catches SQLException and any other runtime exception
            System.out.println("------Something gets Wrong -----");
        }
    }
}
```

### 📤 Expected Output

```
-----Driver Loaded Successfully -----
------Connection Successful -------
1  Gopal Tayade
2  Rahul Sharma
3  Priya Desai
```

### 🔍 Code Flow Summary

```
START
  │
  ├─► Class.forName("org.mariadb.jdbc.Driver")
  │         └─► MariaDB driver registers itself with DriverManager
  │
  ├─► DriverManager.getConnection(url, user, password)
  │         └─► Opens TCP connection to MariaDB on localhost:3306
  │         └─► Authenticates with devuser / devuser
  │         └─► Returns a live Connection object
  │
  ├─► conn.createStatement()
  │         └─► Creates a Statement to send SQL
  │
  ├─► stmt.executeQuery("select * from student")
  │         └─► Sends SQL to MariaDB
  │         └─► Returns a ResultSet with all rows
  │
  └─► while(rs.next())
            └─► Reads id and name column from each row
            └─► Prints them to console
END
```

---

## 🗂️ Project Structure

```
JDBC--java_database_connection-/
│
├── 📁 .idea/                          ← IntelliJ IDEA project config (auto-generated)
│
├── 📁 lib/
│   └── 📄 mariadb-java-client-3.x.x.jar   ← Type 4 MariaDB JDBC Driver
│
├── 📁 src/
│   └── 📄 Main.java                   ← MariaDB connection + data fetch demo
│
├── 📄 db_conn.iml                     ← IntelliJ module file
└── 📄 .gitignore
```

---

## ⚠️ Common Errors & Fixes

| # | ❌ Error / Output | 🔍 Root Cause | ✅ Solution |
|---|------------------|--------------|------------|
| 1 | `-----Driver Load Issue -----` | `.jar` not added to classpath | Add `mariadb-java-client.jar` via **Project Structure → Dependencies** |
| 2 | `------Something gets Wrong -----` (after driver loads) | MariaDB not running | Start MariaDB: `net start mariadb` or `sudo systemctl start mariadb` |
| 3 | `------Something gets Wrong -----` | Wrong username or password | Verify `devuser` / `devuser` credentials exist in MariaDB |
| 4 | `------Something gets Wrong -----` | Database `jdbc` doesn't exist | Run `CREATE DATABASE jdbc;` in MariaDB shell |
| 5 | `------Something gets Wrong -----` | Table `student` doesn't exist | Run the `CREATE TABLE student` script above |
| 6 | `------Something gets Wrong -----` | Wrong port or host in URL | Verify MariaDB is on `localhost:3306` |
| 7 | `ClassNotFoundException` at runtime | Driver class name typo | Ensure it is `org.mariadb.jdbc.Driver` exactly |

> 💡 **Pro tip:** Replace the generic `catch(Exception e)` with `catch(SQLException e) { e.printStackTrace(); }` during development — this prints the actual error message so you know exactly what went wrong instead of just seeing `"Something gets Wrong"`.

---

## 🔑 JDBC Quick Reference Cheat Sheet

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  JDBC CONNECTION FLOW (as used in this project)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  1. Load Driver     →  Class.forName("org.mariadb.jdbc.Driver")
  2. Get Connection  →  DriverManager.getConnection(url, user, pass)
  3. Create Statement→  conn.createStatement()
  4. Execute Query   →  stmt.executeQuery("select * from student")
  5. Read Results    →  while(rs.next()) { rs.getInt("id"); rs.getString("name"); }
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  MARIADB CONNECTION URL BREAKDOWN
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  jdbc:mariadb://localhost:3306/jdbc
   ↑      ↑          ↑       ↑    ↑
  JDBC  driver     server   port  database
  prefix  type      host         name

  STATEMENT TYPES
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Statement          → Static SQL, no parameters
  PreparedStatement  → Parameterized SQL using ?  ← Safer
  CallableStatement  → Stored procedures

  DRIVER TYPES SUMMARY
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Type 1 → JDBC-ODBC Bridge      (Deprecated ⛔)
  Type 2 → Native API            (Avoid      ⚠️)
  Type 3 → Network Protocol      (Rare       ⚠️)
  Type 4 → Pure Java / Thin      (USE THIS   ✅)  ← MariaDB Connector/J
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

