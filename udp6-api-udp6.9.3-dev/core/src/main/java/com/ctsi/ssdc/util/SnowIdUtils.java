package com.ctsi.ssdc.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 雪花ID生成器
 * 静态内部类-->单例
 * 物理网卡地址做datacenterId
 * （datacenterId+jvm进程号）做 工作机器 ID
 *
 * @author zhaoliangliang
 * @date 2021/9/10
 */
public class SnowIdUtils {

    /**
     * 私有的 静态内部类
     */
    private static class SnowFlake {

        /**
         * 内部类对象（单例模式）
         */
        private static final SnowFlake SNOW_FLAKE = new SnowFlake();

        private static final Log logger = LogFactory.getLog(SnowFlake.class);
        private final long twepoch = 1288834974657L;
        private final long workerIdBits = 5L;
        private final long datacenterIdBits = 5L;
        private final long maxWorkerId = 31L;
        private final long maxDatacenterId = 31L;
        private final long sequenceBits = 12L;
        private final long workerIdShift = 12L;
        private final long datacenterIdShift = 17L;
        private final long timestampLeftShift = 22L;
        private final long sequenceMask = 4095L;
        private final long workerId;
        private final long datacenterId;
        private long sequence = 0L;
        private long lastTimestamp = -1L;


        private SnowFlake() {
            //通过当前物理网卡地址获取datacenterId
            this.datacenterId = getDatacenterId(31L);
            //物理网卡地址+jvm进程pi获取workerId
            this.workerId = getMaxWorkerId(this.datacenterId, 31L);
        }

        /**
         * @param workerId     工作机器 ID
         * @param datacenterId 序列号
         */
        /*public SnowFlake(long workerId, long datacenterId) {
            if (workerId > 31L || workerId < 0L) {
                throw new IllegalArgumentException
                        (String.format("worker Id can't be greater than %d or less than 0", 31L));
            }

            if (datacenterId > 31L || datacenterId < 0L) {
                throw new IllegalArgumentException
                        (String.format("datacenter Id can't be greater than %d or less than 0", 31L));
            }
            this.workerId = workerId;
            this.datacenterId = datacenterId;
        }*/

        /**
         * 获取工作组ID
         *
         * @param datacenterId
         * @param maxWorkerId
         * @return
         */
        protected static long getMaxWorkerId(long datacenterId, long maxWorkerId) {
            StringBuilder mpid = new StringBuilder();
            mpid.append(datacenterId);
            //获取jvm进程信息
            String name = ManagementFactory.getRuntimeMXBean().getName();
            if (StringUtils.isNotBlank(name)) {
                //获取进程PID
                mpid.append(name.split("@")[0]);
            }

            //MAC + PID 的 hashcode 获取16个低位
            return (long) (mpid.toString().hashCode() & '\uffff') % (maxWorkerId + 1L);
        }

        /**
         * 通过当前物理网卡地址获取datacenterId
         *
         * @param maxDatacenterId
         * @return
         */
        protected static long getDatacenterId(long maxDatacenterId) {
            long id = 0L;

            try {
                //获取本机(或者服务器ip地址)
                //DESKTOP-123SDAD/192.168.1.87
                InetAddress ip = InetAddress.getLocalHost();
                NetworkInterface network = NetworkInterface.getByInetAddress(ip);
                if (network == null) {
                    id = 1L;
                } else {
                    //获取物理网卡地址
                    byte[] mac = network.getHardwareAddress();
                    if (null != mac) {
                        id = (255L & (long) mac[mac.length - 2] | 65280L & (long) mac[mac.length - 1] << 8) >> 6;
                        id %= maxDatacenterId + 1L;
                    }
                }
            } catch (Exception var7) {
                logger.warn(" getDatacenterId: " + var7.getMessage());
            }
            return id;
        }

        public synchronized long nextId() {
            long timestamp = this.timeGen();
            if (timestamp < this.lastTimestamp) {
                long offset = this.lastTimestamp - timestamp;
                if (offset > 5L) {
                    throw new RuntimeException(String.format("Clock moved backwards.  " +
                            "Refusing to generate id for %d milliseconds", offset));
                }

                try {
                    this.wait(offset << 1);
                    timestamp = this.timeGen();
                    if (timestamp < this.lastTimestamp) {
                        throw new RuntimeException(String.format("Clock moved backwards.  " +
                                "Refusing to generate id for %d milliseconds", offset));
                    }
                } catch (Exception var6) {
                    throw new RuntimeException(var6);
                }
            }

            if (this.lastTimestamp == timestamp) {
                this.sequence = this.sequence + 1L & 4095L;
                if (this.sequence == 0L) {
                    timestamp = this.tilNextMillis(this.lastTimestamp);
                }
            } else {
                this.sequence = ThreadLocalRandom.current().nextLong(1L, 3L);
            }

            this.lastTimestamp = timestamp;
            return timestamp - 1288834974657L << 22 | this.datacenterId << 17 | this.workerId << 12 | this.sequence;
        }

        protected long tilNextMillis(long theLastTimestamp) {
            long timestamp;
            for (timestamp = this.timeGen(); timestamp <= theLastTimestamp; timestamp = this.timeGen()) {
                continue;
            }
            return timestamp;
        }

        protected long timeGen() {
            return SystemClock.now();
        }

    }


    /**
     * 获取long类型雪花ID
     */
    public static long uniqueLong() {
        return SnowFlake.SNOW_FLAKE.nextId();
    }

    /**
     * 测试20万次生成
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

        //计时开始时间
        long start = System.currentTimeMillis();
        //让100个线程同时进行
        final CountDownLatch latch = new CountDownLatch(100);
        //判断生成的20000万条记录是否有重复记录
        final Map<Long, Integer> map = new ConcurrentHashMap();
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("snoewID").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());


        singleThreadPool.shutdown();

        for (int i = 0; i < 100; i++) {
            //创建100个线程
//            new Thread(() -> {
//                for (int s = 0; s < 2000; s++) {
//                    long snowId = SnowIdUtils.uniqueLong();
//                    System.out.println("生成雪花ID= " + snowId);
//                    Integer put = map.put(snowId, 1);
//                    if (put != null) {
//                        throw new RuntimeException("主键重复");
//                    }
//                }
//                latch.countDown();
//            }).start();
            singleThreadPool.execute(()-> {
                //创建100个线程
                for (int s = 0; s < 2000; s++) {
                    long snowId = SnowIdUtils.uniqueLong();
                    System.out.println("生成雪花ID= " + snowId);
                    Integer put = map.put(snowId, 1);
                    if (put != null) {
                        throw new RuntimeException("主键重复");
                    }
                }
                latch.countDown();
            });

        }
        //让上面100个线程执行结束后，在走下面输出信息
        latch.await();
        System.out.println("生成20万条雪花ID总用时= " + (System.currentTimeMillis() - start) + "ms"); //1066ms
    }
}
