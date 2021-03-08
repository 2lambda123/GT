package com.mukit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mukit.common.Converter;
import com.mukit.model.data.CustomerLedgerData;
import com.mukit.model.view.CustomerLedgerView;
import com.mukit.repository.CustomerLedgerRepository;

@Service
public class CustomerLedgerService {

	@Autowired
	private CustomerLedgerRepository customerLedgerRepository;

	public CustomerLedgerView findByCustomerId(Integer cId) {
		CustomerLedgerView customerLedgerView = new CustomerLedgerView();
		try {
			CustomerLedgerData data = customerLedgerRepository.findByCustomerId(cId);
			Converter.dataToViewModelConverterForLedger(customerLedgerView, data);
			System.out.println(customerLedgerView.toString());

		} catch (Exception e) {

		}

		return customerLedgerView;
	}

	public List<CustomerLedgerView> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
