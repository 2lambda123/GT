package com.gt.common;

import java.util.List;

import com.gt.model.data.OrderData;
import com.gt.model.view.OrderView;

public class Converter {

	public static void viewToDataModelConverter(OrderView orderView, OrderData order) {
		order.setId(orderView.getId());
		order.setSymbol(orderView.getSymbol());
		order.setQuantity(orderView.getQuantity());
	}
	
	public static void dataToViewModelConverter(OrderView viewModel, OrderData dataModel) {
		viewModel.setId(dataModel.getId());
		viewModel.setSymbol(dataModel.getSymbol());
		viewModel.setQuantity(dataModel.getQuantity());
	}

	public static void dataToViewModelConverterForList(List<OrderView> viewModel, List<OrderData> dataModel) {
		for (int i=0; i<dataModel.size(); i++) {
			OrderView orderView = new OrderView();
			dataToViewModelConverter(orderView,dataModel.get(i));
			viewModel.add(orderView);
		}
	}

}
