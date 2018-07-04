package f.p.f.core.repository;

import f.p.f.core.domain.Collectivite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectiviteRepository extends JpaRepository<Collectivite, Long> {
}
