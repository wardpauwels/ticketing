package be.ward.ticketing.data.tenant;

import be.ward.ticketing.entities.tenants.Tenant;
import org.springframework.data.repository.CrudRepository;

public interface TenantDao extends CrudRepository<Tenant, String> {
}
