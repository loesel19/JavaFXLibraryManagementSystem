package model;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
public class BookDAO {
    Connection connection = null;

    public BookDAO(String url) throws SQLException {
        //copied from userdao
        connection = DriverManager.getConnection(url);
        System.out.println("Connection established!");
    }
    public void insert(Book b) throws SQLException {
//copied from userdao
        PreparedStatement preparedStatement = connection.prepareStatement("insert into books(name,author,publisher,genre,ISBN,year) values(?,?,?,?,?,?)");
        preparedStatement.setString(1, b.getName());
        preparedStatement.setString(2,b.getAuthor());
        preparedStatement.setString(3,b.getPublisher());
        preparedStatement.setString(4,b.getGenre());
        preparedStatement.setString(5,b.getISBN());
        preparedStatement.setLong(6,b.getYear());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
    public void delete(Book b) throws SQLException {
        //copied from userdao
        PreparedStatement preparedStatement = connection.prepareStatement("delete from books where ID = ?");
        preparedStatement.setInt(1,b.getID());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
    public void update(Book b) throws SQLException {
        //copied from userDAO
        PreparedStatement preparedStatement = connection.prepareStatement("update books set name=?, author=?, publisher=?, genre=?, ISBN=?, year=? where ID = ?");
        preparedStatement.setString(1,b.getName());
        preparedStatement.setString(2,b.getAuthor());
        preparedStatement.setString(3,b.getPublisher());
        preparedStatement.setString(4,b.getGenre());
        preparedStatement.setString(5, b.getISBN());
        preparedStatement.setLong(6, b.getYear());
        preparedStatement.setInt(7, b.getID());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
    public ArrayList<Book> getAll() throws SQLException {
        //copied from userDAO
        PreparedStatement preparedStatement = connection.prepareStatement("select * from books");
        ArrayList<Book> arr = new ArrayList<>();

        ResultSet rs = preparedStatement.executeQuery();

        while(rs.next()){

            Book temp = new Book(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4), rs.getString(5), rs.getString(6),rs.getLong(7));
            arr.add(temp);
        }
preparedStatement.close();
        return arr;

    }
    public ArrayList<Book> getByQuery(String s) throws SQLException {
        //copied from userdao
        //create arrayList
        ArrayList<Book> matches = new ArrayList<>();
        //now query the user table against id, name, author,publisher, genre, isbn, year
        PreparedStatement preparedStatement =
                connection.prepareStatement("select * from books where id like ? OR name like ? OR author like ? OR publisher like ? OR genre like ? OR isbn like ? OR year = ?");
        //now we add in the query arguments, first we will trim the query string
        s = s.trim();
        //regex to check if the string is an int
        if (s.matches("[0-9]+")) {
            preparedStatement.setInt(1, Integer.parseInt(s));
            //also do the long here for year
            preparedStatement.setLong(7, Long.parseLong(s));
        }
        else {//do -1 if the input is not an integer so that we do not match any books here
            preparedStatement.setInt(1, -1);
            preparedStatement.setLong(7, -1);
        }
        //to use wildcards with strings we have to add in the wildcards here
        preparedStatement.setString(2,"%"+s+"%");
        preparedStatement.setString(3,"%"+s+"%");
        preparedStatement.setString(4,"%"+s+"%");
        preparedStatement.setString(5,"%"+s+"%");
        preparedStatement.setString(6,"%"+s+"%");

        ResultSet rs = preparedStatement.executeQuery();
        //now add all results from the resultset into an arraylist
        while(rs.next()){
            Book temp = new Book(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4), rs.getString(5), rs.getString(6),rs.getLong(7));
            matches.add(temp);
        }
//return arraylist
        preparedStatement.close();
        return matches;
    }
    public Book getBook(int id) throws SQLException {
        //copied from userdao
        //just query against the user list on the id
        PreparedStatement preparedStatement = connection.prepareStatement("select * from books where id = ?");
        preparedStatement.setInt(1,id);
        ResultSet rs = preparedStatement.executeQuery();

        //make sure we have a result before trying to create a user
        //not doing this in a while loop because we will only have 1 user with a given id since it is an autoincrement index
        if (rs.next()){
            Book b = new Book(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4), rs.getString(5), rs.getString(6),rs.getLong(7));
            preparedStatement.close();
            return b;
        }else
            preparedStatement.close();
            return null;
    }
}
