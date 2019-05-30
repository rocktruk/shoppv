package com.online.mall.shoppv.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.online.mall.shoppv.common.ConfigConstants;
import com.online.mall.shoppv.common.DictConstantsUtil;
import com.online.mall.shoppv.common.util.CacheUtil;
import com.online.mall.shoppv.common.util.SessionUtil;
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
	
	private ReentrantLock lock = new ReentrantLock();
	
	@Autowired
	private SessionUtil cacheUtil;
	
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
	public List<GoodsWithoutDetail> findGoodsByMenuWithSort(int menuId,String desc,Sort sort,int index,int num)
	{
		GoodsWithoutDetail goods = new GoodsWithoutDetail();
		goods.setGoodsMenuId(menuId);
		goods.setStatus(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODS_STATUS_STAYON));
		if(!StringUtils.isEmptyOrWhitespace(desc))
		{
//			goods.setBrand(desc);
			goods.setTitle(desc);
//			goods.setDesc(desc);
		}
		String[] ignorePaths;
		if(menuId == -1) {
			ignorePaths = new String[5];
			ignorePaths[0] = "inventory";
			ignorePaths[1] = "totalSales";
			ignorePaths[2] = "monthSales";
			ignorePaths[3] = "carriage";
			ignorePaths[4] = "goodsMenuId";
		}else {
			ignorePaths = new String[4];
			ignorePaths[0] = "inventory";
			ignorePaths[1] = "totalSales";
			ignorePaths[2] = "monthSales";
			ignorePaths[3] = "carriage";
		}
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths(ignorePaths)
				//.withMatcher("brand", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
				//.withMatcher("desc", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
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
		GoodsWithoutDetail goods = JSON.parseObject(JSON.toJSONString(getGoods(goodsId).get()), GoodsWithoutDetail.class);
		return Optional.of(goods);
	}
	
	
	
	/**
	 * 分页查询商品列表
	 * @param goodsMenu
	 * @param sort
	 * @param isAsc
	 * @param index
	 * @return
	 */
	@Cacheable(value="getGoodsWithPage",key="'getGoodsWithPage'+#goodsMenu+#desc+#sort+#isAsc+#index")
	public List<GoodsWithoutDetail> loadGoodsWithPage(int goodsMenu,String desc,String sort,String isAsc,int index)
	{
		List<GoodsWithoutDetail> ls = new ArrayList<GoodsWithoutDetail>();
		int length = Integer.parseInt(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODS_LS_LENGTH));
		if(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODSLS_SORT_ALL).equals(sort))
		{
			ls = findGoodsByMenuWithSort(goodsMenu, desc, null, index, length);
		}else if (DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODSLS_SORT_PRICE).equals(sort))
		{
			if (DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODSLS_SORT_PRICE_ASC).equals(isAsc))
			{
				Sort s = new Sort(Direction.ASC,"price");
				ls = findGoodsByMenuWithSort(goodsMenu,desc, s, index, length);
			}else
			{
				Sort s = new Sort(Direction.DESC,"price");
				ls = findGoodsByMenuWithSort(goodsMenu,desc, s, index, length);
			}
		}else
		{
			Sort s = new Sort(Direction.DESC,"monthSales");
			ls = findGoodsByMenuWithSort(goodsMenu,desc, s, index, length);
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
		String desc = request.getParameter("desc");
		String sort = request.getParameter(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODSLS_SORT));
		String isAsc = request.getParameter("isasc");
		int length = Integer.parseInt(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODS_LS_LENGTH));
		List<GoodsWithoutDetail> ls = new ArrayList<GoodsWithoutDetail>();
		if(StringUtils.isEmptyOrWhitespace(sort)||DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODSLS_SORT_ALL).equals(sort))
		{
			request.setAttribute(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODSLS_SORT), DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODSLS_SORT_ALL));
			ls = findGoodsByMenuWithSort(goods,desc, null, 0, length);
		}else
		{
			request.setAttribute(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODSLS_SORT), sort);
			if(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODSLS_SORT_PRICE).equals(sort) && (StringUtils.isEmptyOrWhitespace(isAsc) || DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODSLS_SORT_PRICE_DESC).equals(isAsc)))
			{
				request.setAttribute("isAsc", DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODSLS_SORT_PRICE_ASC));
				Sort s = new Sort(Direction.ASC,"price");
				ls = findGoodsByMenuWithSort(goods,desc, s, 0, length);
			}else if(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODSLS_SORT_PRICE).equals(sort))
			{
				request.setAttribute("isAsc", DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODSLS_SORT_PRICE_DESC));
				Sort s = new Sort(Direction.DESC,"price");
				ls = findGoodsByMenuWithSort(goods,desc, s, 0, length);
			}else
			{
				Sort s = new Sort(Direction.DESC,"monthSales");
				ls = findGoodsByMenuWithSort(goods,desc, s, 0, length);
			}
		}
		
		request.setAttribute("goodsLs", ls);
		request.setAttribute("goods", goods);
		request.setAttribute("title", desc);
	}
	
	/**
	 * 根据商品ID获取商品详情
	 * @param goodsId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Optional<Goods> getGoods(String goodsId) {
		try {
			Future<Optional<Goods>> cacheGoods = (Future<Optional<Goods>>)cacheUtil.getCacheContent(CacheUtil.Caches.goodsTrace.name(),goodsId);
			if(cacheGoods==null) {
				Callable<Optional<Goods>> callable = new Callable<Optional<Goods>>() {
					@Override
					public Optional<Goods> call() throws Exception {
						Optional<Goods> goods = getProduct(goodsId);
						goods.map(g -> {
							if(g.getBanerImages()!=null) {
								g.setBanners(g.getBanerImages().split(","));
							}
							return g;
						}).orElse(new Goods());
						return goods;
					}
				};
				FutureTask<Optional<Goods>> task = new FutureTask<Optional<Goods>>(callable);
				Object  o = cacheUtil.putIfAbsent(CacheUtil.Caches.goodsTrace.name(), goodsId, task);
				if(o == null) {
					cacheGoods = task;
					task.run();
				}
			}
			Optional<Goods> goods = cacheGoods.get();
			return goods;
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			cacheUtil.remove(CacheUtil.Caches.goodsTrace.name(), goodsId);
		}
		return Optional.empty();
	}
	
	/**
	 * 修改商品库存,获取锁5s超时
	 * @param goodsId 商品ID
	 * @param count 修改商品库存数量
	 * @param opera 订单的商品数量加减操作，add,minus
	 * @return
	 */
	@Transactional
	public boolean updGoodsInventory(String goodsId,int count,String opera) {
			try {
				synchronized (goodsId) {
					Optional<Goods> goods = getGoods(goodsId);
					if(goods.isPresent()) {
						long inventory = goods.get().getInventory();
						if(ConfigConstants.OPERA_GOODS_ADD.equals(opera)) {
							if(inventory < count) {
								return false;
							}
							goodRepository.updateGoodsSetInventoryWithId(goodsId, inventory - count);
							return true;
						}else {
							goodRepository.updateGoodsSetInventoryWithId(goodsId, inventory + count);
							return true;
						}
					}
				}
			}catch(Exception e) {
				log.error(e.getMessage(),e);
			}
		return false;
	}
	
	
	/**
	 * 更新商品的销量
	 * @param goodsId
	 * @param count
	 * @return
	 */
	@Transactional
	public boolean updSales(String goodsId,int count) {
		try {
			synchronized (goodsId) {
				Optional<Goods> goods = getGoods(goodsId);
				if(goods.isPresent()) {
					SimpleDateFormat spd = new SimpleDateFormat("yyyyMMdd");
					String current = spd.format(new Date());
					long monthSales = count;
					//没有跑批，暂时先根据月份判断，月销量归零更新
					if(goods.get().getLstUpdDate()!=null) {
						String lstUpdDate = spd.format(goods.get().getLstUpdDate());
						if(lstUpdDate.substring(4, 6).equals(current.substring(4, 6))) {
							monthSales = goods.get().getMonthSales()+count;
						}
					}
					long totalSales = goods.get().getTotalSales()+count;
					goodRepository.updateGoodsSetSales(goodsId, monthSales, totalSales);
					return true;
				}
			}
		}catch(Exception e) {
			log.error(e.getMessage(),e);
		}
	return false;
	}
	
}
