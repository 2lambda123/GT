package com.mukit.common;

import java.util.List;

import com.mukit.model.data.CustomerLedgerData;
import com.mukit.model.data.Order;
import com.mukit.model.view.CustomerLedgerView;
import com.mukit.model.view.OrderView;

public class Converter {

	public static void viewToDataModelConverter(OrderView orderView, Order order) {
		order.setId(orderView.getId());
		order.setSymbol(orderView.getSymbol());
		order.setQuantity(orderView.getQuantity());
	}
	
	public static void dataToViewModelConverter(OrderView viewModel, Order dataModel) {
		viewModel.setId(dataModel.getId());
		viewModel.setSymbol(dataModel.getSymbol());
		viewModel.setQuantity(dataModel.getQuantity());
	}

	public static void dataToViewModelConverterForList(List<OrderView> viewModel, List<Order> dataModel) {
		for (int i=0; i<dataModel.size(); i++) {
			OrderView orderView = new OrderView();
			dataToViewModelConverter(orderView,dataModel.get(i));
			viewModel.add(orderView);
		}
	}

	public static void viewToDataModelConverterForLedger(CustomerLedgerView viewModel,
			CustomerLedgerData dataModel) {
		dataModel.setTransactionId(viewModel.getTransactionId());
		dataModel.setBatchId(viewModel.getBatchId());
		dataModel.setCustomerId(viewModel.getCustomerId());
		dataModel.setTransactionType(viewModel.getTransactionType());
		dataModel.setAmount(viewModel.getAmount());
		dataModel.setTransactionDate(viewModel.getTransactionDate());
		
	}
	
	public static void dataToViewModelConverterForLedger(CustomerLedgerView viewModel,
			CustomerLedgerData dataModel) {
		viewModel.setTransactionId(dataModel.getTransactionId());
		viewModel.setBatchId(dataModel.getBatchId());
		viewModel.setCustomerId(dataModel.getCustomerId());
		viewModel.setTransactionType(dataModel.getTransactionType());
		viewModel.setAmount(dataModel.getAmount());
		viewModel.setTransactionDate(dataModel.getTransactionDate());
		
	}

}
