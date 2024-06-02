package br.com.alura.bytebank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public Connection reperarConexao(){
        try {

            return  DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/byte_bank?user=root&password=963852741");
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public static void main(String... x) {

    }
}
