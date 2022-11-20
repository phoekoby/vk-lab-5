package org.example.service;

import org.example.dao.InvoiceDAO;
import org.example.dao.InvoicePositionDAO;
import org.example.dao.OrganizationDAO;
import org.example.dao.ProductDAO;
import org.example.entity.InvoiceDTO;
import org.example.entity.InvoicePositionDTO;
import org.example.entity.OrganizationDTO;
import org.example.entity.ProductDTO;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.sql.Date;
import java.util.List;
import java.util.Map;

public class ManagementService {
    private final InvoiceDAO invoiceDAO;
    private final InvoicePositionDAO invoicePositionDAO;
    private final OrganizationDAO organizationDAO;
    private final ProductDAO productDAO;

    @Inject
    public ManagementService(@NotNull InvoiceDAO invoiceDAO,
                             @NotNull InvoicePositionDAO invoicePositionDAO,
                             @NotNull OrganizationDAO organizationDAO,
                             @NotNull ProductDAO productDAO) {
        this.invoiceDAO = invoiceDAO;
        this.invoicePositionDAO = invoicePositionDAO;
        this.organizationDAO = organizationDAO;
        this.productDAO = productDAO;
    }

    public void printAllProducts() {
        List<ProductDTO> productDTOS = productDAO
                .getAll()
                .stream()
                .toList();
        System.out.println("---------- All Products ----------");
        productDTOS.forEach(System.out::println);
        System.out.println("----------------------------------");
    }

    public void printAllInvoices() {
        List<InvoiceDTO> invoiceDTOS = invoiceDAO
                .getAll()
                .stream()
                .toList();
        System.out.println("---------- All Invoices ----------");
        invoiceDTOS.forEach(System.out::println);
        System.out.println("----------------------------------");
    }

    public void printAllInvoicePositions() {
        List<InvoicePositionDTO> invoicePositionDTOS = invoicePositionDAO
                .getAll()
                .stream()
                .toList();
        System.out.println("---------- All Invoice Positions ----------");
        invoicePositionDTOS.forEach(System.out::println);
        System.out.println("-------------------------------------------");
    }

    public void printAllOrganizations() {
        List<OrganizationDTO> organizationDTOS = organizationDAO
                .getAll()
                .stream()
                .toList();
        System.out.println("---------- All Organizations ----------");
        organizationDTOS.forEach(System.out::println);
        System.out.println("---------------------------------------");
    }

    public void printFirst10Organizations(Long productId) {
        System.out.println("Выбрать первые 10 поставщиков по количеству поставленного товара: ");
        organizationDAO
                .findFirst10OrganizationsByProduct(productId)
                .forEach(System.out::println);
        System.out.println("-----------------------------------------------------------");
    }

    public void printWithAmountProductMoreThan(Map<Long, Integer> map) {
        System.out.println("Выбрать поставщиков с количеством поставленного товара выше указанного значения (товар и его количество должны допускать множественное указание): ");
        organizationDAO
                .findOrganizationAmountProductMoreThanValue(map)
                .forEach(System.out::println);
        System.out.println("-----------------------------------------------------------");
    }

    public void printAmountAndSumOfProductInPeriod(Date floor, Date roof){
        System.out.println("За каждый день для каждого товара рассчитать количество и сумму полученного товара в указанном периоде, посчитать итоги за период: ");
        var res = productDAO
                .getAmountAndSumPerDayAndResultForPeriod(floor, roof);
        System.out.println("По дням: ");
        res
                .getProductsPerDay()
                .forEach((date, value) -> value
                        .forEach((key, value1) -> System.out.println(date + ": " + key + " "
                                + "amount(" + value1.getAmount() + ") sum(" +
                                value1.getSum() + ")")));
        System.out.println("За период: ");
        res.getAmountSumResultForPeriod()
                .forEach(((product, amountSumDTO) -> System.out.println(product + ": amount(" +
                        amountSumDTO.getAmount() + ") sum(" + amountSumDTO.getSum() + ")")));
        System.out.println("-----------------------------------------------------------");
    }

    public void printAveragePriceInPeriod(Date floor, Date roof){
        System.out.println("Рассчитать среднюю цену по каждому товару за период: ");
        productDAO
                .getAveragePriceForProductsInPeriod(floor,roof)
                .forEach((product, aDouble) -> System.out.println(product + " average price: " + aDouble));
        System.out.println("-----------------------------------------------------------");
    }
    public void printAllDeliveredProductsByOrganizationsInPeriod(Date floor, Date roof){
        System.out.println("Вывести список товаров, поставленных организациями за период. Если организация товары не поставляла, то она все равно должна быть отражена в списке: ");
        productDAO
                .getDeliveredProductsByOrganizationInPeriod(floor, roof)
                .forEach((organization, products) -> System.out.println(organization + " " + products));
    }
}
