package net.codejava;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyEquivalentRepository extends JpaRepository<currency_equivalent, Long> {

	@Query("select a FROM currency_equivalent a where Date = ?1")
	public List<currency_equivalent> search(String keyword);
	
	@Query("select a FROM currency_equivalent a where Date = ?1")
	public List<currency_equivalent> search(Date keyword);

}
