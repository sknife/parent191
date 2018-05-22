package cn.itcast.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.ProductQuery;


@Service("searchService")
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SolrServer solrServer;

//	public Pagination selectPaginationByQuery(Integer pageNo, String keyword) throws SolrServerException
	public Pagination selectPaginationByQuery(Integer pageNo, String keyword, Long brandId, String price) throws Exception{		
		
		ProductQuery productQuery = new ProductQuery();
		// 当前页
		productQuery.setPageNo(Pagination.cpn(pageNo));
		// 每页显示12条
		productQuery.setPageSize(12);
		
		StringBuilder params = new StringBuilder();
		
		SolrQuery solrQuery = new SolrQuery();
		//关键词
		solrQuery.set("q", "name_ik:" + keyword);
		params.append("keyword=").append(keyword);
		
		// 品牌id		
		if (null != brandId) {

			solrQuery.addFilterQuery("brandId:" + brandId);
		}

		// 价格
		if (null != price) {
			String[] strings = price.split("-");
			if (strings.length == 2) {

				solrQuery.addFilterQuery("price:[" + strings[0] + " TO " + strings[1] + "]");
			} else {
				solrQuery.addFilterQuery("price:[" + strings[0] + " TO *]");
			}
		}
		
		// 高亮
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("name_ik");
		//样式<span style='color:red'>2016</span>
		solrQuery.setHighlightSimplePre("<span style='color:red'>");
		solrQuery.setHighlightSimplePost("</span>");
		
		// 排序
		solrQuery.addSort("price", ORDER.asc);
		// 分页
		solrQuery.setStart(productQuery.getStartRow());
		solrQuery.setRows(productQuery.getPageSize());
		
		// 执行查询
		List<Product> products = new ArrayList<Product>();
		QueryResponse response = solrServer.query(solrQuery);

		// 结果集
		SolrDocumentList docs = response.getResults();

		// 取高亮
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		
		// 发现总条数
		long numFound = docs.getNumFound();
		for (SolrDocument doc : docs) {
			// 创建商品对象
			Product product = new Product();
			String id = (String) doc.get("id");
			product.setId(Long.parseLong(id));
			Map<String, List<String>> map = highlighting.get(id);
			List<String> list = map.get("name_ik");
			product.setName(list.get(0));
//			String name = (String) doc.get("name_ik");
//			product.setName(name);			

			product.setImgUrl((String) doc.get("url"));

			product.setPrice((Float) doc.get("price"));

			product.setBrandId(Long.parseLong(String.valueOf(doc.get("brandId"))));

			products.add(product);

		}
		//构建分页对象
		Pagination pagination = new Pagination(
				productQuery.getPageNo(),
				productQuery.getPageSize(),
				(int) numFound,
				products				
				);
		String url = "/search";
		pagination.pageView(url, params.toString());
		return pagination;
	}

}