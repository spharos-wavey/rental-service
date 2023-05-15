package xyz.wavey.rentalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.wavey.rentalservice.model.Insurance;

public interface InsuranceRepo extends JpaRepository<Insurance, Integer> {
}
