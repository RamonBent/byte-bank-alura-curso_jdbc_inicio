/*Existe um padrão muito utilizado para definir essa parte de banco de dados. Ele é conhecido como Data access object (DAO),
no português, objeto de acesso a dados. Como ele dá acesso ao objeto externo, encapsulamos em uma classe DAO e deixamos o banco de dados nele.*/

package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.domain.cliente.Cliente;
import br.com.alura.bytebank.domain.cliente.DadosCadastroCliente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ContaDAO {
    private Connection conn;
    ContaDAO(Connection connection){
        this.conn = connection;
    }
    public void salvar(DadosAberturaConta dadosDaConta){
        var cliente = new Cliente(dadosDaConta.dadosCliente());
        var conta = new Conta(dadosDaConta.numero(), cliente);

        //Clausula
        String sql = "INSERT INTO conta (numero, saldo, nome, cpf, email)" + "VALUES (?, ?, ?, ?, ?)";


        try {
            PreparedStatement preparedStatement =  conn.prepareStatement(sql);
            /*usado para executar consultas SQL com parâmetros, oferecendo vantagens significativas em termos de segurança,
            eficiência e facilidade de uso.*/
            preparedStatement.setInt(1, conta.getNumero());
            preparedStatement.setBigDecimal(2, BigDecimal.ZERO);
            preparedStatement.setString(3, dadosDaConta.dadosCliente().nome());
            preparedStatement.setString(4, dadosDaConta.dadosCliente().cpf());
            preparedStatement.setString(5, dadosDaConta.dadosCliente().email());

            preparedStatement.execute();
            //Conexão deve ser fechada para não dar problemas
            preparedStatement.close();
            conn.close();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Set<Conta> listar(){
        PreparedStatement ps;
        ResultSet resultSet;

        Set<Conta> contas = new HashSet<>();
        String sql = "SELECT * FROM conta";
        try {
            ps = conn.prepareStatement(sql);
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Integer numero = resultSet.getInt(1);
                BigDecimal saldo = resultSet.getBigDecimal(2);
                String nome = resultSet.getString(3);
                String cpf = resultSet.getString(4);
                String email = resultSet.getString(5);

                DadosCadastroCliente dadosCadastroCliente =
                        new DadosCadastroCliente(nome, cpf, email);
                Cliente cliente = new Cliente(dadosCadastroCliente);

                contas.add(new Conta(numero, cliente));

                resultSet.close();
                ps.close();
                conn.close();
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return contas;
    }
}
