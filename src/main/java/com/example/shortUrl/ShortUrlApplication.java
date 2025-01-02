package com.example.shortUrl;

import com.example.shortUrl.model.UrlMapping;
import com.example.shortUrl.repository.UrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.lang.management.*;
import java.util.List;

@SpringBootApplication
@EnableCaching
public class ShortUrlApplication {
	private static final Logger log = LoggerFactory.getLogger(ShortUrlApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ShortUrlApplication.class, args);
		logJvmData();
	}

	private static void logJvmData() {
		Runtime runtime = Runtime.getRuntime();
		OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
		MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
		RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();

		int availableProcessors = runtime.availableProcessors();
		long freeMemory = runtime.freeMemory();
		long totalMemory = runtime.totalMemory();
		long maxMemory = runtime.maxMemory();
		double systemLoadAverage = osBean.getSystemLoadAverage();
		long uptime = runtimeBean.getUptime();

		MemoryUsage heapMemoryUsage = memoryBean.getHeapMemoryUsage();
		MemoryUsage nonHeapMemoryUsage = memoryBean.getNonHeapMemoryUsage();

		log.info("JVM Data:");
		log.info("Available processors: " + availableProcessors);
		log.info("Free memory: " + freeMemory / (1024 * 1024) + " MB");
		log.info("Total memory: " + totalMemory / (1024 * 1024) + " MB");
		log.info("Max memory: " + maxMemory / (1024 * 1024) + " MB");
		log.info("System load average: " + systemLoadAverage);
		log.info("Uptime: " + uptime / 1000 + " seconds");
		log.info("Init Heap memory usage: " + heapMemoryUsage.getInit() / (1024 * 1024) + " MB");
		log.info("Used Heap memory usage: " + heapMemoryUsage.getUsed() / (1024 * 1024) + " MB");
		log.info("Max Heap memory usage: " + heapMemoryUsage.getMax() / (1024 * 1024) + " MB");
		log.info("Init Non-Heap memory usage: " + nonHeapMemoryUsage.getInit() / (1024 * 1024) + " MB");
		log.info("Used Non-Heap memory usage: " + nonHeapMemoryUsage.getUsed() / (1024 * 1024) + " MB");
		log.info("Max Non-Heap memory usage: " + nonHeapMemoryUsage.getMax() / (1024 * 1024) + " MB");


		List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
		for (GarbageCollectorMXBean gcBean : gcBeans) {
			log.info("Garbage Collector: " + gcBean.getName());
		}
	}
}