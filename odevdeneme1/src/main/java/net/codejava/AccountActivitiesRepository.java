package net.codejava;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountActivitiesRepository extends JpaRepository<account_activities, Long> {

	@Query("select a FROM account_activities a where account_id = ?1")
	public List<account_activities> search(Long long1);

}
