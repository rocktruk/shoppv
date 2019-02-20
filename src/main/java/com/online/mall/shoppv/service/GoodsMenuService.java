package com.online.mall.shoppv.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.shoppv.common.ConfigConstants;
import com.online.mall.shoppv.common.DictConstantsUtil;
import com.online.mall.shoppv.entity.GoodsMenu;
import com.online.mall.shoppv.repository.GoodsMenuRepository;

@Service
public class GoodsMenuService {

	@Autowired
	private GoodsMenuRepository goodsMenu;
	
	
	public List<GoodsMenu> findAll()
	{
		List<GoodsMenu> ls = goodsMenu.findAll();
		return ls;
	}
	
	@Transactional
	public boolean addMenu(GoodsMenu menu)
	{
		int n = goodsMenu.addMenu(menu.getId(), menu.getParentId(), menu.getMenuName(), menu.getImageSrc());
		if(n==1)
		{
			return true;
		}else
		{
			return false;
		}
	}
	
	
	/**
	 * 拼装菜单
	 * @param ls
	 * @return
	 */
	public Map<String,Object> menuSort(List<GoodsMenu> ls)
	{
		Map<String,Object> map = new HashMap<String, Object>();
		List<GoodsMenu> masterMenu = new ArrayList<GoodsMenu>();
		List<List<GoodsMenu>> childMenu = new ArrayList<List<GoodsMenu>>();
		for(GoodsMenu menu : ls)
		{
			if(menu.getParentId()==Integer.parseInt(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODS_MENU_NOPARENT)))
			{
				masterMenu.add(menu);
				List<GoodsMenu> childls = eachMenu(ls,menu.getId());
				childMenu.add(childls);
			}
		}
		map.put("classified", masterMenu);
		map.put("prodLs", childMenu);
		return map;
	}
	
	
	private List<GoodsMenu> eachMenu(List<GoodsMenu> ls,int id)
	{
		List<GoodsMenu> childMenu = new ArrayList<GoodsMenu>();
		for(GoodsMenu menu : ls)
		{
			if(id == menu.getParentId())
			{
				childMenu.add(menu);
			}
		}
		return childMenu;
	}
	
}
