package com.basoft.service.util;

import lombok.extern.slf4j.Slf4j;

import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

@Slf4j
public class OuterIPUtil {
    /***************************************方式一：通过过滤获取IP地址-START*********************************************/
    /**
     * 通过过滤获取IP地址
     * 过滤回环网卡、点对点网卡、非活动网卡、虚拟网卡并要求网卡名字是eth或ens开头；再过滤回环地址，并要求是内网地址（非外网）
     *
     * @return
     * @throws SocketException
     */
    public static List<Inet4Address> getLocalIp4AddressFromNetworkInterface() throws SocketException {
        log.info("通过过滤获取IP地址getLocalIp4AddressFromNetworkInterface::::START");
        List<Inet4Address> addresses = new ArrayList<>(1);
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        if (e == null) {
            log.info("通过过滤获取IP地址::::NetworkInterface.getNetworkInterfaces IS NULL");
            return addresses;
        }
        while (e.hasMoreElements()) {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            log.info("通过过滤获取IP地址::::NetworkInterface.nextElement::::" + n.getName());
            if (!isValidInterface(n)) {
                continue;
            }
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
                InetAddress i = (InetAddress) ee.nextElement();
                if (isValidAddress(i)) {
                    log.info("||||||||||||||||||||||GET|||||||||||||||||||||||||||||");
                    addresses.add((Inet4Address) i);
                }
            }
        }
        log.info("addresses list size is :::: " + addresses.size());
        log.info("通过过滤获取IP地址getLocalIp4AddressFromNetworkInterface::::END");
        return addresses;
    }

    /**
     * 过滤回环网卡、点对点网卡、非活动网卡、虚拟网卡并要求网卡名字是eth或ens开头
     *
     * @param ni 网卡
     * @return 如果满足要求则true，否则false
     */
    private static boolean isValidInterface(NetworkInterface ni) throws SocketException {
        return !ni.isLoopback() && !ni.isPointToPoint() && ni.isUp() && !ni.isVirtual()
                && (ni.getName().startsWith("eth") || ni.getName().startsWith("ens"));
    }

    /**
     * 判断是否是IPv4，并且内网地址并过滤回环地址.
     */
    private static boolean isValidAddress(InetAddress address) {
        return address instanceof Inet4Address && address.isSiteLocalAddress() && !address.isLoopbackAddress();
    }
    /***************************************方式一：通过过滤获取IP地址-END*********************************************/


    /***************************************方式二：通过过滤获取IP地址-START*********************************************/
    /**
     * 通过访问外网获取IP地址
     * 当有多个网卡的时候，使用这种方式一般都可以得到想要的IP。甚至不要求外网地址8.8.8.8是可连通的。
     *
     * @return
     * @throws SocketException
     */
    private static Optional<Inet4Address> getIpBySocket() throws SocketException {
        log.info("通过访问外网获取IP地址getIpBySocket::::START");
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            if (socket.getLocalAddress() instanceof Inet4Address) {
                log.info("通过访问外网获取IP地址Result::::" + socket.getLocalAddress().getHostAddress());
                log.info("通过访问外网获取IP地址getIpBySocket::::END");
                return Optional.of((Inet4Address) socket.getLocalAddress());
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        log.info("通过访问外网获取IP地址getIpBySocket::::NULL");
        log.info("通过访问外网获取IP地址getIpBySocket::::END");
        return Optional.empty();
    }
    /***************************************方式二：通过过滤获取IP地址-END*********************************************/


    /***************************************方式三：方式一和方式二相结合-START*********************************************/
    public static Optional<Inet4Address> getLocalIp4Address() throws SocketException {
        log.info("<><><><><><><><><><><><><><><><><><><>><><>GET IP START<><><><><><><><><><><><><><><><><><><>><><>");
        final List<Inet4Address> ipByNi = getLocalIp4AddressFromNetworkInterface();
        if (ipByNi.isEmpty() || ipByNi.size() > 1) {
            final Optional<Inet4Address> ipBySocketOpt = getIpBySocket();
            if (ipBySocketOpt.isPresent()) {
                return ipBySocketOpt;
            } else {
                log.info("<><><><><><><><><><><><><><><><><><><>><><>GET IP MIDDLE<><><><><><><><><><><><><><><><><><><>><><>");
                return ipByNi.isEmpty() ? Optional.empty() : Optional.of(ipByNi.get(0));
            }
        }
        log.info("<><><><><><><><><><><><><><><><><><><>><><>GET IP END<><><><><><><><><><><><><><><><><><><>><><>");
        return Optional.of(ipByNi.get(0));
    }
    /***************************************方式三：方式一和方式二相结合-END*********************************************/


    public static void main(String[] args) {
        try {
            Optional ip = OuterIPUtil.getLocalIp4Address();
            Inet4Address ipTemp = (Inet4Address)ip.get();
            String ipStr = ipTemp.getHostAddress();
            System.out.println(ipStr);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
