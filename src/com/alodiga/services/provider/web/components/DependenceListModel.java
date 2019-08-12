package com.alodiga.services.provider.web.components;

import java.util.List;

import javax.naming.NamingException;

import org.zkoss.zul.AbstractListModel;

import com.alodiga.services.provider.commons.ejbs.ProductEJB;
import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
import com.alodiga.services.provider.commons.models.Product;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;



public class DependenceListModel extends AbstractListModel<ModelsDataProductUnity> {
	private static final long serialVersionUID = -3769143789341663612L;
	private ProductEJB productEJB = null;
	 private List<Product> products = null;
	
	private int _size = -1;
	private ModelsDataProductUnity[] _cache;
	private int _beginOffset;
	private int _limit;
	private boolean _filter;
	private String _filterText;

	public DependenceListModel(int limit) {
		this._filter = false;
		init(limit);
	}

	public DependenceListModel(int limit, String filter) {
		_filter = true;
		_filterText = filter;
		init(limit);
	}

	protected void init(int limit) {
		this._limit = limit;
		this._beginOffset = 0;
		this._size = -1;
		
		productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
		
		// Cargar los beans necesarios
		try {
			EJBRequest request = new EJBRequest();
			request.setFirst(0);
            request.setLimit(null);
            request.setAuditData(null);
            products = productEJB.getProducts(request);
		} catch (NamingException e) {
			e.printStackTrace();
		}
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

//		// Hacer las operaciones necesarias
//		SigacEJBRequest request = new SigacEJBRequest();
//		request.addParameter("offset", index);
//		request.addParameter("limit", limit);
//		request.addParameter("filter", _filter);
//		request.addParameter("filterText", _filterText);
//
//		SigacEJBResponse response = orgBean
//				.getRegionalUnityWithLimitAndOffset(request);
//
//		if (!(Boolean) response.getParameter("success")) {
//			return;
//		}
//
//		@SuppressWarnings("unchecked")
//		List<Product> products = (List<Product>) response
//				.getParameter("dependences");

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
//			SigacEJBRequest request = new SigacEJBRequest();
//			request.addParameter("filter", _filter);
//	
//			if (_filter)
//				request.addParameter("filterText", _filterText);
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
