package com.basoft.service.util;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.basoft.service.enumerate.BusinessTypeEnum;

@Component
public class UidGenerator {
    // 区域ID最大值：7
    // private final static long maxRegionId = ~(-1L << 3);
    // 机器ID最大值：31
    // private final static long maxWorkerId = ~(-1L << 5);
    // 业务ID最大值：31
    // private final static long maxBusinessId = ~(-1L << 5);
    // 序列号最大值：1023
    private final static long sequenceMask = ~(-1L << 10);

	// 时间毫秒左移23位
	private final static long timestampShift = 23;
	// 业务ID左移18位
	private final static long businessIdShift = 18;
	// 区域ID左移15位
	private final static long regionIdShift = 15;
	// 机器ID左移10位
	private final static long workerIdShift = 10;

	private static long lastTimestamp = -1L;

	// 序列号
	private long sequence = 0L;
	@Value("${region-id}")
	private long regionId;
	@Value("${worker-id}")
	private long workerId;

	public long generate(BusinessTypeEnum businessType) {
		return this.nextId(businessType.getCode());
	}

	private synchronized long nextId(int businessId) {
		long timestamp = currentTimestamp();

		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				timestamp = nextMilli();
			}
		} else {
			sequence = new SecureRandom().nextInt(10);
		}

		lastTimestamp = timestamp;

        // 基准时间：2017-01-01 08:00
        return ((timestamp - 1483228800000L) << timestampShift) | (regionId << regionIdShift) | (workerId << workerIdShift) | (businessId << businessIdShift) | sequence;
    }

	private long nextMilli() {
		long timestamp = this.currentTimestamp();
		while (timestamp <= lastTimestamp) {
			timestamp = this.currentTimestamp();
		}
		return timestamp;
	}

	/**
	 * @return System.currentTimeMillis
	 */
	private long currentTimestamp() {
		return System.currentTimeMillis();
	}
}