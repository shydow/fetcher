package com.tangpian.fetcher;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tangpian.fetcher.gplus.GplusBuilder;
import com.tangpian.fetcher.gplus.GplusFetcher;

/**
 * Hello world!
 *
 */
public class App 
{
	private static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args )
    {
    	
		Fetcher fetcher = new GplusFetcher(GplusBuilder.getServicePlus());
        List<String> result = fetcher.fetchContent("111081291678895561458");
        for (String string : result) {
			System.out.println(string);
		}
    }
}
