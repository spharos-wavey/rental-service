package xyz.wavey.rentalservice.insurance.ropository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.wavey.rentalservice.insurance.model.Insurance;

public interface InsuranceRepo extends JpaRepository<Insurance, Integer> {
}
