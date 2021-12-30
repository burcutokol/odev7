package net.codejava;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<account, Long> {

	@Query("select a FROM account a where currency = ?1")
	public List<account> search(String keyword);



}
