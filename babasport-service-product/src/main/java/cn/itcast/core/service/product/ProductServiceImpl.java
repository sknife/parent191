package cn.itcast.core.service.product;
import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
//import org.apache.solr.client.solrj.SolrServer;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.ColorQuery;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.ProductQuery;
import cn.itcast.core.bean.product.ProductQuery.Criteria;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.bean.product.SkuQuery;
import cn.itcast.core.dao.product.ColorDao;
import cn.itcast.core.dao.product.ProductDao;
import cn.itcast.core.dao.product.SkuDao;
import redis.clients.jedis.Jedis;
import cn.itcast.common.page.Pagination;
//import redis.clients.jedis.Jedis;

/**
 * 
 * @author Administrator
 *
 */
@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private ColorDao colorDao;
	
	@Autowired
	private SkuDao skuDao;
	
	@Autowired
	private Jedis jedis;
	
	@Autowired
	private SolrServer solrServer;
	
	
//	@Autowired
//	private JmsTemplate jmsTemplate;
	
	public Pagination selectPaginationByQuery(Integer pageNo,String name,Long brandId,Boolean isShow) {
		
		ProductQuery productQuery = new ProductQuery();
		productQuery.setPageNo(Pagination.cpn(pageNo));
		Criteria criteria = productQuery.createCriteria();
		StringBuilder params = new StringBuilder();
		if(null != name) {
			criteria.andNameLike("%"+name+"%");
			params.append("name=").append(name);
		}
		
		if(null != brandId) {
			criteria.andBrandIdEqualTo(brandId);
			params.append("&brandId=").append(brandId);
		}
		
		if(null != isShow) {
			criteria.andIsShowEqualTo(isShow);
			params.append("&isShow=").append(isShow);
		}else {
			criteria.andIsShowEqualTo(false);
			params.append("&isShow=false");
		}
		
		
		
		Pagination pagination = new Pagination(
				productQuery.getPageNo(),productQuery.getPageSize(),
				productDao.countByExample(productQuery),
				productDao.selectByExample(productQuery)
				);
		String url = "/product/list.do";
		pagination.pageView(url, params.toString());
		
		return pagination;
	}
	
	
	//加载颜色
	public List<Color> selectColorList() {
		ColorQuery query = new ColorQuery();
		query.createCriteria().andParentIdNotEqualTo(0L);
		return colorDao.selectByExample(query);
		
	}
	
	
	public void insertProduct(Product product) {
		//保存商品
		//商品编号
		Long id = jedis.incr("pno");
		product.setId(id);
		product.setIsShow(false);
		
		product.setIsDel(true);
		//product.setId(444L);
		productDao.insertSelective(product);
		
		//返回ID
		
		//保存sku
		String[] colors = product.getColors().split(",");
		String[] sizes = product.getSizes().split(",");
		//颜色
		
		for (String color : colors) {
			for (String size : sizes) {
				Sku sku = new Sku();
				sku.setProductId(product.getId());
				sku.setColorId(Long.parseLong(color));
				sku.setSize(size);
				//随便设
				sku.setMarketPrice(999f);
				sku.setPrice(666f);
				sku.setDeliveFee(8f);
				sku.setStock(0);
				sku.setUpperLimit(200);
				sku.setCreateTime(new Date());
				skuDao.insertSelective(sku);
				
			}
		}
		
		
		
	}
	
	/**
	 * 商品上架
	 * @param ids
	 */
	public void isShow(Long[] ids) {
		for (final Long id : ids) {
			Product product = new Product();
			product.setId(id);
			product.setIsShow(true);
			//商品变更
			productDao.updateByPrimaryKeySelective(product);
			
			//保存商品信息到Solr服务器
			SolrInputDocument doc = new SolrInputDocument();
			//商品ID
			doc.setField("id", id);
			//商品名称
			Product p = productDao.selectByPrimaryKey(id);
			doc.setField("name_ik", p.getName());
			//图片的Url（默认图片）
			/*ImgQuery imgQuery = new ImgQuery();
			//商品ID
			//默认图片  1
			imgQuery.createCriteria().andProductIdEqualTo(id).andIsDefEqualTo(true);
			//只有一条数据
			List<Img> imgs = imgDao.selectByExample(imgQuery);
			doc.setField("url", imgs.get(0).getUrl());*/
			doc.setField("url", p.getImages()[0]);
			//价格  select price from bbs_sku where product_id = 276 order by price asc limit 0, 1 
			SkuQuery skuQuery = new SkuQuery();
			//指定价格字段进行查询
			skuQuery.setFields("price");
			//设置 商品ID  
			skuQuery.createCriteria().andProductIdEqualTo(id);
			//按照价格降序排序
			skuQuery.setOrderByClause("price asc");
			//设置当前页为1
			skuQuery.setPageNo(1);
			//每页数设置为1
			skuQuery.setPageSize(1);
			List<Sku> skus = skuDao.selectByExample(skuQuery);
			doc.setField("price", skus.get(0).getPrice());
			//品牌ID
			doc.setField("brandId", p.getBrandId());
			//保存到Solr服务器
			//提交
			try {
				solrServer.add(doc);
				solrServer.commit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			//发送信息到ActiveMq中
			/*jmsTemplate.send(new MessageCreator(){

				@Override
				public Message createMessage(Session session) throws JMSException {
					// TODO Auto-generated method stub
					
					return session.createTextMessage(String.valueOf(id));
				}
				
			});*/
			
			//静态化
			
			
			
		}
	}
	
	
	
}
