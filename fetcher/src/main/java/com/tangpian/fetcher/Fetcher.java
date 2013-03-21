package com.tangpian.fetcher;

import java.util.List;


public interface Fetcher {
	public List<String> fetchContent(String account);
}
