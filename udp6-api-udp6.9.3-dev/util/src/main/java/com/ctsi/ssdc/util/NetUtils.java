package com.ctsi.ssdc.util;

import java.lang.reflect.Array;

/**
 * 网络相关工具类
 * @author ctsi
 *
 */
public class NetUtils {
    
	/**
	 * IP转长整型.
	 * @param ip	
	 * return result
	 */ 
	

    public static long ip2Long(String ip) {
        String[] tokens = ip.split("[.]");
        assert (tokens.length == 4);
        long result = 0;
        for (int i = 0; i < tokens.length; i++) {
            try {
                result = (result << 8) | Integer.parseInt(tokens[i]);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Incorrect number", e);
            }
        }

        return result;
    }
	/**
	 * 长整型转IP.
	 * @param ip	
	 * return result
	 */ 
	

    public static String long2Ip(long ip) {
        StringBuilder result = new StringBuilder(15);
        result.append((ip >> 24 & 0xff)).append(".");
        result.append((ip >> 16 & 0xff)).append(".");
        result.append((ip >> 8 & 0xff)).append(".");
        result.append(ip & 0xff);

        return result.toString();
    }

	/**
	 * 校验是否满足IP格式.
	 * @param ip	
	 * return boolean
	 */
	

    public static boolean isValidIp(final String ip) {
        final String[] ipAsList = ip.split("\\.");

        // The IP address must have four octets
        if (Array.getLength(ipAsList) != 4) {
            return false;
        }

        for (int i = 0; i < 4; i++) {
            // Each octet must be an integer
            final String octetString = ipAsList[i];
            int octet;
            try {
                octet = Integer.parseInt(octetString);
            } catch (final Exception e) {
                return false;
            }
            // Each octet must be between 0 and 255, inclusive
            if (octet < 0 || octet > 255) {
                return false;
            }

            // Each octetString must have between 1 and 3 characters
            if (octetString.length() < 1 || octetString.length() > 3) {
                return false;
            }
        }

        // IP is good, return true
        return true;
    }
	/**
	 * 校验是否满足CIDR格式.
	 * @param cidr	
	 * return boolean
	 */
	

    public static boolean isValidCIDR(final String cidr) {
        if (cidr == null || cidr.isEmpty()) {
            return false;
        }
        String[] cidrPair = cidr.split("\\/");
        if (cidrPair.length != 2) {
            return false;
        }
        String cidrAddress = cidrPair[0];
        String cidrSize = cidrPair[1];
        if (!isValidIp(cidrAddress)) {
            return false;
        }
        int cidrSizeNum = -1;

        try {
            cidrSizeNum = Integer.parseInt(cidrSize);
        } catch (Exception e) {
            return false;
        }

        if (cidrSizeNum < 0 || cidrSizeNum > 32) {
            return false;
        }

        return true;
    }
	/**
	 * 校验是否满足网络掩码格式.
	 * @param netmask	
	 * return boolean
	 */
	

    public static boolean isValidNetmask(String netmask) {
        if (!isValidIp(netmask)) {
            return false;
        }

        long ip = ip2Long(netmask);
        int count = 0;
        boolean finished = false;
        for (int i = 31; i >= 0; i--) {
            if (((ip >> i) & 0x1) == 0) {
                finished = true;
            } else {
                if (finished) {
                    return false;
                }
                count += 1;
            }
        }

        if (count == 0) {
            return false;
        }

        return true;
    }
	/**
	 * 通过网关和掩码计算CIDR.
	 * @param gatewayStr
	 * @param netmaskStr	
	 * return cidr
	 */
	

    public static String getCidrFromGatewayAndNetmask(String gatewayStr, String netmaskStr) {
        long netmask = ip2Long(netmaskStr);
        long gateway = ip2Long(gatewayStr);
        long firstPart = gateway & netmask;
        long size = getCidrSize(netmaskStr);
        return long2Ip(firstPart) + "/" + size;
    }
	/**
	 * 根据网络掩码校验两个是否为同一子网.
	 * @param ip1
	 * @param ip2	
	 * @param netmask	
	 * return boolean
	 */   
	

    public static boolean sameSubnet(final String ip1, final String ip2, final String netmask) {
        if (ip1 == null || ip1.isEmpty() || ip2 == null || ip2.isEmpty()) {
            return true;
        }
        String subnet1 = NetUtils.getSubNet(ip1, netmask);
        String subnet2 = NetUtils.getSubNet(ip2, netmask);

        return (subnet1.equals(subnet2));
    }
	/**
	 * 根据网络掩码校验两个是否为同一cidr.
	 * @param ip1
	 * @param ip2	
	 * @param cidrSize	
	 * return boolean
	 */
	

    public static boolean sameSubnetCIDR(final String ip1, final String ip2, final long cidrSize) {
        if (ip1 == null || ip1.isEmpty() || ip2 == null || ip2.isEmpty()) {
            return true;
        }
        String subnet1 = NetUtils.getCidrSubNet(ip1, cidrSize);
        String subnet2 = NetUtils.getCidrSubNet(ip2, cidrSize);

        return (subnet1.equals(subnet2));
    }
    /**
	 * 根据网络掩码、ip计算子网
	 * @param ip
	 * @param netmask	
	 * return result
	 */
	

    public static String getSubNet(String ip, String netmask) {
        long ipAddr = ip2Long(ip);
        long subnet = ip2Long(netmask);
        long result = ipAddr & subnet;
        return long2Ip(result);
    }
    /**
	 * 根据CIDR、ip计算子网
	 * @param ip
	 * @param cidrSize	
	 * return result
	 */
	

    public static String getCidrSubNet(String ip, long cidrSize) {
        long numericNetmask = (0xffffffff >> (32 - cidrSize)) << (32 - cidrSize);
        String netmask = NetUtils.long2Ip(numericNetmask);
        return getSubNet(ip, netmask);
    }
    /**
	 * 根据网络掩码、ip计算cidr
	 * @param ip
	 * @param netmask	
	 * return cidr
	 */
	

    public static String ipAndNetMaskToCidr(String ip, String netmask) {
        if (!isValidIp(ip)) {
            return null;
        }

        if (!isValidNetmask(netmask)) {
            return null;
        }

        long ipAddr = ip2Long(ip);
        long subnet = ip2Long(netmask);
        long result = ipAddr & subnet;
        int bits = (subnet == 0) ? 0 : 1;
        long subnet2 = subnet;
        while ((subnet2 = (subnet2 >> 1) & subnet) != 0) {
            bits++;
        }

        return long2Ip(result) + "/" + Integer.toString(bits);
    }
    /**
	 * 根据网络掩码、ip计算ip范围
	 * @param ip
	 * @param netmask	
	 * return ip范围
	 */
	

    public static String[] ipAndNetMaskToRange(String ip, String netmask) {
        long ipAddr = ip2Long(ip);
        long subnet = ip2Long(netmask);
        long start = (ipAddr & subnet) + 1;
        long end = start;
        int bits = (subnet == 0) ? 0 : 1;
        while ((subnet = (subnet >> 1) & subnet) != 0) {
            bits++;
        }
        end = end >> (32 - bits);

        end++;
        end = (end << (32 - bits)) - 2;

        return new String[] {long2Ip(start), long2Ip(end)};

    }
    
    /**
	 * 校验网络A是否属于网络B范围
	 * @param cidrA
	 * @param cidrB
	 * return boolean
	 */ 
	

    public static boolean isNetworkAWithinNetworkB(String cidrA, String cidrB) {
        Long[] cidrALong = cidrToLong(cidrA);
        Long[] cidrBLong = cidrToLong(cidrB);
        if (cidrALong == null || cidrBLong == null) {
            return false;
        }
        long shift = 32 - cidrBLong[1];
        return ((cidrALong[0] >> shift) == (cidrBLong[0] >> shift));
    }
    /**
	 * cidr转long型 
	 * @param cidr
	 * return cidrlong
	 */ 
	

    public static Long[] cidrToLong(String cidr) {
        if (cidr == null || cidr.isEmpty()) {
            return null;
        }
        String[] cidrPair = cidr.split("\\/");
        if (cidrPair.length != 2) {
            return null;
        }
        String cidrAddress = cidrPair[0];
        String cidrSize = cidrPair[1];
        if (!isValidIp(cidrAddress)) {
            return null;
        }
        int cidrSizeNum = -1;

        try {
            cidrSizeNum = Integer.parseInt(cidrSize);
        } catch (Exception e) {
            return null;
        }
        long numericNetmask = (0xffffffff >> (32 - cidrSizeNum)) << (32 - cidrSizeNum);
        long ipAddr = ip2Long(cidrAddress);
        Long[] cidrlong = {ipAddr & numericNetmask, (long)cidrSizeNum};
        return cidrlong;

    }
    /**
	 * 根据CIDR计算子网
	 * @param cidr
	 * return cidrSubNet
	 */ 
	

    public static String getCidrSubNet(String cidr) {
        if (cidr == null || cidr.isEmpty()) {
            return null;
        }
        String[] cidrPair = cidr.split("\\/");
        if (cidrPair.length != 2) {
            return null;
        }
        String cidrAddress = cidrPair[0];
        String cidrSize = cidrPair[1];
        if (!isValidIp(cidrAddress)) {
            return null;
        }
        int cidrSizeNum = -1;

        try {
            cidrSizeNum = Integer.parseInt(cidrSize);
        } catch (Exception e) {
            return null;
        }
        long numericNetmask = (0xffffffff >> (32 - cidrSizeNum)) << (32 - cidrSizeNum);
        String netmask = NetUtils.long2Ip(numericNetmask);
        return getSubNet(cidrAddress, netmask);
    }

    /**
	 * 根据CIDR位数计算CIDR掩码
	 * @param cidrSize
	 * return cidrmask
	 */ 
	

    public static String getCidrNetmask(long cidrSize) {
        long numericNetmask = (0xffffffff >> (32 - cidrSize)) << (32 - cidrSize);
        return long2Ip(numericNetmask);
    }
    /**
	 * 根据CIDR计算网络掩码
	 * @param cidr
	 * return netmask
	 */ 
	

    public static String getCidrNetmask(String cidr) {
        String[] cidrPair = cidr.split("\\/");
        long guestCidrSize = Long.parseLong(cidrPair[1]);
        return getCidrNetmask(guestCidrSize);
    }
    /**
	 * 根据网络掩码计算cidr位数
	 * @param netmask
	 * return size
	 */ 
	

    public static String cidr2Netmask(String cidr) {
        String[] tokens = cidr.split("\\/");
        return getCidrNetmask(Integer.parseInt(tokens[1]));
    }

    /**
	 * 根据网络掩码计算cidr位数
	 * @param netmask
	 * return size
	 */ 
	

    public static long getCidrSize(String netmask) {
        long ip = ip2Long(netmask);
        int count = 0;
        for (int i = 0; i < 32; i++) {
            if (((ip >> i) & 0x1) == 0) {
                count++;
            } else {
                break;
            }
        }

        return 32 - count;
    }
    /**
	 * 验证guest cidr
	 * @param cidr
	 * return boolean
	 */  
	

    public static boolean validateGuestCidr(String cidr) {
        // RFC 1918 - The Internet Assigned Numbers Authority (IANA) has reserved the
        // following three blocks of the IP address space for private internets:
        // 10.0.0.0 - 10.255.255.255 (10/8 prefix)
        // 172.16.0.0 - 172.31.255.255 (172.16/12 prefix)
        // 192.168.0.0 - 192.168.255.255 (192.168/16 prefix)

        String cidr1 = "10.0.0.0/8";
        String cidr2 = "172.16.0.0/12";
        String cidr3 = "192.168.0.0/16";

        if (!isValidCIDR(cidr)) {
            return false;
        }
        if (isNetworkAWithinNetworkB(cidr, cidr1) || isNetworkAWithinNetworkB(cidr, cidr2) || isNetworkAWithinNetworkB(cidr, cidr3)) {
            return true;
        } else {
            return false;
        }
    }
    /**
	 * 根据CIDR获取start ip
	 * @param cidr
	 * @param size	
	 * return startip
	 */   
	

	public static String getIpRangeStartIpFromCidr(final String cidr,
			final long size) {
		final long ip = ip2Long(cidr);
		final long startNetMask = ip2Long(getCidrNetmask(size));
		final long start = (ip & startNetMask) + 1;
		return long2Ip(start);
	}
    /**
	 * 根据CIDR获取end ip
	 * @param cidr
	 * @param size	
	 * return endip
	 */
	

	public static String getIpRangeEndIpFromCidr(final String cidr,
			final long size) {
		final long ip = ip2Long(cidr);
		final long startNetMask = ip2Long(getCidrNetmask(size));
		final long start = (ip & startNetMask) + 1;
		long end = start;
		end = end >> 32 - size;
		end++;
		end = (end << 32 - size) - 2;
		return long2Ip(end);
	}
}
