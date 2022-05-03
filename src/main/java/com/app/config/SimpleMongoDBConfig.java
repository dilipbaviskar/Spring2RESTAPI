package com.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class SimpleMongoDBConfig {

	@Value("${spring.data.mongodb.host}")
	private String mongoDbHost;
	

	@Value("${spring.data.mongodb.database}")
	private String mongoDbName;
	
	@Value("${spring.data.mongodb.port}")
	private String mongoDbPort;
	
	@Value("${spring.data.mongodb.uri}")
	private String mongoDbURI;
	
	private String getMongoDBURI()
	{
		if(mongoDbURI!=null)
			return mongoDbURI;
		else
		return "mongodb://"+mongoDbHost+":"+mongoDbPort+"/"+mongoDbName;
	}
	@Bean("mongodbclient")
	public MongoClient mongodbclient() {
		ConnectionString connectionString = new ConnectionString(getMongoDBURI());
		MongoClientSettings mongoClientSettings = MongoClientSettings.builder().applyConnectionString(connectionString)
				.build();
		return MongoClients.create(mongoClientSettings);
	}

	@Bean("mongodbTemplate")
	public MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(mongodbclient(), "test");
	}
}
