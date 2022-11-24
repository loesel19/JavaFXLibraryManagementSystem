package model;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
public class TransactionDAO {
    Connection connection = null;

    public TransactionDAO(String url) throws SQLException {
        connection = DriverManager.getConnection(url);
        System.out.println("Connection established!");
    }

    public void insert(Transaction t) throws SQLException {
//copied from userDAO
        PreparedStatement preparedStatement = connection.prepareStatement("insert into libTransactions values(?,?,?,?)");
        preparedStatement.setInt(1, t.getBookID());
        preparedStatement.setInt(2, t.getUserID());
        //we have to convert our LocalDate to sql.Date
        java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
        sqlDate = java.sql.Date.valueOf(t.getIssueDate());
        preparedStatement.setDate(3, sqlDate);
        preparedStatement.setBoolean(4, t.isStatus());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void delete(Transaction t) throws SQLException {
        //copied from userdao
        PreparedStatement preparedStatement = connection.prepareStatement("delete from libTransactions where bookID = ? AND userID = ?");
        preparedStatement.setInt(1, t.getBookID());
        preparedStatement.setInt(2, t.getUserID());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void update(Transaction t) throws SQLException {
        //copied from userdao
        PreparedStatement preparedStatement = connection.prepareStatement("update libTransactions set bookID =?, userID = ?, issueDate =?, status =?  where bookID = ? AND userID = ?");
        preparedStatement.setInt(1, t.getBookID());
        preparedStatement.setInt(2, t.getUserID());
        //we have to convert our LocalDate to sql.Date
        java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
        sqlDate = java.sql.Date.valueOf(t.getIssueDate());
        preparedStatement.setDate(3, sqlDate);
        preparedStatement.setBoolean(4, t.isStatus());
        preparedStatement.setInt(5, t.getBookID());
        preparedStatement.setInt(6, t.getUserID());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public ArrayList<Transaction> getAll() throws SQLException {
        //copied from userdao
        PreparedStatement preparedStatement = connection.prepareStatement("select * from libTransactions");
        ArrayList<Transaction> arr = new ArrayList<>();

        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            //before we create the new user object we need to convert the sqlDate to localDate
            LocalDate date = rs.getDate(3).toLocalDate();
            Transaction temp = new Transaction(rs.getInt(1), rs.getInt(2), date, rs.getBoolean(4));
            arr.add(temp);
        }
preparedStatement.close();
        return arr;

    }

    public ArrayList<Transaction> getCurrent() throws SQLException {
        //copied from getAll method
        PreparedStatement preparedStatement = connection.prepareStatement("select * from libTransactions where status =?");
        //just query where status = true
        ArrayList<Transaction> arr = new ArrayList<>();
        preparedStatement.setBoolean(1, true);

        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            //before we create the new user object we need to convert the sqlDate to localDate
            LocalDate date = rs.getDate(3).toLocalDate();
            Transaction temp = new Transaction(rs.getInt(1), rs.getInt(2), date, rs.getBoolean(4));
           System.out.println(temp.toString());
            arr.add(temp);
        }
preparedStatement.close();
        return arr;

    }

    public ArrayList<Transaction> getByUser(int id) throws SQLException {
        //copied from getAll method
        PreparedStatement preparedStatement = connection.prepareStatement("select * from libTransactions where userID =?");
        //just query where userID = input id
        ArrayList<Transaction> arr = new ArrayList<>();
        preparedStatement.setInt(1, id);

        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            //before we create the new user object we need to convert the sqlDate to localDate
            LocalDate date = rs.getDate(3).toLocalDate();
            Transaction temp = new Transaction(rs.getInt(1), rs.getInt(2), date, rs.getBoolean(4));
            arr.add(temp);
        }
preparedStatement.close();
        return arr;

    }
    public ArrayList<Transaction> getByBook(int id) throws SQLException {
        //copied from getAll method
        PreparedStatement preparedStatement = connection.prepareStatement("select * from libTransactions where bookID =?");
        //just query where bookID = input id
        ArrayList<Transaction> arr = new ArrayList<>();
        preparedStatement.setInt(1,id);

        ResultSet rs = preparedStatement.executeQuery();

        while(rs.next()){
            //before we create the new user object we need to convert the sqlDate to localDate
            LocalDate date = rs.getDate(3).toLocalDate();
            Transaction temp = new Transaction(rs.getInt(1),rs.getInt(2),date,rs.getBoolean(4));
            arr.add(temp);
        }
preparedStatement.close();
        return arr;

    }
}
