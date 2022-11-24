package model;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;


public class UserDAO {
    Connection connection = null;
//big thing that i missed in all daos was to close the prepared statements so that sqlite
    //will not have an open statement when we try to run another one
    public UserDAO(String url) throws SQLException {
        connection = DriverManager.getConnection(url);
        System.out.println("Connection established!");
    }

    public void insert(User u) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement("insert into users(name, email, address, dateOfBirth, isStudent, balance) values(?,?,?,?,?,?)");
        preparedStatement.setString(1, u.getName());
        preparedStatement.setString(2,u.getEmail());
        preparedStatement.setString(3,u.getAddress());
        //we have to convert our LocalDate to sql.Date
        java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
        sqlDate = java.sql.Date.valueOf(u.getDateOfBirth());
        preparedStatement.setDate(4,sqlDate);
        preparedStatement.setBoolean(5,u.getStudent());
        preparedStatement.setDouble(6,u.getBalance());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void delete(User u) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("delete from users where ID = ?");
        preparedStatement.setInt(1,u.getID());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void update(User u) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("update users set name=?, email=?, address=?, dateOfBirth=?, isStudent=? where ID = ?");
        preparedStatement.setString(1,u.getName());
        preparedStatement.setString(2,u.getEmail());
        preparedStatement.setString(3,u.getAddress());
        //we have to convert our LocalDate to sql.Date
        java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
        sqlDate = java.sql.Date.valueOf(u.getDateOfBirth());
        preparedStatement.setDate(4,sqlDate);
        preparedStatement.setBoolean(5, u.getStudent());
        preparedStatement.setInt(6, u.getID());
        preparedStatement.executeUpdate();
preparedStatement.close();
    }

    public ArrayList<User> getAll() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("select * from users");
        ArrayList<User> arr = new ArrayList<>();

        ResultSet rs = preparedStatement.executeQuery();

        while(rs.next()){
            //before we create the new user object we need to convert the sqlDate to localDate
            //Get SQL date instance

            java.sql.Date sqlDate = rs.getDate(5);

            //Get LocalDate from SQL date
            LocalDate localDate = sqlDate.toLocalDate();

            User temp = new User(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),localDate, rs.getBoolean(6),rs.getDouble(7));
            arr.add(temp);
        }
preparedStatement.close();
        return arr;

    }
    public ArrayList<User> getByQuery(String s) throws SQLException {
        //create arrayList
        ArrayList<User> matches = new ArrayList<>();
        //now query the user table agains id, name, email, and address
        PreparedStatement preparedStatement =
                connection.prepareStatement("select * from users where id like ? OR name like ? OR email like ?");
        //now we add in the query arguments, first we will trim the query string
        s = s.trim();
        //regex to check if the string is an int
        if (s.matches("[0-9]+"))
        preparedStatement.setInt(1,Integer.parseInt(s));
        else //do -1 if the input is not an integer so that we do not match any users here
            preparedStatement.setInt(1,-1);
        //add wildcard for strings
        preparedStatement.setString(2,"%"+s+"%");
        preparedStatement.setString(3,"%"+s+"%");
        ResultSet rs = preparedStatement.executeQuery();
        //now add all results from the resultset into an arraylist
        while(rs.next()){
            //before we create the new user object we need to convert the sqlDate to localDate
            LocalDate date = rs.getDate(5).toLocalDate();
            User temp = new User(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),date, rs.getBoolean(6),rs.getDouble(7));
            matches.add(temp);
        }
//return arraylist
        preparedStatement.close();
        return matches;
    }
    public User getUser(int id) throws SQLException {
        //just query against the user list on the id
        PreparedStatement preparedStatement = connection.prepareStatement("select * from users where id = ?");
        preparedStatement.setInt(1,id);
        ResultSet rs = preparedStatement.executeQuery();

        //make sure we have a result before trying to create a user
        //not doing this in a while loop because we will only have 1 user with a given id since it is an autoincrement index
        if (rs.next()){
            LocalDate date = rs.getDate(5).toLocalDate();
            User u = new User(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),date, rs.getBoolean(6),rs.getDouble(7));
            preparedStatement.close();
            return u;
        }else{
            preparedStatement.close();
            return null;
        }

    }
}
