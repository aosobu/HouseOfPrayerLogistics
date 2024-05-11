package com.spiritcoder.musalalogistics;

import com.spiritcoder.musalalogistics.commons.util.NetworkUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetSocketAddress;
import java.net.Socket;

@SpringBootApplication
public class MusalaLogisticsApplication implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(MusalaLogisticsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (NetworkUtil.ping("127..0.0.1", 1433)){
			System.out.println("SQL Server is Active");
		}else{
			System.out.println("SQL server is not available on this port");
		}
	}
}
