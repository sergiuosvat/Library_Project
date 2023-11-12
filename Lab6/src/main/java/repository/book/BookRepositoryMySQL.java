package repository.book;

import model.AudioBook;
import model.Book;
import model.EBook;
import model.builder.AudioBookBuilder;
import model.builder.BookBuilder;
import model.builder.EBookBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMySQL implements BookRepository{
    private final Connection connection;

    public BookRepositoryMySQL(Connection connection){
        this.connection = connection;
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM book;";

        List<Book> books = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                books.add(getBookFromResultSet(resultSet));
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM book WHERE id = ?";

        Optional<Book> books_id = Optional.empty();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                books_id = Optional.of(getBookFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return books_id;
    }

    @Override
    public boolean save(Book book) {

        if(book instanceof EBook)
        {
            String sql = "INSERT INTO book VALUES(null, ?, ?, ?, ?, 0,?);";

            try{

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, book.getAuthor());
                preparedStatement.setString(2, book.getTitle());
                preparedStatement.setDate(3, java.sql.Date.valueOf(book.getPublishedDate()));
                preparedStatement.setString(4,((EBook)book).getFormat());
                preparedStatement.setString(5,"ebook");
                int rowsInserted = preparedStatement.executeUpdate();
                return rowsInserted == 1;

            } catch (SQLException e){
                e.printStackTrace();
                return false;
            }
        }else if (book instanceof AudioBook){
            String sql = "INSERT INTO book VALUES(null, ?, ?, ?, null, ?, ?);";

            try{

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, book.getAuthor());
                preparedStatement.setString(2, book.getTitle());
                preparedStatement.setDate(3, java.sql.Date.valueOf(book.getPublishedDate()));
                preparedStatement.setInt(4,((AudioBook)book).getRunTime());
                preparedStatement.setString(5,"audiobook");
                int rowsInserted = preparedStatement.executeUpdate();
                return rowsInserted == 1;

            } catch (SQLException e){
                e.printStackTrace();
                return false;
            }
        }else{
            String sql = "INSERT INTO book VALUES(null, ?, ?, ?,null,0,?);";

            try{

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, book.getAuthor());
                preparedStatement.setString(2, book.getTitle());
                preparedStatement.setDate(3, java.sql.Date.valueOf(book.getPublishedDate()));
                preparedStatement.setString(4,"book");
                int rowsInserted = preparedStatement.executeUpdate();

                return rowsInserted == 1;

            } catch (SQLException e){
                e.printStackTrace();
                return false;
            }
        }
    }

    @Override
    public boolean removeAll() {
        String sql = "TRUNCATE TABLE book";
        try{
            Statement statement = connection.createStatement();
            statement.execute(sql);
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }


    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException {
        String type = resultSet.getString("bookType");
        return switch (type) {
            case "ebook" -> new EBookBuilder()
                    .setFormat(resultSet.getString("format"))
                    .setId(resultSet.getLong("id"))
                    .setAuthor(resultSet.getString("author"))
                    .setTitle(resultSet.getString("title"))
                    .setPublishedDate(new Date((resultSet.getDate("publishedDate")).getTime()).toLocalDate())
                    .build();
            case "audiobook" -> new AudioBookBuilder()
                    .setRunTime(resultSet.getInt("runTime"))
                    .setId(resultSet.getLong("id"))
                    .setAuthor(resultSet.getString("author"))
                    .setTitle(resultSet.getString("title"))
                    .setPublishedDate(new Date((resultSet.getDate("publishedDate")).getTime()).toLocalDate())
                    .build();
            default -> new BookBuilder()
                    .setId(resultSet.getLong("id"))
                    .setAuthor(resultSet.getString("author"))
                    .setTitle(resultSet.getString("title"))
                    .setPublishedDate(new Date((resultSet.getDate("publishedDate")).getTime()).toLocalDate())
                    .build();
        };
    }
}
