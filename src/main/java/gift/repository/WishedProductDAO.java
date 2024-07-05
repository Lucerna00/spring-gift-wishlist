package gift.repository;

import gift.dto.WishedProductDTO;
import java.util.Collection;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class WishedProductDAO {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public WishedProductDAO(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("WISHED_PRODUCT");
    }

    public Collection<WishedProductDTO> getWishedProducts(String memberEmail) {
        var sql = "SELECT * FROM WISHED_PRODUCT WHERE member_email = ?";
        return jdbcTemplate.query(sql, wishedProductRowMapper(), memberEmail);
    }

    private RowMapper<WishedProductDTO> wishedProductRowMapper() {
        return (resultSet, rowNum) -> new WishedProductDTO(
            resultSet.getString("member_email"),
            resultSet.getLong("product_id"),
            resultSet.getInt("amount"));
    }

    public void addWishedProduct(WishedProductDTO wishedProductDTO) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(wishedProductDTO);
        simpleJdbcInsert.execute(parameters);
    }
}