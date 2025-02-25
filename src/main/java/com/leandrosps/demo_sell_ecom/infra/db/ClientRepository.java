package com.leandrosps.demo_sell_ecom.infra.db;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.leandrosps.demo_sell_ecom.domain.Client;
import com.leandrosps.demo_sell_ecom.infra.db.dbmodels.ClientDbModel;

@Repository
public interface ClientRepository extends ListCrudRepository<ClientDbModel, String>, ClientRepositoryCustom {
    Optional<ClientDbModel> findByFkEmail(String fk_email);
}

interface ClientRepositoryCustom {
    void persiste(Client client);
}

class ClientRepositoryCustomImpl implements ClientRepositoryCustom {

    private JdbcClient jdbcClient;

    public ClientRepositoryCustomImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void persiste(Client client) {
        var sql = """
                INSERT INTO
                    clients (id, name, fk_email, city, birthday)
                VALUES
                (:id, :name, :fk_email, :city, :birthday);
                    """;
        this.jdbcClient.sql(sql).param("id", client.getId().toString()).param("name", client.getName())
                .param("fk_email", client.getEmail()).param("city", client.getCity())
                .param("birthday", client.getBirthday()).update();
    }

}
