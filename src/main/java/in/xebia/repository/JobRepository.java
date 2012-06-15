package in.xebia.repository;

import in.xebia.domain.JobData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<JobData, Long>{

}
