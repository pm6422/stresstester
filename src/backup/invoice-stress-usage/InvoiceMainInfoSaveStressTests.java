package com.xforceplus.invoice.stresstest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taobao.stresstester.StressTestUtils;
import com.xforceplus.invoice.common.constant.ChannelSource;
import com.xforceplus.invoice.domain.entity.InvoiceSellerMain;
import com.xforceplus.invoice.transfer.TransferApplication;
import com.xforceplus.invoice.transfer.converter.EntityDataConverter;
import com.xforceplus.invoice.transfer.service.InvoiceSellerMainService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 发票主信息保存压力测试
 */
public class InvoiceMainInfoSaveStressTests {
    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(TransferApplication.class, "--spring.profiles.active=unittest");
        EntityDataConverter entityDataConverter = applicationContext.getBean(EntityDataConverter.class);
        InvoiceSellerMainService invoiceSellerMainService = applicationContext.getBean(InvoiceSellerMainService.class);
        String text = StreamUtils.copyToString(new ClassPathResource("4-0-seller-data.txt").getInputStream(), Charset.defaultCharset());
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> originalRecord = mapper.readValue(text, HashMap.class);
        InvoiceSellerMain record = entityDataConverter.convert(InvoiceSellerMain.class, originalRecord, ChannelSource.PHOENIX_SELLER);
        // 2是并发数，10是请求次数
        StressTestUtils.testAndPrint(2, 10, () -> {
            return invoiceSellerMainService.save(record);
        });
        System.exit(0);
    }
}
