package com.gt.common;

import java.util.List;

import com.gt.common.api.OrderRequest;
import com.gt.common.api.UserLoginRequest;
import com.gt.common.data.OrderData;
import com.gt.common.data.UserData;
import com.gt.common.view.OrderView;
import com.gt.common.view.UserView;

public class Converter {

	public static void requestToDataConverter(OrderRequest request, OrderData order) {
		order.setUserID(request.getUserID());
		order.setId(null);
		order.setSymbol(request.getSymbol());
		order.setQuantity(request.getQuantity());
		order.setPrice(request.getPrice());
		order.setSide(request.getSide());
		order.setQuantityRemaining(request.getQuantity());
	}

	public static void requestToDataConverter(UserLoginRequest request, UserData user) {
		user.setUserID(request.getUsername());
		user.setPassword(request.getPassword());
	}

	public static void viewToDataModelConverter(OrderView orderView, OrderData order) {
		order.setUserID(orderView.getUserID());
		order.setId(orderView.getId());
		order.setSymbol(orderView.getSymbol());
		order.setQuantity(orderView.getQuantity());
		order.setPrice(orderView.getPrice());
		order.setSide(orderView.getSide());
		order.setQuantityRemaining(orderView.getQuantityRemaining());
	}

	public static void viewToDataModelConverter(UserView userView, UserData userData) {
		userData.setUserID(userView.getUserID());
		userData.setPassword(userView.getUserID());
	}
	
	public static void dataToViewModelConverter(OrderView viewModel, OrderData dataModel) {
		viewModel.setUserID(dataModel.getUserID());
		viewModel.setId(dataModel.getId());
		viewModel.setSymbol(dataModel.getSymbol());
		viewModel.setQuantity(dataModel.getQuantity());
		viewModel.setPrice(dataModel.getPrice());
		viewModel.setSide(dataModel.getSide());
		viewModel.setQuantityRemaining(dataModel.getQuantityRemaining());
	}

	public static void dataToViewModelConverter(UserView viewModel, UserData dataModel) {
		viewModel.setUserID(dataModel.getUserID());
		viewModel.setPassword(dataModel.getPassword());
	}

	public static void dataToViewModelConverterForOrderList(List<OrderView> viewModel, List<OrderData> dataModel) {
		for (int i=0; i<dataModel.size(); i++) {
			OrderView orderView = new OrderView();
			dataToViewModelConverter(orderView,dataModel.get(i));
			viewModel.add(orderView);
		}
	}

	public static void dataToViewModelConverterForUserList(List<UserView> viewModel, List<UserData> dataModel) {
		for (int i=0; i<dataModel.size(); i++) {
			UserView userView = new UserView();
			dataToViewModelConverter(userView,dataModel.get(i));
			viewModel.add(userView);
		}
	}

	public static void dataToViewModelConverterForSingleOrder(OrderView viewModel, OrderData dataModel) {
		dataToViewModelConverter(viewModel, dataModel);
	}

}
