package pe.mayciel.fos.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 일반적인 repository 가 상속받는 클래스.<br>
 * 기본 sqlMapClient 를 사용하는 repository 는 상속받아 사용할 것.
 * @author Hwang Seong-wook
 * @since 2013. 01. 21.
 */
@Repository
public abstract class AbstractRepository extends SqlMapClientTemplate {
	@Autowired
	@Override
	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		super.setSqlMapClient(sqlMapClient);
	}
}