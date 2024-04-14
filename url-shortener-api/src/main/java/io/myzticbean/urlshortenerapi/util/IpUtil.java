package io.myzticbean.urlshortenerapi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IpUtil {

    public static String generateRandomIpAddresses() {
        int numIPs = 10; // Adjust the number of IP addresses you want

        List<String> ipList = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numIPs; i++) {
            // Generate random octets (parts) within valid IP address range (0-255)
            int octet1 = random.nextInt(256);
            int octet2 = random.nextInt(256);
            int octet3 = random.nextInt(256);
            int octet4 = random.nextInt(256);

            // Avoid private and reserved IP ranges (see IANA for details)
            while (octet1 == 10 || (octet1 == 172 && (octet2 >= 16 && octet2 <= 31)) || (octet1 == 192 && octet2 == 168)) {
                octet1 = random.nextInt(256);
            }

            // Build the IP address string
            String ipAddress = octet1 + "." + octet2 + "." + octet3 + "." + octet4;
            ipList.add(ipAddress);
        }
        /*
        System.out.println("Sample IP Addresses:");
        for (String ip : ipList) {
            System.out.println(ip);
        }
        */
        int randomIndex = random.nextInt(ipList.size());
        return ipList.get(randomIndex);
    }

}
