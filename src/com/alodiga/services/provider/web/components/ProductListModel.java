package com.alodiga.services.provider.web.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zul.AbstractListModel;

import com.alodiga.services.provider.commons.ejbs.ProductEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.exceptions.NullParameterException;
import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
import com.alodiga.services.provider.commons.models.Product;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.commons.utils.QueryConstants;

public class ProductListModel extends AbstractListModel<ModelsDataProductUnity> {
	private static final long serialVersionUID = -3769143789341663612L;
	private ProductEJB productEJB = null;
	 private List<Product> products = null;
	
	private int _size = -1;
	private ModelsDataProductUnity[] _cache;
	private int _beginOffset;
	private int _limit;
	private boolean _filter;
	private String _filterText;

	public ProductListModel(int limit) {
		this._filter = false;
		init(limit);
	}

	public ProductListModel(int limit, String filter) {
		_filter = true;
		_filterText = filter;
		init(limit);
	}

	protected void init(int limit)  {
		this._limit = limit;
		this._beginOffset = 0;
		this._size = -1;
		
		productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
		
		
	}

	
	public ModelsDataProductUnity getElementAt(int index){
		if (_cache == null || index < _beginOffset || index >= _beginOffset + _cache.length){
			loadToCache(index, _limit);
		}
		return _cache[index - _beginOffset];
	}
	
	protected void loadToCache(int index, int limit) {
		// Inicializar la cache
		_cache = new ModelsDataProductUnity[limit];

		EJBRequest request = new EJBRequest();
		Map params = new HashMap();
        params.put(QueryConstants.PARAM_FILTER, _filter);
        params.put(QueryConstants.PARAM_FILTER_TEXT, _filterText);
        request.setParams(params);

		request.setFirst(index);
		request.setLimit(limit);
		request.setAuditData(null);
		try {
			products = productEJB.getProducts(request);
		} catch (GeneralException e) {
			e.printStackTrace();
		} catch (EmptyListException e) {
			e.printStackTrace();
		} catch (NullParameterException e) {
			e.printStackTrace();
		}

		int i = 0;

		for (Product product : products) {
			ModelsDataProductUnity dataRegionalUnity = new ModelsDataProductUnity();
				
			dataRegionalUnity.setProduct(product);
			dataRegionalUnity.setPartNumber(product.getPartNumber());
			dataRegionalUnity.setDescription(product.getDescription());
			dataRegionalUnity.setAmount(product.getAmount());
			_cache[i++] = dataRegionalUnity;
		}
		_beginOffset = index;
	}

	
	public int getSize(){
		if (_size < 0){
			EJBRequest request = new EJBRequest();
			Map params = new HashMap();
	        params.put(QueryConstants.PARAM_FILTER, _filter);
			if (_filter)
				  params.put(QueryConstants.PARAM_FILTER_TEXT, _filterText);

			request.setParams(params);
//	
//			SigacEJBResponse response = orgBean.getDependenceCount(request);
//	
//			if (!(Boolean) response.getParameter("success"))
//				_size = 0;
//	
//			_size = (Integer) response.getParameter("size");
		}
		return _size;
	}
}
