package com.xforceplus.invoice.stresstest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taobao.stresstester.StressTestUtils;
import com.xforceplus.invoice.common.constant.ChannelSource;
import com.xforceplus.invoice.domain.entity.InvoiceSellerMain;
import com.xforceplus.invoice.transfer.TransferApplication;
import com.xforceplus.invoice.transfer.converter.EntityDataConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 发票转换器压力测试
 */
public class InvoiceConverterStressTests {
    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(TransferApplication.class, "--spring.profiles.active=unittest");
        EntityDataConverter entityDataConverter = applicationContext.getBean(EntityDataConverter.class);
        String text = StreamUtils.copyToString(new ClassPathResource("4-0-seller-data.txt").getInputStream(), Charset.defaultCharset());
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> originalRecord = mapper.readValue(text, HashMap.class);
        // 100是并发数，1000是请求次数
        StressTestUtils.testAndPrint(100, 1000, () ->
                entityDataConverter.convert(InvoiceSellerMain.class, originalRecord, ChannelSource.PHOENIX_SELLER));
        System.exit(0);
    }
}
