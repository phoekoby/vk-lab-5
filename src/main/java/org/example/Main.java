package org.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.example.config.ManagementModule;
import org.example.service.ManagementService;
import org.example.service.MigrationService;

import java.sql.Date;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ManagementModule());
        MigrationService migrationService = injector.getInstance(MigrationService.class);
        migrationService.createMigrations();
        ManagementService managementService = injector.getInstance(ManagementService.class);
        managementService.printAllProducts();
        managementService.printAllInvoices();
        managementService.printAllInvoicePositions();
        managementService.printAllOrganizations();
        managementService.printFirst10Organizations(1L);
        managementService.printWithAmountProductMoreThan(Map.of(1L, 50, 2L, 5));
        managementService.printWithAmountProductMoreThan(Map.of(1L, 9999));
        managementService.printAmountAndSumOfProductInPeriod(Date.valueOf("2022-11-09"), Date.valueOf("2022-11-12"));
        managementService.printAveragePriceInPeriod(Date.valueOf("2022-11-09"), Date.valueOf("2022-11-12"));
        managementService.printAllDeliveredProductsByOrganizationsInPeriod(Date.valueOf("2022-11-09"), Date.valueOf("2022-11-11"));
    }
}