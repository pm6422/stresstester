package com.xforceplus.invoice.stresstest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xforceplus.invoice.common.constant.ChannelSource;
import com.xforceplus.invoice.domain.entity.InvoiceSellerMain;
import com.xforceplus.invoice.transfer.TransferApplication;
import com.xforceplus.invoice.transfer.converter.EntityDataConverter;
import com.xforceplus.invoice.transfer.service.InvoiceSellerMainService;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 发票主信息保存性能测试
 */
@BenchmarkMode({Mode.Throughput, Mode.AverageTime})
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Slf4j
public class InvoiceMainInfoSavePerformanceTests {
    private ConfigurableApplicationContext applicationContext;
    private EntityDataConverter            entityDataConverter;
    private InvoiceSellerMainService       invoiceSellerMainService;
    private InvoiceSellerMain              record;

    /**
     * setup初始化容器的时候只执行一次
     */
    @Setup(Level.Trial)
    public void initialize() throws IOException {
        applicationContext = SpringApplication.run(TransferApplication.class, "--spring.profiles.active=unittest");
        entityDataConverter = applicationContext.getBean(EntityDataConverter.class);
        invoiceSellerMainService = applicationContext.getBean(InvoiceSellerMainService.class);

        String text = StreamUtils.copyToString(new ClassPathResource("4-0-seller-data.txt").getInputStream(), Charset.defaultCharset());
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> originalRecord = mapper.readValue(text, HashMap.class);
        record = entityDataConverter.convert(InvoiceSellerMain.class, originalRecord, ChannelSource.PHOENIX_SELLER);
    }

    @Benchmark
    public void benchMarkSaveMainInfo() {
        invoiceSellerMainService.save(record);
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(InvoiceMainInfoSavePerformanceTests.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(options).run();
    }
}
