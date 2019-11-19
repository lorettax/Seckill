package com.lorettax.base;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.lorettax.utils.Readporperities;

public class BaseMongo {
	protected static MongoClient mongoClient ;
		
		static {
			List<ServerAddress> addresses = new ArrayList<ServerAddress>();
			String[] addressList = Readporperities.getKey("mongoaddr").split(",");
			String[] portList = Readporperities.getKey("mongoport").split(",");
			for (int i = 0; i < addressList.length; i++) {
				ServerAddress address = new ServerAddress(addressList[i], Integer.parseInt(portList[i]));
				addresses.add(address);
				
			}
			mongoClient = new MongoClient(addresses);
		}
	
}


