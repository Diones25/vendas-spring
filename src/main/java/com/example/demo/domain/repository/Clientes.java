package com.example.demo.domain.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.entity.Cliente;

@Repository
public class Clientes {

  private static String INSERT = "INSERT INTO CLIENTE (nome) VALUES (?)";
  private static String SELECT_ALL = "SELECT * FROM CLIENTE";
  private static String UPDATE = "UPDATE CLIENTE SET nome = ? WHERE id = ?";
  private static String DELETE = "DELETE FROM CLIENTE WHERE id = ?";
  private static String SELECT_POR_NOME = "SELECT * FROM CLIENTE WHERE nome like ?";

  @Autowired
  private JdbcTemplate jdbcTemplate;
  
  public Cliente salvar(Cliente cliente) {
    jdbcTemplate.update(INSERT, new Object[] { cliente.getNome() });
    return cliente;
  }

  public Cliente autalizar(Cliente cliente) {
    jdbcTemplate.update(UPDATE, new Object[] {
        cliente.getNome(),
        cliente.getId()
    });

    return cliente;
  }
  
  public void deletar(Integer id) {
    jdbcTemplate.update(DELETE, new Object[] { id });
  }

  @SuppressWarnings("deprecation")
  public List<Cliente> buscarPorNome(String nome) {
    return jdbcTemplate.query(
            SELECT_POR_NOME,
            new Object[] { "%" + nome + "%"},
            obterClienteMapper());
  }

  public List<Cliente> obterTodos() {
    return jdbcTemplate.query(SELECT_ALL, obterClienteMapper());
  }
  
  private RowMapper<Cliente> obterClienteMapper() {
    return new RowMapper<Cliente>() {
      @Override
      public Cliente mapRow(ResultSet resultSet, int i) throws SQLException {
        Integer id = resultSet.getInt("id");
        String nome = resultSet.getString("nome");

        return new Cliente(id, nome);
      }
    };
  }
}
