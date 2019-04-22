package com.online.mall.shoppv.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.mapping.Array;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.online.mall.shoppv.common.ConfigConstants;
import com.online.mall.shoppv.common.DictConstantsUtil;
import com.online.mall.shoppv.entity.Goods;
import com.online.mall.shoppv.entity.GoodsWithoutDetail;
import com.online.mall.shoppv.repository.GoodsRepository;
import com.online.mall.shoppv.repository.GoodsWithoutDetailRepository;

@Service
public class GoodsService {
	
	
	private static final Logger log = LoggerFactory.getLogger(GoodsService.class);

	@Autowired
	private GoodsRepository goodRepository;
	
	@Autowired
	private GoodsWithoutDetailRepository noDetailRepos;
	
	
	public List<GoodsWithoutDetail> findGoodsByMenu(int menuId)
	{
		List<GoodsWithoutDetail> ls = noDetailRepos.selectGoodsByGoodsMenuId(menuId);
		return ls;
	}
	
	/**
	 * 
	 * @param menuId 商品菜单id
	 * @param sort 排序
	 * @param index 从0开始的页面索引
	 * @param num 每次查询的记录数
	 * @return
	 */
	public List<GoodsWithoutDetail> findGoodsByMenuWithSort(int menuId,Sort sort,int index,int num)
	{
		GoodsWithoutDetail goods = new GoodsWithoutDetail();
		goods.setGoodsMenuId(menuId);
		goods.setStatus(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODS_STATUS_STAYON));
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("inventory","totalSales","monthSales","carriage");
		if(menuId == -1) {
			matcher.withIgnorePaths("goodsMenuId");
		}
		Example<GoodsWithoutDetail> example = Example.of(goods,matcher);
		PageRequest page = null;
		if(sort == null)
		{
			page = PageRequest.of(index, num);
		}else
		{
			page = PageRequest.of(index, num, sort);
		}
		return noDetailRepos.findAll(example,page).getContent();
	}
	
	
	
	/**
	 * 根据商品ID查询商品信息
	 * @param goodsId
	 * @return
	 */
	@Cacheable(value="GoodsCacheWithId",key="'getProduct'+#goodsId")
	public Optional<Goods> getProduct(String goodsId)
	{
		return goodRepository.findById(goodsId);
	}
	
	
	@Cacheable(value="GoodsCacheWithId",key="'getProductWithDetail'+#goodsId")
	public Optional<GoodsWithoutDetail> getProductWithDetail(String goodsId)
	{
		return noDetailRepos.findById(goodsId);
	}
	
	
	/**
	 * 分页查询商品列表
	 * @param goodsMenu
	 * @param sort
	 * @param isAsc
	 * @param index
	 * @return
	 */
	@Cacheable(value="getGoodsWithPage",key="'getGoodsWithPage'+#goodsMenu+#sort+#isAsc+#index")
	public List<GoodsWithoutDetail> loadGoodsWithPage(int goodsMenu,String sort,String isAsc,int index)
	{
		List<GoodsWithoutDetail> ls = new ArrayList<GoodsWithoutDetail>();
		int length = Integer.parseInt(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODS_LS_LENGTH));
		if(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODSLS_SORT_ALL).equals(sort))
		{
			ls = findGoodsByMenuWithSort(goodsMenu, null, index, length);
		}else if (DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODSLS_SORT_PRICE).equals(sort))
		{
			if (DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODSLS_SORT_PRICE_ASC).equals(isAsc))
			{
				Sort s = new Sort(Direction.ASC,"price");
				ls = findGoodsByMenuWithSort(goodsMenu, s, index, length);
			}else
			{
				Sort s = new Sort(Direction.DESC,"price");
				ls = findGoodsByMenuWithSort(goodsMenu, s, index, length);
			}
		}else
		{
			Sort s = new Sort(Direction.DESC,"monthSales");
			ls = findGoodsByMenuWithSort(goodsMenu, s, index, length);
		}
		return ls;
	}
	
	
	/**
	 * 商品列表页加载，根据菜单ID，分页排序查询
	 * @param request，request中包含3个get请求参数3种排序方式
	 */
	public void loadGoodsbyMenuIdWithSortAndPage(HttpServletRequest request)
	{
		int goods = -1;
		if(request.getParameter(ConfigConstants.GOODS_KIND) != null) {
			goods = Integer.parseInt(request.getParameter(ConfigConstants.GOODS_KIND));
		}
		String sort = request.getParameter(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODSLS_SORT));
		String isAsc = request.getParameter("isasc");
		int length = Integer.parseInt(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODS_LS_LENGTH));
		List<GoodsWithoutDetail> ls = new ArrayList<GoodsWithoutDetail>();
		if(StringUtils.isEmptyOrWhitespace(sort)||DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODSLS_SORT_ALL).equals(sort))
		{
			request.setAttribute(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODSLS_SORT), DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODSLS_SORT_ALL));
			ls = findGoodsByMenuWithSort(goods, null, 0, length);
		}else
		{
			request.setAttribute(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODSLS_SORT), sort);
			if(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODSLS_SORT_PRICE).equals(sort) && (StringUtils.isEmptyOrWhitespace(isAsc) || DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODSLS_SORT_PRICE_DESC).equals(isAsc)))
			{
				request.setAttribute("isAsc", DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODSLS_SORT_PRICE_ASC));
				Sort s = new Sort(Direction.ASC,"price");
				ls = findGoodsByMenuWithSort(goods, s, 0, length);
			}else if(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODSLS_SORT_PRICE).equals(sort))
			{
				request.setAttribute("isAsc", DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODSLS_SORT_PRICE_DESC));
				Sort s = new Sort(Direction.DESC,"price");
				ls = findGoodsByMenuWithSort(goods, s, 0, length);
			}else
			{
				Sort s = new Sort(Direction.DESC,"monthSales");
				ls = findGoodsByMenuWithSort(goods, s, 0, length);
			}
		}
		
		request.setAttribute("goodsLs", ls);
		request.setAttribute("goods", goods);
	}
	
}
