package com.ctsi.monitor.util;

import com.ctsi.monitor.entity.*;
import com.ctsi.ssdc.utils.IpUtils;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**

 * 服务器相关信息

 */

public class Server {

    private static final int OSHI_WAIT_SECOND = 1000;

    /**
     * CPU相关信息
     */

    private Cpu cpu = new Cpu();

    /**
     * 內存相关信息
     */

    private Mem mem = new Mem();

    /**
     * JVM相关信息
     */

    private Jvm jvm = new Jvm();

    /**
     * 服务器相关信息
     */

    private Sys sys = new Sys();

    /**
     * 磁盘相关信息
     */

    private List sysFiles = new LinkedList();

    public Cpu getCpu() { return cpu; }

    public void setCpu(Cpu cpu) { this.cpu = cpu; }

    public Mem getMem() { return mem; }

    public void setMem(Mem mem) { this.mem = mem; }

    public Jvm getJvm() { return jvm; }

    public void setJvm(Jvm jvm) { this.jvm = jvm; }

    public Sys getSys() { return sys; }

    public void setSys(Sys sys) { this.sys = sys; }

    public List getSysFiles() { return sysFiles; }

    public void setSysFiles(List sysFiles) { this.sysFiles = sysFiles; }

    /**
     * 获取服务器主机相关信息
     * @throws Exception
     */
    public void copyTo() throws Exception {
        // 获取系统信息
        SystemInfo si = new SystemInfo();

        // 根据SystemInfo获取硬件实例
        HardwareAbstractionLayer hal = si.getHardware();

        // 获取硬件CPU信息
        setCpuInfo(hal.getProcessor());

        // 获取硬件内存信息
        setMemInfo(hal.getMemory());

        // 设置服务器信息
        setSysInfo();

        // 设置Java虚拟机
        setJvmInfo();

        // 设置磁盘信息
        setSysFiles(si.getOperatingSystem());
    }
    /**
     * 获取CPU相关信息
     * @throws Exception
     */
    public void getCpuMsg() {
        // 获取系统信息
        SystemInfo si = new SystemInfo();
        // 根据SystemInfo获取硬件实例
        HardwareAbstractionLayer hal = si.getHardware();
        // 获取硬件CPU信息
        setCpuInfo(hal.getProcessor());
    }
    /**
     * 获取硬件内存信息
     * @throws Exception
     */
    public void getMemMsg() {
        // 获取系统信息
        SystemInfo si = new SystemInfo();
        // 根据SystemInfo获取硬件实例
        HardwareAbstractionLayer hal = si.getHardware();
        // 获取硬件内存信息
        setMemInfo(hal.getMemory());
    }
    /**
     * 获取Java虚拟机信息
     * @throws Exception
     */
    public void getJvmMsg() throws Exception {
        // 设置Java虚拟机
        setJvmInfo();
    }
    /**
     * 获取服务器相关信息
     * @throws Exception
     */
    public void getSysFileMsg() {
        // 获取系统信息
        SystemInfo si = new SystemInfo();

        // 设置服务器信息
        setSysInfo();

        // 设置磁盘信息
        setSysFiles(si.getOperatingSystem());
    }

    /**
     * 设置CPU信息
     */

    private void setCpuInfo(CentralProcessor processor) {
        // CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(OSHI_WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        cpu.setCpuNum(processor.getLogicalProcessorCount());// Cpu 核数
        cpu.setTotal(totalCpu);// CPU总的使用率
        cpu.setSys(cSys);// CPU系统使用率
        cpu.setUsed(user);// CPU用户使用率
        cpu.setWait(iowait);// CPU当前等待率
        cpu.setFree(idle);// CPU当前空闲率
    }

    /**
     * 设置内存信息
     */
    private void setMemInfo(GlobalMemory memory) {
        mem.setTotal(memory.getTotal());// 总内存大小
        mem.setUsed(memory.getTotal() - memory.getAvailable());// 已使用内存大小
        mem.setFree(memory.getAvailable());// 空闲内存大小
    }

    /**
     * 设置服务器信息
     */
    private void setSysInfo() {
        // 获取当前的系统属性
        Properties props = System.getProperties();
        sys.setComputerName(IpUtils.getHostName());// 获取主机名称
        sys.setComputerIp(IpUtils.getHostIp());// 获取主机IP
        sys.setOsName(props.getProperty("os.name"));// 获取主机类型 Windows 10
        sys.setOsArch(props.getProperty("os.arch"));// 获取主机显卡类型 amd64
        sys.setUserDir(props.getProperty("user.dir"));
    }

    /**
     * 设置Java虚拟机
     */
    private void setJvmInfo() throws UnknownHostException {
        Properties props = System.getProperties();
        jvm.setTotal(Runtime.getRuntime().totalMemory());// JVM总内存 625.5M
        jvm.setMax(Runtime.getRuntime().maxMemory());// JVM已使用内存 347.99M
        jvm.setFree(Runtime.getRuntime().freeMemory());// JVM空闲内存 277.51M
        jvm.setVersion(props.getProperty("java.version"));// jdk版本 1.8
        jvm.setHome(props.getProperty("java.home"));// JDK安装路径 C:\Program Files\Java\jdk1.8.0_201\jre
    }

    /**
     * 设置磁盘信息
     */
    private void setSysFiles(OperatingSystem os) {
        // 根据 操作系统(OS) 获取 FileSystem
        FileSystem fileSystem = os.getFileSystem();
        // 根据 FileSystem 获取主机磁盘信息list集合
        List<OSFileStore> fsArray = Arrays.asList(fileSystem.getFileStores());
        for (OSFileStore fs : fsArray) {
            long free = fs.getUsableSpace();// 磁盘空闲容量
            long total = fs.getTotalSpace();// 磁盘总容量
            long used = total - free;// 磁盘已使用容量
            SysFile sysFile = new SysFile();
            sysFile.setDirName(fs.getMount());// 磁盘符号 C:\
            sysFile.setSysTypeName(fs.getType());// 磁盘类型 NTFS
            sysFile.setTypeName(fs.getName());// 磁盘名称 本地固定磁盘 (C:)
            sysFile.setTotal(convertFileSize(total));// 磁盘总容量
            sysFile.setFree(convertFileSize(free));// 磁盘空闲容量
            sysFile.setUsed(convertFileSize(used));// 磁盘已使用容量
            sysFile.setUsage(Arith.mul(Arith.div(used, total, 4), 100));// 磁盘资源的使用率
            sysFiles.add(sysFile);
        }
    }

    /**
     * 字节转换
     *
     * @param size 字节大小
     * @return 转换后值
     */
    public String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        }
        else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        }
        else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        }
        else{
            return String.format("%d B", size);
        }
    }
}