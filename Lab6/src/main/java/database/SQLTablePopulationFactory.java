package database;

import static database.Constants.Tables.BOOK;

public class SQLTablePopulationFactory {
    public String getPopulateSQLForTable(String table){
        return switch (table){
            case BOOK -> "INSERT INTO book (author, title, publishedDate) VALUES " +
                    "('Author1', 'Title1', '2022-01-01')," +
                    "('Author2', 'Title2', '2022-02-15')," +
                    "('Author3', 'Title3', '2022-03-20')," +
                    "('Author4', 'Title4', '2022-04-10')," +
                    "('Author5', 'Title5', '2022-05-25');";
            default -> "";
        };

    }
}
