package com.faraone.sequratest;

import com.faraone.sequratest.model.Merchant;
import com.faraone.sequratest.model.Order;
import com.faraone.sequratest.model.Shopper;
import com.faraone.sequratest.repository.MerchantRepository;
import com.faraone.sequratest.repository.OrderRepository;
import com.faraone.sequratest.repository.ShopperRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@Component
public class BaselineSetup {

    private static Logger LOG = LoggerFactory.getLogger(BaselineSetup.class);

    @Autowired
    ObjectMapper objectMapper;

    private Container merchantsContainer;
    private Container orderContainer;
    private Container shoppersContainer;

    @Autowired
    MerchantRepository merchantRepository;
    @Autowired
    ShopperRepository shopperRepository;
    @Autowired
    OrderRepository orderRepository;

    @Value("classpath:merchants.json")
    File merchantsFile;

    @Value("classpath:orders.json")
    File ordersFile;

    @Value("classpath:shoppers.json")
    File shoppersFile;

    @SneakyThrows(IOException.class)
    public void init() {

        LOG.info("Initializing test data");

        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);


        merchantsContainer = objectMapper.readValue(merchantsFile, Container.class);
        orderContainer = objectMapper.readValue(ordersFile, Container.class);
        shoppersContainer = objectMapper.readValue(shoppersFile, Container.class);

        final List<Merchant> merchants = new ArrayList<>();
        final List<Shopper> shoppers = new ArrayList<>();
        final List<Order> orders = new ArrayList<>();

        merchantsContainer.RECORDS.forEach(ms -> {

            MerchantPojo m;
            try {
                m = objectMapper.readValue(objectMapper.writeValueAsString(ms), MerchantPojo.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            Merchant merchant = new Merchant();
            merchant.setCif(m.cif);
            merchant.setName(m.name);
            merchant.setEmail(m.email);
            merchant.setId(Long.valueOf(m.id));
            merchants.add(merchant);
        });
        merchantRepository.saveAll(merchants);

        shoppersContainer.RECORDS.forEach(sp -> {
            ShopperPojo s = new ShopperPojo();
            try {
                s = objectMapper.readValue(objectMapper.writeValueAsString(sp), ShopperPojo.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            Shopper shopper = new Shopper();
            shopper.setId(Long.valueOf(s.id));
            shopper.setName(s.name);
            shopper.setEmail(s.email);
            shopper.setNif(s.nif);
            shoppers.add(shopper);
        });
        shopperRepository.saveAll(shoppers);

        orderContainer.RECORDS.forEach(or -> {
            OrderPojo o = new OrderPojo();
            try {
                o = objectMapper.readValue(objectMapper.writeValueAsString(or), OrderPojo.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            Order order = new Order();
            order.setId(Long.valueOf(o.id));
            OrderPojo finalO = o;
            order.setMerchant(merchants.stream().filter(m -> m.getId().equals(Long.valueOf(finalO.merchant_id))).findFirst().get());
            order.setShopper(shoppers.stream().filter(s -> s.getId().equals(Long.valueOf(finalO.shopper_id))).findFirst().get());
            order.setAmount(BigDecimal.valueOf(Double.parseDouble(o.amount)));

            //20/03/2018 13:48:00
            DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withLocale(Locale.ITALY);  // Specify locale to determine human language and cultural norms used in translating that input string.
            order.setCreatedAt(LocalDateTime.parse(o.created_at, f).toInstant(ZoneOffset.UTC));

            if (!StringUtils.isBlank(o.completed_at)) {
                order.setCompletedAt(LocalDateTime.parse(o.completed_at, f).toInstant(ZoneOffset.UTC));
                order.setStatus(Order.Status.COMPLETED);
            } else {
                order.setStatus(Order.Status.PENDING);
            }
            orders.add(order);
        });

        orderRepository.saveAll(orders);

        LOG.info("Test data initialized");
    }


    public static class Container<T> {
        public List<T> RECORDS;
    }

    public static class MerchantPojo {

        public String id; //fixme there is a known bug in Jackson, it maps all the fields to String firstly without
        // trying to type cast it, I've written a fix for this but I cannot share it on this project, due to this I will
        // explicitly type cast the fields to the correct type (this is also a more secure and controlled way to do it)
        public String name;
        public String email;
        public String cif;

        public MerchantPojo() {
        }
    }

    public static class OrderPojo {

        public String id;
        public String merchant_id;
        public String shopper_id;
        public String amount;
        public String created_at;
        public String completed_at;

        public OrderPojo() {
        }
    }

    public static class ShopperPojo {

        public String id;
        public String name;
        public String email;
        public String nif;

        public ShopperPojo() {
        }
    }
}
