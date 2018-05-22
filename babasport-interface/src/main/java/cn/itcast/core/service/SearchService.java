package cn.itcast.core.service;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Product;

public interface SearchService {
	
	public Pagination selectPaginationByQuery(Integer pageNo,String keyword,Long brandId,String price) throws Exception;
//	public Pagination selectPaginationByQuery(Integer pageNo, String keyword) throws SolrServerException;
	
//	public void insertProductToSolr(Long id);
}
