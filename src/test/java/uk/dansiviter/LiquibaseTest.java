package uk.dansiviter;

import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.sql.DataSource;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.HelidonTest;
import liquibase.integration.jakarta.cdi.CDILiquibase;
import liquibase.integration.jakarta.cdi.CDILiquibaseConfig;
import liquibase.integration.jakarta.cdi.annotations.LiquibaseType;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;

@HelidonTest
class LiquibaseTest {
	@Inject
	CDILiquibase liquibase;

	@Test
	void isInitialized() {
		assertTrue(liquibase.isInitialized());
	}

	@Produces
	@LiquibaseType
	public CDILiquibaseConfig createConfig() {
			CDILiquibaseConfig config = new CDILiquibaseConfig();
			config.setChangeLog("changelog.sql");
			boolean configShouldRun = Boolean.valueOf(System.getProperty("liquibase.config.shouldRun", "true"));
			config.setShouldRun(configShouldRun);
			config.setDropFirst(true);
			return config;
	}

	@Produces
	@LiquibaseType
	public DataSource createDataSource() {
			JdbcDataSource ds = new JdbcDataSource();
			ds.setURL("jdbc:h2:mem:test");
			ds.setUser("sa");
			ds.setPassword("sa");
			return ds;
	}

	@Produces
	@LiquibaseType
	public ResourceAccessor create() {
			return new ClassLoaderResourceAccessor(getClass().getClassLoader());
	}


}
