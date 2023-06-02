package xyz.wavey.rentalservice.base.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import xyz.wavey.rentalservice.model.DatabaseType;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@EnableTransactionManagement
public class RoutingDataSourceConfiguration {
    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource(){
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }
    @Bean(name = "slaveDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slaveDataSource(){
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }


    @Bean(name = "routingDataSource")
    public DataSource routingDataSource(
            @Qualifier(value = "masterDataSource") DataSource masterDataSource,
            @Qualifier(value = "slaveDataSource") DataSource slaveDataSource
    ){
        AbstractRoutingDataSource routingDataSource = new DynamicRoutingDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DatabaseType.MASTER, masterDataSource);
        targetDataSources.put(DatabaseType.SLAVE, slaveDataSource);
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);
        return routingDataSource;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        return new LazyConnectionDataSourceProxy(routingDataSource(masterDataSource(), slaveDataSource()));
    }

}
